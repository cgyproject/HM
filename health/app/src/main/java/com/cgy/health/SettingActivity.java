package com.cgy.health;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TabHost;

/**
 * Created by gun on 15. 6. 3.
 */
public class SettingActivity extends TabActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        spec = tabHost.newTabSpec("beaconSetting").setIndicator("비콘위치설정").setContent(R.id.tab1);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("machineSetting").setIndicator("기구위치설정").setContent(R.id.tab1);
        tabHost.addTab(spec);
    }

    @Override
    protected void onStart() {
        super.onStart();


        if ( this.getTabHost().getCurrentTab() == 0 ) {
            Button button = (Button) this.findViewById(R.id.selectBeaconBtn);
            button.setText("비콘검색");
        }
        else if ( this.getTabHost().getCurrentTab() == 1 ) {
            Button button = (Button) this.findViewById(R.id.selectBeaconBtn);
            button.setText("기구입력");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();

        menu.add(0, 0, 0, "메뉴1").setIcon(R.mipmap.ic_launcher);
        menu.add(0, 1, 0, "메뉴2").setIcon(R.mipmap.ic_launcher);
        menu.add(0, 2, 0, "메뉴3").setIcon(R.mipmap.ic_launcher);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if ( id == 0 ) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(intent, 1002);
            return true;
        }
        else if ( id == 2) {
            this.finish();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
