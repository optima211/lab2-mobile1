package com.example.user.lab2_game;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.user.lab2_game.databinding.ActivityRecordsBinding;

import java.util.ArrayList;

public class RecordsActivity extends BaseActivity {

    private StoredData st;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            ActivityRecordsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_records);

            st = new StoredData();
            ArrayList<RecordItem> recordItems = st.getAllRecords(this);

            TableLayout records_table = findViewById(R.id.records_table);

            TableRow row;
            TextView t1, t2, t3;
            int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 1, getResources().getDisplayMetrics());

            for (int current = 0; current < recordItems.size(); current++) {
                RecordItem recordItem = recordItems.get(current);

                row = new TableRow(this);

                t1 = new TextView(this);
                t2 = new TextView(this);
                t3 = new TextView(this);

                t1.setText(recordItem.Name != null ? recordItem.Name : "");
                t2.setText(recordItem.KillCount != null ? recordItem.KillCount.toString() : "");
                t3.setText(recordItem.CreatedAt != null ? recordItem.CreatedAt : "");

                t1.setGravity(Gravity.START);
                t2.setGravity(Gravity.START);
                t3.setGravity(Gravity.START);

                t1.setTextSize(15);
                t2.setTextSize(15);
                t3.setTextSize(15);

                t1.setPadding(5 * dip, 0, 0, 0);
                t2.setPadding(5 * dip, 0, 0, 0);
                t3.setPadding(5 * dip, 0, 0, 0);

                row.addView(t1);
                row.addView(t2);
                row.addView(t3);

                records_table.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }

            binding.buttonRemoveAllRecords.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    st.deleteAllRecords(RecordsActivity.this);
                    Intent intent = new Intent(RecordsActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
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
    public void onBackPressed() {
        try {
            Intent intent = new Intent(RecordsActivity.this, MainActivity.class);
            startActivity(intent);
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