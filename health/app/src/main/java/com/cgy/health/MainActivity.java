package com.cgy.health;

import android.app.TabActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

import com.cgy.health.model.Beacon;
import com.cgy.health.service.BluetoothLeService;

import java.util.HashMap;
import java.util.Iterator;


public class MainActivity extends TabActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    boolean mIsBound = false;
    Messenger mService = null;
    final Messenger mMessenger = new Messenger(new IncomingHandler());
    Button sendBtn;
    TextView receiveTextView;
    private HashMap<String, Beacon> mBluetoothDeviceMap = null;

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothLeService.MSG_GET_DEVICES:
                    mBluetoothDeviceMap = (HashMap<String, Beacon>)msg.getData().getSerializable("BluetoothDeviceMap");
                    logBLEDevice();
                    break;
                case BluetoothLeService.MSG_GET_DEVICE_LOCATION:
                    //receiveTextView.setText(msg.getData().getString("str1"));
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            //textStatus.setText("Attched.");
            try {
                Message msg = Message.obtain(null, BluetoothLeService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);
            }
            catch (RemoteException e) {
                // In this case the service has crashed before we could even do anything with it
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been unexpectedly disconnected - process crashed.
            mService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendBtn = (Button)findViewById(R.id.sendBtn);
        receiveTextView = (TextView)findViewById(R.id.receiveText);

        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;

        spec = tabHost.newTabSpec("health").setIndicator(getString(R.string.exercise_page_tab)).setContent(R.id.tab1);
        tabHost.addTab(spec);

        startBLEService();
        sendBtn.setOnClickListener(sendBtnListener);
    }

    private View.OnClickListener sendBtnListener = new View.OnClickListener() {
        public void onClick(View v){
            sendMessageToService(1);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if(!mIsBound) {
            startBLEService();
        }
        Button button = (Button) this.findViewById(R.id.selectBeaconBtn);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            stopBLEService();
        }
        catch (Throwable t) {
            Log.e("MainActivity", "Failed to unbind from the service", t);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            stopBLEService();
        }
        catch (Throwable t) {
            Log.e("MainActivity", "Failed to unbind from the service", t);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if ( id == R.id.action_settings ) {
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent, 1001);
            this.finish();
            return true;
        }
        else if ( id == R.id.action_kill) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startBLEService() {
        doBindService();
        startService(new Intent(MainActivity.this, BluetoothLeService.class));
    }

    public void stopBLEService() {
        doUnbindService();
        stopService(new Intent(MainActivity.this, BluetoothLeService.class));
    }

    void doBindService() {
        if(!mIsBound) {
            bindService(new Intent(this, BluetoothLeService.class), mConnection, Context.BIND_AUTO_CREATE);
            mIsBound = true;
        }
        //textStatus.setText("Binding.");
    }

    void doUnbindService() {
        if (mIsBound) {
            // If we have received the service, and hence registered with it, then now is the time to unregister.
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null, BluetoothLeService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                }
                catch (RemoteException e) {
                    // There is nothing special we need to do if the service has crashed.
                }
            }
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    private void sendMessageToService(int intvaluetosend) {
        if (mIsBound) {
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null, BluetoothLeService.MSG_GET_DEVICES, intvaluetosend, 0);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                }
                catch (RemoteException e) {
                }
            }
        }
    }

    private void logBLEDevice() {
        if(mBluetoothDeviceMap == null)
            return;

        Iterator<String> bleKeyList = mBluetoothDeviceMap.keySet().iterator();
        while(bleKeyList.hasNext()) {
            String uuid = bleKeyList.next();
            Beacon beacon = mBluetoothDeviceMap.get(uuid);
            Log.i(TAG, "Bluetooth Device NAME : " + beacon.getName());
            Log.i(TAG, "Bluetooth Device UUID : " + beacon.getDevice().getIBeaconData().getUUID());
            Log.i(TAG, "Bluetooth Device TXPOWER : " + beacon.getDevice().getIBeaconData().getCalibratedTxPower());
        }

    }
}
