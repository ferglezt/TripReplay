package com.ferglezt.tripreplay.mvpinterfaces;

import com.ferglezt.tripreplay.model.Point;

import java.util.List;

public interface TripMVP {
    interface View {
        void setPolylinePoints(List<Point> points);
        void setSpeed(float speed);
        void requestLocationPermission(int code);
        void setEnabledStartButton(boolean enabled);
        void setEnabledStopButton(boolean enabled);
    }

    interface Presenter extends FlowableListener {
        void onCreate();
        void onResume();
        void onPause();
        void onDestroy();
        boolean checkNeedsLocationPermission();
        void onLocationPermissionGranted();
        void onStartRecordingClick();
        void onStopRecordingClick();
    }

    interface Interactor {
        void setFlowableListener(FlowableListener flowableListener);
        void startLocationService();
        void stopLocationService();
        void startLocationListener();
        void stopLocationListener();
        boolean checkLocationPermission();
        boolean isLocationServiceRunning();
        void checkForUnfinishedTrip();

    }

    interface FlowableListener {
        void onPointsReceived(List<Point> points);
        void onUnfinishedTripFound(List<Point> points);
    }
}