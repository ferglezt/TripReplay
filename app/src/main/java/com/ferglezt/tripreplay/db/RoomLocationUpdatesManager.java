package com.ferglezt.tripreplay.db;

import android.location.Location;

import com.ferglezt.tripreplay.model.Point;
import com.ferglezt.tripreplay.service.LocationUpdatesManager;

import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings({"unused"})
public class RoomLocationUpdatesManager implements LocationUpdatesManager {

    private AppDataBase appDataBase;

    public RoomLocationUpdatesManager(AppDataBase appDataBase) {
        this.appDataBase = appDataBase;
    }

    @Override
    public void onLocationUpdate(Location location) {
        final Point point = new Point();
        point.setTripId(Point.DEFAULT_TRIP_ID);
        point.setDate(new Date());
        point.setLatitude(location.getLatitude());
        point.setLongitude(location.getLongitude());

        Completable.fromAction(() -> appDataBase.pointDao().insert(point))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }
}
