package com.ferglezt.tripreplay;


import android.content.Context;

import com.ferglezt.tripreplay.db.AppDataBase;
import com.ferglezt.tripreplay.interactor.TripInteractor;
import com.ferglezt.tripreplay.model.Point;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;

import static org.assertj.core.api.Assertions.*;

public class TripInteractorTest {

    private TripInteractor interactor;

    @Before
    public void setup() {
        Context context = Mockito.mock(Context.class);
        AppDataBase appDataBase = Mockito.mock(AppDataBase.class);
        interactor = new TripInteractor(context, appDataBase);
    }

    @Test
    public void getSpeed_test() {
        Point point1 = new Point();
        point1.setDate(new Date(8000));
        point1.setLatitude(47.80523);
        point1.setLongitude(-120.03866);

        Point point2 = new Point();
        point2.setDate(new Date(10000)); // 2 second difference
        point2.setLatitude(47.80433); // 100 m distance
        point2.setLongitude(-120.03866);

        // 100m/2s =  50m/s = 180 Km/h

        float speed = interactor.getSpeed(point1, point2);
        assertThat(speed).isBetween(180.0f, 180.2f);

    }

}
