package com.snow.map.tracking;

import android.app.Application;
import android.content.Intent;
import android.graphics.Typeface;

import com.snow.map.tracking.services.TrackingLocationService;


public class MapTrackingApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppConfigs.getInstance().ROBOTO_THIN = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Thin.ttf");
        AppConfigs.getInstance().ROBOTO_CONDENSED_REGULAR = Typeface.createFromAsset(getAssets(),"fonts/RobotoCondensed-Regular.ttf");
    }

}
