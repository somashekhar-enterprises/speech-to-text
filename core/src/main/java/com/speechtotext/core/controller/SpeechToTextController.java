package com.speechtotext.core.controller;

import com.speechtotext.core.dto.request.CategorizedSpeechRequest;
import com.speechtotext.core.dto.response.CategorizedSpeechResponse;
import com.speechtotext.core.service.SpeechCategorizer;
import jakarta.inject.Inject;
import com.speechtotext.core.service.SpeechToTextRecognizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.UUID;

import static java.lang.String.format;

@RestController
@RequestMapping("api/v1/speechtotext/")
public class SpeechToTextController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpeechToTextController.class);

    @Value("${audio.storage.path}")
    private String intermediaryAudioStoragePath;

    @Inject
    private SpeechToTextRecognizer speechToTextRecognizer;

    @Inject
    private SpeechCategorizer speechCategorizer;

    @GetMapping("test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok()
                .body("API Server is up!");
    }

    @PostMapping(value = "/transcribe", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
    produces = MediaType.TEXT_PLAIN_VALUE)
    @CrossOrigin(origins = "*")
    public Flux<String> getText(@RequestPart("audio") MultipartFile audioFile) throws IOException {
        byte[] content = convertToWav(audioFile, UUID.randomUUID().toString());
        String[] transcript = speechToTextRecognizer.convert(content).split(" ");
        return Flux.interval(Duration.ofMillis(100)).take(transcript.length)
                .map(f -> format("%s ", transcript[f.intValue()]));
    }

    @PostMapping(value = "/categorize", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public CategorizedSpeechResponse categorize(@RequestPart("audio") MultipartFile audioFile) throws IOException {
        String userId = UUID.randomUUID().toString();
        byte[] content = convertToWav(audioFile, userId);
        String transcript = speechToTextRecognizer.convert(content);
        LOGGER.info("Transcribed audio to: {}", transcript);

        CategorizedSpeechRequest request = new CategorizedSpeechRequest();
        request.setTranscription(transcript);
        request.setUserId(userId);
        return speechCategorizer.getCategoriesFromApi(request);
    }

    private byte[] convertToWav(MultipartFile audioFile, String userId) throws IOException {
        File webmFile = Files.write(Paths.get(format("%s\\input-%s.webm", intermediaryAudioStoragePath, userId)),
                audioFile.getBytes()).toFile();
        String resultLocation = format("%s\\final-%s.wav", intermediaryAudioStoragePath, userId);

        String command = format("ffmpeg -i %s %s", webmFile.getAbsolutePath(), resultLocation);

        try {
            ProcessBuilder builder = new ProcessBuilder(command.split(" "));
            Process process = builder.start();
            int exitCode = process.waitFor();

            LOGGER.debug("Process finished with exitCode: {}", exitCode);
        } catch (InterruptedException e) {
            LOGGER.error("Error when executing command: {}", command, e);
            throw new RuntimeException(e);
        }

        Path resultingWavPath = Paths.get(resultLocation);
        byte[] audioContent = Files.readAllBytes(resultingWavPath);
        cleanupIntermediaryFiles(webmFile.toPath(), resultingWavPath);

        return audioContent;
    }

    private void cleanupIntermediaryFiles(Path webmFilePath, Path resultingWavPath) throws IOException {
        Files.deleteIfExists(webmFilePath);
        Files.deleteIfExists(resultingWavPath);
    }
}