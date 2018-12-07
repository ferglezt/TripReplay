package com.ferglezt.tripreplay.di.component;

import android.app.Application;

import com.ferglezt.tripreplay.TripApplication;
import com.ferglezt.tripreplay.di.module.ActivityBuilderModule;
import com.ferglezt.tripreplay.di.module.AppModule;
import com.ferglezt.tripreplay.di.module.ServiceBuilderModule;
import com.ferglezt.tripreplay.service.GpsService;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton @Component(modules = {AppModule.class, AndroidInjectionModule.class, ActivityBuilderModule.class, ServiceBuilderModule.class})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance Builder application(Application application);
        AppComponent build();
    }
    void inject(TripApplication application);
}
