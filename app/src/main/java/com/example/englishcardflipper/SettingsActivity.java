package com.example.englishcardflipper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SettingsActivity extends AppCompatActivity {
    private Button clear, load;
    private WordDao wordDao;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        load = findViewById(R.id.loadData);
        clear = findViewById(R.id.clearData);

        WordDatabase wordDatabase = WordDatabase.getDatabase(getApplicationContext());
        wordDao = wordDatabase.wordDao();

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoadWordsTask(SettingsActivity.this).execute();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    compositeDisposable.add(wordDao.deleteAll()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                                Log.d("DeleteAllSuccess", "All words deleted successfully");
                            }, throwable -> {
                                Log.e("DeleteAllError", "Error deleting all words: " + throwable.getMessage());
                            }));
                } catch (Exception e) {
                    Log.e("DeleteAllError", "Error deleting all words: " + e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Zwolnij zasoby Disposable w onDestroy()
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}