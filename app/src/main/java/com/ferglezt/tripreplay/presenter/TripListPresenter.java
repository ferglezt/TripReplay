package com.ferglezt.tripreplay.presenter;

import com.ferglezt.tripreplay.model.Trip;
import com.ferglezt.tripreplay.mvpinterfaces.TripListMVP;

import java.util.List;

public class TripListPresenter implements TripListMVP.Presenter {

    private TripListMVP.Interactor interactor;
    private TripListMVP.View view;

    public TripListPresenter(TripListMVP.View view, TripListMVP.Interactor interactor) {
        this.view = view;
        this.interactor = interactor;
        this.interactor.setFlowableListener(this);
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onResume() {
        interactor.findAllTrips();
    }

    @Override
    public void onPause() {
        interactor.disposeFlowableUpdates();
    }

    @Override
    public void onDestroy() {
        interactor.disposeFlowableUpdates();
    }

    @Override
    public void onNewTripClick() {
        view.showNewTripScreen();
    }

    @Override
    public void onTripsFound(List<Trip> trips) {
        view.populateRecyclerView(trips);
    }
}
