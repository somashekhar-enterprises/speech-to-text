package com.speechtotext.core.controller.websocket;

import com.speechtotext.core.dto.request.AudioRequest;
import com.speechtotext.core.dto.request.LoginRequest;
import com.speechtotext.core.dto.response.TranscriptResponse;
import com.speechtotext.core.service.OpenAISpeechToTextRecognizer;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TranscriptionController {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TranscriptionController.class);

    @Inject
    private OpenAISpeechToTextRecognizer openAISpeechToTextRecognizer;

    @MessageMapping("/transcribe")
    @SendTo("/topic/stream")
    TranscriptResponse transcribe(@Payload AudioRequest request) throws Exception {
        byte[] content = request.getContent();
        LOGGER.info("Received audio request with content length {}", content.length);
        String transcript = openAISpeechToTextRecognizer.recognize(content);

        TranscriptResponse response = new TranscriptResponse();
        response.setTranscript(transcript);

        return response;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    LoginRequest chat(LoginRequest message) {
        return message;
    }
}
