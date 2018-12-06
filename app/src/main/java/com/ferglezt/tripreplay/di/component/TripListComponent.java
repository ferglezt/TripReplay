package com.ferglezt.tripreplay.di.component;

import com.ferglezt.tripreplay.di.ActivityScope;
import com.ferglezt.tripreplay.di.module.TripListModule;
import com.ferglezt.tripreplay.view.TripListActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = TripListModule.class)
public interface TripListComponent {
    void inject(TripListActivity activity);
}
