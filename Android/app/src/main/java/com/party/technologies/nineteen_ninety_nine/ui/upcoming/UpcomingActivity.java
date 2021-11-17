package com.party.technologies.nineteen_ninety_nine.ui.upcoming;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.party.PartyInterface;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.ui.Home;
import com.party.technologies.nineteen_ninety_nine.ui.hosting.ConfirmHostFragment;
import com.party.technologies.nineteen_ninety_nine.ui.hosting.HostViewFragment;

public class UpcomingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);
        // Define back button logic that is connected to the parent activity_hosting.xml file.
        findViewById(R.id.back_upcoming).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start a new activity that returns the user home.
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        });
        getSupportFragmentManager().beginTransaction().replace(
                R.id.upcoming_fragment_view, new ViewUpcomingFragment()).commit();
    }
}