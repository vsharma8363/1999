package com.party.technologies.nineteen_ninety_nine.ui;

import androidx.appcompat.app.AppCompatActivity;
import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.Constants;
import com.party.technologies.nineteen_ninety_nine.data.user.User;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EditProfile extends AppCompatActivity {
    private EditText fullName, email,instagram, bio, age;
    private ImageView profilePicture;
    private Uri profilePictureURI;
    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        profilePicture = (ImageView)findViewById(R.id.profile_picture);
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        fullName = (EditText)findViewById(R.id.full_name);
        email = (EditText)findViewById(R.id.email);
        instagram = (EditText)findViewById(R.id.instagram);
        bio = (EditText)findViewById(R.id.bio);

        if(UserInterface.getCurrentUser() != null) {
            User currentUser = UserInterface.getCurrentUser();

            EditText name = (EditText) findViewById(R.id.full_name);
            name.setText(currentUser.getFullName());

            EditText getEmail = (EditText) findViewById(R.id.email);
            getEmail.setText(currentUser.getEmail());

            EditText getInsta = (EditText) findViewById(R.id.instagram);
            getInsta.setText(currentUser.getInstagramUserName());

            EditText getBio = (EditText) findViewById(R.id.bio);
            getBio.setText(currentUser.getBio());

            findViewById(R.id.setup_profile_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (areFieldsValid()) {
                        EditUserProfile();
                        startActivity(new Intent(EditProfile.this, Home.class));
                    }
                }
            });


        }
        findViewById(R.id.cancel_changes_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfile.this, Home.class));
            }
        });
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
        else
            return true;
        return false;
    }


    public void EditUserProfile() {
    //    UserInterface.setupNewUser(profilePictureURI);
        User currentUser = UserInterface.getCurrentUser();
        currentUser.setFullName(fullName.getText().toString());
        currentUser.setEmail(email.getText().toString());
        currentUser.setInstagramUserName(instagram.getText().toString());
        currentUser.setBio(bio.getText().toString());
        UserInterface.updateUserData();
    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
}