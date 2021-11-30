package com.party.technologies.nineteen_ninety_nine.ui.hosting;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.daprlabs.cardstack.SwipeDeck;
import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.party.Party;
import com.party.technologies.nineteen_ninety_nine.data.party.PartyInterface;
import com.party.technologies.nineteen_ninety_nine.data.user.User;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.ui.misc.ImageLoadTask;
import com.party.technologies.nineteen_ninety_nine.ui.misc.UserAdapter;
import com.party.technologies.nineteen_ninety_nine.ui.misc.UserProfile;

import java.util.ArrayList;

public class HostViewFragment extends Fragment {

    private Button screen_guests_btn;
    private LinearLayout guest_list_layout;

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
        screen_guests_btn = view.findViewById(R.id.screen_guests_btn);
        screen_guests_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hostingParty.getGuestsPending().size() > 0)
                    fragmentTransaction.replace(R.id.hosting_fragment_view, new PendingGuestsFragment()).commit();
                else
                    Toast.makeText(getActivity(), "No guests are requesting an invite :(", Toast.LENGTH_SHORT).show();
            }
        });
        guest_list_layout = view.findViewById(R.id.guest_list_layout);
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
        return view;
    }

    private void populatePartyInformation(View view, Party hostingParty) {
        LinearLayout partyImageLayout = new LinearLayout(getActivity());
        partyImageLayout.setOrientation(LinearLayout.HORIZONTAL);
        for(String imageURL: hostingParty.getPartyImages()) {
            ImageView img = new ImageView(getActivity());
            PartyInterface.loadImageToImageView(imageURL, img, getActivity());
            img.setLayoutParams(new LinearLayout.LayoutParams(500, 1000));
            img.setScaleType(ImageView.ScaleType.FIT_CENTER);
            partyImageLayout.addView(img);
        }
        ((HorizontalScrollView)view.findViewById(R.id.party_image_scrollable)).addView(partyImageLayout);
        // Define views and components.
        TextView name = view.findViewById(R.id.hosting_party_name);
        TextView description = view.findViewById(R.id.hosting_party_description);
        TextView address = view.findViewById(R.id.hosting_party_address);
        name.setText(hostingParty.getPartyName());
        description.setText(hostingParty.getPartyDescription());
        address.setText(hostingParty.getAddress());
        populateGuestList(hostingParty);
    }

    public void populateGuestList(Party hostingParty) {
        guest_list_layout.removeAllViews();
        screen_guests_btn.setText("Screen Guests ("  + hostingParty.getGuestsPending().size() +" waiting)");
        // Add all guests to the list:
        ArrayList<User> acceptedGuests = new ArrayList<User>();
        for(String UID:hostingParty.getGuestsApproved())
            acceptedGuests.add(UserInterface.getUser(UID));
        ArrayList<User> deniedGuests = new ArrayList<User>();
        for(String UID:hostingParty.getGuestsDenied())
            deniedGuests.add(UserInterface.getUser(UID));

        for(User guest:acceptedGuests) {
            TextView guestName = new TextView(getActivity());
            //name.setWidth(guest_list_layout.getWidth());
            guestName.setText(guest.getFullName());

            TextView instagramID = new TextView(getActivity());
            //instagramID.setWidth(guest_list_layout.getWidth());
            instagramID.setText("@" + guest.getInstagramUserName());
            instagramID.setTextColor(Color.BLUE);
            instagramID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("http://instagram.com/_u/" + guest.getInstagramUserName());
                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                    likeIng.setPackage("com.instagram.android");

                    try {
                        getActivity().startActivity(likeIng);
                    } catch (ActivityNotFoundException e) {
                        getActivity().startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://instagram.com/" + guest.getInstagramUserName())));
                    }
                }
            });

            TextView phoneNumber = new TextView(getActivity());
            //phoneNumber.setWidth(guest_list_layout.getWidth());
            phoneNumber.setText(guest.getPhoneNumber());
            ImageView profilePicture = new ImageView(getActivity());
            UserInterface.loadImageToImageView(guest.getProfilePicture(), profilePicture, getActivity());
            profilePicture.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
            LinearLayout overallBar = new LinearLayout(getActivity());
            overallBar.setOrientation(LinearLayout.HORIZONTAL);
            overallBar.addView(profilePicture);
            Space space = new Space(getActivity());
            space.setLayoutParams(new LinearLayout.LayoutParams(50, 20));
            overallBar.addView(space);
            LinearLayout dataBar = new LinearLayout(getActivity());
            dataBar.setOrientation(LinearLayout.VERTICAL);
            //dataBar.setMinimumWidth(guest_list_layout.getWidth());
            dataBar.addView(guestName);
            dataBar.addView(phoneNumber);
            dataBar.addView(instagramID);
            overallBar.addView(dataBar);
            Button redo_btn = new Button(getActivity());
            redo_btn.setText("Redo");
            redo_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Let's give them another shot...", Toast.LENGTH_SHORT).show();
                    hostingParty.addPendingGuest(guest.getUID());
                    PartyInterface.updateParty(hostingParty);
                    populateGuestList(hostingParty);
                }
            });
            overallBar.addView(redo_btn);
            overallBar.setBackgroundColor(Color.parseColor("#90ee90"));
            guest_list_layout.addView(overallBar);
        }
        for(User guest:deniedGuests) {
            TextView guestName = new TextView(getActivity());
            //name.setWidth(guest_list_layout.getWidth());
            guestName.setText(guest.getFullName());

            TextView instagramID = new TextView(getActivity());
            //instagramID.setWidth(guest_list_layout.getWidth());
            instagramID.setText("@" + guest.getInstagramUserName());
            instagramID.setTextColor(Color.BLUE);
            instagramID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("http://instagram.com/_u/" + guest.getInstagramUserName());
                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                    likeIng.setPackage("com.instagram.android");

                    try {
                        getActivity().startActivity(likeIng);
                    } catch (ActivityNotFoundException e) {
                        getActivity().startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://instagram.com/" + guest.getInstagramUserName())));
                    }
                }
            });

            TextView phoneNumber = new TextView(getActivity());
            //phoneNumber.setWidth(guest_list_layout.getWidth());
            phoneNumber.setText(guest.getPhoneNumber());
            ImageView profilePicture = new ImageView(getActivity());
            UserInterface.loadImageToImageView(guest.getProfilePicture(), profilePicture, getActivity());
            profilePicture.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
            LinearLayout overallBar = new LinearLayout(getActivity());
            overallBar.setOrientation(LinearLayout.HORIZONTAL);
            overallBar.addView(profilePicture);
            Space space = new Space(getActivity());
            space.setLayoutParams(new LinearLayout.LayoutParams(50, 20));
            overallBar.addView(space);
            LinearLayout dataBar = new LinearLayout(getActivity());
            dataBar.setOrientation(LinearLayout.VERTICAL);
            //dataBar.setMinimumWidth(guest_list_layout.getWidth());
            dataBar.addView(guestName);
            dataBar.addView(phoneNumber);
            dataBar.addView(instagramID);
            overallBar.addView(dataBar);
            Button redo_btn = new Button(getActivity());
            redo_btn.setText("Redo");
            redo_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Let's give them another shot...", Toast.LENGTH_SHORT).show();
                    hostingParty.addPendingGuest(guest.getUID());
                    PartyInterface.updateParty(hostingParty);
                    populateGuestList(hostingParty);
                }
            });
            overallBar.addView(redo_btn);
            overallBar.setBackgroundColor(Color.parseColor("#FF7276"));
            guest_list_layout.addView(overallBar);
        }
    }

}