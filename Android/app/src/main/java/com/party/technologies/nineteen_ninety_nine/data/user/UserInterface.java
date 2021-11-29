package com.party.technologies.nineteen_ninety_nine.data.user;

import android.app.ProgressDialog;
import android.net.Uri;
import android.text.style.IconMarginSpan;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.party.technologies.nineteen_ninety_nine.data.party.Party;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class UserInterface {

    private static FirebaseUser fbUser;
    private static DocumentReference userRef;
    private static User currentUser;
    private static boolean currentUserInitialized;
    private static boolean allUsersInitialized;
    private static CollectionReference usersCollection;
    private static ArrayList<User> allUsers;
    private static String igUserName;
    private static StorageReference storageReference;
    private static ArrayList<String> profilePictures;

    public static void initialize(FirebaseUser firebaseUser) {
        currentUserInitialized = false;
        allUsersInitialized = false;
        fbUser = firebaseUser;
        usersCollection = FirebaseFirestore.getInstance().collection("users");
        userRef = usersCollection.document(fbUser.getUid());
        profilePictures = new ArrayList<String>();
        storageReference = FirebaseStorage.getInstance().getReference();
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
                currentUserInitialized = true;
            }
        });

        usersCollection
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        ArrayList<User> users = new ArrayList<User>();
                        for (QueryDocumentSnapshot doc : value)
                            users.add(doc.toObject(User.class));
                        allUsers = users;
                        allUsersInitialized = true;
                    }
                });
    }

    public static boolean isInitialized() {
        return currentUserInitialized && allUsersInitialized;
    }

    public static boolean isNewUser() {
        return currentUser == null;
    }

    public static void setupNewUser() {
        User newUser = new User(fbUser.getUid(), fbUser.getPhoneNumber());
        currentUser = newUser;
        currentUser.setProfilePictures(profilePictures);
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

    public static User getUser(String UID) {
        for(User u:allUsers) {
            if(u.getUID().equals(UID))
                return u;
        }
        return null;
    }

    public static String getInstagramUserName() {
        return igUserName;
    }

    public static void setInstagramUserName(String userName) {
        igUserName = userName;
    }

    public static void uploadImage(Uri filePath)
    {
        String imagePathFB = "images/" + UUID.randomUUID().toString();
        profilePictures.add(imagePathFB);
        StorageReference ref
                    = storageReference
                    .child(
                            imagePathFB);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                        }
                    });
        }
}