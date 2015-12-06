package com.cgy.health;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.cgy.health.model.Beacon;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gun on 2015-08-08.
 */
public class BeaconField {
    private Context context;
    private ViewGroup layout;
    private Map<String, Button> buttonMap;

    public BeaconField(Context context, ViewGroup layout) {
        this.context = context;
        this.layout = layout;
        buttonMap = new HashMap<String, Button>();
    }

    public void drawBeacon() {
        float width = layout.getWidth();
        float height = layout.getHeight();
        float xPixel = width/100;
        float yPixel = height/100;

        BeaconHandler handler = BeaconHandler.getBeaconHandler();
        for ( Beacon beacon : handler.getBeaconList() ) {
            Button button = buttonMap.get(beacon.getDevice().getIBeaconData().getUUID());
            if ( button != null ) {
                break;
            }
            button = new Button(context);
            button.setText(beacon.getName());
            ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            param.width = 300;
            param.height = 200;
            button.setLayoutParams(param);
            button.setX(xPixel * beacon.getX() - (param.width / 2));
            button.setY(yPixel * beacon.getY() - (param.height / 2));
            layout.addView(button);

            buttonMap.put(beacon.getDevice().getIBeaconData().getUUID(), button);
        }
    }

    private void createFrameLayout() {
        FrameLayout layout = new FrameLayout(context);
    }
}