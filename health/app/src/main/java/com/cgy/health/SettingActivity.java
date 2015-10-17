package com.cgy.health;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import com.cgy.health.model.Beacon;

/**
 * Created by gun on 15. 6. 3.
 */
public class SettingActivity extends TabActivity implements TabHost.OnTabChangeListener {
    private final static String BEACON_SETTING_TABNAME = "beaconSetting";
    private final static String MACHINE_SETTING_TABNAME = "machineSetting";
    final Context context = this;
    private BeaconField beaconField = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        spec = tabHost.newTabSpec(BEACON_SETTING_TABNAME).setIndicator(getString(R.string.setting_page_beacon_tab)).setContent(R.id.tab1);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec(MACHINE_SETTING_TABNAME).setIndicator(getString(R.string.setting_page_machine_tab)).setContent(R.id.tab1);
        tabHost.addTab(spec);
        tabHost.setOnTabChangedListener(this);

        Button button = (Button) this.findViewById(R.id.beaconTabBeaconBtn);
        button.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View var1, MotionEvent var2) {
                switch (var2.getAction()) {
                    case MotionEvent.ACTION_UP:
                        break;
                    default:
                        return true;
                }
                if (isBeaconTab()) {
                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.prompts, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);
                    final EditText xView = (EditText) promptsView.findViewById(R.id.xInput);
                    final EditText yView = (EditText) promptsView.findViewById(R.id.yInput);
                    final EditText nameView = (EditText) promptsView.findViewById(R.id.nameInput);
                    // set dialog message
                    alertDialogBuilder
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            LayoutInflater li = LayoutInflater.from(context);
                                            View promptsView = li.inflate(R.layout.prompts, null);
                                            TextView t = (TextView) promptsView.findViewById(R.id.name);

                                            String x = String.valueOf(xView.getText().toString());
                                            String y = String.valueOf(yView.getText().toString());
                                            BeaconHandler.getBeaconHandler().addBeacon((new Beacon(nameView.getText().toString(), xView.getText().toString(), yView.getText().toString())));

                                            getBeaconField().drawBeacon();
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
//                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    // show it
                    alertDialog.show();
                } else {
                }
                return true;
            }
        });
    }

    private BeaconField getBeaconField() {
        ViewGroup layout = (ViewGroup) this.findViewById(R.id.relatieLayout);
        if ( this.beaconField == null ) {
            beaconField = new BeaconField(this.findViewById(R.id.relatieLayout).getContext(), layout);
        }

        return beaconField;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private boolean isBeaconTab() {
        String currentTabTag = this.getTabHost().getCurrentTabTag();
        return currentTabTag.equals(BEACON_SETTING_TABNAME);
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

        if ( id == R.id.action_exercise ) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(intent, 1002);
            this.finish();
            return true;
        }

        else if ( id == R.id.action_kill ) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabChanged(String s) {
        if ( s.equals("beaconSetting") ) {
            Button button = (Button) this.findViewById(R.id.selectBeaconBtn);
            button.setText(R.string.setting_page_beacon_btn);
        }
        else if ( s.equals("machineSetting") ) {
            Button button = (Button) this.findViewById(R.id.selectBeaconBtn);
            button.setText(R.string.setting_page_machine_btn);
        }
    }
}
