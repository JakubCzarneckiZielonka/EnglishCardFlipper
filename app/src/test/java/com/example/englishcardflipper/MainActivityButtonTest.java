package com.example.englishcardflipper;

import org.junit.Test;
import static org.mockito.Mockito.*;

import android.view.View;
import com.example.englishcardflipper.MainActivity;
import com.example.englishcardflipper.WordDao;
import org.junit.Test;
public class MainActivityButtonTest {

    @Test
    public void testDeleteAllWordsOnClick() {
        // Mockowanie obiektu WordDao
        WordDao wordDaoMock = mock(WordDao.class);

        // Tworzenie obiektu MainActivity z mockowanym WordDao
        MainActivity mainActivity = new MainActivity();
        mainActivity.setWordDao(wordDaoMock);

        // Symulacja kliknięcia w przycisk "Nie umiem"
        mainActivity.onClickNieUmiemButton(null);

        // Weryfikacja, czy metoda deleteAll() została wywołana na mockowanym obiekcie WordDao
        verify(wordDaoMock).deleteAll();
    }
}
