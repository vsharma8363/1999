package com.party.technologies.nineteen_ninety_nine;

import android.graphics.Color;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserStorage {

    private static FirebaseUser fbUser;
    private static FirebaseFirestore db;
    private static DocumentReference userRef;
    private static Map<String, Object> userData;
    private static boolean userDataLoadedFromServer;
    private static boolean partyDataLoadedFromServer;

    public static void initialize(FirebaseUser firebaseUser) {
        userDataLoadedFromServer = false;
        partyDataLoadedFromServer = false;
        fbUser = firebaseUser;
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users").document(fbUser.getUid());
        userDataBackgroundUpdater();
    }

    /**Updates user data in the background using snapshots (Call once and forget).**/
    private static void userDataBackgroundUpdater() {
        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                userData = snapshot.getData();
                if(userData == null) {
                    // This is a new user, set default values for user.
                    userData = new HashMap<String, Object>();
                    userData.put("phone_number", fbUser.getPhoneNumber());
                    userData.put("mile_range", 5.0);
                }
                else {
                    // This user is already registered.
                    resetLoginTime();
                }
                userDataLoadedFromServer = true;
            }
        });
    }

    public static boolean isUserDataLoadedFromServer() {
        return userDataLoadedFromServer;
    }

    public static boolean isPartyDataLoadedFromServer() {
        return partyDataLoadedFromServer;
    }

    public static boolean isNewUser() {
        return userData.get("last_login") == null;
    }

    public static void resetLoginTime() {
        userData.put("last_login", System.currentTimeMillis());
    }

    public static void logout() {
        fbUser = null;
        userRef = null;
        FirebaseAuth.getInstance().signOut();
        userData = null;
    }

    public static String getFullName() {
        return userData.get("full_name").toString();
    }

    public static void setFullName(String fullName) {
        userData.put("full_name", fullName);
        userRef.set(userData);
    }

    public static String getEmail() {
        return userData.get("email").toString();
    }

    public static void setEmail(String email) {
        userData.put("email", email);
        userRef.set(userData);
    }

    public static String getPhoneNumber() {
        return userData.get("phone_number").toString();
    }

    public static String getBio() {
        return userData.get("bio").toString();
    }

    public static void setBio(String bio) {
        userData.put("bio", bio);
        userRef.set(userData);
    }

    public static void setBirthday(Date birthday) {
        System.out.println(birthday);
        userData.put("birthday_millis", birthday.getTime());
        userRef.set(userData);
    }

    public static void setMileRange(double miles) {
        userData.put("mile_range", miles);
        userRef.set(userData);
    }

    public static double getMileRange() {
        return Double.parseDouble(userData.get("mile_range").toString());
    }
}
