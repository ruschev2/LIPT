/**
 * Luis Hernandez, Guillermo Zendejas
 * April 3, 2024
 * PokemonDao.java, this class describes the Data Access Object for the Pokemon entity
 */

package com.example.lipt.Database;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PokemonDao {

    //for creating a new pokemon
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Pokemon pokemon);

    //for deleting an existing pokemon
    @Delete()
    void deletePokemon(Pokemon pokemon);

    //for updating an existing pokemon's values
    @Update
    void updatePokemon(Pokemon pokemon);

    //for retrieving the entire list of Pokemon
    @Query("SELECT * FROM pokemon_table ORDER BY pokedexNumber ASC")
    LiveData<List<Pokemon>> getPokemonlist();
}
