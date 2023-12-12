package com.speechtotext.core.controller;

import com.speechtotext.core.converter.PatientToPatientResponseConverter;
import com.speechtotext.core.dto.request.SummarizeTranscriptRequest;
import com.speechtotext.core.service.OpenAISpeechSummarizer;
import com.speechtotext.core.service.OpenAISpeechToTextRecognizer;
import com.speechtotext.core.service.PatientService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/speechtotext/")
public class SpeechToTextController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpeechToTextController.class);

    @Inject
    private OpenAISpeechToTextRecognizer openAISpeechToTextRecognizer;

    @Inject
    private OpenAISpeechSummarizer speechSummarizer;

    @Inject
    private PatientToPatientResponseConverter converter;

    @Inject
    private PatientService patientService;

    @PostMapping(value = "transcribe", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @CrossOrigin(origins = "*")
    String transcribe(@RequestPart("audio") MultipartFile audioFile) throws Exception {
        byte[] content = audioFile.getBytes();
        return openAISpeechToTextRecognizer.recognize(content);
    }

    @PostMapping(value = "summarize", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @CrossOrigin(origins = "*")
    String summarize(@Valid @RequestBody SummarizeTranscriptRequest request) {
        return speechSummarizer.summarize(request.getTranscript());
    }

}
