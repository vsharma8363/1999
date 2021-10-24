package com.party.technologies.nineteen_ninety_nine;

import static java.lang.Thread.sleep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends Activity {

    private FirebaseAuth mAuth;
    private Intent nextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // 3 second splash screen.
        final Thread checkUserSleepThread = new Thread(){
            public void run(){
                if(currentUser != null) {
                    // Current user is signed in.
                    UserStorage.setFirebaseUser(currentUser);
                    nextPage = new Intent(Splash.this, Home.class);
                }
                else {
                    // No user is signed in.
                    nextPage = new Intent(Splash.this, LoginSignup.class);
                }
                try{
                    sleep(5000);
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