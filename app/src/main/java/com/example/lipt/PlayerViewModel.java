package com.example.lipt;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.lipt.Database.Player;
import com.example.lipt.Database.PlayerRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PlayerViewModel extends AndroidViewModel {
    private final PlayerRepository repository;
    private LiveData<List<Player>> allPlayers;
    private LiveData<List<Player>> allAdmins;

    private LiveData<Player> player;
    Executor executor = Executors.newSingleThreadExecutor();

    public PlayerViewModel(Application application) {
        super(application);
        repository = new PlayerRepository(application);
        allPlayers = repository.getAllPlayers();
        allAdmins = repository.getAllAdmins();

    }

    public LiveData<List<Player>> getAllPlayers() {
        return  allPlayers;
    }

    public void deletePlayerById(int playerId) {
        repository.deletePlayer(playerId);
    }

    public Player getPlayerById(int playerId) {
        return repository.getPlayerById(playerId);
    }

    public LiveData<Player> getPlayerLiveDataById(int playerId) {
        player = repository.getPlayerLiveDataById(playerId);
        return player;
    }

    public void promotePlayerToAdmin(int playerId) {
        repository.promotePlayerToAdmin(playerId);
    }

    public void demotePlayerFromAdmin(int playerId) {
        repository.demotePlayerFromAdmin(playerId);
    }

    public void levelUpPlayer(int playerId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                repository.levelUpPlayer(playerId);
            }
        });
    }

    public LiveData<List<Player>> getAllAdmins() {
        return  allAdmins;
    }




}
