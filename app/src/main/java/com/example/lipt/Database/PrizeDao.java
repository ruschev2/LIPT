/**
 * Luis Hernandez, Guillermo Zendejas
 * April 4, 2024
 * PrizeDao.java, this interface defines the prize data access object
 */

package com.example.lipt.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PrizeDao {

    //for creating new prize
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPrize(Prize prize);

    //for deleting existing prizes
    @Delete()
    void deletePrize(Prize prize);

    //for retrieving the entire list of players

    @Query("SELECT * FROM prize_table ORDER BY id ASC")
    LiveData<List<Prize>> getPrizeList();

}
