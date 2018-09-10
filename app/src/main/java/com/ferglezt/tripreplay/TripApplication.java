package com.ferglezt.tripreplay;

import android.app.Application;

import com.ferglezt.tripreplay.di.AppComponent;
import com.ferglezt.tripreplay.di.AppModule;
import com.ferglezt.tripreplay.di.DaggerAppComponent;

public class TripApplication extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
