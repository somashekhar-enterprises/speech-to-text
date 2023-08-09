package com.speechtotext.core.dto.request;

public class CategorizedSpeechRequest {

    private String transcription;

    private String userId;

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
