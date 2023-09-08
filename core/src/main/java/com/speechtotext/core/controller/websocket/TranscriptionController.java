package com.speechtotext.core.controller.websocket;

import com.speechtotext.core.dto.response.TranscriptResponse;
import com.speechtotext.core.service.FileToByteConverter;
import com.speechtotext.core.service.SpeechToTextRecognizer;
import jakarta.inject.Inject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class TranscriptionController {

    @Inject
    private FileToByteConverter byteConverter;

    @Inject
    private SpeechToTextRecognizer speechToTextRecognizer;

    @MessageMapping("/transcribe")
    @SendTo("/transcript")
    public TranscriptResponse transcribe(@RequestPart("audio") MultipartFile audioFile,
                                         @RequestHeader("x-session-id") String sessionId) throws IOException {
        byte[] content = byteConverter.convertToWav(audioFile, sessionId);
        String transcript = speechToTextRecognizer.convert(content);

        TranscriptResponse response = new TranscriptResponse();
        response.setTranscript(transcript);

        return response;
    }
}
