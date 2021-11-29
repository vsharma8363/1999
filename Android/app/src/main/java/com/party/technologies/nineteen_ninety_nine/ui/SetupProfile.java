package com.party.technologies.nineteen_ninety_nine.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.Constants;
import com.party.technologies.nineteen_ninety_nine.data.user.User;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.social.InstagramScreen;

import java.net.URI;
import java.util.ArrayList;

public class SetupProfile extends AppCompatActivity {

    private EditText fullName, email, bio, age;
    private ImageView profilePicture;
    private Uri profilePictureURI;
    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);
        profilePicture = (ImageView)findViewById(R.id.profile_picture);
        fullName = (EditText)findViewById(R.id.full_name);
        email = (EditText)findViewById(R.id.email);
        email = (EditText)findViewById(R.id.email);
        bio = (EditText)findViewById(R.id.bio);

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        // Instagram integration setup.
        Button instagramButton = findViewById(R.id.connect_instagram);
        instagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SetupProfile.this, InstagramScreen.class));
            }
        });
        new Thread() {
            public void run() {
                while(UserInterface.getInstagramUserName() == null){}
                instagramButton.setText("@" + UserInterface.getInstagramUserName());
                instagramButton.setBackgroundColor(Color.CYAN);
                instagramButton.setClickable(false);
            }
        }.start();
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
        age = findViewById(R.id.date_of_birth);
    }

    public void setupUserProfile() {
        UserInterface.setupNewUser(profilePictureURI);
        User currentUser = UserInterface.getCurrentUser();
        currentUser.setFullName(fullName.getText().toString());
        currentUser.setEmail(email.getText().toString());
        currentUser.setBio(bio.getText().toString());
        currentUser.setAge(age.getText().toString());
        currentUser.setInstagramUserName(UserInterface.getInstagramUserName());
        UserInterface.updateUserData();
    }

    private boolean areFieldsValid() {
        if(fullName.getText().toString().length() <= 0)
            Toast.makeText(getApplicationContext(), Constants.INVALID_FULL_NAME_MSG, Toast.LENGTH_SHORT).show();
        else if(email.getText().toString().length() <= 0)
            Toast.makeText(getApplicationContext(), Constants.INVALID_EMAIL_MSG, Toast.LENGTH_SHORT).show();
        else if(bio.getText().toString().length() <= 0)
            Toast.makeText(getApplicationContext(), Constants.INVALID_BIO_MSG, Toast.LENGTH_SHORT).show();
        else if(profilePictureURI == null)
            Toast.makeText(getApplicationContext(), Constants.INVALID_IMAGES_MSG, Toast.LENGTH_SHORT).show();
        else if(age.getText().toString().length() <= 0)
            Toast.makeText(getApplicationContext(), "Please enter your age!", Toast.LENGTH_SHORT).show();
        else if(UserInterface.getInstagramUserName() == null)
            Toast.makeText(getApplicationContext(), Constants.INVALID_IG, Toast.LENGTH_SHORT).show();
        else
            return true;
        return false;
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            profilePictureURI = data.getData();
            profilePicture.setImageURI(profilePictureURI);
        }
    }
}