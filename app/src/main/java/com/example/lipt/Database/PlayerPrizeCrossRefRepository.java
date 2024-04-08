/**
 * Luis Hernandez, Guillermo Zendejas
 * April 6, 2024
 * PlayerPrizeCrossRefRepository.java, this class describes the repository for our junction table
 */

package com.example.lipt.Database;
import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class PlayerPrizeCrossRefRepository {

    private PlayerPrizeCrossRefDao playerPrizeCrossRefDao;

    private LiveData<List<PlayerPrizeCrossRef>> allPlayerPrizeCrossRefs;

    public PlayerPrizeCrossRefRepository(Application application) {
        PlayerRoomDatabase db = PlayerRoomDatabase.getDatabase(application);
        this.playerPrizeCrossRefDao = db.playerPrizeCrossRefDao();
        allPlayerPrizeCrossRefs = playerPrizeCrossRefDao.getAllPlayerPrizeCrossRefs();
    }


    public List<Integer> getPlayerPrizeIdsForPlayer(int playerId) {
        return playerPrizeCrossRefDao.getPlayerPrizeIdsForPlayer(playerId);
    }

    public void insert(PlayerPrizeCrossRef playerPrizeCrossRef) {
        PlayerRoomDatabase.databaseWriteExecutor.execute(() -> {
            playerPrizeCrossRefDao.insertPlayerPrizeCrossRef(playerPrizeCrossRef);
        });
    }

}

