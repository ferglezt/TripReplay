package com.ferglezt.tripreplay;

import android.arch.persistence.room.Room;

import com.ferglezt.tripreplay.db.AppDataBase;
import com.ferglezt.tripreplay.db.PointDao;
import com.ferglezt.tripreplay.db.TripDao;
import com.ferglezt.tripreplay.model.Point;
import com.ferglezt.tripreplay.model.Trip;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Predicate;

@RunWith(RobolectricTestRunner.class)
public class DbTest {
    private AppDataBase appDataBase;

    @Before
    public void initDb() {
        appDataBase = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.application, AppDataBase.class)
                .allowMainThreadQueries()
                .build();
    }

    @Test
    public void testPointDao() {
        final Point point = new Point();
        point.setDate(new Date());
        point.setLatitude(-17.59);
        point.setLongitude(-19.56);
        point.setTripId(1);

        PointDao pointDao = appDataBase.pointDao();
        pointDao.insert(point);

        pointDao.getAllByTripId(1)
                .test()
                .awaitDone(3, TimeUnit.SECONDS)
                .assertValue(points -> points.get(0).getTripId() == 1)
                .assertValue(points -> points.get(0).getLatitude() == -17.59);
    }

    @Test
    public void testTripDao() {
        Trip trip = new Trip();
        trip.setDescripton("This is a test trip");
        trip.setEndDate(new Date(234567890));
        trip.setStartDate(new Date(123456789));

        final TripDao tripDao = appDataBase.tripDao();
        tripDao.insert(trip);

        tripDao.getAll()
                .test()
                .awaitDone(3, TimeUnit.SECONDS)
                .assertValue(trips -> trips.get(0).getStartDate().getTime() == 123456789)
                .assertValue(trips -> trips.get(0).getDescripton().equals("This is a test trip"));
    }

    @After
    public void closeDb() {
        appDataBase.close();
    }
}
