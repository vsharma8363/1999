package com.party.technologies.nineteen_ninety_nine.ui.upcoming;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.party.technologies.nineteen_ninety_nine.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewUpcomingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewUpcomingFragment extends Fragment {

    public ViewUpcomingFragment() {
        // Required empty public constructor
    }

    public static ViewUpcomingFragment newInstance() {
        ViewUpcomingFragment fragment = new ViewUpcomingFragment();
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
        View view = inflater.inflate(R.layout.fragment_view_upcoming, container, false);
        return view;
    }
}