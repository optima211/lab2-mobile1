package com.example.user.lab2_game;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.user.lab2_game.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {
    public static final String PLAYER_NAME_KEY = "com.example.user.lab2_game.PlayerName";
    public static final String PLAYER_SETTINGS_KEY = "com.example.user.lab2_game.PlayerSettings";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

            binding.buttonStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText editTextName = findViewById(R.id.editTextName);
                    String playerName = editTextName.getText().toString();

                    EditText editTextCount = findViewById(R.id.editTextCount);
                    String playerSettings = editTextCount.getText().toString();

                    Integer playerSettingsInt = tryParseInt(playerSettings);

                    if (isNotNullOrWhiteSpace(playerName)) {
                        editTextName.setError(null);


                        if (playerSettingsInt > 0 && playerSettingsInt <= 100) {
                            editTextCount.setError(null);

                            Intent intent = new Intent(MainActivity.this, GameActivity.class);
                            intent.putExtra(PLAYER_NAME_KEY, playerName);
                            intent.putExtra(PLAYER_SETTINGS_KEY, playerSettings);
                            startActivity(intent);
                        } else {
                            editTextCount.setError("Количество должно быть от 1 до 100");
                        }

                    } else {
                        editTextName.setError("Имя не должно быть пустым");
                    }
                }
            });

            binding.buttonRecords.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, RecordsActivity.class);
                    startActivity(intent);
                }
            });
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }


    public static Integer tryParseInt(final String obj) {
        Integer retVal;
        try {
            retVal = Integer.parseInt(obj);
        } catch (NumberFormatException nfe) {
            retVal = 0;
        }
        return retVal;
    }

    public static boolean isNotNullOrWhiteSpace(final String string) {
        return string != null && !string.isEmpty() && !string.trim().isEmpty();
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
    public void onBackPressed() {
        try {
            finish();
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }

    @Override
    public void onPause() {
        try {
            super.onPause();
            if (ApplicationHolder.soundService != null) ApplicationHolder.soundService.pauseMusic();
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }

    @Override
    public void onResume() {
        try {
            super.onResume();
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


