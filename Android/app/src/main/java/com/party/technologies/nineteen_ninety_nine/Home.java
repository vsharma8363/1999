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
        if(!UserStorage.isUserSetup()) {
            Intent setupProfile = new Intent(Home.this, SetupProfile.class);
            Home.this.startActivity(setupProfile);
        }
        testData = (TextView)findViewById(R.id.testData);
        testData.setText(UserStorage.getUser().getFullName() + "\n" +
                UserStorage.getUser().getBio());
    }

    public void logOut(View v) {
        FirebaseAuth.getInstance().signOut();
        UserStorage.wipeStoredData();
        Intent login = new Intent(Home.this, LoginSignup.class);
        Home.this.startActivity(login);
    }

    public void editProfile(View v) {
        //Intent profileCreator = new Intent(Home.this, SetupProfile.class);
        //Home.this.startActivity(profileCreator);
    }
}