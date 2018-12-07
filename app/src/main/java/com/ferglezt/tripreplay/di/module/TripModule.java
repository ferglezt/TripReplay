package com.ferglezt.tripreplay.di.module;

import android.content.Context;

import com.ferglezt.tripreplay.db.AppDataBase;
import com.ferglezt.tripreplay.interactor.TripInteractor;
import com.ferglezt.tripreplay.mvpinterfaces.TripMVP;
import com.ferglezt.tripreplay.presenter.TripPresenter;
import com.ferglezt.tripreplay.view.TripActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class TripModule {

    @Provides
    public TripMVP.View provideView(TripActivity tripActivity) {
        return tripActivity;
    }

    @Provides
    public TripMVP.Interactor provideInteractor(Context context, AppDataBase appDataBase) {
        return new TripInteractor(context, appDataBase);
    }

    @Provides
    public TripMVP.Presenter providePresenter(TripMVP.View view, TripMVP.Interactor interactor) {
        return new TripPresenter(view, interactor);
    }
}
