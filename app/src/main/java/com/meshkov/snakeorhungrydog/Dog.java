package com.meshkov.snakeorhungrydog;

public class Dog {
    private float xHead;
    private float yHead;
    private String direction;
    private int score;
    private int life;

    public Dog() {
        score = 0;
        life = 1;
        xHead = 0;
        yHead = 1;
        direction = "RIGHT";
    }

    public float getxHead() {
        return xHead;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public float getyHead() {
        return yHead;
    }

    public void setxHead(float xHead) {
        this.xHead = xHead;
    }

    public void setyHead(float yHead) {
        this.yHead = yHead;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }
}
