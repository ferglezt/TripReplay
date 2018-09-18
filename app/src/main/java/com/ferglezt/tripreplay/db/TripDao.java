package com.ferglezt.tripreplay.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ferglezt.tripreplay.model.Trip;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;


@Dao
public interface TripDao {
    @Query("SELECT * FROM trips")
    Flowable<List<Trip>> getAll();

    @Query("SELECT * FROM trips WHERE id = :id")
    Maybe<Trip> findById(int id);

    @Insert
    long insert(Trip trip);

    @Delete
    void delete(Trip trip);

    @Update
    void update(Trip trip);
}
