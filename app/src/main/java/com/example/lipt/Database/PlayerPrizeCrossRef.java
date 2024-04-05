/**
 * Luis Hernandez, Guillermo Zendejas
 * April 4, 2024
 * PlayerPrizeCrossRef.java, this describes the junction table between player and prize
 */

package com.example.lipt.Database;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "player_prize_cross_ref",
        primaryKeys = {"playerId", "prizeId"},
        foreignKeys = {
                    @ForeignKey(entity = Player.class,
                                parentColumns = "id",
                                childColumns = "playerId"),
                    @ForeignKey(entity = Prize.class,
                                parentColumns = "id",
                                childColumns = "prizeId")
        })
public class PlayerPrizeCrossRef {

    private int playerId;
    private int prizeId;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(int prizeId) {
        this.prizeId = prizeId;
    }
}




