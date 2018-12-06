package com.ferglezt.tripreplay.di.component;

import android.content.Context;

import com.ferglezt.tripreplay.db.AppDataBase;
import com.ferglezt.tripreplay.di.module.AppModule;
import com.ferglezt.tripreplay.view.TripActivity;
import com.ferglezt.tripreplay.service.GpsService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton @Component(modules = {AppModule.class})
public interface AppComponent {
    AppDataBase getAppDataBase();
    Context getContext();
    void inject(GpsService gpsService);
}
