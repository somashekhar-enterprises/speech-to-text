package com.speechtotext.core.controller;

import jakarta.inject.Inject;
import com.speechtotext.core.service.SpeechToTextRecognizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("api/v1/speechtotext/")
public class SpeechToTextController {

    @Value("${ffmpeg.executable.location}")
    private String ffmpegExecutable;

    @Inject
    private SpeechToTextRecognizer speechToTextRecognizer;

    @GetMapping("test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok()
                .body("API Server is up!");
    }

    @PostMapping(value = "/transcribe", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
    produces = MediaType.TEXT_PLAIN_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<StreamingResponseBody> getText(@RequestPart("audio") MultipartFile audioFile) throws IOException {
        File wavFile = convertToWav(audioFile);
        byte[] content = Files.readAllBytes(wavFile.getAbsoluteFile().toPath());
        String transcript = speechToTextRecognizer.convert(content);
        StreamingResponseBody responseBody = response -> {
            for (byte transcriptByte : transcript.getBytes()) {
                try {
                    Thread.sleep(100);
                    response.write(transcriptByte);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(responseBody);
    }

    private File convertToWav(MultipartFile audioFile) throws IOException {
        File webmFile = Files.write(Paths.get("D:\\speech-to-text\\core\\src\\main\\resources\\static\\audio" +
                "\\input.webm"), audioFile.getBytes()).toFile();

        File wavFile = Files.createFile(Paths.get("D:\\speech-to-text\\core\\src\\main\\resources" +
                "\\static\\audio\\final.wav")).toFile();
        String command = String.format("%s\\ffmpeg -i %s %s", ffmpegExecutable, webmFile.getAbsolutePath(),
                wavFile.getAbsolutePath());

        ProcessBuilder builder = new ProcessBuilder(command.split(" "));
        Process process = builder.start();

        return wavFile;

    }

    private Long sizeFromFile(Path path) {
        try {
            return Files.size(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return 0L;
    }
}
