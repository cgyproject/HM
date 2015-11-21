package com.cgy.health.model;

import com.cgy.health.lib.uk.co.alt236.bluetoothlelib.device.beacon.ibeacon.IBeaconDevice;

/**
 * Created by gun on 2015-07-25.
 */
public class Beacon {
    private String name;
    private int x;
    private int y;
    private int rssi;
    private String uuid;
    private IBeaconDevice device;

    public Beacon(String name, String uuid, int rssi) {
        this.name = name;
        this.uuid = uuid;
        this.rssi = rssi;
    }
    public Beacon(IBeaconDevice device, String name, String uuid, int rssi) {
        this.device = device;
        this.name = name;
        this.uuid = uuid;
        this.rssi = rssi;
    }
    public Beacon(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public Beacon(String name, String x, String y) {
        this(name, x, y, "");
    }

    public Beacon(String name, String x, String y, String uuid) {
        this.name = name;
        this.x = Integer.parseInt(x);
        this.y = Integer.parseInt(y);
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object object) {
        if ( object instanceof Beacon ) {
            return this.getName().equals(((Beacon) object).getName());
        }

        return false;
    }

    public String getName() {
        return name;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public String getUuid() {
        return uuid;
    }
    public void setRssi(int rssi) { this.rssi = rssi; }
    public int getRssi() { return rssi; }
}
