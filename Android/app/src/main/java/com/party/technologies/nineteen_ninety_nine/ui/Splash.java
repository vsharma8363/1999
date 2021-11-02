package com.party.technologies.nineteen_ninety_nine.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.party.PartyInterface;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;

public class Splash extends Activity {

    private FirebaseAuth mAuth;
    private Intent nextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // 5 second splash screen.
        final Thread checkUserSleepThread = new Thread(){
            public void run(){
                if(currentUser != null) {
                    // Some user is signed in.
                    UserInterface.initialize(currentUser);
                    // Wait until the user data has loaded before navigating to home screen.
                    while(!UserInterface.isInitialized()) {}
                    if(UserInterface.isNewUser())
                        nextPage = new Intent(Splash.this, SetupProfile.class);
                    else
                        nextPage = new Intent(Splash.this, Home.class);
                }
                else {
                    // No user is signed in.
                    // Direct the user to the Login/Signup Screen.
                    nextPage = new Intent(Splash.this, LoginSignup.class);
                }
                // Load party data
                PartyInterface.initialize();
                while(!PartyInterface.isInitialized()) {}
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                }
                Splash.this.startActivity(nextPage);
            }
        };
        checkUserSleepThread.start();
    }

}