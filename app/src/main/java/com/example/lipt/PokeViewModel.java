/**
 * Luis Hernandez
 * April 1, 2024
 * PokeViewModel.java, this describes the viewmodel for our database
 * The viewmodel provides data to the UI which survives configuration changes.
 */

package com.example.lipt;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class PokeViewModel extends AndroidViewModel {

    private PokeRepository repo;

    private final LiveData<List<Player>> allPlayers;

    public PokeViewModel (Application application) {
        super(application);
        repo = new PokeRepository(application);
        allPlayers = repo.getAllPlayers();
    }

    LiveData<List<Player>> getAllPlayers() {return allPlayers;}

    public void insert(Player player) {repo.insert(player);}

}
