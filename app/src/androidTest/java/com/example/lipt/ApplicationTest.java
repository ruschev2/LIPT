package com.example.lipt;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.lipt.Database.Player;
import com.example.lipt.Database.PlayerDao;
import com.example.lipt.Database.PlayerPrizeCrossRef;
import com.example.lipt.Database.PlayerPrizeCrossRefDao;
import com.example.lipt.Database.PlayerRoomDatabase;
import com.example.lipt.Database.Pokemon;
import com.example.lipt.Database.PokemonDao;
import com.example.lipt.Database.PokemonRoomDatabase;
import com.example.lipt.Database.Prize;
import com.example.lipt.Database.PrizeDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ApplicationTest {

    private PlayerRoomDatabase db;
    private PokemonRoomDatabase db2;
    private PlayerDao playerDao;
    private PrizeDao prizeDao;
    private PokemonDao pokemonDao;
    private PlayerPrizeCrossRefDao playerPrizeDao;

    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, PlayerRoomDatabase.class).build();
        db2 = Room.inMemoryDatabaseBuilder(context, PokemonRoomDatabase.class).build();
        playerDao = db.playerDao();
        prizeDao = db.prizeDao();
        playerPrizeDao = db.playerPrizeCrossRefDao();
        pokemonDao = db2.pokemonDAO();
    }

    @After
    public void tearDown() throws Exception {
        db.close();
        db2.close();
    }

    @Test
    public void insertPlayer() {
        Player player = new Player(1, "Gumby", "Hello123", false);
        playerDao.insertPlayer(player);

        Player grabbedPlayer = playerDao.getPlayerByUsername("Gumby");
        assertNotNull(grabbedPlayer);
        assertEquals("Gumby", grabbedPlayer.getUsername());
    }

    @Test
    public void updatePlayer() {
        Player player = new Player(1, "Pokey", "Goodbye456", false);
        playerDao.insertPlayer(player);
        assertNotNull(playerDao.getPlayerById(1));
        Player player2 = new Player(1, "Pokey", "Hello345", false);
        playerDao.updatePlayer(player2);

        Player grabbedPlayer = playerDao.getPlayerById(1);
        assertNotNull(grabbedPlayer);
        assertEquals("Hello345", grabbedPlayer.getPassword());
    }

    @Test
    public void deletePlayer() {
        Player player = new Player(1, "Gumby", "Hello123", false);
        playerDao.insertPlayer(player);
        assertNotNull(playerDao.getPlayerById(1));

        playerDao.deletePlayer(player);

        Player deletedPlayer = playerDao.getPlayerByUsername("Gumby");
        assertNull(deletedPlayer);
    }

    @Test
    public void insertPrize() {
        Prize prize = new Prize(1, "Cool Prize", 123);
        prizeDao.insertPrize(prize);

        Prize grabbedPrize = prizeDao.getPrizeById(1);
        assertNotNull(grabbedPrize);
        assertEquals("Cool Prize", grabbedPrize.getName());
    }

    @Test
    public void updatePrize() {
        Prize prize = new Prize(1, "Cooler Prize", 123);
        prizeDao.insertPrize(prize);
        assertNotNull(prizeDao.getPrizeById(1));
        Prize prize2 = new Prize(1, "Coolest Prize", 123);
        prizeDao.updatePrize(prize2);

        Prize grabbedPrize = prizeDao.getPrizeById(1);
        assertNotNull(grabbedPrize);
        assertEquals("Coolest Prize", grabbedPrize.getName());

    }

    @Test
    public void deletePrize() {
        Prize prize = new Prize(1, "Cooler Prize", 123);
        prizeDao.insertPrize(prize);
        assertNotNull(prizeDao.getPrizeById(1));

        prizeDao.deletePrize(prize);

        Prize deletedPrize = prizeDao.getPrizeById(1);
        assertNull(deletedPrize);

    }

    @Test
    public void insertPlayerPrizeCrossRef() {
        Player testPlayer = new Player(1, "John123", "Wick3456", false );
        Prize testPrize = new Prize(2, "Cool Prize", 123);
        playerDao.insertPlayer(testPlayer);
        prizeDao.insertPrize(testPrize);

        PlayerPrizeCrossRef playerPrizeRef = new PlayerPrizeCrossRef(1, 2);
        playerPrizeDao.insertPlayerPrizeCrossRef(playerPrizeRef);

        PlayerPrizeCrossRef grabbedRef = playerPrizeDao.getReference(1, 2);
        assertNotNull(grabbedRef);
        assertEquals(1, grabbedRef.getPlayerId());

    }

    /**
     * SINCE PLAYERPRIZECROSSREF IS A JUNCTION TABLE WITH ONLY TWO FIELDS (PRIMARY/FOREIGN KEYS)
     * IN A MANY TO MANY RELATION, AN UPDATE METHOD IS REDUNDANT
     */

    @Test
    public void deleteplayerPrizeCrossRef() {
        Player testPlayer = new Player(1, "John123", "Wick3456", false );
        Prize testPrize = new Prize(2, "Cool Prize", 123);
        playerDao.insertPlayer(testPlayer);
        prizeDao.insertPrize(testPrize);

        PlayerPrizeCrossRef playerPrizeRef = new PlayerPrizeCrossRef(1, 2);
        playerPrizeDao.insertPlayerPrizeCrossRef(playerPrizeRef);
        assertNotNull(playerPrizeDao.getReference(1, 2));

        playerPrizeDao.deletePlayerPrizeCrossRef(playerPrizeRef);

        PlayerPrizeCrossRef deletedRef = playerPrizeDao.getReference(1, 2);
        assertNull(deletedRef);
    }

    @Test
    public void insertPokemon() {
        Pokemon pokemon = new Pokemon(1, "Bulbasaur", 123, 123);
        pokemonDao.insertPokemon(pokemon);

        Pokemon grabbedPokemon = pokemonDao.getPokemonById(1);
        assertNotNull(grabbedPokemon);
        assertEquals("Bulbasaur", grabbedPokemon.getName());
    }

    @Test
    public void updatePokemon() {
        Pokemon pokemon = new Pokemon(1, "Bulbasaur", 123, 123);
        pokemonDao.insertPokemon(pokemon);
        assertNotNull(pokemonDao.getPokemonById(1));
        Pokemon pokemon2 = new Pokemon(1, "Not Bulbasaur", 123, 123);
        pokemonDao.updatePokemon(pokemon2);

        Pokemon grabbedPokemon = pokemonDao.getPokemonById(1);
        assertNotNull(grabbedPokemon);
        assertEquals("Not Bulbasaur", grabbedPokemon.getName());

    }

    @Test
    public void deletePokemon() {
        Pokemon pokemon = new Pokemon(1, "Bulbasaur", 123, 123);
        pokemonDao.insertPokemon(pokemon);
        assertNotNull(pokemonDao.getPokemonById(1));

        pokemonDao.deletePokemon(pokemon);
        Pokemon deletedPokemon = pokemonDao.getPokemonById(1);
        assertNull(deletedPokemon);
    }


}