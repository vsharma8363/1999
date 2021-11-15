package com.party.technologies.nineteen_ninety_nine.ui.hosting;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.party.Party;
import com.party.technologies.nineteen_ninety_nine.data.party.PartyInterface;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HostViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HostViewFragment extends Fragment {

    public HostViewFragment() {
        // Required empty public constructor
    }

    public static HostViewFragment newInstance() {
        HostViewFragment fragment = new HostViewFragment();
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
        View view = inflater.inflate(R.layout.fragment_hosting_already, container, false);
        // Setup fragment manager.
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        TextView name = view.findViewById(R.id.hosting_party_name);
        TextView description = view.findViewById(R.id.hosting_party_description);
        TextView address = view.findViewById(R.id.hosting_party_address);

        view.findViewById(R.id.hosting_edit_party).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.replace(R.id.hosting_fragment_view, new EditPartyFragment()).commit();
            }
        });

        Party hostingParty = PartyInterface.getPartyByHost(UserInterface.getCurrentUserUID());
        if (hostingParty == null) {
            // You are not supposed to be on this page
            fragmentTransaction.replace(R.id.hosting_fragment_view, new ConfirmHostFragment()).commit();
        }
        name.setText(hostingParty.getPartyName());
        description.setText(hostingParty.getPartyDescription());
        address.setText(hostingParty.getAddress());

        return view;
    }
}