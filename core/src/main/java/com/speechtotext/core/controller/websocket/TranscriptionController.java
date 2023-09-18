package com.speechtotext.core.controller.websocket;

import com.speechtotext.core.dto.response.TranscriptResponse;
import com.speechtotext.core.service.SpeechToTextRecognizer;
import jakarta.inject.Inject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class TranscriptionController {

    @Inject
    private SpeechToTextRecognizer speechToTextRecognizer;

    @MessageMapping("/transcribe")
    @SendTo("/topic/stream")
    public TranscriptResponse transcribe(byte[] audioData) throws IOException {
        String transcript = speechToTextRecognizer.convert(audioData);

        TranscriptResponse response = new TranscriptResponse();
        response.setTranscript(transcript);

        return response;
    }
}
