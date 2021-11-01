package com.party.technologies.nineteen_ninety_nine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.party.technologies.nineteen_ninety_nine.data.User;

public class SetupProfile extends AppCompatActivity {

    private TextView fullNameView, emailView, bioView;
    private EditText fullName, email, bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);
        fullNameView = (TextView)findViewById(R.id.full_name_textview);
        fullName = (EditText)findViewById(R.id.full_name);
        emailView = (TextView)findViewById(R.id.email_textview);
        email = (EditText)findViewById(R.id.email);
        emailView = (TextView)findViewById(R.id.email_textview);
        email = (EditText)findViewById(R.id.email);
        bioView = (TextView)findViewById(R.id.bio_textview);
        bio = (EditText)findViewById(R.id.bio);
        // If user is editing a profile already setup, populate screen with data.
        if(!User.isNewUser()) {
            fullName.setText(User.getFullName());
            email.setText(User.getEmail());
            bio.setText(User.getBio());
        }
    }

    public void submitProfile(View v) {
        if(fullName.getText().toString().length() <= 0) {
            fullNameView.setText("Please enter your name!");
            fullNameView.setTextColor(Color.RED);
        }
        else if(email.getText().toString().length() <= 0) {
            emailView.setText("Please enter your email!");
            emailView.setTextColor(Color.RED);
        }
        else if(bio.getText().toString().length() <= 0) {
            bioView.setText("Please enter your bio!");
            bioView.setTextColor(Color.RED);
        }
        else {
            User.setFullName(fullName.getText().toString());
            User.setEmail(email.getText().toString());
            User.setBio(bio.getText().toString());
            User.resetLoginTime();
            startActivity(new Intent(SetupProfile.this, Home.class));
        }
    }
}