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
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

import com.cgy.health.service.BluetoothLeService;


public class MainActivity extends TabActivity {

    boolean mIsBound = false;
    Messenger mService = null;
    final Messenger mMessenger = new Messenger(new IncomingHandler());
    Button sendBtn;
    TextView receiveTextView;

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothLeService.MSG_INIT_BEACON_LOCATION_CLIENT:
                    //receiveTextView.setText(msg.getData().getString("data"));
                    break;
                case BluetoothLeService.MSG_DEVICE_LOCATION:
                    receiveTextView.setText(msg.getData().getString("str1"));
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

        AlertDialog a;

        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;

        spec = tabHost.newTabSpec("health").setIndicator(getString(R.string.exercise_page_tab)).setContent(R.id.tab1);
        tabHost.addTab(spec);

        startBLEService();
        sendBtn.setOnClickListener(sendBtnListener);
    }

    private View.OnClickListener sendBtnListener = new View.OnClickListener() {
        public void onClick(View v){
            //이 부분이 로그상 Timer의 work count 증가 값 정의 하는 부분
            //현재 Init 을 호출 하고 있지만 추후에 값을 전달해서 세팅 가능하도록 하면됨
            sendMessageToService(1);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        Button button = (Button) this.findViewById(R.id.selectBeaconBtn);
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
        bindService(new Intent(this, BluetoothLeService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
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
                    Message msg = Message.obtain(null, BluetoothLeService.MSG_INIT_BEACON_LOCATION_CLIENT, intvaluetosend, 0);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                }
                catch (RemoteException e) {
                }
            }
        }
    }

}
