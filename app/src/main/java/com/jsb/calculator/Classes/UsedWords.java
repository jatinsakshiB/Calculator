package com.jsb.calculator.Classes;

import androidx.annotation.NonNull;

public class UsedWords {

    private String word;
    private int usedTime;

    public UsedWords(String word, int usedTime) {
        this.word = word;
        this.usedTime = usedTime;
    }

    public UsedWords() {
    }

    public String getWord() {
        return word;
    }

    public int getUsedTime() {
        return usedTime;
    }

    @NonNull
    @Override
    public String toString() {
        return word;
    }
}
