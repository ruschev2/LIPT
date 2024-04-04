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

    public Pokemon(int pokedexNum, String name, String imageFilepath, String soundFilepath) {
        this.pokedexNumber = pokedexNum;
    }

    @PrimaryKey
    @ColumnInfo(name = "pokedexNumber")
    private int pokedexNumber;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "soundFilepath")
    private String sound_filepath;

    @ColumnInfo(name = "imageFilepath")
    private String image_filepath;

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

    public String getSound_filepath() {
        return sound_filepath;
    }

    public void setSound_filepath(String sound_filepath) {
        this.sound_filepath = sound_filepath;
    }

    public String getImage_filepath() {
        return image_filepath;
    }

    public void setImage_filepath(String image_filepath) {
        this.image_filepath = image_filepath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon pokemon = (Pokemon) o;
        return pokedexNumber == pokemon.pokedexNumber && Objects.equals(name, pokemon.name) && Objects.equals(sound_filepath, pokemon.sound_filepath) && Objects.equals(image_filepath, pokemon.image_filepath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pokedexNumber, name, sound_filepath, image_filepath);
    }
}
