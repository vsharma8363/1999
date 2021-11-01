package com.party.technologies.nineteen_ninety_nine.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PartyInterface {

    // The maximum number of miles range possible for any user.
    private static final double MAXIMUM_PARTIES_RADIUS_MILES = 100.0;
    private static ArrayList<Party> allParties;
    private static CollectionReference partyCollection;
    private static boolean finishedInit;

    public static void initialize() {
        finishedInit = false;
        partyCollection = FirebaseFirestore.getInstance().collection("parties");
        launchPartyDataListener();
    }

    private static void launchPartyDataListener() {
        partyCollection
                // Here we can limit the amount of parties we are updating.
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        ArrayList<Party> parties = new ArrayList<Party>();
                        for (QueryDocumentSnapshot doc : value)
                            parties.add(new Party(doc.getData()));
                        allParties = parties;
                        finishedInit = true;
                    }
                });
    }

    public static boolean isInitialized() {
        return finishedInit;
    }

    // Returns party ID
    public static String publishParty(Party party) {
        String partyID = generatePartyID();
        updateParty(partyID, party);
        return partyID;
    }

    public static void updateParty(String partyID, Party party) {
        DocumentReference partyRef = partyCollection.document(partyID);
        party.setPartyID(partyID);
        partyRef.set(party.getHashMap());
    }

    private static String generatePartyID() {
        return UserInterface.getCurrentUserUID() + "_" + System.currentTimeMillis();
    }

    // Run this in a thread to be sure there is no program crash.
    public static ArrayList<Party> getPartiesByHost(String hostUID) {
        ArrayList<Party> output = new ArrayList<Party>();
        for(Party p:allParties) {
            if(p.getHostID().equals(hostUID))
                output.add(p);
        }
        return output;
    }

    /**Returns parties located in the radius (miles)**/
    public static ArrayList<Party> getPartiesInRadius(double longitude, double latitude, double radiusMiles) {
        return null;
    }

}