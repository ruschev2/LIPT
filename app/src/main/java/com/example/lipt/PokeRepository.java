/**
 * Luis Hernandez
 * April 1, 2024
 * PokeRepository.java, this describes the repository class for abstracting access to multiple data sources
 */


package com.example.lipt;
import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class PokeRepository {

    private PlayerDao playerDao;
    private LiveData<List<Player>> allPlayers;

    PokeRepository(Application application) {
        PokeRoomDatabase db = PokeRoomDatabase.getDatabase(application);
        playerDao = db.playerDao();
        allPlayers = playerDao.getPlayerList();
    }

    LiveData<List<Player>> getAllPlayers() {
        return allPlayers;
    }

    void insert(Player player) {
        PokeRoomDatabase.databaseWriteExecutor.execute(() -> {
            playerDao.insert(player);
        });
    }


}
