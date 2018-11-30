package com.ferglezt.tripreplay.interactor;

import com.ferglezt.tripreplay.db.AppDataBase;
import com.ferglezt.tripreplay.mvpinterfaces.TripListMVP;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TripListInteractor implements TripListMVP.Interactor {

    private TripListMVP.FlowableListener flowableListener;
    private AppDataBase appDataBase;
    private Disposable databaseDisposable;

    public TripListInteractor(AppDataBase appDataBase) {
        this.appDataBase = appDataBase;
    }

    @Override
    public void setFlowableListener(TripListMVP.FlowableListener flowableListener) {
        this.flowableListener = flowableListener;
    }

    @Override
    public void findAllTrips() {
        databaseDisposable = appDataBase
                .tripDao()
                .getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(trips -> flowableListener.onTripsFound(trips));
    }

    @Override
    public void disposeFlowableUpdates() {
        if (databaseDisposable != null) {
            databaseDisposable.dispose();
        }
    }
}
