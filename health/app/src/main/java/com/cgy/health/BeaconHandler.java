package com.cgy.health;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gun on 2015-07-25.
 */
public final class BeaconHandler {
    private static BeaconHandler thisContext = new BeaconHandler();
    private List<Beacon> beaconList;

    private BeaconHandler() {
    }

    public static BeaconHandler getBeaconHandler() {
        return thisContext;
    }

    public boolean addBeacon(Beacon newBeacon) {
        if ( beaconList == null ) {
            beaconList = new ArrayList<Beacon>();
        }

        for( Beacon beacon : beaconList ) {
            if ( beacon.equals(newBeacon) ) {
                return false;
            }
        }
        beaconList.add(newBeacon);
        return true;
    }

    public List<Beacon> getBeaconList() {
        return beaconList;
    }
}
