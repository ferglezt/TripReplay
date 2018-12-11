package com.ferglezt.tripreplay.interactor;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.ferglezt.tripreplay.db.AppDataBase;
import com.ferglezt.tripreplay.model.Point;
import com.ferglezt.tripreplay.model.Trip;
import com.ferglezt.tripreplay.mvpinterfaces.TripMVP;
import com.ferglezt.tripreplay.service.GpsService;
import com.ferglezt.tripreplay.util.GpsUtil;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
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
    private Disposable saveTripDisposable;

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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(points -> {
                    if (points.size() > 0) {
                        flowableListener.onUnfinishedTripFound(points);
                    }
                    unfinishedTripDisposable.dispose();
                });
    }

    @Override
    public void saveUnfinishedTrip(List<Point> points) {
        Trip trip = new Trip();
        trip.setStartDate(points.get(0).getDate());
        trip.setEndDate(points.get(points.size() - 1).getDate());

        saveTripDisposable = Single.fromCallable(() -> appDataBase.tripDao().insert(trip))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(id -> {
                    appDataBase.pointDao().updateTripId(id, Point.DEFAULT_TRIP_ID);
                    saveTripDisposable.dispose();
                });
    }

    @Override
    public void deleteUnfinishedTrip(List<Point> points) {
        Completable.fromAction(() -> appDataBase.pointDao().deleteByTripId(points.get(0).getTripId()))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public float getSpeed(Point point1, Point point2) {
        //TODO move this code to a util function if speed is needed in another component
        float distance = GpsUtil.distanceBetween(point1.getLatitude(), point1.getLongitude(),
                                                 point2.getLatitude(), point2.getLongitude());

        long time = point2.getDate().getTime() - point1.getDate().getTime();
        long timeSeconds = time / 1000;
        float speed = distance / timeSeconds;

        return (float)3.6 * speed; // Km/h

    }

}
