package com.party.technologies.nineteen_ninety_nine.ui.hosting;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.party.Party;
import com.party.technologies.nineteen_ninety_nine.data.party.PartyInterface;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.ui.Home;

import java.util.List;

public class EditPartyFragment extends Fragment {

    private final String MISSING_INFO_MSG = "Please enter a valid ";
    private LatLng address_latlng;
    private TextView name;
    private TextView description;
    private TextView address;
    private TextView apartment_unit;

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
        View view = inflater.inflate(R.layout.fragment_edit_party, container, false);

        Party currentParty = PartyInterface.getPartyByHost(UserInterface.getCurrentUserUID());

        view.findViewById(R.id.delete_party).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PartyInterface.deleteParty(currentParty.getPartyID());
                startActivity(new Intent(getContext(), Home.class));
            }
        });
        
        // Define all displays
        name = view.findViewById(R.id.party_name);
        name.setText(currentParty.getPartyName());
        description = view.findViewById(R.id.party_description);
        description.setText(currentParty.getPartyDescription());
        address = view.findViewById(R.id.party_address);
        address.setText(currentParty.getAddress());
        apartment_unit = view.findViewById(R.id.suite_unit);
        apartment_unit.setText(currentParty.getApartment_unit());

        // Set submission logic.
        view.findViewById(R.id.done_editing_party).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address_latlng = getLocationFromAddress(getActivity(), address.getText().toString());
                if(fieldsAreValid()) {
                    // Get latest party data
                    Party updatedParty = PartyInterface.getPartyByHost(UserInterface.getCurrentUserUID());
                    // All information is valid, create a new party object
                    updatedParty.setPartyName(name.getText().toString());
                    updatedParty.setPartyDescription(description.getText().toString());
                    updatedParty.setAddress(address.getText().toString());
                    updatedParty.setApartment_unit(apartment_unit.getText().toString());
                    updatedParty.setLongitude(address_latlng.longitude);
                    updatedParty.setLatitude(address_latlng.latitude);
                    PartyInterface.updateParty(updatedParty);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(
                            R.id.hosting_fragment_view, new HostViewFragment()).commit();
                }
            }
        });
        return view;
    }

    public boolean fieldsAreValid() {
        if(name.getText().length() <= 0)
            Toast.makeText(getActivity(), MISSING_INFO_MSG + "party name.", Toast.LENGTH_SHORT).show();
        else if(description.getText().length() <= 0)
            Toast.makeText(getActivity(), MISSING_INFO_MSG + "party description.", Toast.LENGTH_SHORT).show();
        else if(description.getText().length() <= 0)
            Toast.makeText(getActivity(), MISSING_INFO_MSG + "party name.", Toast.LENGTH_SHORT).show();
        else if(address_latlng == null)
            Toast.makeText(getActivity(), MISSING_INFO_MSG + "address.", Toast.LENGTH_SHORT).show();
        else
            return true;
        return false;
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