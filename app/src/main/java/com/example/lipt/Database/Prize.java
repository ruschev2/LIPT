/**
 * Luis Hernandez, Guillermo Zendejas
 * April 4, 2024
 * Prize.java, this class describes the prize item awarded to players during gameplay
 */

package com.example.lipt.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Junction;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "prize_table")
public class Prize {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int prizeID;

    private String name;
    private int imageResourceId;

    public Prize(String name, int imageResourceId) {
        this.name = name;
        this.imageResourceId = imageResourceId;
    }



    public int getPrizeID() {
        return prizeID;
    }

    public void setPrizeID(int prizeID) {
        this.prizeID = prizeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prize prize = (Prize) o;
        return prizeID == prize.prizeID && imageResourceId == prize.imageResourceId && Objects.equals(name, prize.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prizeID, name, imageResourceId);
    }
}
