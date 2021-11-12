package com.party.technologies.nineteen_ninety_nine.ui;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.shape.MarkerEdgeTreatment;
import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.party.Party;
import com.party.technologies.nineteen_ninety_nine.data.party.PartyInterface;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.ui.hosting.HostingActivity;
import com.party.technologies.nineteen_ninety_nine.ui.pages.Profile;
import com.party.technologies.nineteen_ninety_nine.ui.pages.Settings;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity implements OnMapReadyCallback {

    private final double METERS_VARIATION = 50.0;
    private Map<Marker, String> markersPartyHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Define hashmap for markers to string.
        markersPartyHashMap = new HashMap<Marker, String>();
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
                startActivity(new Intent(getApplicationContext(), HostingActivity.class));
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
        // Set origin location for map.
        setMapOriginView(googleMap);
        // Populate map with data
        populateMapWithParties(googleMap);

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                String selectedPartyID = markersPartyHashMap.get(marker);
                if(PartyInterface.getPartyByID(selectedPartyID).getHostID().equals(UserInterface.getCurrentUserUID()))
                    Toast.makeText(getApplicationContext(), "That's your party you silly goose! \uD80C\uDD6C", Toast.LENGTH_SHORT).show();
                else {
                    Intent i = new Intent(Home.this, ViewParty.class);
                    i.putExtra("partyID", selectedPartyID);
                    startActivity(i);
                }
            }
        });
    }

    private void populateMapWithParties(GoogleMap googleMap) {
        for(Party party: PartyInterface.getAllParties()) {
            LatLng partyLocation = new LatLng(party.getLatitude(), party.getLongitude());
            LatLng newRandomLocation = randomizeLatLng(partyLocation, METERS_VARIATION);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(newRandomLocation)
                    .title(party.getPartyName())
                    .snippet(party.getPartyDescription());
            CircleOptions boundingCircle = new CircleOptions()
                    .center(partyLocation)
                    .fillColor(Color.parseColor("#3c1361"))
                    .radius(METERS_VARIATION);
            Marker marker = googleMap.addMarker(markerOptions);
            Circle circle = googleMap.addCircle(boundingCircle);
            // Add marker to hashmap of markers to parties
            markersPartyHashMap.put(marker, party.getPartyID());
        }
    }

    private void setMapOriginView(GoogleMap googleMap) {
        LatLng WSP = new LatLng(40.7309, -73.9973);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(WSP, 16));
    }

    // This method returns a LatLng randomly changed by a max of variationMeters from the original
    // spot.
    private LatLng randomizeLatLng(LatLng original, double variationMeters) {
        // TODO: Implement function to randomize a location.
        return original;
    }
}