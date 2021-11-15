package com.party.technologies.nineteen_ninety_nine.ui.hosting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.party.PartyInterface;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.ui.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HostingParentView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosting);
        // Define back button logic that is connected to the parent activity_hosting.xml file.
        findViewById(R.id.back_hosting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start a new activity that returns the user home.
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        });
        Fragment nextFragment = null;
        // Decide which fragment to load on screen startup.
        if (PartyInterface.getPartyByHost(UserInterface.getCurrentUserUID()) == null)
            // If there is no party currently hosted by the user, ask the user if they wish to host
            // a new party.
            nextFragment = new ConfirmHostFragment();
        else
            // If there is a party currently being hosted, direct them to host view
            nextFragment = new HostViewFragment();
        getSupportFragmentManager().beginTransaction().replace(
                R.id.hosting_fragment_view, nextFragment).commit();
    }
}