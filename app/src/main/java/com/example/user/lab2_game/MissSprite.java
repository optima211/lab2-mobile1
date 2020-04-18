package com.example.user.lab2_game;

import android.graphics.Bitmap;
import android.graphics.Canvas;


class MissSprite {
    private float x;
    private float y;
    private Bitmap bmp;
    private int timeCounter;

    MissSprite(GameView gameView, float x, float y, Bitmap bmp) {
        this.x = Math.min(Math.max(x - bmp.getWidth() / 2, 0), gameView.getWidth() - bmp.getWidth());
        this.y = Math.min(Math.max(y - bmp.getHeight() / 2, 0), gameView.getHeight() - bmp.getHeight());
        this.bmp = bmp;
    }

    void onDraw(Canvas canvas) {
        canvas.drawBitmap(bmp, x, y, null);
    }

    boolean isRespawn() {
        final int TIME_LIMIT = 5;
        if (timeCounter >= TIME_LIMIT) {
            return true;
        }
        timeCounter++;
        return false;
    }
}