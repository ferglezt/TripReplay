package com.ferglezt.tripreplay.di.component;

import com.ferglezt.tripreplay.di.ActivityScope;
import com.ferglezt.tripreplay.di.module.TripListModule;
import com.ferglezt.tripreplay.view.TripListActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent(modules = TripListModule.class)
@ActivityScope
public interface TripListComponent extends AndroidInjector<TripListActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<TripListActivity>{}
}