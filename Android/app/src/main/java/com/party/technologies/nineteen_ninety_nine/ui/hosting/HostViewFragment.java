package com.party.technologies.nineteen_ninety_nine.ui.hosting;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.party.Party;
import com.party.technologies.nineteen_ninety_nine.data.party.PartyInterface;
import com.party.technologies.nineteen_ninety_nine.data.user.User;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;

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
                fragmentTransaction.replace(R.id.hosting_fragment_view, new EditPartyFragment()).commit();
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

        LinearLayout requested = view.findViewById(R.id.requested);
        requested.removeAllViews();
        for (User pending:pendingGuests) {
            Button approve = new Button(getActivity());
            approve.setText("Approve");
            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hostingParty.addApprovedGuest(pending.getUID());
                    populateGuestScreeningView(view, hostingParty);
                    PartyInterface.updateParty(hostingParty);
                }
            });
            Button deny = new Button(getActivity());
            deny.setText("Deny");
            deny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hostingParty.addDeniedGuest(pending.getUID());
                    populateGuestScreeningView(view, hostingParty);
                    PartyInterface.updateParty(hostingParty);
                }
            });
            TextView userName = new TextView(getActivity());
            userName.setText(pending.getFullName());
            LinearLayout subview = new LinearLayout(getActivity());
            subview.setOrientation(LinearLayout.HORIZONTAL);
            subview.addView(userName);
            subview.addView(deny);
            subview.addView(approve);
            subview.setBackgroundColor(Color.GRAY);
            requested.addView(subview);
        }
        for (User accepted:acceptedGuests) {
            Button deny = new Button(getActivity());
            deny.setText("Deny");
            deny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hostingParty.addDeniedGuest(accepted.getUID());
                    populateGuestScreeningView(view, hostingParty);
                    PartyInterface.updateParty(hostingParty);
                }
            });
            TextView userName = new TextView(getActivity());
            userName.setText(accepted.getFullName());
            LinearLayout subview = new LinearLayout(getActivity());
            subview.setOrientation(LinearLayout.HORIZONTAL);
            subview.addView(userName);
            subview.addView(deny);
            subview.setBackgroundColor(Color.GREEN);
            requested.addView(subview);
        }
        for (User denied:deniedGuests) {
            Button approve = new Button(getActivity());
            approve.setText("Approve");
            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hostingParty.addApprovedGuest(denied.getUID());
                    populateGuestScreeningView(view, hostingParty);
                    PartyInterface.updateParty(hostingParty);
                }
            });
            TextView userName = new TextView(getActivity());
            userName.setText(denied.getFullName());
            LinearLayout subview = new LinearLayout(getActivity());
            subview.setOrientation(LinearLayout.HORIZONTAL);
            subview.addView(userName);
            subview.addView(approve);
            subview.setBackgroundColor(Color.RED);
            requested.addView(subview);
        }
    }


}