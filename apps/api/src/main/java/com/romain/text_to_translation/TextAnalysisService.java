package com.romain.text_to_translation;


import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.ImageGenerationData;
import com.azure.ai.openai.models.ImageGenerationOptions;
import com.azure.ai.openai.models.ImageGenerations;
import com.azure.core.credential.KeyCredential;
import com.romain.core.SecretConfigService;

import reactor.core.publisher.Mono;

@Service
public class TextAnalysisService {

    @Autowired
    private SecretConfigService secretConfigService;

    // private String performTextAnalysis(String text) {
    //     System.err.println(secretConfigService.getSecretKey());

    //     LinkedHashMap<String, WordInfo> wordMap = new LinkedHashMap<>();
    //     String[] words = text.split("\\W+");
    //     List<String> wordsList = Arrays.asList(words);
    //     Collections.sort(wordsList);
    //     int total = words.length;

    //     for (int i = 0; i < words.length; i++) {

    //         String word = wordsList.get(i).toLowerCase();

    //         if (word.length() <= 3 || word.matches("\\d+")) { // not interesting word
    //             continue;  // Passe au mot suivant
    //         }

    //         if (wordMap.containsKey(word)) {
    //             WordInfo info = wordMap.get(word);
    //             info.incrementCount();
    //         } else {
    //             float progress = ((float) i / total) * 100;
    //             int truncatedProgress = (int) progress;
    //             wordMap.put(word, new WordInfo(word, truncatedProgress, 1));
    //         }
    //     }

    //     return convertToCSV(wordMap);
    // }

    public String convertToCSV(Map<String, WordInfo> analysisResult) {
       StringWriter sw = new StringWriter();

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
            .setHeader( "Word", "First Position", "Count" )
            .build();

        try (final CSVPrinter printer = new CSVPrinter(sw, csvFormat)) {
            for (Map.Entry<String, WordInfo> entry : analysisResult.entrySet()) {
                try {
                    printer.printRecord(entry.getKey(), entry.getValue().getFirstPosition(), entry.getValue().getCount());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }

    public Mono<String> createThreadAndRunToGetWordsToLearn() {
        String API_URL = "https://api.openai.com/v1/threads";

        WebClient client = WebClient.create(API_URL);

        String requestBody = """
            {
                "assistant_id": "asst_elxgPtaCGaTx3ztYMfrfmeGn",
                "thread": {
                    "messages": [
                        {"role": "user", "content": "Donne moi les mots 500 clés à apprendre, dans leur ordre d'apparition dans le livre."}
                    ]
                }
            }
            """;

        // Envoi de la requête POST sans corps (données vides)
        ThreadResponse response = client.post()
                .headers( h -> h.setBearerAuth(secretConfigService.getSecretKey()))
                .contentType(MediaType.APPLICATION_JSON)
                .header("OpenAI-Beta", "assistants=v2")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(ThreadResponse.class)
                .block();
        // log response
        System.out.println(response.getId());
                // log response
        String threadId = response.getId();

        // while getRunUpdated not return completed
        // repeat each 500ms
        // return result
        ApiResponse runResponse = createRun(threadId);
        String runId = runResponse.getId();
        int maxRetries = 20; // Nombre maximum de tentatives pour éviter une boucle infinie
        int retries = 0;

        while (!runResponse.getStatus().equals("completed") && retries < maxRetries) {
            try {
                retries++;
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runResponse = getRunUpdated(runId, threadId);
        }
        // return result
        return Mono.just(runResponse.getThreadId());
    }

    private ApiResponse createRun(String threadId) {
        String API_URL = "https://api.openai.com/v1/";

        WebClient client = WebClient.create(API_URL);

        String requestBody = """
            {
                "assistant_id": "asst_elxgPtaCGaTx3ztYMfrfmeGn",
            }
            """;

        // Envoi de la requête POST sans corps (données vides)
        ApiResponse response = client.post()
                .uri(uriBuilder -> uriBuilder
                    .path("/v1/threads/{threadId}/runs")
                    .build(threadId))
                .headers( h -> h.setBearerAuth(secretConfigService.getSecretKey()))
                .contentType(MediaType.APPLICATION_JSON)
                .header("OpenAI-Beta", "assistants=v2")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .block();
        return response;
    }

    private ApiResponse getRunUpdated(String runId, String threadId) {
        // print runId and threadId
        System.out.println("runId: " + runId);
        System.out.println("threadId: " + threadId);
        String API_URL = "https://api.openai.com/";
        WebClient client = WebClient.create(API_URL);

        return client.get()
        .uri(uriBuilder -> uriBuilder
                .path("/v1/threads/{threadId}/runs/{runId}")
                .build(threadId, runId))
        .retrieve()
        .bodyToMono(ApiResponse.class)
                .block();  // On bloque pour attendre la réponse (comportement synchrone)
    }

    public String createImage() {
        OpenAIClient client = new OpenAIClientBuilder()
            .credential(new KeyCredential(secretConfigService.getSecretKey()))
            .buildClient();

        ImageGenerationOptions imageGenerationOptions = new ImageGenerationOptions(
            "A drawing of a vet and pets in the style of Van Gogh");
        String deploymentOrModelName = "dall-e-3";
        ImageGenerations images = client.getImageGenerations(deploymentOrModelName, imageGenerationOptions);

        for (ImageGenerationData imageGenerationData : images.getData()) {
            System.out.printf(
                "Image location URL that provides temporary access to download the generated image is %s.%n",
                imageGenerationData.getUrl());
            return imageGenerationData.getUrl();
        }
        return "";
    }

}
