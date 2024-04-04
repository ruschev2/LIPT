/**
 * Luis Hernandez, Guillermo Zendejas
 * April 3, 2024
 * Pokemon.java, this class describes the Pokemon entity
 */

package com.example.lipt.Database;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Objects;

@Entity(tableName = "pokemon_table")
public class Pokemon {

    public Pokemon(int pokedexNumber, String name, int imageResourceId, int soundResourceId) {
        this.pokedexNumber = pokedexNumber;
        this.name = name;
        this.imageResourceId = imageResourceId;
        this.soundResourceId = soundResourceId;
    }

    @PrimaryKey
    @ColumnInfo(name = "pokedexNumber")
    private int pokedexNumber;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "soundId")
    private int soundResourceId;

    @ColumnInfo(name = "imageId")
    private int imageResourceId;

    public int getPokedexNumber() {
        return pokedexNumber;
    }

    public void setPokedexNumber(int pokedexNumber) {
        this.pokedexNumber = pokedexNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSoundResourceId() {
        return soundResourceId;
    }

    public void setSoundResourceId(int soundResourceId) {
        this.soundResourceId = soundResourceId;
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
        Pokemon pokemon = (Pokemon) o;
        return pokedexNumber == pokemon.pokedexNumber && soundResourceId == pokemon.soundResourceId && imageResourceId == pokemon.imageResourceId && Objects.equals(name, pokemon.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pokedexNumber, name, soundResourceId, imageResourceId);
    }
}
