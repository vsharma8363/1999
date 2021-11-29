package com.party.technologies.nineteen_ninety_nine.ui.hosting;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.party.Party;
import com.party.technologies.nineteen_ninety_nine.data.party.PartyInterface;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.ui.Home;

import java.util.ArrayList;
import java.util.List;

public class CreatePartyFragment extends Fragment {

    private final String MISSING_INFO_MSG = "Please enter a valid ";
    private LatLng address_latlng;
    private TextView name;
    private TextView description;
    private TextView address;
    private TextView apartment_unit;
    private int PICK_IMAGE_MULTIPLE = 1;
    private HorizontalScrollView scrollView;
    private ArrayList<Uri> partyImages;
    private Party currentParty;

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
        currentParty = PartyInterface.getPartyByHost(UserInterface.getCurrentUserUID());
        partyImages = new ArrayList<Uri>();
        // Define all displays
        name = view.findViewById(R.id.party_name);
        description = view.findViewById(R.id.party_description);
        address = view.findViewById(R.id.party_address);
        apartment_unit = view.findViewById(R.id.suite_unit);
        scrollView = view.findViewById(R.id.scroll_view);
        // Create image adding logic

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

                    if(currentParty!= null) {
                        Party updatedParty = PartyInterface.getPartyByHost(UserInterface.getCurrentUserUID());
                        // All information is valid, create a new party object
                        updatedParty.setPartyName(name.getText().toString());
                        updatedParty.setPartyDescription(description.getText().toString());
                        updatedParty.setAddress(address.getText().toString());
                        updatedParty.setApartment_unit(apartment_unit.getText().toString());
                        updatedParty.setLongitude(address_latlng.longitude);
                        updatedParty.setLatitude(address_latlng.latitude);
                        if(partyImages.size() > 0) {
                            PartyInterface.resetPartyImages();
                            for(Uri partyURI:partyImages)
                                PartyInterface.uploadPartyImage(partyURI);
                            updatedParty.setPartyImages(PartyInterface.getPartyImages());
                        }
                        PartyInterface.updateParty(updatedParty);
                    }
                    else {
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
                    }
                    startActivity(new Intent(getContext(), Home.class));
                }
            }
        });

        LinearLayout partyImageLayout = new LinearLayout(getActivity());
        partyImageLayout.setOrientation(LinearLayout.HORIZONTAL);
        if(currentParty != null) {
            for(String imageURL:currentParty.getPartyImages()) {
                ImageView img = new ImageView(getActivity());
                PartyInterface.loadImageToImageView(imageURL, img, getActivity());
                img.setLayoutParams(new LinearLayout.LayoutParams(500, 1000));
                img.setScaleType(ImageView.ScaleType.FIT_CENTER);
                partyImageLayout.addView(img);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
                    }
                });
            }
            name.setText(currentParty.getPartyName());
            description.setText(currentParty.getPartyDescription());
            address.setText(currentParty.getAddress());
            apartment_unit.setText(currentParty.getApartment_unit());
            view.findViewById(R.id.delete_party).setVisibility(View.VISIBLE);
            view.findViewById(R.id.delete_party).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PartyInterface.deleteParty(currentParty.getPartyID());
                    startActivity(new Intent(getContext(), Home.class));
                }
            });
        }
        else {
            view.findViewById(R.id.delete_party).setVisibility(View.INVISIBLE);
            ImageView img = new ImageView(getActivity());
            img.setImageDrawable(getResources().getDrawable(R.drawable.add_image));
            img.setLayoutParams(new LinearLayout.LayoutParams(500, 1000));
            img.setScaleType(ImageView.ScaleType.FIT_CENTER);
            partyImageLayout.addView(img);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
                }
            });
        }
        scrollView.addView(partyImageLayout);


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
        else if(partyImages.size() <= 0 && currentParty == null)
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
        scrollView.removeAllViews();
        LinearLayout partyImageLayout = new LinearLayout(getActivity());
        partyImageLayout.setOrientation(LinearLayout.HORIZONTAL);
        for(Uri uri:partyImages) {
            ImageView img = new ImageView(getActivity());
            img.setImageURI(uri);
            img.setLayoutParams(new LinearLayout.LayoutParams(500, 1000));
            img.setScaleType(ImageView.ScaleType.FIT_CENTER);
            partyImageLayout.addView(img);
        }
        scrollView.addView(partyImageLayout);

    }
}