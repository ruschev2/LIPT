/**
 * Guillermo Zendejas, Luis Hernandez
 * April 15, 2024
 * ViewModel used to encapsulate Player repository calls and other data behavior.
 */

package com.example.lipt;
import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.lipt.Database.Player;
import com.example.lipt.Database.PlayerRepository;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * ViewModel used to encapsulate Player repository calls and other data related behavior.
 */
public class PlayerViewModel extends AndroidViewModel {
    private final PlayerRepository repository;
    private LiveData<List<Player>> allPlayers;
    private LiveData<List<Player>> allAdmins;
    private LiveData<Player> player;
    Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Constructor initializes PlayerViewModel and sets allPlayers and allAdmins LiveData.
     * @param application The current application.
     */
    public PlayerViewModel(Application application) {
        super(application);
        repository = new PlayerRepository(application);
        allPlayers = repository.getAllPlayers();
        allAdmins = repository.getAllAdmins();

    }

    /**
     * Getter for a LiveData object containing all players.
     * @return a LiveData object containing all players.
     */
    public LiveData<List<Player>> getAllPlayers() {
        return  allPlayers;
    }

    /**
     * Calls repository method to delete a specific player.
     * @param playerId The ID of player to be deleted.
     */
    public void deletePlayerById(int playerId) {
        repository.deletePlayer(playerId);
    }

    /**
     * Calls on repository method to get a player LiveData object.
     * @param playerId ID of player being requested.
     * @return a LiveData<Player> object.
     */
    public LiveData<Player> getPlayerLiveDataById(int playerId) {
        player = repository.getPlayerLiveDataById(playerId);
        return player;
    }

    /**
     * Makes a repository method call to change a specific player's isAdmin boolean to true.
     * @param playerId ID of player to be promoted.
     */
    public void promotePlayerToAdmin(int playerId) {
        repository.promotePlayerToAdmin(playerId);
    }

    /**
     * Makes a repository call to change a specific player's isAdmin boolean to false.
     * @param playerId ID of player to be promoted.
     */
    public void demotePlayerFromAdmin(int playerId) {
        repository.demotePlayerFromAdmin(playerId);
    }

    /**
     * Makes a repository method call to increase a player's level by one.
     * @param playerId ID of player who's level is to be increased.
     */
    public void levelUpPlayer(int playerId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                repository.levelUpPlayer(playerId);
            }
        });
    }

    /**
     * Getter for allAdmins field.
     * @return A LiveData object containing a list of all Players.
     */
    public LiveData<List<Player>> getAllAdmins() {
        return  allAdmins;
    }
}
