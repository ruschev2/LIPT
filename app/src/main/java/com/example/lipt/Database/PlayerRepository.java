/**
 * Luis Hernandez
 * April 1, 2024
 * PokeRepository.java, this describes the repository class for abstracting access to multiple data sources
 */

package com.example.lipt.Database;
import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class PlayerRepository {

    private PlayerDao playerDao;
    private LiveData<List<Player>> allPlayers;
    public PlayerRepository(Application application) {
        PlayerRoomDatabase db = PlayerRoomDatabase.getDatabase(application);
        this.playerDao = db.playerDao();
        allPlayers = playerDao.getPlayerList();
    }

    public LiveData<List<Player>> getAllPlayers() {
        return allPlayers;
    }

    public void insert(Player player) {
        PlayerRoomDatabase.databaseWriteExecutor.execute(() -> {
            playerDao.insertPlayer(player);
        });
    }

    //for retrieving a specific player
    public Player getPlayerById(int playerId) {
        return playerDao.getPlayerById(playerId);
    }

    //for leveling up a player
    public void levelUpPlayer(int playerId) {
        Player player = playerDao.getPlayerById(playerId);
        if(player != null) {
            player.setTrainer_level(player.getTrainer_level() + 1);
            playerDao.updatePlayer(player);
        }
    }

    //for increasing a player's rounds played
    public void increasePlayerRoundsPlayed(int playerId) {
        Player player = playerDao.getPlayerById(playerId);
        if(player != null) {
            player.setRounds_played(player.getRounds_played() + 1);
            playerDao.updatePlayer(player);
        }
    }

    //for increasing a player's points
    public void increasePlayerPoints(int playerId, int points) {
        Player player = playerDao.getPlayerById(playerId);
        if(player != null) {
            player.setPoints(player.getPoints() + points);
            playerDao.updatePlayer(player);
        }
    }
}
