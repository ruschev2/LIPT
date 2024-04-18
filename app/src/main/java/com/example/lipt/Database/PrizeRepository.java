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

    private final PrizeDao prizeDao;
    private final LiveData<List<Prize>> allPrizes;
    private final List<Prize> fullPrizeList;

    public PrizeRepository(Application application) {
        PlayerRoomDatabase db = PlayerRoomDatabase.getDatabase(application);
        this.prizeDao = db.prizeDao();
        allPrizes = prizeDao.getPrizeList();
        fullPrizeList = prizeDao.getFullPrizeList();
    }

    public void insert(Prize prize) {
        PlayerRoomDatabase.databaseWriteExecutor.execute(() -> prizeDao.insertPrize(prize));
    }

    /**
     * this method is for attaining a livedata wrapped list of all prizes from the database
     * @return a livedata wrapped list of all prizes from the database
     */
    public LiveData<List<Prize>> getAllPrizes() {return allPrizes;}

    /**
     * this method is for attaining a standard list of all prizes from the database
     * @return a list of all prizes from the database
     */
    public List<Prize> getPrizeList() {return fullPrizeList;}


}
