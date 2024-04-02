/**
 * Luis Hernandez, Guillermo Zendejas
 * April 1, 2024
 * PokeRoomDatabase.java, this abstract class describes the room database for our application
 */

package com.example.lipt;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Player.class}, version = 1, exportSchema = false)
public abstract class PokeRoomDatabase extends RoomDatabase {

    //instantiating our Data Access Object for data manipulation
    public abstract PlayerDao playerDao();

    private static volatile PokeRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static PokeRoomDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (PokeRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PokeRoomDatabase.class, "poke_database").build();
                }
            }
        }
        return INSTANCE;
    }

}

