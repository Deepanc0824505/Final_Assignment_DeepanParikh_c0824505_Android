package com.example.fa_deepanparikh_c0824505_android;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {

    private Context context;
    private static DatabaseClient mInstance;

    private com.example.fa_deepanparikh_c0824505_android.AppDatabase appDatabase;

    private DatabaseClient(Context context) {
        this.context = context;
        appDatabase = Room.databaseBuilder(context, com.example.fa_deepanparikh_c0824505_android.AppDatabase.class, "MyFavoritePlaces").build();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(context);
        }
        return mInstance;
    }

    public com.example.fa_deepanparikh_c0824505_android.AppDatabase getAppDatabase() {
        return appDatabase;
    }
}