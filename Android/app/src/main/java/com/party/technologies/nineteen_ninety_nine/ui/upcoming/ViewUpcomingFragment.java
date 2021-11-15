package com.party.technologies.nineteen_ninety_nine.ui.upcoming;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Space;
import android.widget.TextView;

import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.party.Party;
import com.party.technologies.nineteen_ninety_nine.data.party.PartyInterface;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.ui.Home;
import com.party.technologies.nineteen_ninety_nine.ui.ViewParty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewUpcomingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewUpcomingFragment extends Fragment {

    public ViewUpcomingFragment() {
        // Required empty public constructor
    }

    public static ViewUpcomingFragment newInstance() {
        ViewUpcomingFragment fragment = new ViewUpcomingFragment();
        return fragment;
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
        LinearLayout upcomingView = view.findViewById(R.id.upcoming_list);
        String currentUID = UserInterface.getCurrentUserUID();
        for(Party party: PartyInterface.getUpcomingParties(currentUID)) {
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
            if(party.getGuestsApproved().contains(currentUID)) {
                partyElement.setBackgroundColor(Color.GREEN);
                TextView address = new TextView(getActivity());
                address.setTextColor(Color.BLACK);
                address.setText("Address: " + party.getAddress());
                address.setTextSize(18f);
                partyElement.addView(address);
            }
            else if(party.getGuestsPending().contains(currentUID))
                partyElement.setBackgroundColor(Color.GRAY);
            else if(party.getGuestsDenied().contains(currentUID))
                partyElement.setBackgroundColor(Color.RED);
            partyElement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), ViewParty.class);
                    i.putExtra("partyID", party.getPartyID());
                    startActivity(i);
                }
            });
            upcomingView.addView(partyElement);
        }
        return view;
    }
}