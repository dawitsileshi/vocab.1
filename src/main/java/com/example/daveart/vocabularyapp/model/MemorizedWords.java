package com.example.daveart.vocabularyapp.model;

public class MemorizedWords {

    private long id;
    private String word;
    private boolean selected;

    public String getWord() {
        return word;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
