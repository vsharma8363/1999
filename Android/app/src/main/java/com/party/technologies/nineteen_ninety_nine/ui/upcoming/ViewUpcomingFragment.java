package com.party.technologies.nineteen_ninety_nine.ui.upcoming;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.party.Party;
import com.party.technologies.nineteen_ninety_nine.data.party.PartyInterface;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.ui.party_viewer.ViewParty;

import java.util.ArrayList;

public class ViewUpcomingFragment extends Fragment {

    public ViewUpcomingFragment() {
    }

    public static ViewUpcomingFragment newInstance() {
        return new ViewUpcomingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_upcoming, container, false);
        // Linear Layout to present all party views
        LinearLayout upcomingList = view.findViewById(R.id.upcoming_list);

        ArrayList<Party> acceptedParties = new ArrayList<Party>();
        ArrayList<Party> pendingParties = new ArrayList<Party>();
        for(Party party: PartyInterface.getUpcomingParties(UserInterface.getCurrentUserUID())) {
            if(party.getGuestsPending().contains(UserInterface.getCurrentUserUID()))
                pendingParties.add(party);
            else if(party.getGuestsApproved().contains(UserInterface.getCurrentUserUID()))
                acceptedParties.add(party);
        }

        /**
         * TODO(Jason): This file along with res/layout/fragment_view_upcoming.xml define
         * the layout and function of our upcoming parties page (that list view of parties).
         *
         * Basically, you have to implement a listview that can show each individual party that is upcoming
         * the same way we have it designed on figma.
         *
         * Parties that you have been accepted to are in the acceptedParties ArrayList, and pending
         * requests are in the pendingParties ArrayList (we will not show rejected parties).
         */



        ArrayList<String> allPartyNames = new ArrayList<String>();
        for(Party party: PartyInterface.getUpcomingParties(UserInterface.getCurrentUserUID())) {
            upcomingList.addView(getPartyView(party));
        }
        return view;
    }

    private LinearLayout getPartyView(Party party) {
        // Define overall part element
        LinearLayout partyElement = new LinearLayout(getActivity());
        partyElement.setOrientation(LinearLayout.VERTICAL);
        // Party title
        TextView partyTitle = new TextView(getActivity());
        partyTitle.setTextColor(Color.BLACK);
        partyTitle.setText(party.getPartyName());
        partyTitle.setTextSize(18f);
        // Party description
        TextView partyDescription = new TextView(getActivity());
        partyDescription.setTextColor(Color.BLACK);
        partyDescription.setText(party.getPartyDescription());
        partyDescription.setTextSize(14f);
        // Add data to element
        partyElement.addView(partyTitle);
        partyElement.addView(partyDescription);
        if(party.getGuestsApproved().contains(UserInterface.getCurrentUserUID())) {
            partyElement.setBackgroundColor(Color.GREEN);
            TextView address = new TextView(getActivity());
            address.setTextColor(Color.BLACK);
            address.setText("Address: " + party.getAddress());
            address.setTextSize(18f);
            partyElement.addView(address);
        }
        else if(party.getGuestsPending().contains(UserInterface.getCurrentUserUID()))
            partyElement.setBackgroundColor(Color.GRAY);
        else if(party.getGuestsDenied().contains(UserInterface.getCurrentUserUID()))
            partyElement.setBackgroundColor(Color.RED);
        else
            partyElement.setBackgroundColor(Color.YELLOW);
        partyElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ViewParty.class);
                i.putExtra("partyID", party.getPartyID());
                startActivity(i);
            }
        });
        return partyElement;
    }
}