package com.snow.map.tracking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.snow.map.tracking.fragments.CustomMapFragment;

public class MapActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CustomMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mapFragment = new CustomMapFragment();
        getFragmentManager().beginTransaction().add(R.id.mapContainer, mapFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.map_action_normal:
                mapFragment.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.map_action_hybrid:
                mapFragment.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.map_action_satellite:
                mapFragment.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.map_action_terrain:
                mapFragment.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            case R.id.map_action_none:
                mapFragment.setMapType(GoogleMap.MAP_TYPE_NONE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
