/**
 * Luis Hernandez
 * April 1, 2024
 * PlayerDAO.java, this interface describes the Data Access Object for the player entity
 */

package com.example.lipt;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface PlayerDao {

    //for creating new player account
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Player player);

    //for deleting existing player account [ADMIN ONLY]
    @Delete()
    void deletePlayer(Player player);
}
