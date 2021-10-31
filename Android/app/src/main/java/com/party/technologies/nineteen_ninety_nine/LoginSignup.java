package com.party.technologies.nineteen_ninety_nine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import java.util.concurrent.TimeUnit;

public class LoginSignup extends AppCompatActivity {

    // Firebase authentication materials.
    private FirebaseAuth mAuth;
    private IntlPhoneInput phoneNumberSignup;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    // Toast messages for the user.
    private final String INVALID_PHONE_NUMBER_MSG = "Please enter a valid phone number";
    private final String SERVER_FAILURE_MSG = "Server request error, please try again";
    private final String EXCEEDED_SERVER_REQUESTS_MSG = "SMS server quota has been exceeded";
    private final String INCORRECT_VERIFICATION_MSG = "Incorrect verification code";
    private final String SUCCESSFUL_LOGIN_MSG = "Looks like you're ready to party :)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        mAuth = FirebaseAuth.getInstance();
        phoneNumberSignup = findViewById(R.id.phone_number_signup);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(getApplicationContext(), SERVER_FAILURE_MSG, Toast.LENGTH_LONG).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(getApplicationContext(), EXCEEDED_SERVER_REQUESTS_MSG, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
                setContentView(R.layout.activity_login_verification);
            }
        };
    }

    private String getPhoneNumber() {
        if(phoneNumberSignup.isValid())
            return phoneNumberSignup.getNumber();
        else
            return null;
    }

    public void signUp(View v) {
        String phoneNumber = getPhoneNumber();
        if(phoneNumber != null)
            startPhoneNumberVerification(phoneNumber);
        else {
            Toast.makeText(getApplicationContext(), INVALID_PHONE_NUMBER_MSG, Toast.LENGTH_LONG).show();
        }
    }

    public void verifyCode(View v) {
        String verificationCode = ((EditText)findViewById(R.id.verification_code_field)).getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
        signInWithPhoneAuthCredential(credential);
    }

    public void resendVerification(View v) {
        String phoneNumber = getPhoneNumber();
        if(phoneNumber != null)
            resendVerificationCode(phoneNumber, this.mResendToken);
        else {
            Toast.makeText(getApplicationContext(), INVALID_PHONE_NUMBER_MSG, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            new Thread(new Runnable() {
                                public void run() {
                                    UserStorage.initialize(mAuth.getCurrentUser());
                                    // Wait until the user data has loaded before navigating to home screen.
                                    while(!UserStorage.isUserDataLoadedFromServer()) {}
                                    Intent nextPage;
                                    if(UserStorage.isNewUser())
                                        nextPage = new Intent(LoginSignup.this, SetupProfile.class);
                                    else
                                        nextPage = new Intent(LoginSignup.this, Home.class);
                                    LoginSignup.this.startActivity(nextPage);
                                }
                            }).start();
                        } else {
                            // Failure
                            Toast.makeText(getApplicationContext(), INCORRECT_VERIFICATION_MSG, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}