package com.meshkov.snakeorhungrydog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ContinueActivity extends AppCompatActivity {
    private static final String APP_PREFERENCES_MAX_PASSED_LEVEL = "MAX_PASSED_LEVEL";
    private static final String APP_PREFERENCES_SOUND_ON = "SOUND_ON";
    private int maxPassedLevel = 0;
    private Boolean soundOn = true;
    private ListView levelListView;
    private SharedPreferences prefs;
    private final PlaySound playSound = new PlaySound();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue);
        levelListView = findViewById(R.id.levelListView);
        prefs = getSharedPreferences("settings", Context.MODE_PRIVATE);
        if (prefs.contains(APP_PREFERENCES_SOUND_ON)) {
            soundOn = prefs.getBoolean(APP_PREFERENCES_SOUND_ON, true);
        }
        if (prefs.contains(APP_PREFERENCES_MAX_PASSED_LEVEL)) {
            maxPassedLevel = prefs.getInt(APP_PREFERENCES_MAX_PASSED_LEVEL, 0);
        }

        final String[] levels = new String[]{getString(R.string.level_1), getString(R.string.level_2), getString(R.string.level_3), getString(R.string.level_4), getString(R.string.level_5),
                getString(R.string.level_6), getString(R.string.level_7), getString(R.string.level_8), getString(R.string.level_9), getString(R.string.level_10)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, levels) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                String string = textView.getText().toString();
                int bound = Integer.parseInt(string.split(" ")[1]);
                if (bound > maxPassedLevel) {
                    textView.setTextColor(getResources().getColor(R.color.grey));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.black));
                }
                return view;
            }
        };
        levelListView.setAdapter(adapter);

        levelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                playSoundButton();
                String text = ((TextView) view).getText().toString();
                int levelNumber = Integer.parseInt(text.split(" ")[1]);
                if (levelNumber <= maxPassedLevel + 1) {
                    Level level = LevelXML.getLevelParamsFromXML(ContinueActivity.this, levelNumber);
                    Intent intent = GameActivity.NewIntent(ContinueActivity.this, level);
                    startActivity(intent);
                }
            }
        });
    }

    private void playSoundButton() {
        if (soundOn) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    playSound.playSoundButtonPressed(ContinueActivity.this);
                }
            });
            thread.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.contains(APP_PREFERENCES_SOUND_ON)) {
            soundOn = prefs.getBoolean(APP_PREFERENCES_SOUND_ON, true);
        }
        if (prefs.contains(APP_PREFERENCES_MAX_PASSED_LEVEL)) {
            maxPassedLevel = prefs.getInt(APP_PREFERENCES_MAX_PASSED_LEVEL, 0);
        }
    }

    public static Intent NewIntent(Context context) {
        Intent intent = new Intent(context, ContinueActivity.class);
        return intent;
    }
}