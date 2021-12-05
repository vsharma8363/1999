package com.party.technologies.nineteen_ninety_nine.ui.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.user.User;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.ui.Home;
import com.party.technologies.nineteen_ninety_nine.ui.party_viewer.ViewParty;

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
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
            }
        });

        User user = UserInterface.getCurrentUser();

        TextView fullname = findViewById(R.id.activity_settings_fullname);
        fullname.setText(user.getFullName());
        TextView age = findViewById(R.id.activity_settings_birthday);
        age.setText(user.getAge());
        TextView mobile = findViewById(R.id.activity_settings_mobile);
        mobile.setText(user.getPhoneNumber());
        TextView email = findViewById(R.id.activity_settings_email);
        email.setText(user.getEmail());
        TextView instagram = findViewById(R.id.activity_settings_instagram);
        instagram.setText("@" + user.getInstagramUserName());
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://instagram.com/_u/" + user.getInstagramUserName());
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    Settings.this.startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    Settings.this.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/" + user.getInstagramUserName())));
                }
            }
        });
        instagram.setTextColor(Color.BLUE);



        /**
         * TODO(Bea): Create a list view like the one described under the "Settings" page in the wireframes.
         *  The XML layout file that defines what its going to look like:
         *      - res/layout/activity_settings.xml
         *  Basically, only this file and the XML file should have to be edited to implement the list view
         *  The list view is the one we have on Figma.
         */
    }
}