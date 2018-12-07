package com.ferglezt.tripreplay.di.module;


import com.ferglezt.tripreplay.di.ServiceScope;
import com.ferglezt.tripreplay.service.GpsService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceBuilderModule {

    @ContributesAndroidInjector
    @ServiceScope
    abstract GpsService gpsService();

}
