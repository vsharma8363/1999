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
import android.widget.Toast;

import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.user.User;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.ui.Home;
import com.party.technologies.nineteen_ninety_nine.ui.LoginSignup;
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
        TextView safe_party_practices = findViewById(R.id.activity_settings_safe_party_practices);
        safe_party_practices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.bestcolleges.com/blog/college-party-safety/"));
                startActivity(browserIntent);
            }
        });
        TextView feedback = findViewById(R.id.activity_settings_feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/vsharma8363/1999/issues"));
                startActivity(browserIntent);
            }
        });
        TextView rate_our_app = findViewById(R.id.activity_settings_rate_our_app);
        rate_our_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/vsharma8363/1999/issues"));
                startActivity(browserIntent);
            }
        });
        TextView privacy_policy = findViewById(R.id.activity_settings_privacy_policy);
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/vsharma8363/1999/blob/main/Privacy%20Policy.md"));
                startActivity(browserIntent);
            }
        });
        TextView alcohol_drug_hotline = findViewById(R.id.activity_settings_alcohol_hotline);
        alcohol_drug_hotline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:18006624357"));
                startActivity(intent);
            }
        });
        TextView opiate_resources = findViewById(R.id.activity_settings_opiate_resources);
        opiate_resources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hhs.gov/opioids/treatment/index.html"));
                startActivity(browserIntent);
            }
        });
        TextView logout = findViewById(R.id.activity_settings_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInterface.logoutCurrentUser();
                startActivity(new Intent(Settings.this, LoginSignup.class));
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