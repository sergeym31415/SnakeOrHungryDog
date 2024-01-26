package com.meshkov.snakeorhungrydog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private LevelDatabase levelDatabase;
    private long bestTimeDB;
    private static final int MAX_LEVEL_IN_GAME = 10;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Boolean soundOn = true;
    private int maxPassedLevel = 0;
    private long workingTime = 0;
    private long boneExistingTime = 0;
    private long boneNotActiveTime = 0;
    private long heartExistingTime = 0;
    private long heartNotActiveTime = 0;
    private long enemyExistingTime = 0;
    private long enemyNotActiveTime = 0;
    private static final String APP_PREFERENCES_SOUND_ON = "SOUND_ON";
    private static final String APP_PREFERENCES_MAX_PASSED_LEVEL = "MAX_PASSED_LEVEL";
    private static final int DELAY_TIME = 2; // Than bigger than slower
    private static int maxLevelScore;
    private static float dogSpeedX;
    private static float dogSpeedY;
    private static final int RADIUS_BUTTON = 200;
    private static final int OFFSET_BUTTON = 10;
    private static final float LINE_WIDTH = 2;
    private static final int STEP_GRID = 1;
    private static final int DOG_IMAGE_SOURCE_SIZE = 1000;
    private static final int ENEMY_IMAGE_SOURCE_SIZE = 600;
    private static final float TEXT_SIZE = 60;
    private static final int BONE_IMAGE_SOURCE_SIZE = 400;
    private static int centerXButton; //X CENTER OF BUTTON
    private static int centerYButton; //Y CENTER OF BUTTON
    private static final int DOG_WIDTH = 100;
    private static final int HEART_WIDTH = 100;
    private static final int ENEMY_WIDTH = 100;
    private static final int BONE_MEDIUM_WIDTH = 100;
    private static final int BONE_BIG_WIDTH = 200;
    private static final int BONE_SMALL_WIDTH = 50;

    private static int lifeOfBoneInSeconds;
    private static int lifeOfHeartInSeconds;
    private static int lifeOfEnemyInSeconds;
    private static int boneNotActiveInSeconds;
    private static int heartNotActiveInSeconds;
    private static int enemyNotActiveInSeconds;
    private static int levelNumber;
    private long levelTime;
    private DrawView drawView;
    private int colorBackground;
    private int countRows;
    private int countColumns;
    private final Dog dog = new Dog();
    private Bone bone = new Bone(1);
    private Heart heart = new Heart(1);
    private Enemy enemy = new Enemy(1);

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Level level;
    private String scoreResource;
    private String levelResource;
    private String lifeResource;
    private String timeResource;
    private String secResource;
    private String recordResource;
    private final Random random = new Random();
    private Date nowDate;
    private final Date startDate = new Date();
    private final PlaySound playSound = new PlaySound();
    private static Boolean pauseGame = false;

    private void getScreenSizes() {
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int maxX = point.x;
        int maxY = point.y;
        countColumns = maxX / STEP_GRID;
        countRows = (maxY - 2 * RADIUS_BUTTON - OFFSET_BUTTON) / STEP_GRID;
        centerXButton = maxX - RADIUS_BUTTON - OFFSET_BUTTON;
        centerYButton = maxY - RADIUS_BUTTON - OFFSET_BUTTON;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getScreenSizes();
        drawView = new DrawView(this);
        prefs = getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = prefs.edit();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setContentView(R.layout.activity_game);
        setContentView(drawView);

        initViews();

        if (prefs.contains(APP_PREFERENCES_SOUND_ON)) {
            soundOn = prefs.getBoolean(APP_PREFERENCES_SOUND_ON, true);
        }
        if (prefs.contains(APP_PREFERENCES_MAX_PASSED_LEVEL)) {
            maxPassedLevel = prefs.getInt(APP_PREFERENCES_MAX_PASSED_LEVEL, 0);
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                levelDatabase = LevelDatabase.getInstance(getApplication());
                bestTimeDB = levelDatabase.levelsDao().getBestTimeByNumber(levelNumber);
                float stepX = dogSpeedX;
                float stepY = dogSpeedY;


                while (dog.getLife() > 0 && dog.getScore() < maxLevelScore) {
                    if (!pauseGame) {
                        nowDate = new Date();
                        String direction = dog.getDirection();
                        float x = dog.getxHead();
                        float y = dog.getyHead();
                        System.out.println(direction);
                        switch (direction) {
                            case "LEFT":
                                stepX = -Math.abs(dogSpeedX);
                                stepY = 0;
                                break;
                            case "RIGHT":
                                stepX = Math.abs(dogSpeedX);
                                stepY = 0;
                                break;
                            case "UP":
                                stepX = 0;
                                stepY = -Math.abs(dogSpeedY);
                                break;
                            case "DOWN":
                                stepX = 0;
                                stepY = Math.abs(dogSpeedY);
                                break;
                        }
                        if ((x + stepX + DOG_WIDTH >= countColumns * STEP_GRID)) {
                            stepX *= -1;
                            dog.setDirection("LEFT");
                        } else if (x + stepX <= 0) {
                            stepX *= -1;
                            dog.setDirection("RIGHT");
                        } else if (y + stepY <= 0) {
                            stepY *= -1;
                            dog.setDirection("DOWN");
                        } else if (y + stepY + DOG_WIDTH >= countRows * STEP_GRID) {
                            stepY *= -1;
                            dog.setDirection("UP");
                        }
                        if (bone.isActive() &&
                                (nowDate.getTime() - bone.getTimeCreated().getTime()) / 1000.0 >
                                        lifeOfBoneInSeconds) {
                            bone.setActive(false);
                            bone.setTimeIsNotActive(new Date());
                        }
                        if (heart.isActive() &&
                                (nowDate.getTime() - heart.getTimeCreated().getTime()) / 1000.0 >
                                        lifeOfHeartInSeconds) {
                            heart.setActive(false);
                            heart.setTimeIsNotActive(new Date());
                        }
                        if (enemy.isActive() &&
                                (nowDate.getTime() - enemy.getTimeCreated().getTime()) / 1000.0 >
                                        lifeOfEnemyInSeconds) {
                            enemy.setActive(false);
                            enemy.setTimeIsNotActive(new Date());
                        }
                        if (bone.isActive() && isCollision(x, y, DOG_WIDTH, bone.getX(),
                                bone.getY(), bone.getWidth())) {
                            if (soundOn) {
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        playSound.playSoundEat(GameActivity.this);
                                    }
                                });
                                thread.start();
                            }
                            bone.setActive(false);
                            bone.setTimeIsNotActive(new Date());
                            dog.setScore(dog.getScore() + bone.getNominal());
                        }
                        if (heart.isActive() && isCollision(x, y, DOG_WIDTH, heart.getX(),
                                heart.getY(), heart.getWidth())) {
                            if (soundOn) {
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        playSound.playSoundEatHeart(GameActivity.this);
                                    }
                                });
                                thread.start();
                            }
                            heart.setActive(false);
                            heart.setTimeIsNotActive(new Date());
                            dog.setLife(dog.getLife() + heart.getNominal());
                        }
                        if (enemy.isActive() && isCollision(x, y, DOG_WIDTH, enemy.getX(),
                                enemy.getY(), enemy.getWidth())) {
                            if (soundOn) {
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        playSound.playSoundEnemyPunch(GameActivity.this);
                                    }
                                });
                                thread.start();
                            }
                            enemy.setActive(false);
                            enemy.setTimeIsNotActive(new Date());
                            dog.setLife(dog.getLife() - enemy.getNominal());
                        }
                        if (!bone.isActive() && (nowDate.getTime() - bone.getTimeIsNotActive().getTime()) / 1000.0 >
                                boneNotActiveInSeconds) {
                            int boneWidth;
                            int boneNominal = 1 + random.nextInt(3);
                            switch (boneNominal) {
                                case 1:
                                    boneWidth = BONE_SMALL_WIDTH;
                                    break;
                                case 2:
                                    boneWidth = BONE_MEDIUM_WIDTH;
                                    break;
                                default:
                                    boneWidth = BONE_BIG_WIDTH;
                                    break;
                            }
                            int boneX = random.nextInt(countColumns * STEP_GRID - boneWidth);
                            int boneY = random.nextInt(countRows * STEP_GRID - boneWidth);
                            while (isCollision(x, y, DOG_WIDTH * 3, boneX, boneY, boneWidth)) {
                                boneX = random.nextInt(countColumns * STEP_GRID - boneWidth);
                                boneY = random.nextInt(countRows * STEP_GRID - boneWidth);
                            }
                            bone = new Bone(boneNominal, boneWidth, boneX, boneY);
                        }
                        if (!heart.isActive() && (nowDate.getTime() - heart.getTimeIsNotActive().getTime()) / 1000.0 >
                                heartNotActiveInSeconds) {
                            int heartX = random.nextInt(countColumns * STEP_GRID - HEART_WIDTH);
                            int heartY = random.nextInt(countRows * STEP_GRID - HEART_WIDTH);
                            while (isCollision(x, y, DOG_WIDTH * 3, heartX, heartY, HEART_WIDTH)) {
                                heartX = random.nextInt(countColumns * STEP_GRID - HEART_WIDTH);
                                heartY = random.nextInt(countRows * STEP_GRID - HEART_WIDTH);
                            }
                            heart = new Heart(1, HEART_WIDTH, heartX, heartY);
                        }
                        if (!enemy.isActive() && (nowDate.getTime() - enemy.getTimeIsNotActive().getTime()) / 1000.0 >
                                enemyNotActiveInSeconds) {
                            int enemyX = random.nextInt(countColumns * STEP_GRID - ENEMY_WIDTH);
                            int enemyY = random.nextInt(countRows * STEP_GRID - ENEMY_WIDTH);
                            while (isCollision(x, y, DOG_WIDTH * 3, enemyX, enemyY, ENEMY_WIDTH)) {
                                enemyX = random.nextInt(countColumns * STEP_GRID - ENEMY_WIDTH);
                                enemyY = random.nextInt(countRows * STEP_GRID - ENEMY_WIDTH);
                            }
                            enemy = new Enemy(1, ENEMY_WIDTH, enemyX, enemyY);
                        }
                        dog.setxHead(x + stepX);
                        dog.setyHead(y + stepY);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                toInvalidate();
                            }
                        });
                        try {
                            Thread.sleep(DELAY_TIME);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if (dog.getLife() <= 0) {
                    Intent intent = FinishActivity.NewIntent(GameActivity.this, false);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return;
                }
                maxPassedLevel = Math.max(maxPassedLevel, levelNumber);
                editor.putInt(APP_PREFERENCES_MAX_PASSED_LEVEL, maxPassedLevel).apply();


                levelTime = (nowDate.getTime() - startDate.getTime());
                boolean isNewRecord = bestTimeDB <= 0 || levelTime < bestTimeDB;
                if (isNewRecord) {
                    level.setNumber(levelNumber);
                    level.setBestTime(levelTime);
                    if (bestTimeDB <= 0) {
                        levelDatabase.levelsDao().insert(level);
                    } else {
                        levelDatabase.levelsDao().update(level);
                    }
                }
                Intent intent;
                if (levelNumber < MAX_LEVEL_IN_GAME) {
                    intent = ScreenLevelActivity.NewIntent(GameActivity.this,
                            LevelXML.getLevelParamsFromXML(GameActivity.this, ++levelNumber),
                            isNewRecord
                    );
                } else {
                    intent = FinishActivity.NewIntent(GameActivity.this, true);
                    if (soundOn) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                playSound.playSoundGameCompleted(GameActivity.this);
                            }
                        });
                        thread.start();
                    }
                }
                startActivity(intent);
            }
        });
        thread.start();
    }

    private boolean isCollision(float x1, float y1, float width1, float x2, float y2, float width2) {
        return ((x1 <= x2 && x1 + width1 >= x2) || (x2 <= x1 && x2 + width2 >= x1))
                && ((y1 <= y2 && y1 + width1 >= y2) || (y2 <= y1 && y2 + width2 >= y1));
    }


    private void initViews() {
        level = (Level) getIntent().getSerializableExtra("level");
        colorBackground = Color.parseColor(level.getColor());
        levelNumber = level.getNumber();

        dogSpeedX = level.getSpeed();
        dogSpeedY = level.getSpeed();
        maxLevelScore = level.getNeed_level_score();
        lifeOfBoneInSeconds = level.getLife_of_bone_in_seconds();
        lifeOfHeartInSeconds = level.getLife_of_heart_in_seconds();
        lifeOfEnemyInSeconds = level.getLife_of_enemy_in_seconds();
        boneNotActiveInSeconds = level.getBone_not_active_in_seconds();
        heartNotActiveInSeconds = level.getHeart_not_active_in_seconds();
        enemyNotActiveInSeconds = level.getEnemy_not_active_in_seconds();
        drawView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                drawView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                scoreResource = getString(R.string.score);
                lifeResource = getString(R.string.life);
                levelResource = getString(R.string.level_num);
                timeResource = getString(R.string.time);
                secResource = getString(R.string.secs);
                recordResource = getString(R.string.record);
            }
        });

        drawView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.invalidate();
                String direction = direction(motionEvent);
                dog.setDirection(direction);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = MainActivity.NewIntent(this);
        startActivity(intent);
    }

    private void toInvalidate() {
        this.drawView.invalidate();
    }

    private String direction(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        float geometricX = x - centerXButton;
        float geometricY = y - centerYButton;
        if (x >= centerXButton - RADIUS_BUTTON && x <= centerXButton + RADIUS_BUTTON
                && y >= centerYButton - RADIUS_BUTTON && y <= centerYButton + RADIUS_BUTTON) {
            if (geometricY >= -geometricX && geometricY >= geometricX) {
                return "DOWN";
            } else if (geometricY <= -geometricX && geometricY <= geometricX) {
                return "UP";
            } else if (geometricY <= geometricX && geometricY >= -geometricX) {
                return "RIGHT";
            } else if (geometricY >= geometricX && geometricY <= -geometricX) {
                return "LEFT";
            }
        }
        return "EMPTY";
    }

    public static Intent NewIntent(Context context, Level level) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra("level", level);
        return intent;
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseGame = true;
        nowDate = new Date();
        workingTime = nowDate.getTime() - startDate.getTime();
        boneExistingTime = nowDate.getTime() - bone.getTimeCreated().getTime();
        boneNotActiveTime = nowDate.getTime() - bone.getTimeIsNotActive().getTime();
        heartExistingTime = nowDate.getTime() - heart.getTimeCreated().getTime();
        heartNotActiveTime = nowDate.getTime() - heart.getTimeIsNotActive().getTime();
        enemyExistingTime = nowDate.getTime() - enemy.getTimeCreated().getTime();
        enemyNotActiveTime = nowDate.getTime() - enemy.getTimeIsNotActive().getTime();
    }

    @Override
    protected void onResume() {
        super.onResume();
        nowDate = new Date();
        startDate.setTime(nowDate.getTime() - workingTime);
        bone.setTimeCreated(new Date(nowDate.getTime() - boneExistingTime));
        bone.setTimeIsNotActive(new Date(nowDate.getTime() - boneNotActiveTime));
        heart.setTimeCreated(new Date(nowDate.getTime() - heartExistingTime));
        heart.setTimeIsNotActive(new Date(nowDate.getTime() - heartNotActiveTime));
        enemy.setTimeCreated(new Date(nowDate.getTime() - enemyExistingTime));
        enemy.setTimeIsNotActive(new Date(nowDate.getTime() - enemyNotActiveTime));
        pauseGame = false;
        if (prefs.contains(APP_PREFERENCES_SOUND_ON)) {
            soundOn = prefs.getBoolean(APP_PREFERENCES_SOUND_ON, true);
        }
        if (prefs.contains(APP_PREFERENCES_MAX_PASSED_LEVEL)) {
            maxPassedLevel = prefs.getInt(APP_PREFERENCES_MAX_PASSED_LEVEL, 0);
        }
    }

    class DrawView extends View {

        Paint paintBackground;
        Paint paintBackgroundField;
        Paint paintLine;
        Paint paintFrame;
        Paint paintTextScore;
        Paint paintBoneNominal;
        Bitmap bitmapArrows;
        Bitmap bitmapHeart;
        Paint paintBone;
        Bitmap bitmapDog;
        ArrayList<Bitmap> bitmapsBones = new ArrayList<>();
        ArrayList<Bitmap> bitmapsEnemies = new ArrayList<>();
        Boolean flag_init = false;
        private final Rect backgroundRect;

        public DrawView(Context context) {
            super(context);
            backgroundRect = new Rect(0, 0, countColumns * STEP_GRID, countRows * STEP_GRID);
            paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintLine.setColor(Color.RED);
            paintLine.setStrokeWidth(LINE_WIDTH);
            paintFrame = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintFrame.setColor(Color.BLACK);
            paintFrame.setStrokeWidth(LINE_WIDTH);
            paintFrame.setStyle(Paint.Style.STROKE);
            paintBone = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintTextScore = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintTextScore.setStyle(Paint.Style.FILL);
            paintTextScore.setTextSize(TEXT_SIZE);
            paintTextScore.setColor(Color.WHITE);
            paintBoneNominal = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintBoneNominal.setStyle(Paint.Style.FILL);
            paintBoneNominal.setTextSize(30);
            paintBoneNominal.setColor(Color.WHITE);

            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = DOG_IMAGE_SOURCE_SIZE / DOG_WIDTH;
            BitmapFactory.Options bitmapOptionsEnemy = new BitmapFactory.Options();
            bitmapOptionsEnemy.inSampleSize = ENEMY_IMAGE_SOURCE_SIZE / ENEMY_WIDTH;
            bitmapDog = BitmapFactory.decodeResource(getResources(), R.drawable.dog,
                    bitmapOptions);
            BitmapFactory.Options bitmapOptionsMediumBone = new BitmapFactory.Options();
            bitmapOptionsMediumBone.inSampleSize = BONE_IMAGE_SOURCE_SIZE / BONE_MEDIUM_WIDTH;
            BitmapFactory.Options bitmapOptionsSmallBone = new BitmapFactory.Options();
            bitmapOptionsSmallBone.inSampleSize = BONE_IMAGE_SOURCE_SIZE / BONE_SMALL_WIDTH;
            BitmapFactory.Options bitmapOptionsBigBone = new BitmapFactory.Options();
            bitmapOptionsBigBone.inSampleSize = BONE_IMAGE_SOURCE_SIZE / BONE_BIG_WIDTH;
            bitmapsBones.add(BitmapFactory.decodeResource(getResources(), R.drawable.bone_yellow,
                    bitmapOptionsSmallBone));
            bitmapsBones.add(BitmapFactory.decodeResource(getResources(), R.drawable.bone_pink,
                    bitmapOptionsSmallBone));
            bitmapsBones.add(BitmapFactory.decodeResource(getResources(), R.drawable.bone_blue,
                    bitmapOptionsSmallBone));
            bitmapsBones.add(BitmapFactory.decodeResource(getResources(), R.drawable.bone_yellow,
                    bitmapOptionsMediumBone));
            bitmapsBones.add(BitmapFactory.decodeResource(getResources(), R.drawable.bone_pink,
                    bitmapOptionsMediumBone));
            bitmapsBones.add(BitmapFactory.decodeResource(getResources(), R.drawable.bone_blue,
                    bitmapOptionsMediumBone));
            bitmapsBones.add(BitmapFactory.decodeResource(getResources(), R.drawable.bone_yellow,
                    bitmapOptionsBigBone));
            bitmapsBones.add(BitmapFactory.decodeResource(getResources(), R.drawable.bone_pink,
                    bitmapOptionsBigBone));
            bitmapsBones.add(BitmapFactory.decodeResource(getResources(), R.drawable.bone_blue,
                    bitmapOptionsBigBone));

            bitmapsEnemies.add(BitmapFactory.decodeResource(getResources(), R.drawable.bear,
                    bitmapOptionsEnemy));
            bitmapsEnemies.add(BitmapFactory.decodeResource(getResources(), R.drawable.tiger,
                    bitmapOptionsEnemy));
            bitmapsEnemies.add(BitmapFactory.decodeResource(getResources(), R.drawable.vacuum_cleaner,
                    bitmapOptionsEnemy));
        }

        @SuppressLint({"DefaultLocale", "DrawAllocation"})
        @Override
        protected void onDraw(Canvas canvas) {
            if (!flag_init) {
                flag_init = true;
                bitmapArrows = MySVG.getBitmapFromVectorDrawable(getContext(), R.drawable.arrows,
                        2 * RADIUS_BUTTON, 2 * RADIUS_BUTTON);
                bitmapHeart = MySVG.getBitmapFromVectorDrawable(getContext(), R.drawable.heart,
                        HEART_WIDTH, HEART_WIDTH);

                paintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintBackgroundField = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintBackgroundField.setColor(colorBackground);
                paintBackgroundField.setStyle(Paint.Style.FILL);

            }

            //Draw background
            canvas.drawColor(getResources().getColor(R.color.mint_green));
            canvas.drawRect(backgroundRect, paintBackgroundField);
            int xHead = (int) dog.getxHead();
            int yHead = (int) dog.getyHead();

            //Draw arrows
            canvas.drawBitmap(bitmapArrows, centerXButton - RADIUS_BUTTON,
                    centerYButton - RADIUS_BUTTON, paintBackground);

            //Draw BONE
            if (bone.isActive()) {
                canvas.drawBitmap(bitmapsBones.get(3 * (bone.getNominal() - 1) + bone.getColor()),
                        new Rect(0, 0, bone.getWidth(), bone.getWidth()),
                        new Rect(bone.getX(), bone.getY(),
                                bone.getX() + bone.getWidth(),
                                bone.getY() + bone.getWidth()), paintBone);
            }

            //Draw HEART
            if (heart.isActive()) {
                canvas.drawBitmap(bitmapHeart,
                        new Rect(0, 0, heart.getWidth(), heart.getWidth()),
                        new Rect(heart.getX(), heart.getY(),
                                heart.getX() + heart.getWidth(),
                                heart.getY() + heart.getWidth()), paintBone);
            }

            //Draw ENEMY
            if (enemy.isActive()) {
                canvas.drawBitmap(bitmapsEnemies.get(enemy.getColor()), new Rect(0, 0, ENEMY_WIDTH, ENEMY_WIDTH),
                        new Rect(enemy.getX(), enemy.getY(), enemy.getX() + ENEMY_WIDTH, enemy.getY() + ENEMY_WIDTH),
                        paintBone);
            }

            //Draw DOG
            canvas.drawBitmap(bitmapDog, new Rect(0, 0, DOG_WIDTH, DOG_WIDTH),
                    new Rect(xHead, yHead, xHead + DOG_WIDTH, yHead + DOG_WIDTH),
                    paintBackground);

            //Draw frame
            canvas.drawRect(new Rect(0, 0, countColumns * STEP_GRID,
                    countRows * STEP_GRID), paintFrame);

            //Draw score
            canvas.drawText(levelResource + levelNumber, 0,
                    TEXT_SIZE + centerYButton - RADIUS_BUTTON, paintTextScore);
            canvas.drawText(scoreResource + dog.getScore() + "/" + maxLevelScore, 0,
                    2 * TEXT_SIZE + centerYButton - RADIUS_BUTTON, paintTextScore);
            canvas.drawText(lifeResource + dog.getLife(), 0,
                    3 * TEXT_SIZE + centerYButton - RADIUS_BUTTON, paintTextScore);
            canvas.drawText(timeResource + String.format("%.1f",
                            (nowDate.getTime() - startDate.getTime()) / 1000.0) + " " + secResource,
                    0,
                    4 * TEXT_SIZE + centerYButton - RADIUS_BUTTON,
                    paintTextScore);
            if (bestTimeDB != 0) {
                canvas.drawText(recordResource + String.format("%.1f", bestTimeDB / 1000.0) +
                                " " + secResource,
                        0,
                        5 * TEXT_SIZE + centerYButton - RADIUS_BUTTON,
                        paintTextScore);
            }

        }
    }
}