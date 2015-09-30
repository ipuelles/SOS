package com.snow.map.tracking.fragments;

import android.app.DownloadManager;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.snow.map.tracking.AppConfigs;
import com.snow.map.tracking.AppQueue;
import com.snow.map.tracking.R;
import com.snow.map.tracking.models.Wheather;
import com.snow.map.tracking.services.LocationData;
import com.snow.map.tracking.services.TrackingLocationService;
import com.snow.map.tracking.utils.ViewUtils;

public class InfoFragment extends Fragment {

    private TextView txtSpeed;
    private TextView txtSpeedUnit;
    private TextView txtMaxSpeed;
    private TextView txtAvgSpeed;
    private TextView txtDistance;

    private TextView txtTemperature;
    private TextView txtTempUnit;
    private TextView txtWheatherDes;
    private Wheather objWeather;

    private BroadcastReceiver locationChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int action = intent.getIntExtra(TrackingLocationService.LOCATION_SERVICE_UPDATE, 0);

            switch (action) {
                case TrackingLocationService.ACTION_LOCATION_CHANGED:
                    updateSpeedInfo();
                    updateWheatherInfo();
                    break;
                default:
                    break;
            }
        }
    };


    private void updateSpeedInfo() {
        txtSpeed.setText(String.valueOf(LocationData.getInstance().getCurrentSpeed()));
        txtMaxSpeed.setText(String.valueOf(LocationData.getInstance().getMaxSpeed()));
        txtDistance.setText(String.format("%4.1f", LocationData.getInstance().getDistance()));
        txtAvgSpeed.setText(String.valueOf(LocationData.getInstance().getAvgSpeed()));
    }

    private void updateWheatherInfo() {
        Location location = LocationData.getInstance().getCurrentLocation();
        String url = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s", location.getLatitude(), location.getLongitude());
        AppQueue.getInstance(getActivity().getBaseContext()).addRequest(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Log.v("JSON", response);
                objWeather = gson.fromJson(response, new TypeToken<Wheather>() {
                }.getType());
                if (objWeather != null) {
                    txtTemperature.setText(String.valueOf(objWeather.getTemperature()));
                    if (objWeather.weather != null && objWeather.weather.size() > 0)
                        txtWheatherDes.setText(objWeather.weather.get(0).description);
                    txtTempUnit.setText(objWeather.getTemperatureUnit());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));
    }

    private void updateUnit() {
        txtSpeedUnit.setText(LocationData.getInstance().getSpeedUnit());
        if(objWeather!=null) {
            txtTemperature.setText(String.valueOf(objWeather.getTemperature()));
            txtTempUnit.setText(objWeather.getTemperatureUnit());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_fragment, container, false);
        ViewUtils.setTypeface(AppConfigs.getInstance().ROBOTO_CONDENSED_REGULAR,view);
        txtSpeed = (TextView) view.findViewById(R.id.txtSpeed);
        txtSpeedUnit = (TextView) view.findViewById(R.id.txtSpeedUnit);
        txtMaxSpeed = (TextView) view.findViewById(R.id.txtMaxSpeed);
        txtAvgSpeed = (TextView) view.findViewById(R.id.txtAvgSpeed);
        txtDistance = (TextView) view.findViewById(R.id.txtDistance);
        //Wheather
        txtTemperature = (TextView) view.findViewById(R.id.temperature);
        txtTempUnit = (TextView) view.findViewById(R.id.tempUnit);
        txtWheatherDes = (TextView) view.findViewById(R.id.wheaterDesc);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                locationChange, new IntentFilter(TrackingLocationService.LOCATION_SERVICE_UPDATE));
        //Update Speed info when resume
        if (LocationData.getInstance().getCurrentLocation() != null) {
            Intent intent = new Intent(TrackingLocationService.LOCATION_SERVICE_UPDATE);
            intent.putExtra(TrackingLocationService.LOCATION_SERVICE_UPDATE, TrackingLocationService.ACTION_LOCATION_CHANGED);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        }
        //update unit info
        updateUnit();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(locationChange);
        super.onPause();
    }
}
