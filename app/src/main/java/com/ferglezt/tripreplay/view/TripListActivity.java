package com.ferglezt.tripreplay.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.ferglezt.tripreplay.BaseActivity;
import com.ferglezt.tripreplay.R;
import com.ferglezt.tripreplay.di.module.TripListModule;
import com.ferglezt.tripreplay.model.Trip;
import com.ferglezt.tripreplay.mvpinterfaces.TripListMVP;
import com.ferglezt.tripreplay.view.adapter.TripAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class TripListActivity extends BaseActivity implements TripListMVP.View {

    @BindView(R.id.trip_recycler) RecyclerView tripRecycler;

    private TripAdapter adapter;
    @Inject TripListMVP.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        AndroidInjection.inject(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((view) -> {
            presenter.onNewTripClick();
        });

        presenter.onCreate();
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

    @Override
    public void populateRecyclerView(List<Trip> trips) {
        adapter = new TripAdapter(trips);
        adapter.setOnItemClickListener((trip) -> presenter.onItemClick(trip));
        tripRecycler.setLayoutManager(new LinearLayoutManager(this));
        tripRecycler.setAdapter(adapter);
        //TODO: Manage gestures like swipe in recyclerview
    }

    @Override
    public void showNewTripScreen() {
        Intent intent = new Intent(this, TripActivity.class);
        startActivity(intent);
    }

    @Override
    public void showTripDetal(Trip trip) {
        Toast.makeText(this, "TODO: " + trip.getId(), Toast.LENGTH_LONG).show();
    }
}
