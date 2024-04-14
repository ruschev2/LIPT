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

    /**
     * this method is for attaining all players
     * @return a livedata wrapped list of all players in the database
     */
    public LiveData<List<Player>> getAllPlayers() {
        return allPlayers;
    }

    /**
     * this method inserts a new player into the database
     * @param player the new player which will be deposited into the table
     */
    public void insert(Player player) {
        PlayerRoomDatabase.databaseWriteExecutor.execute(() -> {
            playerDao.insertPlayer(player);
        });
    }

    /**
     * this method grabs a specific player from the player table
     * @param playerId the ID of the player to grab
     * @return the player we wish to grab
     */
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

    /**
     * this method resets a player's trainer level to zero
     * @param playerId the ID of the player whose level will be reset
     */
    public void resetPlayerLevel(int playerId) {
        Player player = playerDao.getPlayerById(playerId);
        if(player != null) {
            player.setTrainer_level(0);
            playerDao.updatePlayer(player);
        }
    }

    /**
     * this method increases a player's rounds played
     * @param playerId the ID of the player whose rounds played field will be incremented
     */
    public void increasePlayerRoundsPlayed(int playerId) {
        Player player = playerDao.getPlayerById(playerId);
        if(player != null) {
            player.setRounds_played(player.getRounds_played() + 1);
            playerDao.updatePlayer(player);
        }
    }

    /**
     * this method increases a player's points
     * @param playerId the ID of the player whose points field will be increased
     * @param points the final score of a completed round which will be added to player's points
     */
    public void increasePlayerPoints(int playerId, int points) {
        Player player = playerDao.getPlayerById(playerId);
        if(player != null) {
            player.setPoints(player.getPoints() + points);
            playerDao.updatePlayer(player);
        }
    }

    /**
     * this method removes a player from the database
     * it will never delete an admin, because admins are eternal
     * @param playerId the ID of player who will be removed from the table
     */
    public void deletePlayer(int playerId) {
        new Thread(() -> {
            playerDao.deletePlayerById(playerId);
        }).start();
    }

}
