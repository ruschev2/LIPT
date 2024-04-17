/**
 * Luis Hernandez
 * April 1, 2024
 * PlayerDAO.java, this interface describes the Data Access Object for the player entity
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
public interface PlayerDao {

    //for creating new player account
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlayer(Player player);

    //for updating a player
    @Update()
    void updatePlayer(Player player);

    //for deleting existing player account
    @Delete()
    void deletePlayer(Player player);

    //for deleting existing player's account, by playerId
    @Query("DELETE FROM player_table WHERE id = :playerId")
    void deletePlayerById(int playerId);

    //for retrieving the entire list of players
    @Query("SELECT * FROM player_table ORDER BY id ASC")
    LiveData<List<Player>> getPlayerList();

    //for retrieving a specific player by ID
    @Query("SELECT * FROM player_table WHERE id = :playerId")
    Player getPlayerById(int playerId);

    //for retrieving a specific player by username
    @Query("SELECT * from player_table WHERE username = :userName")
    Player getPlayerByUsername(String userName);

    @Query("SELECT * FROM player_table WHERE id = :playerId")
    LiveData<Player> getPlayerLiveDataById(int playerId);


    @Query("SELECT * FROM player_table WHERE admin = true")
    LiveData<List<Player>> getAllAdmins();


}