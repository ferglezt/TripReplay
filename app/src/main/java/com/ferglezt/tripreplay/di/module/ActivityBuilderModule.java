package com.ferglezt.tripreplay.di.module;

import android.app.Activity;

import com.ferglezt.tripreplay.di.component.TripComponent;
import com.ferglezt.tripreplay.di.component.TripListComponent;
import com.ferglezt.tripreplay.view.TripActivity;
import com.ferglezt.tripreplay.view.TripListActivity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class ActivityBuilderModule {

    @Binds
    @IntoMap
    @ActivityKey(TripActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindTripActivity(TripComponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(TripListActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindTripListActivity(TripListComponent.Builder builder);

}