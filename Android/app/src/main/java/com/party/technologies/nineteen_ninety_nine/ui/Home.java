package com.party.technologies.nineteen_ninety_nine.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.user.User;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;

public class Home extends AppCompatActivity {

    TextView userDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userDataTextView = findViewById(R.id.testData);
        User user = UserInterface.getCurrentUser();
        userDataTextView.setText(
                user.getFullName() + "\n" +
                user.getEmail() + "\n" +
                user.getPhoneNumber() + "\n" +
                user.getBio());
    }

    public void editProfile(View v) {
        startActivity(new Intent(Home.this, SetupProfile.class));
    }

    public void logOut(View v) {
        UserInterface.logoutCurrentUser();
        startActivity(new Intent(Home.this, LoginSignup.class));
    }
}