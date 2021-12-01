package com.party.technologies.nineteen_ninety_nine.ui.hosting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.party.PartyInterface;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.ui.Home;
import com.party.technologies.nineteen_ninety_nine.ui.party_viewer.CreateParty;
import com.party.technologies.nineteen_ninety_nine.ui.party_viewer.ViewParty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class HostingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosting);
        // Finish activity
        ImageButton done = findViewById(R.id.activity_hosting_done_btn);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Decide which fragment to load on screen startup.
        if (PartyInterface.getPartyByHost(UserInterface.getCurrentUserUID()) == null)
        {    // If there is no party currently hosted by the user, ask the user if they wish to host
            // a new party.
            findViewById(R.id.yes_host_1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchPartyCreatorFragment();
                }
            });
            findViewById(R.id.yes_host_2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchPartyCreatorFragment();
                }
            });
        }
        else {
            finish();
            Intent i = new Intent(getApplicationContext(), ViewParty.class);
            i.putExtra("partyID", PartyInterface.getPartyByHost(UserInterface.getCurrentUserUID()).getPartyID());
            startActivity(i);
        }
    }

    private void launchPartyCreatorFragment() {
        finish();
        startActivity(new Intent(getApplicationContext(), CreateParty.class));
    }
}