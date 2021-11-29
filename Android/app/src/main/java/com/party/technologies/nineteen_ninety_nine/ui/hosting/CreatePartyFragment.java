package com.party.technologies.nineteen_ninety_nine.ui.hosting;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.party.Party;
import com.party.technologies.nineteen_ninety_nine.data.party.PartyInterface;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;

import java.util.ArrayList;
import java.util.List;

public class CreatePartyFragment extends Fragment {

    private final String MISSING_INFO_MSG = "Please enter a valid ";
    private LatLng address_latlng;
    private TextView name;
    private TextView description;
    private TextView address;
    private TextView apartment_unit;
    private LinearLayout partyImageLayout;
    private ImageView addPartyImage;
    private int PICK_IMAGE_MULTIPLE = 1;
    private ArrayList<Uri> partyImages;

    public CreatePartyFragment() {
    }

    public static CreatePartyFragment newInstance() {
        return new CreatePartyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_party, container, false);
        partyImages = new ArrayList<Uri>();
        // Define all displays
        name = view.findViewById(R.id.party_name);
        description = view.findViewById(R.id.party_description);
        address = view.findViewById(R.id.party_address);
        apartment_unit = view.findViewById(R.id.suite_unit);
        partyImageLayout = view.findViewById(R.id.party_images);
        addPartyImage = view.findViewById(R.id.add_party_image);
        // Create image adding logic
        addPartyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });

        // Set submission logic.
        view.findViewById(R.id.done_editing_party).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address_latlng = getLocationFromAddress(getActivity(), address.getText().toString());
                if(fieldsAreValid()) {
                    // Upload all pictures
                    for(Uri uri: partyImages) {
                        PartyInterface.uploadPartyImage(uri);
                    }
                    // All information is valid, create a new party object
                    Party newParty = new Party(UserInterface.getCurrentUserUID(),
                            name.getText().toString(),
                            description.getText().toString(),
                            address.getText().toString(),
                            apartment_unit.getText().toString(),
                            address_latlng.longitude,
                            address_latlng.latitude,
                            PartyInterface.getPartyImages());
                    PartyInterface.publishParty(newParty);
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
        else if(partyImages.size() <= 0)
            Toast.makeText(getActivity(), "Please upload images of the party", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        partyImages = new ArrayList<Uri>();
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_MULTIPLE) {
            if(resultCode == Activity.RESULT_OK) {
                if(data.getClipData() != null) {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for(int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        partyImages.add(imageUri);
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                    }
                }
            } else if(data.getData() != null) {
                Uri imagePath = data.getData();
                partyImages.add(imagePath);
                //do something with the image (save it to some directory or whatever you need to do with it here)
            }
        }
        for(Uri uri:partyImages) {
            ImageView im = new ImageView(getActivity());
            im.setImageURI(uri);
            im.setLayoutParams(new LinearLayout.LayoutParams(addPartyImage.getWidth(), addPartyImage.getHeight()));
            im.setScaleType(ImageView.ScaleType.FIT_CENTER);
            partyImageLayout.addView(im);
        }
        partyImageLayout.setScrollBarSize(10);
    }
}