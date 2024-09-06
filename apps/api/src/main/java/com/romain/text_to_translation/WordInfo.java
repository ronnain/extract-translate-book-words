package com.romain.text_to_translation;

public class WordInfo {
    private String word;
    private int firstPosition;
    private int count;

    public WordInfo(String word, int firstPosition, int count) {
        this.word = word;
        this.firstPosition = firstPosition;
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public int getFirstPosition() {
        return firstPosition;
    }

    public int getCount() {
        return count;
    }

    public void incrementCount() {
        this.count++;
    }

    @Override
    public String toString() {
        return word + "," + firstPosition + "," + count;
    }
}
