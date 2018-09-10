package com.ferglezt.tripreplay.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.ferglezt.tripreplay.model.Point;
import com.ferglezt.tripreplay.model.Trip;

@Database(entities = {Point.class, Trip.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDataBase extends RoomDatabase {
    public abstract TripDao tripDao();
    public abstract PointDao pointDao();
}
