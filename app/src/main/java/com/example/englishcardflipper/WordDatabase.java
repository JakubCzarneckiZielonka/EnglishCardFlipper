package com.example.englishcardflipper;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Word.class, version = 1, exportSchema = false)
public abstract class WordDatabase extends RoomDatabase {

    private static WordDatabase wordDatabase;

    public static synchronized WordDatabase getDatabase(Context context){
        if (wordDatabase == null){
            wordDatabase = Room.databaseBuilder(
                    context.getApplicationContext(),
                    WordDatabase.class,
                    "word_db"
            ).build();
        }
        return wordDatabase;
    }
    public abstract WordDao wordDao();
}
