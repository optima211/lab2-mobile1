package com.example.user.lab2_game;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.content.Context.MODE_PRIVATE;

class StoredData {
    private String RECORDS_FILE = "RECORDS_FILE";
    private String RECORDS_KEY = "RECORDS_DATA";

    void saveNewRecord(Context context, RecordItem item) {
        Gson gson = new Gson();

        SharedPreferences prefs = context.getSharedPreferences(RECORDS_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        ArrayList<RecordItem> records = null;
        String data = prefs.getString(RECORDS_KEY, null);
        if (data != null) {
            Type collectionType = new TypeToken<ArrayList<RecordItem>>() {
            }.getType();
            records = gson.fromJson(data, collectionType);
        }
        if (records == null) {
            records = new ArrayList<>();
        }

        records.add(item);

        Collections.sort(records, new Comparator<RecordItem>() {
            @Override
            public int compare(RecordItem o1, RecordItem o2) {
                return o2.KillCount.compareTo(o1.KillCount);
            }
        });

        String json = gson.toJson(records);

        editor.putString(RECORDS_KEY, json);
        editor.apply();
    }

    ArrayList<RecordItem> getAllRecords(Context context) {
        Gson gson = new Gson();

        SharedPreferences prefs = context.getSharedPreferences(RECORDS_FILE, MODE_PRIVATE);

        ArrayList<RecordItem> records = null;
        String data = prefs.getString(RECORDS_KEY, null);
        if (data != null) {
            Type collectionType = new TypeToken<ArrayList<RecordItem>>() {
            }.getType();
            records = gson.fromJson(data, collectionType);
        }
        if (records == null) {
            records = new ArrayList<>();
        }
        return records;
    }

    void deleteAllRecords(Context context) {

        SharedPreferences prefs = context.getSharedPreferences(RECORDS_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(RECORDS_KEY, null);
        editor.apply();
    }
}