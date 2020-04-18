package com.example.user.lab2_game;

import android.os.CountDownTimer;

public abstract class CountDownTimerPausable {
    private long countDownInterval;
    private long millisRemaining;

    private CountDownTimer countDownTimer = null;

    private boolean isPaused = true;

    CountDownTimerPausable(long millisInFuture, long countDownInterval) {
        super();
        this.countDownInterval = countDownInterval;
        this.millisRemaining = millisInFuture;
    }

    private void createCountDownTimer() {
        countDownTimer = new CountDownTimer(millisRemaining, countDownInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                millisRemaining = millisUntilFinished;
                CountDownTimerPausable.this.onTick(millisUntilFinished);

            }

            @Override
            public void onFinish() {
                CountDownTimerPausable.this.onFinish();
            }
        };
    }

    public abstract void onTick(long millisUntilFinished);

    public abstract void onFinish();

    public final void cancel() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        this.millisRemaining = 0;
    }

    public synchronized final CountDownTimerPausable start() {
        if (isPaused) {
            createCountDownTimer();
            countDownTimer.start();
            isPaused = false;
        }
        return this;
    }

    void pause() throws IllegalStateException {
        if (!isPaused) {
            countDownTimer.cancel();
        } else {
            throw new IllegalStateException("CountDownTimerPausable is already in pause state, start counter before pausing it.");
        }
        isPaused = true;
    }

    public boolean isPaused() {
        return isPaused;
    }
}
