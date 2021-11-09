package com.party.technologies.nineteen_ninety_nine.data.user;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class UserInterface {

    private static FirebaseUser fbUser;
    private static DocumentReference userRef;
    private static User currentUser;
    private static boolean isInitializationFinished;

    public static void initialize(FirebaseUser firebaseUser) {
        isInitializationFinished = false;
        fbUser = firebaseUser;
        userRef = FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(fbUser.getUid());
        launchBackgroundUpdater();
    }

    /**Updates user data in the background using snapshots (Call once and forget).**/
    private static void launchBackgroundUpdater() {
        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                currentUser = snapshot.toObject(User.class);
                if(currentUser != null) {
                    currentUser.resetLoginTime();
                }
                isInitializationFinished = true;
            }
        });
    }

    public static boolean isInitialized() {
        return isInitializationFinished;
    }

    public static boolean isNewUser() {
        return currentUser == null;
    }

    public static void setupNewUser() {
        User newUser = new User(fbUser.getPhoneNumber());
        currentUser = newUser;
        userRef.set(newUser);
    }

    public static void logoutCurrentUser() {
        FirebaseAuth.getInstance().signOut();
        fbUser = null;
        userRef = null;
        currentUser = null;
    }

    public static String getCurrentUserUID() {
        return fbUser.getUid();
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void updateUserData() {
        userRef.set(currentUser);
    }
}
