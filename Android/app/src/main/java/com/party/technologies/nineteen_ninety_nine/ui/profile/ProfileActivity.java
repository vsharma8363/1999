package com.party.technologies.nineteen_ninety_nine.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.user.User;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.ui.Home;
import com.party.technologies.nineteen_ninety_nine.ui.upcoming.ViewUpcomingFragment;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // Define back button logic.
        findViewById(R.id.activity_profile_done_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        getSupportFragmentManager().beginTransaction().replace(
                R.id.activity_profile_fragment_view, new ProfileFragment()).commit();
    }
}