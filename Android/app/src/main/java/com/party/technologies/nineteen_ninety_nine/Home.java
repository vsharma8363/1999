package com.party.technologies.nineteen_ninety_nine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.party.technologies.nineteen_ninety_nine.data.Party;
import com.party.technologies.nineteen_ninety_nine.data.PartyInterface;
import com.party.technologies.nineteen_ninety_nine.data.User;
import com.party.technologies.nineteen_ninety_nine.data.UserInterface;

import java.util.ArrayList;

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