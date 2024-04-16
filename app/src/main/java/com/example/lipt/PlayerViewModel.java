package com.example.lipt;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.lipt.Database.Player;
import com.example.lipt.Database.PlayerRepository;

import java.util.List;

public class PlayerViewModel extends AndroidViewModel {
    private final PlayerRepository repository;
    private LiveData<List<Player>> allPlayers;

    public PlayerViewModel(Application application) {
        super(application);
        repository = new PlayerRepository(application);
        allPlayers = repository.getAllPlayers();
    }

    public LiveData<List<Player>> getAllPlayers() {
        return  allPlayers;
    }

    public void deletePlayerById(int playerId) {
        repository.deletePlayer(playerId);
    }
}
