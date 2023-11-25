package com.speechtotext.core.controller;

import com.speechtotext.core.dto.request.CategorizedSpeechRequest;
import com.speechtotext.core.dto.response.CategorizedSpeechResponse;
import com.speechtotext.core.service.FileToByteConverter;
import com.speechtotext.core.service.SpeechCategorizer;
import jakarta.inject.Inject;
import com.speechtotext.core.service.SpeechToTextRecognizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/speechtotext/")
public class SpeechToTextController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpeechToTextController.class);

    @Inject
    private SpeechToTextRecognizer speechToTextRecognizer;

    @Inject
    private FileToByteConverter byteConverter;

    @Inject
    private SpeechCategorizer speechCategorizer;

    @PostMapping(value = "categorize", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    ResponseEntity<CategorizedSpeechResponse> categorize(@RequestHeader("x-session-id") String sessionId,
                                                                @RequestPart("audio") MultipartFile audioFile) throws IOException {
        byte[] content = byteConverter.convertToWav(audioFile, sessionId);
        String transcript = speechToTextRecognizer.convert(content);
        LOGGER.info("Transcribed audio to: {}", transcript);

        CategorizedSpeechRequest request = new CategorizedSpeechRequest();
        request.setTranscription(transcript);
        request.setSessionId(sessionId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(speechCategorizer.getCategoriesFromApi(request));
    }

}
