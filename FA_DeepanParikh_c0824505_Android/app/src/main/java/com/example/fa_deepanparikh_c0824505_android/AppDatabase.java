package com.example.fa_deepanparikh_c0824505_android;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Place.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DAO placeDao();
}