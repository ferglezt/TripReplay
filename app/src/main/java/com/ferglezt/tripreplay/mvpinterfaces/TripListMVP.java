package com.ferglezt.tripreplay.mvpinterfaces;

import com.ferglezt.tripreplay.model.Trip;

import java.util.List;

public interface TripListMVP {
    interface View {
        void populateRecyclerView(List<Trip> trips);
        void showNewTripScreen();
        void showTripDetal(Trip trip);
    }

    interface Presenter extends FlowableListener {
        void onCreate();
        void onResume();
        void onPause();
        void onDestroy();
        void onNewTripClick();
        void onItemClick(Trip trip);
    }

    interface Interactor {
        void setFlowableListener(FlowableListener flowableListener);
        void findAllTrips();
        void disposeFlowableUpdates();
    }

    interface FlowableListener {
        void onTripsFound(List<Trip> trips);
    }
}
