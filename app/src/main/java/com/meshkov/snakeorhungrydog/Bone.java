package com.meshkov.snakeorhungrydog;

import java.util.Date;
import java.util.Random;

public class Bone implements GameObject {
    private final int nominal;
    private int width;
    private boolean isActive;
    private int x;
    private int y;
    private int color;
    private Date timeCreated;
    private Date timeIsNotActive;

    public Date getTimeIsNotActive() {
        return timeIsNotActive;
    }

    public void setTimeIsNotActive(Date timeIsNotActive) {
        this.timeIsNotActive = timeIsNotActive;
    }

    public int getNominal() {
        return nominal;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getColor() {
        return color;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public int getWidth() {
        return width;
    }

    public Bone(int nominal, int width, int x, int y) {
        Random random = new Random();
        color = random.nextInt(3);
        this.nominal = nominal;
        this.width = width;
        this.x = x;
        this.y = y;
        isActive = true;
        timeCreated = new Date();
        timeIsNotActive = new Date();
    }

    public Bone(int nominal) {
        this.nominal = nominal;
        isActive = false;
        timeCreated = new Date();
        timeIsNotActive = new Date();
    }
}
