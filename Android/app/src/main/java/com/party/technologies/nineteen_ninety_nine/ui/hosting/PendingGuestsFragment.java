package com.party.technologies.nineteen_ninety_nine.ui.hosting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daprlabs.cardstack.SwipeDeck;
import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.party.Party;
import com.party.technologies.nineteen_ninety_nine.data.party.PartyInterface;
import com.party.technologies.nineteen_ninety_nine.data.user.User;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.ui.misc.UserAdapter;
import com.party.technologies.nineteen_ninety_nine.ui.misc.UserProfile;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PendingGuestsFragment extends Fragment {

    public PendingGuestsFragment() {
    }

    public static HostViewFragment newInstance() {
        return new HostViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_guests, container, false);
        Party hostingParty = PartyInterface.getPartyByHost(UserInterface.getCurrentUserUID());
        // Populate party requests screen.
        populateGuestScreeningView(view, PartyInterface.getPartyByHost(UserInterface.getCurrentUserUID()));
        return view;
    }

    private void populateGuestScreeningView(View view, Party hostingParty) {
        ArrayList<User> pendingGuests = new ArrayList<User>();
        for(String UID:hostingParty.getGuestsPending())
            pendingGuests.add(UserInterface.getUser(UID));

        ArrayList<UserProfile> profiles = new ArrayList<UserProfile>();
        SwipeDeck cardStack = (SwipeDeck) view.findViewById(R.id.swipe_deck);
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

        final UserAdapter adapter = new UserAdapter(profiles, getActivity());

        // on below line we are setting adapter to our card stack.
        cardStack.setAdapter(adapter);

        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                // Accept
                User guest = pendingGuests.get(position);
                // on card swiped to right we are displaying a toast message.
                hostingParty.addDeniedGuest(guest.getUID());
                Toast.makeText(getActivity(), guest.getFullName() + " isn't coming.", Toast.LENGTH_SHORT).show();
                PartyInterface.updateParty(hostingParty);
            }

            @Override
            public void cardSwipedRight(int position) {
                // Accept
                User guest = pendingGuests.get(position);
                // on card swiped to right we are displaying a toast message.
                hostingParty.addApprovedGuest(guest.getUID());
                Toast.makeText(getActivity(), guest.getFullName() + " is invited!", Toast.LENGTH_SHORT).show();
                PartyInterface.updateParty(hostingParty);
            }

            @Override
            public void cardsDepleted() {
                // this method is called when no card is present
                Toast.makeText(getActivity(), "No more guests to screen", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.hosting_fragment_view, new HostViewFragment()).commit();
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