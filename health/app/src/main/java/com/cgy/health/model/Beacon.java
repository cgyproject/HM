package com.cgy.health.model;

/**
 * Created by gun on 2015-07-25.
 */
public class Beacon {
    private String name;
    private int x;
    private int y;
    private int rssi;
    private String uuid;

    public Beacon(String name, String uuid, int rssi) {
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
        this.name = name;
        this.x = Integer.parseInt(x);
        this.y = Integer.parseInt(y);
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
