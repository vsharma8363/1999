package com.party.technologies.nineteen_ninety_nine.ui.profile;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.user.User;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.social.InstagramScreen;
import com.party.technologies.nineteen_ninety_nine.ui.Home;
import com.party.technologies.nineteen_ninety_nine.ui.SetupProfile;
import com.party.technologies.nineteen_ninety_nine.ui.party_viewer.ViewParty;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        User user = UserInterface.getCurrentUser();
        UserInterface.loadImageToImageView(user.getProfilePicture(),
                view.findViewById(R.id.profile_pic_view),
                getActivity());
        TextView fullName = view.findViewById(R.id.full_name);
        fullName.setText(user.getFullName());
        TextView phoneNumber = view.findViewById(R.id.phone_number);
        phoneNumber.setText(user.getPhoneNumber());
        TextView email = view.findViewById(R.id.email);
        email.setText(user.getEmail());
        TextView bio = view.findViewById(R.id.bio);
        bio.setText(user.getBio());
        // Connect to instagram
        Button instagramButton = view.findViewById(R.id.connect_to_instagram);
        instagramButton.setText("@" + UserInterface.getCurrentUser().getInstagramUserName());
        instagramButton.setBackgroundColor(Color.CYAN);
        instagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://instagram.com/_u/" + UserInterface.getCurrentUser().getInstagramUserName());
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    getActivity().startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    getActivity().startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/" + UserInterface.getCurrentUser().getInstagramUserName())));
                }
            }
        });
        return view;
    }
}