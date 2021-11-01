package com.party.technologies.nineteen_ninety_nine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.party.technologies.nineteen_ninety_nine.data.User;

public class Home extends AppCompatActivity {

    TextView testData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        testData = (TextView)findViewById(R.id.testData);
        testData.setText(
                User.getFullName() + "\n" +
                User.getEmail() + "\n" +
                User.getPhoneNumber() + "\n" +
                User.getBio() + "\n" +
                User.getMileRange());
    }

    public void editProfile(View v) {
        startActivity(new Intent(Home.this, SetupProfile.class));
    }

    public void logOut(View v) {
        User.logout();
        startActivity(new Intent(Home.this, LoginSignup.class));
    }

}