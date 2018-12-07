package com.ferglezt.tripreplay.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.ferglezt.tripreplay.db.AppDataBase;
import com.ferglezt.tripreplay.db.RoomLocationUpdatesManager;
import com.ferglezt.tripreplay.service.LocationUpdatesManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    public Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    public AppDataBase provideAppDataBase(Context context) {
        return Room.databaseBuilder(context, AppDataBase.class, "Trip").build();
    }

    @Provides
    public LocationUpdatesManager provideLocationUpdatesManager(AppDataBase appDataBase) {
        return new RoomLocationUpdatesManager(appDataBase);
    }

}
