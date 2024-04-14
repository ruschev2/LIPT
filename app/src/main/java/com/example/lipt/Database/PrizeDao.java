/**
 * Luis Hernandez, Guillermo Zendejas
 * April 4, 2024
 * PrizeDao.java, this interface defines the prize data access object
 */

package com.example.lipt.Database;

import android.content.DialogInterface;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PrizeDao {

    //for creating new prize
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPrize(Prize prize);

    //for updating a prize
    @Update()
    void updatePrize(Prize prize);

    //for deleting existing prizes
    @Delete()
    void deletePrize(Prize prize);

    //for retrieving the entire list of prizes, wrapped in livedata
    @Query("SELECT * FROM prize_table ORDER BY id ASC")
    LiveData<List<Prize>> getPrizeList();

    //for retrieving the entire list of prizes, not wrapped in livedata
    @Query("SELECT * FROM prize_table ORDER BY id ASC")
    List<Prize> getFullPrizeList();

    //for retrieving a specific prize by its ID
    @Query("SELECT * FROM prize_table WHERE id = :prizeId")
    Prize getPrizeById(int prizeId);

}
