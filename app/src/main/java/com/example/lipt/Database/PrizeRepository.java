/**
 * Luis Hernandez, Guillermo Zendejas
 * April 4, 2024
 * PrizeRepository.java, this class describes the prize repository
 */

package com.example.lipt.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PrizeRepository {

    private PrizeDao prizeDao;

    private LiveData<List<Prize>> allPrizes;

    public PrizeRepository(Application application) {
        PrizeRoomDatabase db = PrizeRoomDatabase.getDatabase(application);
        this.prizeDao = db.prizeDao();
        allPrizes = prizeDao.getPrizeList();
    }

    public LiveData<List<Prize>> getAllPrizes() {return allPrizes;}

    public void insert(Prize prize) {
        PrizeRoomDatabase.databaseWriteExecutor.execute(() -> {
            prizeDao.insert(prize);
        });
    }

}
