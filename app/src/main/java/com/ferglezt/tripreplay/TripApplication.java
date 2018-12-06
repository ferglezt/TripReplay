package com.ferglezt.tripreplay;

import android.app.Application;

import com.ferglezt.tripreplay.di.component.AppComponent;
import com.ferglezt.tripreplay.di.component.DaggerAppComponent;
import com.ferglezt.tripreplay.di.module.AppModule;

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
