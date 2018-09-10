package com.ferglezt.tripreplay.di;

import com.ferglezt.tripreplay.db.AppDataBase;
import com.ferglezt.tripreplay.view.TripActivity;
import com.ferglezt.tripreplay.service.GpsService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton @Component(modules = {AppModule.class})
public interface AppComponent {
    AppDataBase getAppDataBase();
    void inject(GpsService gpsService);
    void inject(TripActivity tripActivity);
}
