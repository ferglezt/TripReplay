package com.ferglezt.tripreplay.di.component;

import com.ferglezt.tripreplay.di.ActivityScope;
import com.ferglezt.tripreplay.di.module.TripModule;
import com.ferglezt.tripreplay.view.TripActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = TripModule.class)
@ActivityScope
public interface TripComponent {
    void inject(TripActivity activity);
}
