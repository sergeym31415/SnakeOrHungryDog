package com.meshkov.snakeorhungrydog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Boolean soundOn = true;
    private static final String APP_PREFERENCES_SOUND_ON = "SOUND_ON";
    private SwitchCompat switchSound;
    private final PlaySound playSound = new PlaySound();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initViews();
    }

    public static Intent NewIntent(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        return intent;
    }

    private void initViews() {
        switchSound = findViewById(R.id.switchSound);

        prefs = getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = prefs.edit();
        if (prefs.contains(APP_PREFERENCES_SOUND_ON)) {
            soundOn = prefs.getBoolean(APP_PREFERENCES_SOUND_ON, true);
        }

        switchSound.setChecked(soundOn);
        switchSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSwitchSound();
            }
        });
    }

    private void clickSwitchSound() {
        playSoundButton();
        soundOn = !soundOn;
        editor.putBoolean(APP_PREFERENCES_SOUND_ON, soundOn).apply();
    }

    private void playSoundButton() {
        if (soundOn) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    playSound.playSoundButtonPressed(SettingsActivity.this);
                }
            });
            thread.start();
        }
    }
}