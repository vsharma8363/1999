package com.party.technologies.nineteen_ninety_nine.ui.fragments.pages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.party.Party;
import com.party.technologies.nineteen_ninety_nine.data.party.PartyInterface;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Hosting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Hosting extends Fragment {

    public Hosting() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment hosting.
     */
    // TODO: Rename and change types and number of parameters
    public static Hosting newInstance(String param1, String param2) {
        Hosting fragment = new Hosting();
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
        return inflater.inflate(R.layout.fragment_hosting, container, false);
    }
}