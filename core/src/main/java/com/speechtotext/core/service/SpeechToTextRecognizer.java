package com.speechtotext.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.NotImplementedException;

import java.io.IOException;

public interface SpeechToTextRecognizer {

        default String recognize(byte[] content) throws Exception {
                throw new NotImplementedException("This method is not implemented");
        };
}
