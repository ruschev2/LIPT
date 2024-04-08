/**
 * Luis Hernandez, Guillermo Zendejas
 * April 4, 2024
 * PokemonRepository.java, this class describes the pokemon data repository for our application
 */

package com.example.lipt.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PokemonRepository {

    private PokemonDao pokemonDao;

    private LiveData<List<Pokemon>> allPokemon;

    public PokemonRepository(Application application) {
        PokemonRoomDatabase db = PokemonRoomDatabase.getDatabase(application);
        this.pokemonDao = db.pokemonDAO();
        allPokemon = pokemonDao.getPokemonlist();
    }

    public LiveData<List<Pokemon>> getAllPokemon() { return allPokemon;}

    public void insert(Pokemon pokemon) {
        PokemonRoomDatabase.databaseWriteExecutor.execute(() -> {
            pokemonDao.insertPokemon(pokemon);
        });
    }

}
