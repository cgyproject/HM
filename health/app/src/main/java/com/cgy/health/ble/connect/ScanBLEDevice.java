package com.cgy.health.ble.connect;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.cgy.health.lib.uk.co.alt236.bluetoothlelib.device.beacon.ibeacon.IBeaconDevice;
import com.cgy.health.lib.uk.co.alt236.bluetoothlelib.device.beacon.ibeacon.IBeaconManufacturerData;
import com.cgy.health.model.Beacon;

import java.util.HashMap;

/**
 * Created by jordan on 2015. 8. 8..
 */
public class ScanBLEDevice {
    private final static String TAG = ScanBLEDevice.class.getSimpleName();

    private boolean mScanning;
    private static final long SCAN_PERIOD = 10000;

    private BluetoothAdapter mBluetoothAdapter;
    private HashMap<String, Beacon> mBluetoothDeviceMap = new HashMap<String, Beacon>();


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
                    try {
                        IBeaconDevice beaconDevice = new IBeaconDevice(device, rssi, scanRecord);
                        if (beaconDevice != null) {
                            IBeaconManufacturerData beaconManufacturerData = beaconDevice.getIBeaconData();
                            Beacon beacon = null;
                            beacon = mBluetoothDeviceMap.get(beaconManufacturerData.getUUID());
                            if (beacon == null) {
                                beacon = new Beacon(beaconDevice, device.getName(), rssi);
                                mBluetoothDeviceMap.put(beaconManufacturerData.getUUID(), beacon);
                            } else {
                                beacon.setRssi(rssi);
                                beacon.setDevice(beaconDevice);
                                mBluetoothDeviceMap.put(beaconManufacturerData.getUUID(), beacon);
                            }
                            Log.i(TAG, "Bluetooth Device Scan NAME : " + device.getName());
                            Log.i(TAG, "Bluetooth Device Scan UUID : " + beaconManufacturerData.getUUID());
                            Log.i(TAG, "Bluetooth Device Scan TXPOWER : " + beaconManufacturerData.getCalibratedTxPower());
                        }
                    } catch (Exception exception) {
                        Log.i(TAG, "Bluetooth Device Scan Error : " + exception.getStackTrace());
                        exception.getStackTrace();
                    }
                }
    };

    public HashMap<String, Beacon> getBluetoothDeviceMap() {
        mBluetoothDeviceMap.clear();
        scanLeDevice(true);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scanLeDevice(false);
        return mBluetoothDeviceMap;
    }
}
