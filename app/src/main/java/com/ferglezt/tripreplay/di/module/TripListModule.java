package com.ferglezt.tripreplay.di.module;

import com.ferglezt.tripreplay.db.AppDataBase;
import com.ferglezt.tripreplay.interactor.TripListInteractor;
import com.ferglezt.tripreplay.mvpinterfaces.TripListMVP;
import com.ferglezt.tripreplay.presenter.TripListPresenter;
import com.ferglezt.tripreplay.view.TripListActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class TripListModule {

    private TripListActivity tripListActivity;

    public TripListModule(TripListActivity tripListActivity) {
        this.tripListActivity = tripListActivity;
    }

    @Provides
    public TripListMVP.View provideView() {
        return tripListActivity;
    }

    @Provides
    public TripListMVP.Interactor provideInteractor(AppDataBase appDataBase) {
        return new TripListInteractor(appDataBase);
    }

    @Provides
    public TripListMVP.Presenter providePresenter(TripListMVP.View view, TripListMVP.Interactor interactor) {
        return new TripListPresenter(view, interactor);
    }
}
