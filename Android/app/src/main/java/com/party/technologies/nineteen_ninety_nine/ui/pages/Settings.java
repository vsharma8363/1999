package com.party.technologies.nineteen_ninety_nine.ui.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.ui.Home;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // Define back button logic.
        Button back = findViewById(R.id.back_settings);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        });

        /**
         * TODO(Bea): Create a list view like the one described under the "Settings" page in the wireframes.
         *  The XML file related to this java ile is res/layout/activity_settings.xml
         */
    }
}