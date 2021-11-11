package com.party.technologies.nineteen_ninety_nine.ui.pages;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.party.technologies.nineteen_ninety_nine.R;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateParty#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateParty extends Fragment {

    public CreateParty() {
        // Required empty public constructor
    }

    public static CreateParty newInstance() {
        CreateParty fragment = new CreateParty();
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
        View view = inflater.inflate(R.layout.fragment_create_edit_party, container, false);
        //get the spinner from the xml.
        Spinner dropdown = view.findViewById(R.id.party_type);
        //create a list of items for the spinner.
        String[] items = new String[]{"Kickback", "House Party", "Video Games", "TBD..."};
        return view;
    }
}