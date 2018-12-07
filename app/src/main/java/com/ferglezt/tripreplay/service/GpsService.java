package com.ferglezt.tripreplay.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ferglezt.tripreplay.BuildConfig;
import com.ferglezt.tripreplay.R;
import com.ferglezt.tripreplay.view.TripActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class GpsService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private static final String TAG = GpsService.class.getSimpleName();

    private GoogleApiClient googleApiClient;

    @Inject
    LocationUpdatesManager locationUpdatesManager;

    @Override
    public void onCreate() {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");
        super.onCreate();

        AndroidInjection.inject(this);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getApplication())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        String notificationChannel = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = createNotificationChannel();
        }

        Intent notificationIntent = new Intent(this, TripActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, notificationChannel)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("My Awesome App")
                .setContentText("Doing some work...")
                .setContentIntent(pendingIntent).build();

        startForeground(1337, notification);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel() {
        String channelId = "my_service";
        String channelName = "My Background Service";
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
        notificationChannel.setLightColor(Color.BLUE);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nManager != null)
            nManager.createNotificationChannel(notificationChannel);
        return channelId;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onStartCommand");

        super.onStartCommand(intent, flags, startId);

        if (googleApiClient != null && !googleApiClient.isConnected()) {
            googleApiClient.connect();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopLocationUpdates();

        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(BuildConfig.DEBUG) Log.e(TAG, "Location permissions needed");
            return;
        }

        if(BuildConfig.DEBUG) Log.d(TAG, "Starting location updates");

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                googleApiClient, this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (BuildConfig.DEBUG) Log.d(TAG, "Google Api Client connection stablished");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (BuildConfig.DEBUG) Log.e(TAG, "onConnectionFailed: " + connectionResult.getErrorMessage());
    }

    @Override
    public void onLocationChanged(Location location) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onLocationChanged: " + location.getLatitude() + "," + location.getLongitude());
        locationUpdatesManager.onLocationUpdate(location);
    }
}
