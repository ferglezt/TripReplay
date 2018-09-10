package com.ferglezt.tripreplay.service;

import android.location.Location;

public interface LocationUpdatesManager {
    void onLocationUpdate(Location location);
}
