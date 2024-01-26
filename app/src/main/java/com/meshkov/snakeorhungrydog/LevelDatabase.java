package com.meshkov.snakeorhungrydog;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Level.class}, version = 2)
public abstract class LevelDatabase extends RoomDatabase {
    private static LevelDatabase instance = null;
    private static final String DB_NAME = "levels.db";

    public static LevelDatabase getInstance(Application application) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            application,
                            LevelDatabase.class,
                            DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract LevelsDao levelsDao();

}
