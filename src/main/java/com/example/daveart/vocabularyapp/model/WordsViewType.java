package com.example.daveart.vocabularyapp.model;

public class WordsViewType {

    private SavedWord savedWord;
    private String firstLetter;
    private int viewType;

    public WordsViewType(SavedWord savedWord){
        this.savedWord = savedWord;
        viewType = 1;
    }

    public WordsViewType(String firstLetter){
        this.firstLetter = firstLetter;
        viewType = 2;
    }

    public SavedWord getSavedWord() {
        return savedWord;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public int getViewType() {
        return viewType;
    }
}
