package com.party.technologies.nineteen_ninety_nine.ui.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.party.Party;
import com.party.technologies.nineteen_ninety_nine.data.party.PartyInterface;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.ui.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Hosting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosting);
        // Define back button logic.
        Button back = findViewById(R.id.back_hosting);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        });
        // Define individual possible fragments
        Fragment newPartyFragment = new HostingNewParty();
        Fragment createPartyFragment = new CreateParty();
        // Create fragment management
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        // Load party data
        Party partyByUser = PartyInterface.getPartyByHost(UserInterface.getCurrentUserUID());
        partyByUser = null;
        // If user is currently not hosting a party
        if (partyByUser == null) {
            transaction.replace(R.id.hosting_fragment_view, newPartyFragment);
        }
        else {}
        transaction.commit();
    }
}