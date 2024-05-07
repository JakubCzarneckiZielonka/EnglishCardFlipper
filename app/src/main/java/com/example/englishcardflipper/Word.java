package com.example.englishcardflipper;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "words")
public class Word {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "english_word")
    private String englishWord;
    @ColumnInfo(name = "polish_word")
    private String polishWord;
    @ColumnInfo(name = "is_correct")
    private boolean isCorrect;
    @ColumnInfo(name = "word_today")
    private int wordToday;
    @ColumnInfo(name = "word_all")
    private int wordAll;

    public Word(int id, String englishWord, String polishWord, boolean isCorrect, int wordToday, int wordAll) {
        this.id = id;
        this.englishWord = englishWord;
        this.polishWord = polishWord;
        this.isCorrect = isCorrect;
        this.wordToday = wordToday;
        this.wordAll = wordAll;
    }
    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", englishWord='" + englishWord + '\'' +
                ", polishWord='" + polishWord + '\'' +
                ", isCorrect=" + isCorrect +
                ", wordToday=" + wordToday +
                ", wordAll=" + wordAll +
                '}';
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public String getPolishWord() {
        return polishWord;
    }

    public void setPolishWord(String polishWord) {
        this.polishWord = polishWord;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public int getWordToday() {
        return wordToday;
    }

    public void setWordToday(int wordToday) {
        this.wordToday = wordToday;
    }

    public int getWordAll() {
        return wordAll;
    }

    public void setWordAll(int wordAll) {
        this.wordAll = wordAll;
    }
}
