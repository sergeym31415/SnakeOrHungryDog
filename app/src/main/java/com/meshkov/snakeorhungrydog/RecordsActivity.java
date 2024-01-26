package com.meshkov.snakeorhungrydog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RecordsActivity extends AppCompatActivity {
    private ListView listRecords;
    private LevelDatabase levelDatabase;
    private ArrayList<Level> levels;
    private String[] levelsString;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onResume() {
        super.onResume();
        updateRecords();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        listRecords = findViewById(R.id.listRecords);
        updateRecords();
    }

    private void updateRecords() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                getRecordsFromDb();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        insertToList();
                    }
                });
            }
        });
        thread.start();
    }

    public static Intent NewIntent(Context context) {
        Intent intent = new Intent(context, RecordsActivity.class);
        return intent;
    }

    @SuppressLint("DefaultLocale")
    private void getRecordsFromDb() {
        levelDatabase = LevelDatabase.getInstance(getApplication());
        levels = (ArrayList<Level>) levelDatabase.levelsDao().getLevels();
        levelsString = new String[levels.size()];
        int i = 0;
        for (Level level : levels) {
            levelsString[i] = getString(R.string.level_num) + level.getNumber() +
                    ": " + String.format("%.1f", level.getBestTime() / 1000.0) + " " +
                    getString(R.string.secs);
            i++;
        }
    }

    private void insertToList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, levelsString) {
        };
        listRecords.setAdapter(adapter);
    }
}