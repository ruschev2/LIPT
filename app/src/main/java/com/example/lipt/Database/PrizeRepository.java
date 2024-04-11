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

    private List<Prize> fullPrizeList;
    public PrizeRepository(Application application) {
        PlayerRoomDatabase db = PlayerRoomDatabase.getDatabase(application);
        this.prizeDao = db.prizeDao();
        allPrizes = prizeDao.getPrizeList();
        fullPrizeList = prizeDao.getFullPrizeList();
    }

    public LiveData<List<Prize>> getAllPrizes() {return allPrizes;}

    public List<Prize> getPrizeList() {return fullPrizeList;}

    public void insert(Prize prize) {
        PlayerRoomDatabase.databaseWriteExecutor.execute(() -> {
            prizeDao.insertPrize(prize);
        });
    }

}
