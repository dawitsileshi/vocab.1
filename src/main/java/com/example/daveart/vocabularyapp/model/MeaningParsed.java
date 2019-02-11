package com.example.daveart.vocabularyapp.model;

public class MeaningParsed {

    private String word_type;
    private String meaning;
    private String example;
    private boolean selected;

    public MeaningParsed(String word_type, String meaning, String example){
        this.example = example;
        this.word_type = word_type;
        this.meaning = meaning;
    }

    public String getWord_type() {
        return word_type;
    }

    public void setWord_type(String word_type) {
        this.word_type = word_type;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
