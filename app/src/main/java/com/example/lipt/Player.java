/**
 * Luis Hernandez, Guillermo Zendejas
 * April 1, 2024
 * Player.java, this class describes the player database entity
 */

package com.example.lipt;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "player_table")
public class Player {

    @PrimaryKey
    @ColumnInfo
    private int mUserID;
    public Player(int userID, String username, String password, boolean admin) {
        this.mUserID = userID;
        this.mUsername = username;
        this.mPassword = password;
        this.trainer_level = 0;
        this.accuracy = 0.0;
        this.rounds_played = 0;
        this.isAdmin = admin;
    }

    @ColumnInfo(name = "username")
    private String mUsername;

    @ColumnInfo(name = "password")
    private String mPassword;

    @ColumnInfo(name = "trainerLevel")
    private int trainer_level;

    @ColumnInfo(name = "admin")
    private boolean isAdmin;

    @ColumnInfo(name = "accuracy")
    private double accuracy;

    @ColumnInfo(name = "roundsPlayed")
    private int rounds_played;

}
