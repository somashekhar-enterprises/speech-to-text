package com.speechtotext.core.dto.request;

import java.util.List;

public class OpenAISummarizeRequest {

    private String model;

    private List<OpenAIMessageRequest> messages;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<OpenAIMessageRequest> getMessages() {
        return messages;
    }

    public void setMessages(List<OpenAIMessageRequest> messages) {
        this.messages = messages;
    }

    public void setMessage(int index, OpenAIMessageRequest message) {
        this.messages.set(index, message);
    }
}
