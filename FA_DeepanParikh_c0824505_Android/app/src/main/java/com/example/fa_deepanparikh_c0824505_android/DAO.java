package com.example.fa_deepanparikh_c0824505_android;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface DAO {
    @Query("SELECT * FROM Place")
    List<Place> getAll();

    @Insert
    void insert(Place place);

    @Delete
    void delete(Place place);

    @Update
    void update(Place place);

    @Query("SELECT COUNT(id) FROM Place")
    int getCount();
}
