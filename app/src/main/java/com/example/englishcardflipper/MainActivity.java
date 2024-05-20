package com.example.englishcardflipper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import me.ibrahimsn.lib.OnItemReselectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {

    private TextView pytanieTextView;
    private TextView odpowiedzTextView;
    private Button sprawdzamButton;
    private Button umiemButton;
    private Button nieUmiemButton;
    private WordDao wordDao;
    private List<Word> words;
    private int currentIndex = 0;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private SmoothBottomBar smoothBottomBar;
    Button buttonPobierz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        pytanieTextView = findViewById(R.id.pytanie);
        odpowiedzTextView = findViewById(R.id.odpowiedz);
        sprawdzamButton = findViewById(R.id.sprawdzam);
        umiemButton = findViewById(R.id.btn_Good);
        nieUmiemButton = findViewById(R.id.btn_Bad);
        smoothBottomBar = findViewById(R.id.bottombar);
        buttonPobierz = findViewById(R.id.buttonpobierz);

        buttonPobierz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoadWordsTask(MainActivity.this).execute();
            }
        });
        WordDatabase wordDatabase = WordDatabase.getDatabase(getApplicationContext());
        wordDao = wordDatabase.wordDao();

        // Pobranie słów z bazy danych asynchronicznie przy użyciu RxJava
        compositeDisposable.add(wordDao.getAllWordsRx()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setWords, throwable -> {
                    // Obsługa błędów muszę tu cos wymyślić by przecwiczyc
                }));


        sprawdzamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (words != null && !words.isEmpty()) {
                    Word currentWord = words.get(currentIndex);
                    odpowiedzTextView.setText(currentWord.getEnglishWord());
                    handleFirstRecord(words); // Wywołanie metody handleFirstRecord()
                }
            }
        });
        nieUmiemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (words != null && !words.isEmpty()) {
                    // Przejście do kolejnego słowa
                    currentIndex = (currentIndex + 1) % words.size();
                    displayCurrentWord();
                }
            }
        });



        umiemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (words != null && !words.isEmpty()) {
                    Word currentWord = words.get(currentIndex);

                    // Zwiększenie wartości wordToday i wordAll o jeden
                    currentWord.setWordToday(currentWord.getWordToday() + 1);
                    currentWord.setWordAll(currentWord.getWordAll() + 1);

                    compositeDisposable.add(Completable.fromAction(() -> wordDao.updateWord(currentWord))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                                // Aktualizacja zakończona pomyślnie
                                Log.d("WordUpdateSuccess", "Word updated successfully: " + currentWord.toString());
                                // Przejdź do kolejnego słowa
                                currentIndex = (currentIndex + 1) % words.size();
                                displayCurrentWord();
                            }, throwable -> {
                                // Błąd podczas aktualizacji słowa w bazie danych
                                Log.e("WordUpdateError", "Error updating word: " + throwable.getMessage());
                            }));
                }
            }
        });


        smoothBottomBar.setOnItemReselectedListener(new OnItemReselectedListener() {
            @Override
            public void onItemReselect(int position) {
                switch (position) {
                    case 1:
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, GameActivity.class));
                        break;
                }
            }
        });

    }

    private void setWords(List<Word> words) {
        this.words = words;
        displayCurrentWord();
    }

    private void displayCurrentWord() {
        compositeDisposable.add(Completable.fromAction(() -> {
                    // Pobierz tylko niepoprawne polskie słowa
                    List<String> incorrectPolishWords = wordDao.getIncorrectPolishWords();
                    if (!incorrectPolishWords.isEmpty()) {
                        String currentPolishWord = incorrectPolishWords.get(currentIndex);
                        pytanieTextView.setText(currentPolishWord);
                        odpowiedzTextView.setText("");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {

                }, throwable -> {

                    Log.e("WordFetchError", "Error fetching incorrect words: " + throwable.getMessage());
                }));
    }


    // Metoda obsługująca pierwszy rekord
    private void handleFirstRecord(List<Word> words) {
        if (!words.isEmpty()) {
            Word firstRecord = words.get(0);

            Log.d("FirstRecord", "Record: " + firstRecord.toString());
        } else {

            Log.d("FirstRecord", "Baza danych jest pusta.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }


}