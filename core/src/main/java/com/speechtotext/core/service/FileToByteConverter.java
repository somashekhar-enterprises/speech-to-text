package com.speechtotext.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;

@Service
public class FileToByteConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileToByteConverter.class);

    @Value("${audio.storage.path}")
    private String intermediaryAudioStoragePath;

    public byte[] convertToWav(MultipartFile audioFile, String userId) throws IOException {
        File webmFile = Files.write(Paths.get(format("%s/input-%s.webm", intermediaryAudioStoragePath, userId)),
                audioFile.getBytes()).toFile();
        String resultLocation = format("%s/final-%s.wav", intermediaryAudioStoragePath, userId);

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
