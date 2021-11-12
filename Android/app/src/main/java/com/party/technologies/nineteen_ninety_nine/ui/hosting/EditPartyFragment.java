package com.party.technologies.nineteen_ninety_nine.ui.hosting;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.party.Party;
import com.party.technologies.nineteen_ninety_nine.data.party.PartyInterface;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.ui.Home;

import java.util.List;

public class EditPartyFragment extends Fragment {

    private final String MISSING_INFO_MSG = "Please enter a valid ";

    public EditPartyFragment() {
    }

    public static EditPartyFragment newInstance() {
        return new EditPartyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_edit_party, container, false);

        // Define all displays
        TextView name = view.findViewById(R.id.party_name);
        TextView description = view.findViewById(R.id.party_description);
        TextView address = view.findViewById(R.id.party_address);
        TextView apartment_unit = view.findViewById(R.id.suite_unit);


        // Check if user is creating a party or editing new one
        Party currentParty = PartyInterface.getPartyByHost(UserInterface.getCurrentUserUID());
        if (currentParty != null) {
            // Populate display with current party data.
            name.setText(currentParty.getPartyName());
            description.setText(currentParty.getPartyDescription());
            address.setText(currentParty.getAddress());
            apartment_unit.setText(currentParty.getApartment_unit());

            // Give option to delete current party
            view.findViewById(R.id.delete_party).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PartyInterface.deleteParty(currentParty.getPartyID());
                    startActivity(new Intent(getActivity(), Home.class));
                }
            });

        }
        else
            // Set delete button to invisible
            view.findViewById(R.id.delete_party).setVisibility(View.INVISIBLE);

        // Set submission logic.
        view.findViewById(R.id.done_editing_party).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng address_latlng = getLocationFromAddress(getActivity(), address.getText().toString());
                if(name.getText().length() <= 0){
                    Toast.makeText(getActivity(), MISSING_INFO_MSG + "party name.", Toast.LENGTH_SHORT).show();
                }
                else if(description.getText().length() <= 0){
                    Toast.makeText(getActivity(), MISSING_INFO_MSG + "party description.", Toast.LENGTH_SHORT).show();
                }
                else if(description.getText().length() <= 0){
                    Toast.makeText(getActivity(), MISSING_INFO_MSG + "party name.", Toast.LENGTH_SHORT).show();
                }
                else if(address_latlng == null) {
                    Toast.makeText(getActivity(), MISSING_INFO_MSG + "address.", Toast.LENGTH_SHORT).show();
                }
                else {
                    // All information is valid, create a new party object
                    Party newParty = new Party(UserInterface.getCurrentUserUID(),
                            name.getText().toString(),
                            description.getText().toString(),
                            address.getText().toString(),
                            apartment_unit.getText().toString(),
                            address_latlng.longitude,
                            address_latlng.latitude);

                    if (currentParty != null) {
                        // if user is editing a party, overwrite the existing party
                        PartyInterface.overwriteParty(currentParty.getPartyID(), newParty);
                    }
                    else {
                        // if this is a new party, ask the user to confirm with a popup dialog
                        PartyInterface.publishParty(newParty);
                    }
                    getActivity().getSupportFragmentManager().beginTransaction().replace(
                            R.id.hosting_fragment_view, new HostViewFragment()).commit();
                }
            }
        });
        return view;
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng address_latlng = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            address_latlng = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {
            address_latlng = null;
        }

        return address_latlng;
    }

}