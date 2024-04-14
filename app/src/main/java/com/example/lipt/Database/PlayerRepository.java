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

    /**
     * this method returns a specific player from the table by their ID
     * @param playerId the ID of the player who will be grabbed
     * @return the player grabbed
     */
    public Player getPlayerById(int playerId) {
        return playerDao.getPlayerById(playerId);
    }

    /**
     * this method levels up a player by one
     * @param playerId the ID of the player whose trainer level will be incremented
     */
    public void levelUpPlayer(int playerId) {
        Player player = playerDao.getPlayerById(playerId);
        if(player != null) {
            player.setTrainer_level(player.getTrainer_level() + 1);
            playerDao.updatePlayer(player);
        }
    }

    /**
     * this method increments a player's rounds played field
     * @param playerId the ID of the player whose rounds played will be increased by one
     */
    public void increasePlayerRoundsPlayed(int playerId) {
        Player player = playerDao.getPlayerById(playerId);
        if(player != null) {
            player.setRounds_played(player.getRounds_played() + 1);
            playerDao.updatePlayer(player);
        }
    }

    /**
     * this method increases a player's points field after a game round is completed
     * @param playerId the ID of player who will be boosted
     * @param points the final score earned by player for the game round
     */
    public void increasePlayerPoints(int playerId, int points) {
        Player player = playerDao.getPlayerById(playerId);
        if(player != null) {
            player.setPoints(player.getPoints() + points);
            playerDao.updatePlayer(player);
        }
    }

}
