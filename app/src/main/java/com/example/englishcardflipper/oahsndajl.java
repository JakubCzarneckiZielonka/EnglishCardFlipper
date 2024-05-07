package com.example.englishcardflipper;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import java.util.List;
/*
public class MainActivity extends AppCompatActivity {

    private Button btn_good, btn_bad;
    private ViewFlipper v_flipper;
    private CompositeDisposable disposables = new CompositeDisposable(); // Manage RxJava subscriptions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        v_flipper = findViewById(R.id.v_flipper);
        btn_bad = findViewById(R.id.btn_Bad);
        btn_good = findViewById(R.id.btn_Good);

        v_flipper.setAutoStart(false);

        // Fetch words from Room database using RxJava
        WordDatabase db = WordDatabase.getDatabase(this);
        final WordDao wordDao = db.wordDao();
        Observable<List<Word>> allWordsObservable = wordDao.getAllWordsRx();

        disposables.add(
                allWordsObservable
                        .subscribeOn(Schedulers.io()) // Run on background thread
                        .observeOn(AndroidSchedulers.mainThread()) // Switch to main thread for UI updates
                        .subscribe(words -> {
                            // Update UI with the list of words
                            for (Word word : words) {
                                flipperImages(word.getEnglishWord() + "\n" + word.getPolishWord());
                            }
                        }, throwable -> {
                            // Handle errors from database operations (optional)
                            // Log the error or display a message to the user
                        })
        );

        // Define click listener for ViewFlipper (handles both buttons)
        View.OnClickListener flipClickListener = v -> v_flipper.showNext();
        v_flipper.setOnClickListener(flipClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear(); // Unsubscribe from RxJava subscriptions to prevent leaks
    }

    public void flipperImages(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(24);
        textView.setGravity(Gravity.CENTER);
        v_flipper.addView(textView);
        v_flipper.setInAnimation(this, R.anim.slide_in);
        v_flipper.setOutAnimation(this, R.anim.slide_out);
    }
}
*/