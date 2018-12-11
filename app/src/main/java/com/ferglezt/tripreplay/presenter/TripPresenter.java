package com.ferglezt.tripreplay.presenter;

import android.util.Log;

import com.ferglezt.tripreplay.BuildConfig;
import com.ferglezt.tripreplay.model.Point;
import com.ferglezt.tripreplay.mvpinterfaces.TripMVP;

import java.util.List;


public class TripPresenter implements TripMVP.Presenter {

    private static final String TAG = TripPresenter.class.getSimpleName();

    private TripMVP.View view;
    private TripMVP.Interactor interactor;

    public TripPresenter(TripMVP.View view, TripMVP.Interactor interactor) {
        this.view = view;
        this.interactor = interactor;
        this.interactor.setFlowableListener(this);
    }

    @Override
    public void onCreate() {
        if(!interactor.isLocationServiceRunning()) {
            interactor.checkForUnfinishedTrip();
        }
    }

    @Override
    public void onResume() {
        interactor.startLocationListener();

        if (interactor.isLocationServiceRunning()) {
            view.setEnabledStartButton(false);
            view.setEnabledStopButton(true);
            view.showActionBar();
        } else {
            view.setEnabledStartButton(true);
            view.setEnabledStopButton(false);
            view.hideActionBar();
        }
    }

    @Override
    public void onPause() {
        interactor.stopLocationListener();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean checkNeedsLocationPermission() {
        return interactor.checkLocationPermission();
    }

    @Override
    public void onLocationPermissionGranted() {
        this.onCreate();
    }

    @Override
    public void onStartRecordingClick() {
        interactor.startLocationService();
        interactor.startLocationListener();
        view.setEnabledStartButton(false);
        view.setEnabledStopButton(true);
        view.showActionBar();
    }

    @Override
    public void onStopRecordingClick() {
        interactor.stopLocationService();
        interactor.stopLocationListener();
        view.setEnabledStartButton(true);
        view.setEnabledStopButton(false);
        view.hideActionBar();
        interactor.checkForUnfinishedTrip();
    }

    @Override
    public void onSaveUnfinishedTripClick(List<Point> points) {
        interactor.saveUnfinishedTrip(points);
    }

    @Override
    public void onDeleteUnfinishedTripClick(List<Point> points) {
        interactor.deleteUnfinishedTrip(points);
    }

    @Override
    public void onPointsReceived(List<Point> points) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onPointsReceived");

        view.setPolylinePoints(points);

        if (points.size() > 1) {
            Point point1 = points.get(points.size() - 2);
            Point point2 = points.get(points.size() - 1);
            view.setSpeed(interactor.getSpeed(point1, point2));
        }
    }

    @Override
    public void onUnfinishedTripFound(List<Point> points) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onUnfinishedTripFound");

        view.showUnfinishedTripDialog(points);
    }
}
