package com.example.myapplication;


import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.SharedPreferences;
import com.example.myapplication.dummy.DummyContent;

import static java.lang.Float.parseFloat;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private DummyContent.DummyItem mItem;
    private static final String SHARED_PREF_NAME = "username";
    private static final String KEY_MOMENT = "key_moment";
    private SharedPreferences  mySharedPreferences;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_detail);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mySharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        final String moment = mySharedPreferences.getString(KEY_MOMENT,null);
        mItem = DummyContent.ITEM_MAP.get(moment);
        Float alt = parseFloat(mItem.alt);
        Float lat = parseFloat(mItem.lat);


        // Add a marker in Sydney, Australia, and move the camera.
        LatLng position = new LatLng(alt, lat);
        mMap.addMarker(new MarkerOptions().position(position).title("Marker in UNAJ"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(alt, lat), 15.0f));

    }
}