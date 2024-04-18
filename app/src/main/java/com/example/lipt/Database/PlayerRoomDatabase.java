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

@Database(entities = {Player.class, Prize.class, PlayerPrizeCrossRef.class}, version = 5, exportSchema = false)
public abstract class PlayerRoomDatabase extends RoomDatabase {

    //instantiating our Data Access Object for data manipulation
    public abstract PlayerDao playerDao();
    public abstract PrizeDao prizeDao();
    public abstract PlayerPrizeCrossRefDao playerPrizeCrossRefDao();
    private static volatile PlayerRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static PlayerRoomDatabase getDatabase(final Context context) {
        //make sure that this class is not active elsewhere at this time
        //lock this operation into a single thread
        //parameter = reference to compiled version to the class we are working in
        if(INSTANCE == null) {
            synchronized (PlayerRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlayerRoomDatabase.class, "player_database")
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
            Log.i(MainActivity.TAG, "MAIN DATABASE CREATED!");
            databaseWriteExecutor.execute(() -> {
                PlayerDao dao = INSTANCE.playerDao();
                Player player = new Player(1, "admin1", "password123", true);
                player.setTrainer_level(15);
                dao.insertPlayer(player);
                Player player2 = new Player(2, "player1", "password123", false);
                player2.setTrainer_level(10);
                dao.insertPlayer(player2);
                Player player3 = new Player(3, "player2", "password123", false);
                player3.setTrainer_level(20);
                dao.insertPlayer(player3);
            });
        }
    };

}

