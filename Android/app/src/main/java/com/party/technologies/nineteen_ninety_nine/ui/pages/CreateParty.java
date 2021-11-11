package com.party.technologies.nineteen_ninety_nine.ui.pages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.party.technologies.nineteen_ninety_nine.R;

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
        return inflater.inflate(R.layout.fragment_create_edit_party, container, false);
    }
}