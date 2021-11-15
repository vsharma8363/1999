package com.party.technologies.nineteen_ninety_nine.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.user.User;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;

import java.util.ArrayList;

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
        findViewById(R.id.instagram_connection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> instagramPictures = new ArrayList<String>();
                /** TODO(Bea): this onClick method is executed when the
                 * instagram connection button is clicked, it should direct you
                 * to an instagram signin, then we need to store all their
                 * instagram pictures into the arraylist of picture urls above.
                 * **/
            }
        });
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
            /**UserInterface.setupNewUser();
            // Get the current user and update data.
            User currentUser = UserInterface.getCurrentUser();
            currentUser.setFullName(fullName.getText().toString());
            currentUser.setEmail(email.getText().toString());
            currentUser.setBio(bio.getText().toString());
            UserInterface.updateUserData();
            startActivity(new Intent(SetupProfile.this, Home.class));**/
        }
    }
}