package com.party.technologies.nineteen_ninety_nine.ui.upcoming;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.party.technologies.nineteen_ninety_nine.R;

public class UpcomingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);
        // Define back button logic that is connected to the parent activity_hosting.xml file.
        findViewById(R.id.activity_upcoming_done_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportFragmentManager().beginTransaction().replace(
                R.id.activity_upcoming_fragment_view, new ViewUpcomingFragment()).commit();
    }
}