package com.meshkov.snakeorhungrydog;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ScreenLevelActivity extends AppCompatActivity {
    private Level level;
    private Boolean isNewRecord;
    private TextView levelTextView;
    private TextView recordTextView;
    private static final int WAIT_SEC = 1100;
    private String levelResource;
    private String recordResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_level);
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        level = (Level) getIntent().getSerializableExtra("level");
        isNewRecord = getIntent().getBooleanExtra("isNewRecord", false);
        levelResource = getString(R.string.level_num);
        recordResource = getString(R.string.new_record);
        levelTextView = findViewById(R.id.levelTextView);
        recordTextView = findViewById(R.id.recordTextView);
        levelTextView.setText(levelResource + "" + level.getNumber());
        if (isNewRecord) {
            recordTextView.setText(recordResource);
        } else {
            recordTextView.setText("");
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(WAIT_SEC);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                startGameActivity(level);
            }
        });
        thread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    private void startGameActivity(Level level) {
        Intent intent = GameActivity.NewIntent(this, level);
        startActivity(intent);
    }

    public static Intent NewIntent(Context context, Level level, Boolean isNewRecord) {
        Intent intent = new Intent(context, ScreenLevelActivity.class);
        intent.putExtra("level", level);
        intent.putExtra("isNewRecord", isNewRecord);
        return intent;
    }
}