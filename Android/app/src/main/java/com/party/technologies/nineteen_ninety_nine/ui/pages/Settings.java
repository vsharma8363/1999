package com.party.technologies.nineteen_ninety_nine.ui.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.ui.Home;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // Define back button logic.
        findViewById(R.id.activity_settings_done_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * TODO(Bea): Create a list view like the one described under the "Settings" page in the wireframes.
         *  The XML layout file that defines what its going to look like:
         *      - res/layout/activity_settings.xml
         *  Basically, only this file and the XML file should have to be edited to implement the list view
         *  The list view is the one we have on Figma.
         */
    }
}