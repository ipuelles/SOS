package com.snow.map.tracking.fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.snow.map.tracking.R;
import com.snow.map.tracking.services.LocationData;
import com.snow.map.tracking.services.TrackingLocationService;


public class CustomMapFragment extends Fragment {

    private MapView mapView;
    private GoogleMap map;


    private BroadcastReceiver locationServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int action = intent.getIntExtra(TrackingLocationService.LOCATION_SERVICE_UPDATE, 0);
            switch (action) {
                case TrackingLocationService.ACTION_LOCATION_CHANGED:
                    Location location = LocationData.getInstance().getCurrentLocation();
                    if (location != null) {
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(
                                new LatLng(location.getLatitude(), location.getLongitude()));
                        map.moveCamera(cameraUpdate);
                    }
                    break;
                default:

            }
        }
    };

    public void setMapType(int mapType) {
        map.setMapType(mapType);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_map_fragment, container, false);
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);

        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Location loc = LocationData.getInstance().getCurrentLocation();
        if (loc != null) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 16);
            map.animateCamera(cameraUpdate);
        } else {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 16);
            map.animateCamera(cameraUpdate);
        }
        return v;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(locationServiceReceiver,
                new IntentFilter(TrackingLocationService.LOCATION_SERVICE_UPDATE));
        super.onResume();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(locationServiceReceiver);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
