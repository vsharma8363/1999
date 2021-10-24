package com.party.technologies.nineteen_ninety_nine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SetupProfile extends AppCompatActivity {

    private EditText fullName;
    private EditText email;
    private EditText bio;
    private Thread homeThread;
    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);
        fullName = (EditText)findViewById(R.id.full_name);
        email = (EditText)findViewById(R.id.email);
        bio = (EditText)findViewById(R.id.bio);
    }

    public void submitProfile(View v) {
        User newUser = new User();
        newUser.setFullName(fullName.getText().toString());
        newUser.setEmail(email.getText().toString());
        newUser.setBio(bio.getText().toString());
        UserStorage.updateUserData(newUser);
        final Thread waitForUserDataUpdate = new Thread(){
            public void run(){
                try{
                    sleep(5000);
                    Intent home = new Intent(SetupProfile.this, Home.class);
                    SetupProfile.this.startActivity(home);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                }
            }
        };
        waitForUserDataUpdate.start();
    }
}