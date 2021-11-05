package com.party.technologies.nineteen_ninety_nine.ui.fragments.pages;

import android.content.Intent;
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
import com.party.technologies.nineteen_ninety_nine.ui.LoginSignup;
import com.party.technologies.nineteen_ninety_nine.ui.fragments.Home;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Settings extends Fragment {

    public Settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Settings.
     */
    // TODO: Rename and change types and number of parameters
    public static Settings newInstance() {
        Settings fragment = new Settings();
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        setupView(view);
        return view;
    }

    private void setupView(View view) {
        // Show user data.
        User user = UserInterface.getCurrentUser();
        TextView fullName = view.findViewById(R.id.full_name);
        fullName.setText(user.getFullName() + "\n");
        TextView phoneNumber = view.findViewById(R.id.phone_number);
        phoneNumber.setText(user.getPhoneNumber() + "\n");
        TextView email = view.findViewById(R.id.email);
        email.setText(user.getEmail() + "\n");
        TextView bio = view.findViewById(R.id.bio);
        bio.setText(user.getBio() + "\n");
        // Enable logout functionality.
        Button logoutBtn = (Button)view.findViewById(R.id.logout_button);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserInterface.logoutCurrentUser();
                startActivity(new Intent(getActivity(), LoginSignup.class));
            }
        });
    }
}