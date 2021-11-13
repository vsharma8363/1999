package com.party.technologies.nineteen_ninety_nine.data.party;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;

import java.util.ArrayList;

public class PartyInterface {

    private static ArrayList<Party> allParties;
    private static CollectionReference partyCollection;
    private static boolean finishedInit;

    /**
     * Begins initialization process that activates a listener for the party database.
     * Information from the party database will be updated in real time via Firebase.
     */
    public static void initialize() {
        finishedInit = false;
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
                            parties.add(new Party(doc.getData()));
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
        // Overwrite party object from local storage.
        for(Party party:allParties) {
            if(party.getPartyID().equals(updatedParty.getPartyID())) {
                allParties.remove(party);
                allParties.add(updatedParty);
                break;
            }
        }
        // Overwrite party data on server.
        DocumentReference partyRef = partyCollection.document(updatedParty.getPartyID());
        partyRef.set(updatedParty.getHashMap());
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
            if(party.getRequestedInvites().contains(UID))
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
        return allParties;
    }

    /**Returns parties located in the radius (miles)**/
    public static ArrayList<Party> getPartiesInRadius(double longitude, double latitude, double radiusMiles) {
        return null;
    }
}