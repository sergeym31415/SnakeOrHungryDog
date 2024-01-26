package com.meshkov.snakeorhungrydog;

import android.content.Context;
import android.media.MediaPlayer;

public class PlaySound {
    MediaPlayer mediaPlayerSoundEat;
    MediaPlayer mediaPlayerSoundHeart;
    MediaPlayer mediaPlayerSoundEnemy;
    MediaPlayer mediaPlayerSoundGameCompleted;
    MediaPlayer mediaPlayerSoundButtonPressed;

    public void playSoundButtonPressed(Context context) {
        if (mediaPlayerSoundButtonPressed == null){
            mediaPlayerSoundButtonPressed = MediaPlayer.create(context, R.raw.button);
        }
        if (!mediaPlayerSoundButtonPressed.isPlaying()) {
            mediaPlayerSoundButtonPressed.start();
        }
    }

    public void playSoundEat(Context context) {
        if (mediaPlayerSoundEat == null){
            mediaPlayerSoundEat = MediaPlayer.create(context, R.raw.eat);
        }
        if (!mediaPlayerSoundEat.isPlaying()) {
            mediaPlayerSoundEat.start();
        }
    }

    public void playSoundGameCompleted(Context context) {
        if (mediaPlayerSoundGameCompleted == null){
            mediaPlayerSoundGameCompleted = MediaPlayer.create(context, R.raw.level_completed);
        }
        if (!mediaPlayerSoundGameCompleted.isPlaying()) {
            mediaPlayerSoundGameCompleted.start();
        }
    }
    public void playSoundEatHeart(Context context) {
        if (mediaPlayerSoundHeart == null){
            mediaPlayerSoundHeart = MediaPlayer.create(context, R.raw.heart);
        }
        if (!mediaPlayerSoundHeart.isPlaying()) {
            mediaPlayerSoundHeart.start();
        }
    }

    public void playSoundEnemyPunch(Context context) {
        if (mediaPlayerSoundEnemy == null){
            mediaPlayerSoundEnemy = MediaPlayer.create(context, R.raw.punch);
        }
        if (!mediaPlayerSoundEnemy.isPlaying()) {
            mediaPlayerSoundEnemy.start();
        }
    }
}
