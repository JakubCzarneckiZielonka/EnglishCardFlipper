package com.example.englishcardflipper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import androidx.room.Room;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


public class LoadWordsTask extends AsyncTask<Void, Void, Void> {
    private final Context context;
    private final WordDatabase wordDatabase;

    public LoadWordsTask(Context context) {
        this.context = context;
        wordDatabase = Room.databaseBuilder(context, WordDatabase.class, "word_db").build();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            loadWordsFromTxt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void loadWordsFromTxt() throws IOException {
        // Odczytanie plików zasobów
        BufferedReader englishReader = new BufferedReader(new InputStreamReader(context.getAssets().open("Angielski")));
        BufferedReader polishReader = new BufferedReader(new InputStreamReader(context.getAssets().open("Polski")));

        String englishWord;
        String polishWord;
        while ((englishWord = englishReader.readLine()) != null && (polishWord = polishReader.readLine()) != null) {
            // Tworzenie obiektu Word z odczytanymi słowami
            Word word = new Word(0, englishWord, polishWord, 0, 0, 0);

            // Sprawdzenie, czy słówko już istnieje w bazie danych
            List<Word> existingWords = wordDatabase.wordDao().getWordByEnglishWord(englishWord);
            if (existingWords.isEmpty()) {
                wordDatabase.wordDao().insertWord(word);
                Log.d("LoadWordsTask", String.format("Word saved: %s (%s)", englishWord, polishWord));
            } else {
                Log.d("LoadWordsTask", String.format("Word already exists: %s (%s)", englishWord, polishWord));
            }
        }

        // Zamknięcie BufferedReader
        englishReader.close();
        polishReader.close();
    }
}
