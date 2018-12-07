package com.ferglezt.tripreplay.di.component;

import com.ferglezt.tripreplay.di.ActivityScope;
import com.ferglezt.tripreplay.di.module.TripModule;
import com.ferglezt.tripreplay.view.TripActivity;

import dagger.Component;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent(modules = TripModule.class)
@ActivityScope
public interface TripComponent extends AndroidInjector<TripActivity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<TripActivity>{}
}
