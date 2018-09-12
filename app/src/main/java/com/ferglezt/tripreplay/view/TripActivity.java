package com.ferglezt.tripreplay.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.ferglezt.tripreplay.BuildConfig;
import com.ferglezt.tripreplay.R;
import com.ferglezt.tripreplay.TripApplication;
import com.ferglezt.tripreplay.db.AppDataBase;
import com.ferglezt.tripreplay.interactor.TripInteractor;
import com.ferglezt.tripreplay.model.Point;
import com.ferglezt.tripreplay.mvpinterfaces.TripMVP;
import com.ferglezt.tripreplay.presenter.TripPresenter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TripActivity extends FragmentActivity implements OnMapReadyCallback, TripMVP.View {

    private static final String TAG = TripActivity.class.getSimpleName();

    private static final int REQUEST_LOCATION_ON_MAP_READY = 1;

    private static final float SLIDING_LAYOUT_ANCHOR = 0.5f;

    @BindView(R.id.sliding_layout) SlidingUpPanelLayout slidingUpPanelLayout;
    @BindView(R.id.start_recording_btn) Button startRecordingButton;
    @BindView(R.id.stop_recording_button) Button stopRecordingButton;

    private GoogleMap mMap;
    private Polyline polyline;

    private TripMVP.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        ButterKnife.bind(this);

        slidingUpPanelLayout.setAnchorPoint(SLIDING_LAYOUT_ANCHOR);

        AppDataBase appDataBase = ((TripApplication) getApplication()).getAppComponent().getAppDataBase();
        TripMVP.Interactor interactor = new TripInteractor(this, appDataBase);
        presenter = new TripPresenter(this, interactor);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        presenter.onCreate();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.CYAN);
        polylineOptions.width(8);
        polyline = mMap.addPolyline(polylineOptions);

        if (presenter.checkNeedsLocationPermission()) {
            requestLocationPermission(REQUEST_LOCATION_ON_MAP_READY);
            return;
        }

        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void setPolylinePoints(List<Point> points) {
        if (polyline == null) {
            if (BuildConfig.DEBUG) Log.e(TAG, "setPolylinePoints: null polyline instance");
            return;
        }

        List<LatLng> latLngList = new ArrayList<>();

        for (Point point : points) {
            LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
            latLngList.add(latLng);
        }

        polyline.setPoints(latLngList);
    }

    @Override
    public void setSpeed(float speed) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        switch(requestCode) {
            case REQUEST_LOCATION_ON_MAP_READY:
                if (presenter.checkNeedsLocationPermission()) {
                    requestLocationPermission(REQUEST_LOCATION_ON_MAP_READY);
                    return;
                }
                mMap.setMyLocationEnabled(true);
        }

        presenter.onLocationPermissionGranted();
    }

    @Override
    public void requestLocationPermission(int code) {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                code);
    }

    @Override
    public void setEnabledStartButton(boolean enabled) {
        startRecordingButton.setEnabled(enabled);
        startRecordingButton.setAlpha(enabled ? 1.0f : 0.3f);
    }

    @Override
    public void setEnabledStopButton(boolean enabled) {
        stopRecordingButton.setEnabled(enabled);
        stopRecordingButton.setAlpha(enabled ? 1.0f : 0.3f);

    }

    @Override
    public void showUnfinishedTripDialog(String message) {
        //TODO : Add positive and negative buttons
        new AlertDialog.Builder(this)
                .setMessage(message)
                .show();

    }

    @OnClick(R.id.start_recording_btn)
    public void onStartRecordingClick() {
        presenter.onStartRecordingClick();
    }

    @OnClick(R.id.stop_recording_button)
    public void onStopRecordingClick() {
        presenter.onStopRecordingClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
