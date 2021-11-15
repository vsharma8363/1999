package com.party.technologies.nineteen_ninety_nine.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.party.Party;
import com.party.technologies.nineteen_ninety_nine.data.party.PartyInterface;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;

public class ViewParty extends AppCompatActivity {

    private String partyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_party);
        // Get partyID from previous intent.
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            partyID = extras.getString("partyID");
        if(partyID == null || PartyInterface.getPartyByID(partyID) == null) {
            // There was some kind of error, return to main screen.
            Toast.makeText(getApplicationContext(), "Wait, that wasn't supposed to happen :o", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ViewParty.this, Home.class));
        }
        Party party = PartyInterface.getPartyByID(partyID);
        TextView partyName = findViewById(R.id.viewing_party_name);
        TextView partyDescription = findViewById(R.id.viewing_party_description);
        Button requestInvite = findViewById(R.id.request_invite);
        requestInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                party.addPendingGuest(UserInterface.getCurrentUserUID());
                PartyInterface.updateParty(party);
                startActivity(new Intent(ViewParty.this, Home.class));
            }
        });
        partyName.setText(party.getPartyName());
        partyDescription.setText(party.getPartyDescription());
    }
}