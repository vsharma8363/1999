package com.party.technologies.nineteen_ninety_nine;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserStorage {

    private static FirebaseUser fbUser;
    private static User user;
    private static FirebaseFirestore db;
    private static DocumentReference userRef;
    private static boolean loadingUserData;

    public static void setFirebaseUser(FirebaseUser firebaseUser) {
        loadingUserData = false;
        fbUser = firebaseUser;
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users").document(fbUser.getUid());
        loadUserData();
    }

    public static boolean isLoadingUserData() {
        return loadingUserData;
    }

    public static boolean isUserSetup() {
        return user != null;
    }

    public static User getUser() {
        return user;
    }

    public static void updateUserData(User user) {
        loadingUserData = true;
        userRef.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadUserData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public static void wipeStoredData() {
        fbUser = null;
    }

    public static String getUID() {
        return fbUser.getUid();
    }

    private static void loadUserData() {
        loadingUserData = true;
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user = document.toObject(User.class);
                    } else {
                        user = null;
                    }
                    loadingUserData = false;
                } else {
                    Log.d(TAG, "get() failed with ", task.getException());
                }
            }
        });
    }
}
