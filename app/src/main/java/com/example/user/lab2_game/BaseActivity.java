package com.example.user.lab2_game;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public abstract class BaseActivity extends AppCompatActivity {
    private android.content.ServiceConnection ServiceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder binder) {
            try {
                ApplicationHolder.soundService = ((SoundService.ServiceBinder) binder).getService();
                ApplicationHolder.soundService.resumeMusic();
            } catch (Exception ex) {
                Log.v(ex.getMessage(), "error");
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            try {
                ApplicationHolder.soundService = null;
            } catch (Exception ex) {
                Log.v(ex.getMessage(), "error");
            }
        }
    };

    protected void BindSoundService() {
        try {
            bindService(new Intent(this, SoundService.class), ServiceConnection, Context.BIND_AUTO_CREATE);
            ApplicationHolder.soundServiceIsBound = true;
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }

    protected void UnbindSoundService() {
        try {
            if (ApplicationHolder.soundServiceIsBound) {
                unbindService(ServiceConnection);
                ApplicationHolder.soundServiceIsBound = false;
            }
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }
}
