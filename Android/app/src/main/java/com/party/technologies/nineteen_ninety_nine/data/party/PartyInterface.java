package com.party.technologies.nineteen_ninety_nine.data.party;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.party.technologies.nineteen_ninety_nine.data.user.User;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.ui.misc.ImageLoadTask;

import java.util.ArrayList;
import java.util.UUID;


public class PartyInterface {

    private static ArrayList<Party> allParties;
    private static StorageReference storageReference;

    public static ArrayList<String> getPartyImages() {
        return partyImages;
    }

    public static void uploadPartyImage(Uri partyImage) {
        String firebasePartyImage = "images/" + UUID.randomUUID().toString();
        storageReference.child(firebasePartyImage).putFile(partyImage);
        partyImages.add(firebasePartyImage);
    }

    public static void loadImageToImageView(String imagePath, ImageView imageView, Context context) {
        StorageReference ref = storageReference.child(imagePath);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                new ImageLoadTask(uri.toString(), imageView).execute();
                System.out.println(uri.toString());
            }
        });
    }

    private static ArrayList<String> partyImages;
    private static CollectionReference partyCollection;
    private static boolean finishedInit;

    /**
     * Begins initialization process that activates a listener for the party database.
     * Information from the party database will be updated in real time via Firebase.
     */
    public static void initialize() {
        allParties = new ArrayList<Party>();
        partyImages = new ArrayList<String>();
        finishedInit = false;
        storageReference = FirebaseStorage.getInstance().getReference();
        partyCollection = FirebaseFirestore.getInstance().collection("parties");
        launchBackgroundUpdater();
    }

    /**
     * Snapshot listener that will update allParties variable with a list of all
     * parties taking place when the database detects a change (add/remove/update).
     */
    private static void launchBackgroundUpdater() {
        partyCollection
                // Here we can limit the amount of parties we are updating.
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        ArrayList<Party> parties = new ArrayList<Party>();
                        for (QueryDocumentSnapshot doc : value)
                            parties.add(doc.toObject(Party.class));
                        allParties = parties;
                        finishedInit = true;
                    }
                });
    }

    public static boolean isInitialized() {
        return finishedInit;
    }

    /**
     * Publishes a party to the party database.
     * @param party A party object that the user wishes to publish to database.
     * @return ID of party in string format.
     */
    public static void publishParty(Party party) {
        party.setPartyID(generatePartyID());
        updateParty(party);
    }

    /**
     * Overwrites an existing party in the database with the corresponding partyID.
     * @param updatedParty A party object that the user wishes to overwrite.
     * @return ID of party in string format.
     */
    public static void updateParty(Party updatedParty) {
        // Overwrite local party data.
        if(!allParties.contains(updatedParty))
            allParties.add(updatedParty);
        else {
            for(Party p:allParties) {
                if(p.getPartyID() == updatedParty.getPartyID()) {
                    allParties.remove(p);
                    allParties.add(updatedParty);
                    break;
                }
            }
        }
        // Overwrite party data on server.
        DocumentReference partyRef = partyCollection.document(updatedParty.getPartyID());
        partyRef.set(updatedParty);
    }

    /**
     * @return A random party ID containing the current users UID.
     */
    private static String generatePartyID() {
        return UserInterface.getCurrentUserUID() + "_" + System.currentTimeMillis();
    }

    public static ArrayList<Party> getUpcomingParties(String UID) {
        ArrayList<Party> upcomingParties = new ArrayList<Party>();
        for(Party party:allParties) {
            if(party.getGuestsApproved().contains(UID) ||
                party.getGuestsPending().contains(UID) ||
                party.getGuestsDenied().contains(UID) ||
                party.getHostID().equals(UID))
                upcomingParties.add(party);
        }
        return upcomingParties;
    }

    /**
     * Returns a Party object hosted by a host with the UID.
     * @param hostUID A UID to search for.
     * @return Party object of party hosted by the host. (null if no party is being hosted)
     */
    public static Party getPartyByHost(String hostUID) {
        for(Party party:allParties) {
            if(party.getHostID().equals(hostUID))
                return party;
        }
        return null;
    }

    public static Party getPartyByID(String partyID) {
        for(Party party:allParties) {
            if(party.getPartyID().equals(partyID))
                return party;
        }
        return null;
    }

    public static void deleteParty(String partyID) {
        // Remove party from local storage, to keep illusion of instantaneous server updates.
        for(Party party:allParties) {
            if(party.getPartyID().equals(partyID)) {
                allParties.remove(party);
                break;
            }
        }
        // Push a request to delete the party from server storage.
        partyCollection.document(partyID).delete();
    }

    public static ArrayList<Party> getAllParties() {
        return (allParties == null)? new ArrayList<Party>() : allParties;
    }

    /**Returns parties located in the radius (miles)**/
    public static ArrayList<Party> getPartiesInRadius(double longitude, double latitude, double radiusMiles) {
        return null;
    }

    public static void resetPartyImages() {
        partyImages.clear();
    }
}