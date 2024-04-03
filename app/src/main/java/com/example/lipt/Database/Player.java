/**
 * Luis Hernandez, Guillermo Zendejas
 * April 1, 2024
 * Player.java, this class describes the player database entity
 */

package com.example.lipt.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "player_table")
public class Player {

    //constructor for Player entity
    public Player(String username, String password, boolean admin) {
        this.username = username;
        this.password = password;
        this.trainer_level = 0;
        this.accuracy = 0.0;
        this.rounds_played = 0;
        this.isAdmin = admin;
    }
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int userID;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "trainerLevel")
    private int trainer_level;

    @ColumnInfo(name = "admin")
    private boolean isAdmin;

    @ColumnInfo(name = "accuracy")
    private double accuracy;

    @ColumnInfo(name = "roundsPlayed")
    private int rounds_played;


    //accessors and manipulators

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTrainer_level() {
        return trainer_level;
    }

    public void setTrainer_level(int trainer_level) {
        this.trainer_level = trainer_level;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public int getRounds_played() {
        return rounds_played;
    }

    public void setRounds_played(int rounds_played) {
        this.rounds_played = rounds_played;
    }
}
