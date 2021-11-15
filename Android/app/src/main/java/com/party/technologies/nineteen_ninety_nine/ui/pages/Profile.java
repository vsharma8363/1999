package com.party.technologies.nineteen_ninety_nine.ui.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.user.User;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.ui.Home;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // Define back button logic.
        Button back = findViewById(R.id.back_profile);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        });
        // Button to allow user to connect instagram

        // Show user data.
        User user = UserInterface.getCurrentUser();
        TextView fullName = findViewById(R.id.full_name);
        fullName.setText(user.getFullName() + "\n");
        TextView phoneNumber = findViewById(R.id.phone_number);
        phoneNumber.setText(user.getPhoneNumber() + "\n");
        TextView email = findViewById(R.id.email);
        email.setText(user.getEmail() + "\n");
        TextView bio = findViewById(R.id.bio);
        bio.setText(user.getBio() + "\n");
    }
}