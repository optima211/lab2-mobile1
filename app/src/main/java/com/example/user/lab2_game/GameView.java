package com.example.user.lab2_game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressLint("ViewConstructor")
public class GameView extends SurfaceView {

    private SoundPool sounds;
    private int soundKill;
    private int soundMiss;

    private GameLoopThread gameLoopThread;
    private List<Sprite> sprites = new ArrayList<>();
    private List<BloodSprite> temps = new ArrayList<>();
    private List<MissSprite> misses = new ArrayList<>();
    private RecordItem recordItem;
    private long lastClick;
    private Bitmap bmpBlood;
    private Bitmap bmpMiss;
    private Integer _playerSettings;

    public GameView(Context context, Integer playerSettings, RecordItem recordItem) {
        super(context);

        try {
            _playerSettings = playerSettings;
            this.recordItem = recordItem;

            sounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            soundKill = sounds.load(context, R.raw.kill, 1);
            soundMiss = sounds.load(context, R.raw.miss, 1);


            gameLoopThread = new GameLoopThread(this);

            getHolder().addCallback(new SurfaceHolder.Callback() {

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    boolean retry = true;
                    gameLoopThread.setRunning(false);
                    while (retry) {
                        try {
                            gameLoopThread.join();
                            retry = false;
                        } catch (InterruptedException e) {
                            Log.v(e.getMessage(), "error");
                        }
                    }
                }

                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    for (int i = 1; i <= _playerSettings; i++) {
                        sprites.add(createSprite());
                    }

                    gameLoopThread.setRunning(true);
                    gameLoopThread.start();
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }
            });

            bmpBlood = BitmapFactory.decodeResource(getResources(), R.drawable.blood1);
            bmpMiss = BitmapFactory.decodeResource(getResources(), R.drawable.miss);
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }

    private Sprite createSprite() {
        try {
            int resource = 0;
            BugType bugType = BugType.randomBugType();
            switch (bugType) {
                case SPIDER:
                    resource = R.drawable.spiders;
                    break;
                case WASP:
                    resource = R.drawable.wasps;
                    break;
            }

            Bitmap bmp = BitmapFactory.decodeResource(getResources(), resource);
            return new Sprite(this, bmp, bugType);
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
        return null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            canvas.drawColor(Color.WHITE);
            @SuppressLint("DrawAllocation") List<MissSprite> missToRemove = new ArrayList<>();
            @SuppressLint("DrawAllocation") List<BloodSprite> tempsToRemove = new ArrayList<>();
            @SuppressLint("DrawAllocation") List<UUID> spriteIdsToRespawn = new ArrayList<>();

            for (int i = temps.size() - 1; i >= 0; i--) {
                BloodSprite temp = temps.get(i);
                temp.onDraw(canvas);

                if (temp.isRespawn()) {
                    tempsToRemove.add(temp);
                    spriteIdsToRespawn.add(temp.spriteId);
                }
            }

            if (tempsToRemove.size() > 0) {
                for (BloodSprite temp : tempsToRemove) {
                    temps.remove(temp);
                }
            }

            for (Sprite sprite : sprites) {
                if (spriteIdsToRespawn.indexOf(sprite.uniqueID) > -1) {
                    sprite.setNewRandomLocation();
                    sprite.setAlive(true);
                }

                if (sprite.isAlive) {
                    sprite.onDraw(canvas);
                }
            }

            for (MissSprite miss : misses) {
                miss.onDraw(canvas);

                if (miss.isRespawn()) {
                    missToRemove.add(miss);
                }
            }

            if (missToRemove.size() > 0) {
                for (MissSprite miss : missToRemove) {
                    misses.remove(miss);
                }
            }
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 300) {
            lastClick = System.currentTimeMillis();
            float x = event.getX();
            float y = event.getY();

            boolean isCollision = false;

            synchronized (getHolder()) {
                for (int i = sprites.size() - 1; i >= 0; i--) {
                    Sprite sprite = sprites.get(i);

                    if (sprite.isCollision(x, y)) {
                        isCollision = true;
                        sounds.play(soundKill, 1.0f, 1.0f, 0, 0, 1.5f);

                        switch (sprite.bugType) {
                            case SPIDER:
                                recordItem.SpiderCount++;
                                break;
                            case WASP:
                                recordItem.WaspsCount++;
                                break;
                        }

                        sprite.setAlive(false);

                        BloodSprite bloodSprite = new BloodSprite(sprite.uniqueID, this, x, y, bmpBlood);
                        temps.add(bloodSprite);


                        break;
                    }
                }
            }

            if (isCollision) recordItem.KillCount++;
            else {
                sounds.play(soundMiss, 1.0f, 1.0f, 0, 0, 1.5f);

                MissSprite missSprite = new MissSprite(this, x, y, bmpMiss);
                misses.add(missSprite);

                recordItem.KillCount--;

            }
        }
        return true;
    }

    public void resume() {
        try {
            gameLoopThread = new GameLoopThread(this);
            gameLoopThread.setRunning(true);
            gameLoopThread.start();
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }

    public void pause() {
        try {
            gameLoopThread.setRunning(false);
            boolean retry = true;
            while (retry) {
                try {
                    gameLoopThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    Log.v(e.getMessage(), "error");
                }
            }
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }
}