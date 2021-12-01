package com.party.technologies.nineteen_ninety_nine.ui.party_viewer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.daprlabs.cardstack.SwipeDeck;
import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.party.Party;
import com.party.technologies.nineteen_ninety_nine.data.party.PartyInterface;
import com.party.technologies.nineteen_ninety_nine.data.user.User;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.ui.misc.UserAdapter;
import com.party.technologies.nineteen_ninety_nine.ui.misc.UserProfile;

import java.util.ArrayList;

public class ScreenGuests extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_guests);
        populateGuestScreeningView(PartyInterface.getPartyByHost(UserInterface.getCurrentUserUID()));
    }

    private void populateGuestScreeningView(Party hostingParty) {
        ArrayList<User> pendingGuests = new ArrayList<User>();
        for(String UID:hostingParty.getGuestsPending())
            pendingGuests.add(UserInterface.getUser(UID));

        ArrayList<UserProfile> profiles = new ArrayList<UserProfile>();
        SwipeDeck cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);
        for(User guest:pendingGuests) {
            UserProfile profile = new UserProfile(
                    guest.getFullName(),
                    guest.getPhoneNumber(),
                    guest.getBio(),
                    guest.getAge(),
                    guest.getInstagramUserName(),
                    guest.getProfilePicture());
            profiles.add(profile);
        }

        final UserAdapter adapter = new UserAdapter(profiles, getApplicationContext());

        // on below line we are setting adapter to our card stack.
        cardStack.setAdapter(adapter);

        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                // Accept
                User guest = pendingGuests.get(position);
                // on card swiped to right we are displaying a toast message.
                hostingParty.addDeniedGuest(guest.getUID());
                Toast.makeText(getApplicationContext(), guest.getFullName() + " isn't coming.", Toast.LENGTH_SHORT).show();
                PartyInterface.updateParty(hostingParty);
            }

            @Override
            public void cardSwipedRight(int position) {
                // Accept
                User guest = pendingGuests.get(position);
                // on card swiped to right we are displaying a toast message.
                hostingParty.addApprovedGuest(guest.getUID());
                Toast.makeText(getApplicationContext(), guest.getFullName() + " is invited!", Toast.LENGTH_SHORT).show();
                PartyInterface.updateParty(hostingParty);
            }

            @Override
            public void cardsDepleted() {
                // this method is called when no card is present
                Toast.makeText(getApplicationContext(), "No more guests to screen", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void cardActionDown() {

            }

            @Override
            public void cardActionUp() {

            }
        });
    }
}