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

import java.util.ArrayList;

public class SetupProfile extends AppCompatActivity {

    private EditText fullName, email, bio;
    private int PICK_IMAGE_MULTIPLE = 1;
    private int position = 0;
    EditText age;
    private ImageSwitcher imageView;
    private ArrayList<Uri> mArrayUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);
        mArrayUri = new ArrayList<Uri>();
        fullName = (EditText)findViewById(R.id.full_name);
        email = (EditText)findViewById(R.id.email);
        email = (EditText)findViewById(R.id.email);
        bio = (EditText)findViewById(R.id.bio);
        // Add images.
        imageView = findViewById(R.id.image);
        Button previous = findViewById(R.id.left);
        mArrayUri = new ArrayList<Uri>();

        // showing all images in imageswitcher
        imageView.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView1 = new ImageView(getApplicationContext());
                return imageView1;
            }
        });
        Button next = findViewById(R.id.right);

        // click here to select next image
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < mArrayUri.size() - 1) {
                    // increase the position by 1
                    position++;
                    imageView.setImageURI(mArrayUri.get(position));
                } else {
                    Toast.makeText(SetupProfile.this, "Last Image Already Shown", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // click here to view previous image
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    // decrease the position by 1
                    position--;
                    imageView.setImageURI(mArrayUri.get(position));
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // initialising intent
                Intent intent = new Intent();

                // setting type to select to be image
                intent.setType("image/*");

                // allowing multiple image to be selected
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });
        imageView.setImageURI(Uri.parse("android.resource://com.party.technologies.nineteen_ninety_nine/drawable/add_image"));
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
        for(Uri uri:mArrayUri) {
            UserInterface.uploadImage(uri);
        }
        UserInterface.setupNewUser();
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
        else if(mArrayUri.size() <= 0)
            Toast.makeText(getApplicationContext(), Constants.INVALID_IMAGES_MSG, Toast.LENGTH_SHORT).show();
        else if(age.getText().toString().length() <= 0)
            Toast.makeText(getApplicationContext(), "Please enter your age!", Toast.LENGTH_SHORT).show();
        else if(UserInterface.getInstagramUserName() == null)
            Toast.makeText(getApplicationContext(), Constants.INVALID_IG, Toast.LENGTH_SHORT).show();
        else
            return true;
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mArrayUri = new ArrayList<Uri>();
        // When an Image is picked
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                int cout = data.getClipData().getItemCount();
                for (int i = 0; i < cout; i++) {
                    // adding imageuri in array
                    Uri imageurl = data.getClipData().getItemAt(i).getUri();
                    mArrayUri.add(imageurl);
                }
                // setting 1st selected image into image switcher
                imageView.setImageURI(mArrayUri.get(0));
                position = 0;
            } else {
                Uri imageurl = data.getData();
                mArrayUri.add(imageurl);
                imageView.setImageURI(mArrayUri.get(0));
                position = 0;
            }
        } else {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }
}