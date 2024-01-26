package com.meshkov.snakeorhungrydog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private Button buttonNewGame;
    private Button buttonContinueGame;
    private Button buttonSettings;
    private Button buttonRecords;
    private ImageView imageViewLogo;
    private SharedPreferences prefs;
    private Boolean soundOn = true;
    private final PlaySound playSound = new PlaySound();
    private static final String APP_PREFERENCES_SOUND_ON = "SOUND_ON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("settings", Context.MODE_PRIVATE);
        if (prefs.contains(APP_PREFERENCES_SOUND_ON)) {
            soundOn = prefs.getBoolean(APP_PREFERENCES_SOUND_ON, true);
        }
        initViews();
    }

    private void playSoundButton() {
        if (soundOn) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    playSound.playSoundButtonPressed(MainActivity.this);
                }
            });
            thread.start();
        }
    }

    private void initViews() {
        buttonNewGame = findViewById(R.id.buttonNewGame);
        buttonSettings = findViewById(R.id.buttonSettings);
        buttonContinueGame = findViewById(R.id.buttonContinueGame);
        buttonRecords = findViewById(R.id.buttonRecords);
        imageViewLogo = findViewById(R.id.imageViewLogo);
        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSoundButton();
                startScreenLevelActivity();
            }
        });
        buttonContinueGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSoundButton();
                Intent intent = ContinueActivity.NewIntent(MainActivity.this);
                startActivity(intent);
            }
        });
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSoundButton();
                startSettingsActivity();
            }
        });
        buttonRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSoundButton();
                startRecordsActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public static Intent NewIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    private void startScreenLevelActivity() {
        Level level = LevelXML.getLevelParamsFromXML(this, 1);

        Intent intent = ScreenLevelActivity.NewIntent(this, level, false);
        startActivity(intent);
    }

    private void startSettingsActivity() {
        Intent intent = SettingsActivity.NewIntent(this);
        startActivity(intent);
    }

    private void startRecordsActivity() {
        Intent intent = RecordsActivity.NewIntent(this);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.contains(APP_PREFERENCES_SOUND_ON)) {
            soundOn = prefs.getBoolean(APP_PREFERENCES_SOUND_ON, true);
        }
    }

}