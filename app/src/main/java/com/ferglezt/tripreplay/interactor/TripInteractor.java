package com.ferglezt.tripreplay.interactor;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.ferglezt.tripreplay.db.AppDataBase;
import com.ferglezt.tripreplay.model.Point;
import com.ferglezt.tripreplay.mvpinterfaces.TripMVP;
import com.ferglezt.tripreplay.service.GpsService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class TripInteractor implements TripMVP.Interactor {
    private static final String TAG = TripInteractor.class.getSimpleName();

    private Context context;
    private AppDataBase appDataBase;
    private TripMVP.FlowableListener flowableListener;
    private Disposable locationDisposable;
    private Disposable unfinishedTripDisposable;

    public TripInteractor(Context context, AppDataBase appDataBase) {
        this.context = context;
        this.appDataBase = appDataBase;
    }

    @Override
    public void setFlowableListener(TripMVP.FlowableListener flowableListener) {
        this.flowableListener = flowableListener;
    }

    @Override
    public void startLocationService() {
        context.startService(new Intent(context, GpsService.class));
    }

    @Override
    public void stopLocationService() {
        context.stopService(new Intent(context, GpsService.class));
    }

    @Override
    public void startLocationListener() {
        locationDisposable = appDataBase
                .pointDao()
                .getAllByTripId(Point.DEFAULT_TRIP_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(points -> flowableListener.onPointsReceived(points));
    }

    @Override
    public void stopLocationListener() {
        if (locationDisposable != null) {
            locationDisposable.dispose();
        }
    }

    @Override
    public boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public boolean isLocationServiceRunning() {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (GpsService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void checkForUnfinishedTrip() {
        unfinishedTripDisposable = appDataBase
                .pointDao()
                .getAllByTripId(Point.DEFAULT_TRIP_ID)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(points -> {
                    if (points.size() > 0) {
                        flowableListener.onUnfinishedTripFound(points);
                    }
                    unfinishedTripDisposable.dispose();
                });
    }

}
