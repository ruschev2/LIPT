/**
 * Luis Hernandez, Guillermo Zendejas
 * April 4, 2024
 * PrizeRoomDatabase.java, this class describes our prize room data
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

@Database(entities = {Prize.class}, version = 1, exportSchema = false)
public abstract class PrizeRoomDatabase extends RoomDatabase {

    //instantiating our Data Access Object for data manipulation
    public abstract PrizeDao prizeDao();

    private static volatile PrizeRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static PrizeRoomDatabase getDatabase(final Context context) {
        //make sure that this class is not active elsewhere at this time
        //lock this operation into a single thread
        //parameter = reference to compiled version to the class we are working in
        if(INSTANCE == null) {
            synchronized (PrizeRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    PrizeRoomDatabase.class, "prize_database")
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
            });
        }
    };
}
