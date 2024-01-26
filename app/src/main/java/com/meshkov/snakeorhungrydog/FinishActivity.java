package com.meshkov.snakeorhungrydog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FinishActivity extends AppCompatActivity {
    private Boolean isWin;
    private TextView resultTextView;
    private String resultText;
    private static final int WAIT_SEC = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
    }

    private void init() {
        isWin = getIntent().getBooleanExtra("isWin", false);
        resultTextView = findViewById(R.id.resultTextView);

        if (isWin) {
            resultText = getResources().getString(R.string.result_win);
        } else {
            resultText = getResources().getString(R.string.result_lose);
        }
        resultTextView.setText(resultText);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(WAIT_SEC);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                startMainActivity();
            }
        });
        thread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    private void startMainActivity() {
        Intent intent = MainActivity.NewIntent(this);
        startActivity(intent);
    }

    public static Intent NewIntent(Context context, Boolean isWin) {
        Intent intent = new Intent(context, FinishActivity.class);
        intent.putExtra("isWin", isWin);
        return intent;
    }
}