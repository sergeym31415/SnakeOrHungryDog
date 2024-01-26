package com.meshkov.snakeorhungrydog;

import java.util.Date;
import java.util.Random;

public class Enemy implements GameObject {
    private final int nominal;
    private int width;
    private boolean isActive;
    private int x;
    private int y;
    private Date timeCreated;
    private Date timeIsNotActive;
    private int color;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Enemy(int nominal, int width, int x, int y) {
        Random random = new Random();
        this.nominal = nominal;
        this.width = width;
        this.x = x;
        this.y = y;
        this.isActive = true;
        this.timeCreated = new Date();
        this.color = random.nextInt(3);
        this.timeIsNotActive = new Date();
    }

    public Enemy(int nominal) {
        this.nominal = nominal;
        this.isActive = false;
        this.timeCreated = new Date();
        this.timeIsNotActive = new Date();
    }

    public Date getTimeIsNotActive() {
        return timeIsNotActive;
    }

    public void setTimeIsNotActive(Date timeIsNotActive) {
        this.timeIsNotActive = timeIsNotActive;
    }

    public int getNominal() {
        return nominal;
    }

    public int getWidth() {
        return width;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }
}
