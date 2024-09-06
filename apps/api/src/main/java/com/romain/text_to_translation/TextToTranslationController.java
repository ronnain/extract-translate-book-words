package com.romain.text_to_translation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.Decoder.Text;

@RestController
@RequestMapping("/translate")
 @CrossOrigin(origins = "*")
public class TextToTranslationController {

    @Autowired
    private TextAnalysisService textAnalysisService;

    @GetMapping
    public String translateText() {
        return textAnalysisService.analyzeText("Hello, World! Hello2, World2! Hello, World!Hello, World!").toString();
    }

    @PostMapping(value="/analyze", produces="text/csv")
    public void analyzeText(@RequestParam("text") MultipartFile file, HttpServletResponse response) throws IOException {
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        String csvResult = textAnalysisService.analyzeText(content);

        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"result.csv\"");

        response.getWriter().write(csvResult);
        response.flushBuffer();
    }


    @GetMapping(value="/test")
    public String test() {
        return textAnalysisService.callExternalApi();
    }
}