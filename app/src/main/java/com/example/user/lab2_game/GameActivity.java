package com.example.user.lab2_game;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class GameActivity extends BaseActivity {
    protected GameView gameView;
    protected TextView secondsRemainingText;
    protected TextView killCountText;
    private RecordItem recordItem = new RecordItem();
    private CountDownTimerPausable timer;
    private Boolean processStarted;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            processStarted = false;

            Intent i = getIntent();
            String playerName = i.getStringExtra(MainActivity.PLAYER_NAME_KEY);
            Integer playerSettings = Integer.parseInt(i.getStringExtra(MainActivity.PLAYER_SETTINGS_KEY));

            recordItem.Name = playerName;
            gameView = new GameView(this, playerSettings, recordItem);
            setContentView(gameView);

            secondsRemainingText = new TextView(this);
            addContentView(secondsRemainingText, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            killCountText = new TextView(this);
            killCountText.setPadding(0, 40 ,0 ,0);
            addContentView(killCountText, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            int TIME_LIMIT = 60;
            timer = new CountDownTimerPausable(TIME_LIMIT * 1000, 1000) {

                public void onTick(long ms) {
                    long s = ms / 1000;
                    String remainingText = "Осталось времени: " + s + " с";
                    secondsRemainingText.setText(remainingText);

                    if (s <= 15){
                        secondsRemainingText.setTextColor(Color.RED);
                    }

                    String killText = "Убито насекомых: " + recordItem.KillCount;
                    killCountText.setText(killText);

                    processStarted = true;
                }

                public void onFinish() {
                    Intent intent = new Intent(GameActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(GameActivity.this, "Игра окончена", Toast.LENGTH_LONG).show();
                }
            }.start();
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    onBackPressed();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        try {
            super.onStop();
            try {
                StoredData st = new StoredData();
                recordItem.CreatedAt = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date());
                st.saveNewRecord(this, recordItem);
            } catch (Exception ex) {
                Log.v(ex.getMessage(), "error");
            }
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }

    @Override
    public void onBackPressed() {
        try {
            gameView.pause();
            timer.pause();

            new AlertDialog.Builder(this)
                    .setMessage("Вы действительно хотите выйти в меню?")
                    .setCancelable(false)
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(GameActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            gameView.resume();
                            timer.start();
                        }
                    })
                    .show();
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }

    @Override
    protected void onPause() {
        try {
            super.onPause();
            if (processStarted) {
                gameView.pause();
            }
            if (ApplicationHolder.soundService != null) ApplicationHolder.soundService.pauseMusic();
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            if (processStarted) {
                gameView.resume();
            }

            if (ApplicationHolder.soundService == null) BindSoundService();
            if (ApplicationHolder.soundService != null)
                ApplicationHolder.soundService.resumeMusic();
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }

    @Override
    public void onDestroy() {
        try {
            super.onDestroy();
            if (ApplicationHolder.soundService != null) ApplicationHolder.soundService.stopMusic();
            if (ApplicationHolder.soundService != null) UnbindSoundService();
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }
}
