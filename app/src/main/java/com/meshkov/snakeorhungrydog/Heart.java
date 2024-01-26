package com.meshkov.snakeorhungrydog;

import java.util.Date;

public class Heart implements GameObject {
    private final int nominal;
    private int width;
    private boolean isActive;
    private int x;
    private int y;
    private Date timeCreated;
    private Date timeIsNotActive;

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

    public Date getTimeIsNotActive() {
        return timeIsNotActive;
    }

    public void setTimeIsNotActive(Date timeIsNotActive) {
        this.timeIsNotActive = timeIsNotActive;
    }

    public Heart(int nominal, int width, int x, int y) {
        this.nominal = nominal;
        this.width = width;
        this.x = x;
        this.y = y;
        isActive = true;
        timeCreated = new Date();
        timeIsNotActive = new Date();
    }

    public Heart(int nominal) {
        this.nominal = nominal;
        isActive = false;
        timeCreated = new Date();
        timeIsNotActive = new Date();
    }
}
