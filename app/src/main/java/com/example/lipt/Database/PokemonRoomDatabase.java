/**
 * Luis Hernandez, Guillermo Zendejas
 * April 3, 2024
 * PokemonRoomRepository.java, this abstract class describes the room database for Pokemon
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


@Database(entities = {Pokemon.class}, version = 1, exportSchema = false)
public abstract class PokemonRoomDatabase extends RoomDatabase {

    //instantiating our Data Access Object for data manipulation
    public abstract PokemonDao pokemonDAO();

    private static volatile PokemonRoomDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static PokemonRoomDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (PokemonRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    PokemonRoomDatabase.class, "pokemon_database")
                    .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    //popluating database with default values
    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.i(MainActivity.TAG, "DATABASE CREATED!");
            databaseWriteExecutor.execute(() -> {
                PokemonDao dao = INSTANCE.pokemonDAO();
                Pokemon pokemon = new Pokemon(1, "Bulbasaur", 0, 0);
                dao.insert(pokemon);
            });
        }
    };

}
