package com.speechtotext.core.dto.response;

public class CategorizedSpeechResponse {

    private CategoriesResponse categories;

    public CategoriesResponse getCategories() {
        return categories;
    }

    public void setCategories(CategoriesResponse categories) {
        this.categories = categories;
    }
}
