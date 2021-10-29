package com.party.technologies.nineteen_ninety_nine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    TextView testData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        testData = (TextView)findViewById(R.id.testData);
        testData.setText(
                UserStorage.getFullName() + "\n" +
                UserStorage.getEmail() + "\n" +
                UserStorage.getPhoneNumber() + "\n" +
                UserStorage.getBio() + "\n" +
                UserStorage.getMileRange());
    }

    public void editProfile(View v) {
        startActivity(new Intent(Home.this, SetupProfile.class));
    }

    public void logOut(View v) {
        UserStorage.logout();
        startActivity(new Intent(Home.this, LoginSignup.class));
    }

}