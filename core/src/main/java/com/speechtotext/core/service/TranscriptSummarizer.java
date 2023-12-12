package com.speechtotext.core.service;

public interface TranscriptSummarizer {

    String summarize(String transcript) throws Exception;
}
