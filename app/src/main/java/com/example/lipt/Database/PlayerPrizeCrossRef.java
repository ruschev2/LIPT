/**
 * Luis Hernandez, Guillermo Zendejas
 * April 4, 2024
 * PlayerPrizeCrossRef.java, this describes the junction table between player and prize
 */

package com.example.lipt.Database;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "player_prize_cross_ref",
        primaryKeys = {"playerId", "prizeId"},
        foreignKeys = {
                    @ForeignKey(entity = Player.class,
                                parentColumns = "id",
                                childColumns = "playerId",
                                onDelete = ForeignKey.CASCADE),
                    @ForeignKey(entity = Prize.class,
                                parentColumns = "id",
                                childColumns = "prizeId")
        },
        indices = {
                @Index("playerId"),
                @Index("prizeId")
        })
public class PlayerPrizeCrossRef {

    public PlayerPrizeCrossRef(int playerId, int prizeId) {
        this.playerId = playerId;
        this.prizeId = prizeId;
    }

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




