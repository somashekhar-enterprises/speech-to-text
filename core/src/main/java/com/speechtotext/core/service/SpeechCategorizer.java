package com.speechtotext.core.service;

import com.speechtotext.core.dto.request.CategorizedSpeechRequest;
import com.speechtotext.core.dto.response.CategorizedSpeechResponse;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SpeechCategorizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpeechCategorizer.class);

    @Value("${nlp.flask.api.url}")
    private String categorizeApiUrl;

    private RestTemplate restClient;

    @PostConstruct
    void init() {
        restClient = new RestTemplate();
    }

    public CategorizedSpeechResponse getCategoriesFromApi(CategorizedSpeechRequest categorizedSpeechRequest) {
        LOGGER.info("Calling Flask API with URL: {}", categorizeApiUrl);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CategorizedSpeechRequest> httpRequest = new HttpEntity<>(categorizedSpeechRequest, httpHeaders);

        return restClient.postForObject(categorizeApiUrl, httpRequest, CategorizedSpeechResponse.class);
    }
}
