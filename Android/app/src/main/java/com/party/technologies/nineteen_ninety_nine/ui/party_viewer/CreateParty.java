package com.party.technologies.nineteen_ninety_nine.ui.party_viewer;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;
import com.party.technologies.nineteen_ninety_nine.ui.Home;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateParty extends AppCompatActivity implements OnMapReadyCallback {

    private Party party;
    private EditText name;
    private EditText description;
    private EditText address;
    private EditText unit;
    private Button startTimeBtn;
    private Button endTimeBtn;
    private Calendar startTime;
    private Calendar endTime;
    private GoogleMap map;
    private final String MISSING_INFO_MSG = "Please enter a valid ";
    private HorizontalScrollView scrollView;
    private ArrayList<Uri> partyImages;
    private int PICK_IMAGE_MULTIPLE = 1;
    private LatLng address_latlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);
        startTime = Calendar.getInstance();
        endTime = Calendar.getInstance();
        partyImages = new ArrayList<Uri>();
        // Finish activity
        ImageButton done = findViewById(R.id.activity_create_party_done_btn);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        party = PartyInterface.getPartyByHost(UserInterface.getCurrentUserUID());
        name = findViewById(R.id.activity_create_party_name);
        description = findViewById(R.id.activity_create_party_description);
        address = findViewById(R.id.activity_create_party_address);
        address.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (map != null) {
                        address_latlng = getLocationFromAddress(getApplicationContext(), address.getText().toString());
                        // Party is being edited
                        map.clear();
                        MarkerOptions markerOptions = new MarkerOptions().position(address_latlng);
                        map.addMarker(markerOptions);
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(address_latlng, 17));
                    }
                }
            }
        });
        unit = findViewById(R.id.activity_create_party_unit);
        startTimeBtn = findViewById(R.id.activity_create_party_date_time_start);
        startTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseStartDate();
            }
        });
        endTimeBtn = findViewById(R.id.activity_create_party_date_time_end);
        endTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseEndDate();
            }
        });
        scrollView = findViewById(R.id.activity_create_party_images_scrollview);
        populateFields();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_create_party_map);
        mapFragment.getMapAsync(CreateParty.this);

        findViewById(R.id.activity_create_party_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address_latlng = getLocationFromAddress(getApplicationContext(), address.getText().toString());
                if (fieldsAreValid()) {
                    // Upload all pictures
                    for (Uri uri : partyImages) {
                        PartyInterface.uploadPartyImage(uri);
                    }

                    if (party != null) {
                        Party updatedParty = PartyInterface.getPartyByHost(UserInterface.getCurrentUserUID());
                        // All information is valid, create a new party object
                        updatedParty.setPartyName(name.getText().toString());
                        updatedParty.setPartyDescription(description.getText().toString());
                        updatedParty.setAddress(address.getText().toString());
                        updatedParty.setApartment_unit(unit.getText().toString());
                        updatedParty.setLongitude(address_latlng.longitude);
                        updatedParty.setLatitude(address_latlng.latitude);
                        updatedParty.setStartTime(startTime.getTimeInMillis());
                        updatedParty.setEndTime(endTime.getTimeInMillis());
                        if (partyImages.size() > 0) {
                            PartyInterface.resetPartyImages();
                            for (Uri partyURI : partyImages)
                                PartyInterface.uploadPartyImage(partyURI);
                            updatedParty.setPartyImages(PartyInterface.getPartyImages());
                        }
                        PartyInterface.updateParty(updatedParty);
                    } else {
                        // All information is valid, create a new party object
                        Party newParty = new Party(UserInterface.getCurrentUserUID(),
                                name.getText().toString(),
                                description.getText().toString(),
                                address.getText().toString(),
                                unit.getText().toString(),
                                address_latlng.longitude,
                                address_latlng.latitude,
                                PartyInterface.getPartyImages(),
                                startTime.getTimeInMillis(),
                                endTime.getTimeInMillis());
                        PartyInterface.publishParty(newParty);
                    }
                    finish();
                }
            }
        });
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
        if (party != null) {
            // Party is being edited
            LatLng partyLocation = new LatLng(party.getLatitude(), party.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(partyLocation);
            googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(partyLocation, 17));
        }
        map = googleMap;
    }

    public boolean fieldsAreValid() {
        if(name.getText().length() <= 0)
            Toast.makeText(getApplicationContext(), MISSING_INFO_MSG + "party name.", Toast.LENGTH_SHORT).show();
        else if(description.getText().length() <= 0)
            Toast.makeText(getApplicationContext(), MISSING_INFO_MSG + "party description.", Toast.LENGTH_SHORT).show();
        else if(description.getText().length() <= 0)
            Toast.makeText(getApplicationContext(), MISSING_INFO_MSG + "party name.", Toast.LENGTH_SHORT).show();
        else if(address_latlng == null)
            Toast.makeText(getApplicationContext(), MISSING_INFO_MSG + "address.", Toast.LENGTH_SHORT).show();
        else if(partyImages.size() <= 0 && party == null)
            Toast.makeText(getApplicationContext(), "Please upload images of the party", Toast.LENGTH_SHORT).show();
        else
            return true;
        return false;
    }


    private void populateFields() {
        LinearLayout partyImageLayout = new LinearLayout(getApplicationContext());
        partyImageLayout.setOrientation(LinearLayout.HORIZONTAL);
        if(party != null) {
            for(String imageURL:party.getPartyImages()) {
                ImageView img = new ImageView(getApplicationContext());
                PartyInterface.loadImageToImageView(imageURL, img, getApplicationContext());
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
            name.setText(party.getPartyName());
            description.setText(party.getPartyDescription());
            address.setText(party.getAddress());
            startTimeBtn.setText(ViewParty.toHumanReadableDate(party.getStartTime()));
            endTimeBtn.setText(ViewParty.toHumanReadableDate(party.getEndTime()));
            startTime.setTimeInMillis(party.getStartTime());
            endTime.setTimeInMillis(party.getStartTime());

            unit.setText(party.getApartment_unit());
            findViewById(R.id.activity_create_delete_party_btn).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.activity_create_delete_party_btn)).setTextColor(Color.RED);
            findViewById(R.id.activity_create_delete_party_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PartyInterface.deleteParty(party.getPartyID());
                    startActivity(new Intent(getApplicationContext(), Home.class));
                }
            });
        }
        else {
            findViewById(R.id.activity_create_delete_party_btn).setVisibility(View.INVISIBLE);
            ImageView img = new ImageView(getApplicationContext());
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
        LinearLayout partyImageLayout = new LinearLayout(getApplicationContext());
        partyImageLayout.setOrientation(LinearLayout.HORIZONTAL);
        for(Uri uri:partyImages) {
            ImageView img = new ImageView(getApplicationContext());
            img.setImageURI(uri);
            img.setLayoutParams(new LinearLayout.LayoutParams(500, 1000));
            img.setScaleType(ImageView.ScaleType.FIT_CENTER);
            partyImageLayout.addView(img);
        }
        scrollView.addView(partyImageLayout);

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

    public void chooseStartDate() {
        final Calendar currentDate = Calendar.getInstance();
        startTime = Calendar.getInstance();
        new DatePickerDialog(CreateParty.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                startTime.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(CreateParty.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        startTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        startTime.set(Calendar.MINUTE, minute);
                        Log.v(TAG, "The choosen one " + startTime.getTime());
                        startTimeBtn.setText(ViewParty.toHumanReadableDate(startTime.getTimeInMillis()));
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    public void chooseEndDate() {
        final Calendar currentDate = Calendar.getInstance();
        endTime = Calendar.getInstance();
        new DatePickerDialog(CreateParty.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                endTime.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(CreateParty.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        endTime.set(Calendar.MINUTE, minute);
                        Log.v(TAG, "The choosen one " + endTime.getTime());
                        endTimeBtn.setText(ViewParty.toHumanReadableDate(endTime.getTimeInMillis()));
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
}