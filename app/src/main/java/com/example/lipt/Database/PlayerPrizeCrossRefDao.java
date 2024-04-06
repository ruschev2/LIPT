/**
 * Luis Hernandez, Guillermo Zendejas
 * April 6, 2024
 * PlayerPrizeCrossRefDao.java, this class describes the data access object for the junction table
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
public interface PlayerPrizeCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPlayerPrizeCrossRef(PlayerPrizeCrossRef playerPrizeCrossRef);

    @Update()
    void updatePlayerPrizeCrossRef(PlayerPrizeCrossRef playerPrizeCrossRef);

    @Delete()
    void deletePlayerPrizeCrossRef(PlayerPrizeCrossRef playerPrizeCrossRef);

    @Query("SELECT * FROM player_prize_cross_ref ORDER BY playerId ASC")
    LiveData<List<PlayerPrizeCrossRef>> getAllPlayerPrizeCrossRefs();

    @Query("SELECT prizeId FROM player_prize_cross_ref WHERE playerId = :playerId")
    List<Integer> getPlayerPrizeIdsForPlayer(int playerId);

    @Query("DELETE FROM player_prize_cross_ref WHERE playerId = :playerId")
    void deleteByPlayerId(int playerId);

}