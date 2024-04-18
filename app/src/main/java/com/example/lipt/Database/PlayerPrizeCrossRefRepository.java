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

    private final PlayerPrizeCrossRefDao playerPrizeCrossRefDao;

    private final LiveData<List<PlayerPrizeCrossRef>> allPlayerPrizeCrossRefs;

    public PlayerPrizeCrossRefRepository(Application application) {
        PlayerRoomDatabase db = PlayerRoomDatabase.getDatabase(application);
        this.playerPrizeCrossRefDao = db.playerPrizeCrossRefDao();
        allPlayerPrizeCrossRefs = playerPrizeCrossRefDao.getAllPlayerPrizeCrossRefs();
    }

    /**
     * this method returns a livedata wrapped list of all playerprizerefs in the database
     * @return the livedata wrapped list of playerprizerefs
     */
    public LiveData<List<PlayerPrizeCrossRef>> getAllPlayerPrizeCrossRefs() {
        return allPlayerPrizeCrossRefs;
    }


    /**
     * this method returns a regular list of all player prize refs for a given player
     * @param playerId the ID of the player whose list is requested
     * @return the list of playerprizerefs
     */
    public List<Integer> getPlayerPrizeIdsForPlayer(int playerId) {
        return playerPrizeCrossRefDao.getPlayerPrizeIdsForPlayer(playerId);
    }

    /**
     * this method inserts a new player prize reference
     * @param playerPrizeCrossRef the new player prize reference to be inserted into room table
     */
    public void insert(PlayerPrizeCrossRef playerPrizeCrossRef) {
        PlayerRoomDatabase.databaseWriteExecutor.execute(() -> playerPrizeCrossRefDao.insertPlayerPrizeCrossRef(playerPrizeCrossRef));
    }

    /**
     * this method removes all prizes from a player's account
     * @param prizeId the ID of the player whose prizes will be removed
     */
    public void removePlayerPrizes(int prizeId) {
        playerPrizeCrossRefDao.deleteByPlayerId(prizeId);
    }

}

