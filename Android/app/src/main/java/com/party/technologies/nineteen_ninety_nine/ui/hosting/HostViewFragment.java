package com.party.technologies.nineteen_ninety_nine.ui.hosting;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;

public class HostViewFragment extends Fragment {

    public HostViewFragment() {
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
        View view = inflater.inflate(R.layout.fragment_hosting_already, container, false);
        // Setup fragment manager.
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        // Get current party hosted by user
        Party hostingParty = PartyInterface.getPartyByHost(UserInterface.getCurrentUserUID());
        if (hostingParty == null) {
            // You are not supposed to be on this page, redirect user to confirm new party.
            fragmentTransaction.replace(R.id.hosting_fragment_view, new ConfirmHostFragment()).commit();
        }
        // Enable edit party button functionality
        view.findViewById(R.id.hosting_edit_party).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.replace(R.id.hosting_fragment_view, new CreatePartyFragment()).commit();
            }
        });
        // Populate party information on screen.
        populatePartyInformation(view, hostingParty);
        // Populate party requests screen.
        populateGuestScreeningView(view, hostingParty);
        return view;
    }

    private void populatePartyInformation(View view, Party hostingParty) {
        // Define views and components.
        TextView name = view.findViewById(R.id.hosting_party_name);
        TextView description = view.findViewById(R.id.hosting_party_description);
        TextView address = view.findViewById(R.id.hosting_party_address);
        name.setText(hostingParty.getPartyName());
        description.setText(hostingParty.getPartyDescription());
        address.setText(hostingParty.getAddress());
    }

    private void populateGuestScreeningView(View view, Party hostingParty) {
        ArrayList<User> acceptedGuests = new ArrayList<User>();
        for(String UID:hostingParty.getGuestsApproved())
            acceptedGuests.add(UserInterface.getUser(UID));
        ArrayList<User> pendingGuests = new ArrayList<User>();
        for(String UID:hostingParty.getGuestsPending())
            pendingGuests.add(UserInterface.getUser(UID));
        ArrayList<User> deniedGuests = new ArrayList<User>();
        for(String UID:hostingParty.getGuestsDenied())
            deniedGuests.add(UserInterface.getUser(UID));

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