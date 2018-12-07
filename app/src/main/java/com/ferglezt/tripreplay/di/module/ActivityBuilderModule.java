package com.ferglezt.tripreplay.di.module;

import com.ferglezt.tripreplay.di.ActivityScope;
import com.ferglezt.tripreplay.view.TripActivity;
import com.ferglezt.tripreplay.view.TripListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = TripModule.class)
    @ActivityScope
    abstract TripActivity tripActivity();

    @ContributesAndroidInjector(modules = TripListModule.class)
    @ActivityScope
    abstract TripListActivity tripListActivity();

}