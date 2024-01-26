package com.meshkov.snakeorhungrydog;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LevelsDao {
    @Query("SELECT * FROM level_records")
    List<Level> getLevels();

    @Query("SELECT best_time FROM level_records WHERE number = :numberLevel")
    long getBestTimeByNumber(int numberLevel);

    @Insert
    void insert(Level level);
    @Update
    void update(Level level);
}
