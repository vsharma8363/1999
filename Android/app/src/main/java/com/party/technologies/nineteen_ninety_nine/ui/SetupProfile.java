package com.party.technologies.nineteen_ninety_nine.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.Constants;
import com.party.technologies.nineteen_ninety_nine.data.user.User;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.social.InstagramScreen;

public class SetupProfile extends AppCompatActivity {

    private EditText fullName, email, bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);
        fullName = (EditText)findViewById(R.id.full_name);
        email = (EditText)findViewById(R.id.email);
        email = (EditText)findViewById(R.id.email);
        bio = (EditText)findViewById(R.id.bio);
        // Define create profile button login
        findViewById(R.id.setup_profile_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(areFieldsValid()) {
                    setupUserProfile();
                    // Redirect to home screen.
                    startActivity(new Intent(SetupProfile.this, Home.class));
                }
            }
        });
    }

    public void setupUserProfile() {
        UserInterface.setupNewUser();
        User currentUser = UserInterface.getCurrentUser();
        currentUser.setFullName(fullName.getText().toString());
        currentUser.setEmail(email.getText().toString());
        currentUser.setBio(bio.getText().toString());
        UserInterface.updateUserData();
    }

    private boolean areFieldsValid() {
        if(fullName.getText().toString().length() <= 0)
            Toast.makeText(getApplicationContext(), Constants.INVALID_FULL_NAME_MSG, Toast.LENGTH_SHORT).show();
        else if(email.getText().toString().length() <= 0)
            Toast.makeText(getApplicationContext(), Constants.INVALID_EMAIL_MSG, Toast.LENGTH_SHORT).show();
        else if(bio.getText().toString().length() <= 0)
            Toast.makeText(getApplicationContext(), Constants.INVALID_BIO_MSG, Toast.LENGTH_SHORT).show();
        else
            return true;
        return false;
    }
}