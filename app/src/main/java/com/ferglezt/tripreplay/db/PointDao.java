package com.ferglezt.tripreplay.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ferglezt.tripreplay.model.Point;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;


@Dao
public interface PointDao {
    @Query("SELECT * FROM points WHERE tripId = :tripId")
    Flowable<List<Point>> getAllByTripId(int tripId);

    @Insert
    void insert(Point point);

    @Query("DELETE FROM points WHERE tripId = :tripId")
    void deleteByTripId(int tripId);

    @Query("UPDATE points SET tripId = :newTripId WHERE tripId = :oldTripId")
    void updateTripId(long newTripId, long oldTripId);
}
