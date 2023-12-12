package com.speechtotext.core.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;

@Service
public class OpenAISpeechToTextRecognizer implements SpeechToTextRecognizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAISpeechToTextRecognizer.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.transcription.url}")
    private String apiUrl;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        webClient = WebClient.create();
    }

    @Override
    public String recognize(byte[] audioContent) throws Exception {
        Resource resource = new ByteArrayResource(audioContent) {
            @Override
            public String getFilename() {
                return "audio.wav";
            }
        };

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", resource).filename("audio.wav");
        bodyBuilder.part("model", "whisper-1");

        WebClient.RequestBodySpec uri = webClient.post()
                .uri(apiUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        JsonNode json = MAPPER.readTree(uri.body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(String.class)
                .block());

        LOGGER.info("Finished transcribing audio");

        return json.path("text").asText();
    }

}
