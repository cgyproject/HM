package com.cgy.health.ble.connect;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.cgy.health.lib.uk.co.alt236.bluetoothlelib.device.beacon.ibeacon.IBeaconDevice;
import com.cgy.health.model.Beacon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordan on 2015. 8. 8..
 */
public class ScanBLEDevice {
    private boolean mScanning;
    private static final long SCAN_PERIOD = 10000;

    private BluetoothAdapter mBluetoothAdapter;
    private List<Beacon> mBluetoothDeviceList = new ArrayList<Beacon>();


    public ScanBLEDevice(BluetoothAdapter bluetoothAdapter) {
        mBluetoothAdapter = bluetoothAdapter;
    }

    public void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
           if(mScanning) {
               mScanning = false;
               mBluetoothAdapter.stopLeScan(mLeScanCallback);
           }

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    IBeaconDevice beaconDevice = new IBeaconDevice(device, rssi, scanRecord);
                    Beacon beacon = new Beacon(beaconDevice, device.getName(), device.getAddress(), rssi);
                    mBluetoothDeviceList.add(beacon);
                }
    };

    public List<Beacon> getBluetoothDeviceList() {
        mBluetoothDeviceList.clear();
        scanLeDevice(true);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scanLeDevice(false);
        return mBluetoothDeviceList;
    }
}
