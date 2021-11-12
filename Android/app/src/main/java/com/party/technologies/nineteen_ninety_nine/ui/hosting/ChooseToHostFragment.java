package com.party.technologies.nineteen_ninety_nine.ui.hosting;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.party.technologies.nineteen_ninety_nine.R;

public class ChooseToHostFragment extends Fragment {

    public ChooseToHostFragment() {
    }

    public static ChooseToHostFragment newInstance() {
        return new ChooseToHostFragment();
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
        // Both buttons 'yes' and 'yes' should direct user to party creator page.
        view.findViewById(R.id.yes_host_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPartyCreatorFragment();
            }
        });
        view.findViewById(R.id.yes_host_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPartyCreatorFragment();
            }
        });
        // Return the view page
        return view;
    }

    private void launchPartyCreatorFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(
                R.id.hosting_fragment_view, new EditPartyFragment()).commit();
    }
}