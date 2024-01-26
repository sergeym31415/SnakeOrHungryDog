package com.meshkov.snakeorhungrydog;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "level_records")
public class Level implements Serializable {
    private int need_level_score;
    private int life_of_bone_in_seconds;
    private int life_of_heart_in_seconds;
    private int life_of_enemy_in_seconds;
    private int bone_not_active_in_seconds;
    private int heart_not_active_in_seconds;
    private int enemy_not_active_in_seconds;
    private float speed;
    private String color;
    @PrimaryKey
    private int number;
    @ColumnInfo(name = "best_time", defaultValue = "-1")
    private long bestTime;

    public Level() {
    }

    public long getBestTime() {
        return bestTime;
    }

    public void setBestTime(long bestTime) {
        this.bestTime = bestTime;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getNeed_level_score() {
        return need_level_score;
    }

    public void setNeed_level_score(int need_level_score) {
        this.need_level_score = need_level_score;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getLife_of_bone_in_seconds() {
        return life_of_bone_in_seconds;
    }

    public void setLife_of_bone_in_seconds(int life_of_bone_in_seconds) {
        this.life_of_bone_in_seconds = life_of_bone_in_seconds;
    }

    public int getLife_of_heart_in_seconds() {
        return life_of_heart_in_seconds;
    }

    public void setLife_of_heart_in_seconds(int life_of_heart_in_seconds) {
        this.life_of_heart_in_seconds = life_of_heart_in_seconds;
    }

    public int getLife_of_enemy_in_seconds() {
        return life_of_enemy_in_seconds;
    }

    public void setLife_of_enemy_in_seconds(int life_of_enemy_in_seconds) {
        this.life_of_enemy_in_seconds = life_of_enemy_in_seconds;
    }

    public int getBone_not_active_in_seconds() {
        return bone_not_active_in_seconds;
    }

    public void setBone_not_active_in_seconds(int bone_not_active_in_seconds) {
        this.bone_not_active_in_seconds = bone_not_active_in_seconds;
    }

    public int getHeart_not_active_in_seconds() {
        return heart_not_active_in_seconds;
    }

    public void setHeart_not_active_in_seconds(int heart_not_active_in_seconds) {
        this.heart_not_active_in_seconds = heart_not_active_in_seconds;
    }

    public int getEnemy_not_active_in_seconds() {
        return enemy_not_active_in_seconds;
    }

    public void setEnemy_not_active_in_seconds(int enemy_not_active_in_seconds) {
        this.enemy_not_active_in_seconds = enemy_not_active_in_seconds;
    }
}
