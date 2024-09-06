package com.romain.text_to_translation;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class TextAnalysisService {

    private Map<String, WordInfo> performTextAnalysis(String text) {
        LinkedHashMap<String, WordInfo> wordMap = new LinkedHashMap<>();
        String[] words = text.split("\\W+");
        List<String> wordsList = Arrays.asList(words);
        Collections.sort(wordsList);
        int total = words.length;

        // LinkedHashMap<String, WordInfo> wordMap = Arrays.stream(words)
        // .map(String::toLowerCase)
        // .filter(word -> word.length() >= 3 && !word.matches("\\d+"))  // Filtrer les mots de 1 caractère & Filtrer les nombres
        // .sorted()  // Trier par ordre alphabétique
        // .collect(Collectors.toMap(
        //     word -> word,
        //     word -> {
        //         int progress = (int) (((float) wordsList.indexOf(word) / total) * 100);
        //         return new WordInfo(word, progress, 1);
        //     },
        //     (existing, replacement) -> {
        //         existing.incrementCount();
        //         return existing;
        //     },
        //     LinkedHashMap::new  // Utiliser LinkedHashMap pour préserver l'ordre de tri
        // ));

        for (int i = 0; i < words.length; i++) {

            String word = wordsList.get(i).toLowerCase();

            if (word.length() <= 3 || word.matches("\\d+")) { // not interesting word
                continue;  // Passe au mot suivant
            }

            if (wordMap.containsKey(word)) {
                WordInfo info = wordMap.get(word);
                info.incrementCount();
            } else {
                float progress = ((float) i / total) * 100;
                int truncatedProgress = (int) progress;
                wordMap.put(word, new WordInfo(word, truncatedProgress, 1));
            }
        }

        return wordMap;
    }

    public String analyzeText(String content) {
        StringBuilder csvBuilder = new StringBuilder();
    csvBuilder.append("Word,First Position,Count\n");

    Map<String, WordInfo> analysisResult = performTextAnalysis(content);
    for (Map.Entry<String, WordInfo> entry : analysisResult.entrySet()) {
        csvBuilder.append(entry.getKey())
                  .append(",")
                  .append(entry.getValue().getFirstPosition())
                  .append(",")
                  .append(entry.getValue().getCount())
                  .append("\n");
    }

    return csvBuilder.toString();
    }

    public String callExternalApi() {
        String url = "https://api.coindesk.com/v1/bpi/currentprice.json";

        // Créer le WebClient
        WebClient client = WebClient.create(url);

        // Effectuer un appel GET avec les paramètres de requête
        Mono<String> response = client.get()
                .retrieve()
                .bodyToMono(String.class); // Récupère le corps de la réponse en tant que String

        // Abonnement pour récupérer et afficher la réponse
        response.subscribe(res -> {
            System.out.println("Réponse : " + res);
        });

        // todo print

        return new String("ok");
    }

}
