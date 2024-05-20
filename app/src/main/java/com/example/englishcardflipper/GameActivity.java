package com.example.englishcardflipper;
/* future class for hangman */
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class GameActivity extends AppCompatActivity {

    private CompositeDisposable disposable = new CompositeDisposable();

    private WordDao wordDao;
    private List<Word> words;
    private String secretWord;
    private StringBuilder guessedWord;
    private int triesLeft = 6;

    private TextView textViewWord;
    private EditText editTextGuess;
    private Button buttonCheck;
    private TextView textViewResult,textViewTranslation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        wordDao = WordDatabase.getDatabase(this).wordDao();

        textViewWord = findViewById(R.id.textViewWord);
        editTextGuess = findViewById(R.id.editTextGuess);
        buttonCheck = findViewById(R.id.buttonCheck);
        textViewResult = findViewById(R.id.textViewResult);
        textViewTranslation = findViewById(R.id.textViewTranslation);

        // Download words from the database
        disposable.add(wordDao.getAllWordsRx()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::initializeGame, Throwable::printStackTrace));

        buttonCheck.setOnClickListener(view -> {
            String guess = editTextGuess.getText().toString().trim();
            if (!TextUtils.isEmpty(guess)) {
                checkLetter(guess);
            }
        });
    }

    private void initializeGame(List<Word> words) {
        // Choose a random word from the database
        this.words = words;
        Random random = new Random();
        Word randomWord = words.get(random.nextInt(words.size()));
        secretWord = randomWord.getEnglishWord().toLowerCase();
        String polishTranslation = randomWord.getPolishWord();

        guessedWord = new StringBuilder();
        for (int i = 0; i < secretWord.length(); i++) {
            guessedWord.append("*");
        }
        textViewWord.setText(guessedWord);
        textViewTranslation.setText(polishTranslation);
    }


    private void checkLetter(String guess) {
        if (triesLeft > 0) {
            char letter = guess.toLowerCase().charAt(0);

            boolean letterFound = false;
            for (int i = 0; i < secretWord.length(); i++) {
                if (secretWord.charAt(i) == letter) {
                    guessedWord.setCharAt(i, letter);
                    letterFound = true;
                }
            }

            if (!letterFound) {
                triesLeft--;
            }

            textViewWord.setText(guessedWord);
            textViewResult.setText("Tries left: " + triesLeft);

            if (guessedWord.toString().equals(secretWord)) {
                textViewResult.setText("Congratulations! You guessed the word.");
                buttonCheck.setEnabled(false);
            }

            if (triesLeft == 0) {
                textViewResult.setText("Game over! The word was: " + secretWord);
                buttonCheck.setEnabled(false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        disposable.clear();
    }
}