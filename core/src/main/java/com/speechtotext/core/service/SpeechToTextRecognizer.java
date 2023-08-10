package com.speechtotext.core.service;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SpeechToTextRecognizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpeechToTextRecognizer.class);

    public String convert(byte[] audioContent) {
        try (SpeechClient speechClient = SpeechClient.create()) {
            LOGGER.info("Created speechClient: {} for audio content: {}", speechClient, audioContent);
            // Transcribes your audio file using the specified configuration.
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setLanguageCode("en-IN")
                    .setEnableWordTimeOffsets(true)
                    .setEnableAutomaticPunctuation(true)
                    .setEnableWordConfidence(true)
                    .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(ByteString.copyFrom(audioContent)).build();

            // Performs speech recognition on the audio file
            RecognizeResponse response = speechClient.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            StringBuilder transcription = new StringBuilder();
            for (SpeechRecognitionResult result: results) {
                transcription.append(result.getAlternatives(0).getTranscript());
            }

            return transcription.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
