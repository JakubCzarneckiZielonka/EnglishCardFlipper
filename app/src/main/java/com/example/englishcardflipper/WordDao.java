package com.example.englishcardflipper;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface WordDao {

    @Query("SELECT * FROM words WHERE english_word = :englishWord")
    List<Word> getWordByEnglishWord(String englishWord);
    @Query("SELECT * FROM words WHERE is_correct = 0")
    List<Word> getIncorrectWords();

    @Query("SELECT polish_word FROM words WHERE is_correct = 0")
    List<String> getIncorrectPolishWords();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWord(Word word);

    @Delete
    void deleteWord(Word word);
    @Query("SELECT * FROM words")
    List<Word> getAllWords();
    @Query("SELECT * FROM words")
    Observable<List<Word>> getAllWordsRx();
    @Query("DELETE FROM words")
    Completable deleteAll();

    @Update()
    Completable updateWord(Word word);

    @Query("SELECT * FROM words WHERE is_correct = 0")
    Observable<List<Word>> getIncorrectWordsRx();
}
