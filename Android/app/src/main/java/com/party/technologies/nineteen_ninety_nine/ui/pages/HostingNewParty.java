package com.party.technologies.nineteen_ninety_nine.ui.pages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.party.technologies.nineteen_ninety_nine.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HostingNewParty#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HostingNewParty extends Fragment {

    public HostingNewParty() {
        // Required empty public constructor
    }

    public static HostingNewParty newInstance() {
        HostingNewParty fragment = new HostingNewParty();
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
        View view = inflater.inflate(R.layout.fragment_new_host, container, false);
        // Add functionality for buttons.
        Button yes1 = (Button) view.findViewById(R.id.yes_host_1);
        Button yes2 = (Button) view.findViewById(R.id.yes_host_2);
        // Add fragment functionality
        Fragment fragment = new CreateParty();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        yes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.replace(R.id.hosting_fragment_view, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        yes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.replace(R.id.hosting_fragment_view, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        // Return the view page
        return view;
    }
}