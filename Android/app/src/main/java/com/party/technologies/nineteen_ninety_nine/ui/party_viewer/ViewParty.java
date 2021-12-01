package com.party.technologies.nineteen_ninety_nine.ui.party_viewer;

import static android.content.ContentValues.TAG;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.party.Party;
import com.party.technologies.nineteen_ninety_nine.data.party.PartyInterface;
import com.party.technologies.nineteen_ninety_nine.data.user.User;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.ui.hosting.HostingActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ViewParty extends AppCompatActivity implements OnMapReadyCallback {

    private String partyID;
    private Party party;
    private TextView partyAddress;
    private TextView partyUnit;
    private LinearLayout guest_list_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_party);
        // Get partyID from previous intent.
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            partyID = extras.getString("partyID");
        if(partyID == null || PartyInterface.getPartyByID(partyID) == null) {
            // There was some kind of error, return to main screen.
            Toast.makeText(getApplicationContext(), "Something broke :o", Toast.LENGTH_SHORT).show();
            finish();
        }
        LinearLayout addressData = findViewById(R.id.activity_view_party_address_layout);
        guest_list_layout = findViewById(R.id.activity_view_party_guest_list);
        addressData.setVisibility(INVISIBLE);
        party = PartyInterface.getPartyByID(partyID);
        populatePartyPictures();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_view_party_map);
        // Textviews
        TextView name = findViewById(R.id.activity_view_party_name);
        name.setText(party.getPartyName());
        TextView description = findViewById(R.id.activity_view_party_description);
        description.setText(party.getPartyDescription());
        TextView dateTime = findViewById(R.id.activity_view_party_date_time);
        dateTime.setText(toHumanReadableDate(party.getStartTime())
                + " - " + toHumanReadableDate(party.getEndTime()));
        dateTime.setTextColor(Color.BLACK);
        partyAddress = ((TextView)findViewById(R.id.activity_view_party_address));
        partyAddress.setText(party.getAddress());
        partyAddress.setTextColor(Color.BLUE);
        partyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", party.getLatitude(), party.getLongitude());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getApplicationContext().startActivity(intent);
            }
        });
        partyUnit = ((TextView)findViewById(R.id.activity_view_party_unit));
        partyUnit.setText("Unit/Apt: " + party.getApartment_unit());
        // Apply for attendance
        Button applyForAttendance = findViewById(R.id.activity_view_party_request_btn);
        Button screenGuests = findViewById(R.id.activity_view_party_screen_guests_btn);
        screenGuests.setVisibility(INVISIBLE);
        if (party.getGuestsPending().contains(UserInterface.getCurrentUserUID())) {
            // Guest is waiting approval
            applyForAttendance.setClickable(false);
            applyForAttendance.setBackgroundColor(Color.GRAY);
            applyForAttendance.setText("Waiting for Approval");
        }
        else if (party.getGuestsApproved().contains(UserInterface.getCurrentUserUID())) {
            // Guest has been approved to party
            applyForAttendance.setClickable(false);
            applyForAttendance.setBackgroundColor(Color.parseColor("#90ee90"));
            applyForAttendance.setText("You got plans!");
            // Show address
            addressData.setVisibility(VISIBLE);
        }
        else if (party.getGuestsDenied().contains(UserInterface.getCurrentUserUID())) {
            applyForAttendance.setClickable(false);
            applyForAttendance.setBackgroundColor(Color.GRAY);
            applyForAttendance.setText("Party Unavailable");
        }
        else if (party.getHostID().equals(UserInterface.getCurrentUserUID())) {
            applyForAttendance.setText("Edit Party ✏");
            applyForAttendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), CreateParty.class));
                }
            });
            screenGuests.setVisibility(VISIBLE);
            if (party.getGuestsPending().size() > 0) {
                screenGuests.setText("Screen Guests (" + party.getGuestsPending().size() + ") waiting");
                screenGuests.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        startActivity(new Intent(getApplicationContext(), ScreenGuests.class));
                    }
                });
            }
            else {
                screenGuests.setClickable(false);
                screenGuests.setText("No more invite requests!");
            }

        }
        else {
            applyForAttendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    party.addPendingGuest(UserInterface.getCurrentUserUID());
                    PartyInterface.updateParty(party);
                    Toast.makeText(getApplicationContext(), "Invitation request sent... sit back and wait", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        // Finish activity
        ImageButton done = findViewById(R.id.activity_view_party_done_btn);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mapFragment.getMapAsync(ViewParty.this);
        populateGuestList(party);
    }

    public void populatePartyPictures() {
        LinearLayout partyImageLayout = new LinearLayout(getApplicationContext());
        partyImageLayout.setOrientation(LinearLayout.HORIZONTAL);
        for(String imageURL: party.getPartyImages()) {
            ImageView img = new ImageView(getApplicationContext());
            PartyInterface.loadImageToImageView(imageURL, img, getApplicationContext());
            img.setLayoutParams(new LinearLayout.LayoutParams(750, 1500));
            img.setScaleType(ImageView.ScaleType.FIT_CENTER);
            partyImageLayout.addView(img);
        }
        ((HorizontalScrollView)findViewById(R.id.activity_view_party_images_scrollview)).addView(partyImageLayout);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        LatLng partyLocation = new LatLng(party.getLatitude(), party.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(partyLocation);
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(partyLocation, 17));
    }

    public static String toHumanReadableDate(long time) {
        DateFormat dateFormat = new SimpleDateFormat("E MMM dd hh:mm a");
        return dateFormat.format(new Date(time));
    }

    public void populateGuestList(Party hostingParty) {
        guest_list_layout.removeAllViews();
        // Add all guests to the list:
        ArrayList<User> acceptedGuests = new ArrayList<User>();
        for(String UID:hostingParty.getGuestsApproved())
            acceptedGuests.add(UserInterface.getUser(UID));

        for(User guest:acceptedGuests) {
            TextView guestName = new TextView(getApplicationContext());
            //name.setWidth(guest_list_layout.getWidth());
            guestName.setText(guest.getFullName());

            TextView instagramID = new TextView(getApplicationContext());
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
                        getApplicationContext().startActivity(likeIng);
                    } catch (ActivityNotFoundException e) {
                        getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://instagram.com/" + guest.getInstagramUserName())));
                    }
                }
            });
            ImageView profilePicture = new ImageView(getApplicationContext());
            UserInterface.loadImageToImageView(guest.getProfilePicture(), profilePicture, getApplicationContext());
            profilePicture.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
            LinearLayout overallBar = new LinearLayout(getApplicationContext());
            overallBar.setOrientation(LinearLayout.HORIZONTAL);
            overallBar.addView(profilePicture);
            Space space = new Space(getApplicationContext());
            space.setLayoutParams(new LinearLayout.LayoutParams(50, 20));
            overallBar.addView(space);
            LinearLayout dataBar = new LinearLayout(getApplicationContext());
            dataBar.setOrientation(LinearLayout.VERTICAL);
            dataBar.addView(guestName);
            if (party.getGuestsApproved().contains(UserInterface.getCurrentUserUID()) ||
                party.getHostID().equals(UserInterface.getCurrentUserUID())) {
                TextView phoneNumber = new TextView(getApplicationContext());
                phoneNumber.setText(guest.getPhoneNumber());
                dataBar.addView(phoneNumber);
            }
            dataBar.addView(instagramID);
            overallBar.addView(dataBar);
            overallBar.setBackgroundColor(Color.parseColor("#90ee90"));
            guest_list_layout.addView(overallBar);
        }
    }
}