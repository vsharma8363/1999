package com.party.technologies.nineteen_ninety_nine.ui;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.ui.pages.Hosting;
import com.party.technologies.nineteen_ninety_nine.ui.pages.Profile;
import com.party.technologies.nineteen_ninety_nine.ui.pages.Settings;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Define profile button logic.
        Button profile = findViewById(R.id.profile_button);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Profile.class));
            }
        });
        // Define hosting button logic.
        Button hosting = findViewById(R.id.hosting_button);
        hosting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Hosting.class));
            }
        });
        // Define settings button logic.
        Button settings = findViewById(R.id.settings_button);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Settings.class));
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        // Populate map with data
        LatLng Fidi = new LatLng(40.7077, -74.0083);
        googleMap.addMarker(new MarkerOptions()
                .position(Fidi)
                .title("Marker in Fidi"));
        LatLng WSP = new LatLng(40.7309, -73.9973);
        googleMap.addMarker(new MarkerOptions()
                .position(WSP)
                .title("Party in WSP"))
                .setSnippet("We will have buttons and other \nstuff here. A fun view to see party info.");
        LatLng Soho = new LatLng(40.7246, -74.0019);
        googleMap.addMarker(new MarkerOptions()
                .position(Soho)
                .title("Marker in Fidi"));
        LatLng hellsKitchen = new LatLng(40.7638, -73.9918);
        googleMap.addMarker(new MarkerOptions()
                .position(hellsKitchen)
                .title("Marker in Fidi"));

        LatLng random1 = new LatLng(40.732607, -74.008018);
        googleMap.addMarker(new MarkerOptions()
                .position(random1)
                .title("Marker in Fidi"));

        LatLng random2 = new LatLng(41.716938, -73.989649);
        googleMap.addMarker(new MarkerOptions()
                .position(random2)
                .title("Marker in Fidi"));

        LatLng random3 = new LatLng(40.742986, -73.977652);
        googleMap.addMarker(new MarkerOptions()
                .position(random3)
                .title("Marker in Fidi"));
        // [START_EXCLUDE silent]
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(WSP, 16));
    }
}