/**
 * Luis Hernandez, Guillermo Zendejas
 * April 1, 2024
 * PokeRoomDatabase.java, this abstract class describes the room database for our application
 */

package com.example.lipt.Database;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.lipt.MainActivity;

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
        //make sure that this class is not active elsewhere at this time
        //lock this operation into a single thread
        //parameter = reference to compiled version to the class we are working in
        if(INSTANCE == null) {
            synchronized (PokeRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PokeRoomDatabase.class, "poke_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    //populating the database with default values
    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.i(MainActivity.TAG, "DATABASE CREATED!");
            databaseWriteExecutor.execute(() -> {
                PlayerDao dao = INSTANCE.playerDao();
                Player player = new Player(0, "admin1", "password123", true);
                dao.insert(player);
                player = new Player(1, "player1", "password123", false);
                dao.insert(player);
            });
        }
    };


    //android room with a view guide method
    /*
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {

                PlayerDao dao = INSTANCE.playerDao();

                Player player = new Player(0, "admin1", "password123", true);
                dao.insert(player);
                player = new Player(1, "player1", "password123", false);
                dao.insert(player);
            });
        }
    }; */

}

