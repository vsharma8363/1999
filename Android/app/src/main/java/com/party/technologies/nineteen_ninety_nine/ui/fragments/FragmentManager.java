package com.party.technologies.nineteen_ninety_nine.ui.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.party.technologies.nineteen_ninety_nine.ui.fragments.pages.Hosting;
import com.party.technologies.nineteen_ninety_nine.ui.fragments.pages.Map;
import com.party.technologies.nineteen_ninety_nine.ui.fragments.pages.Settings;
import com.party.technologies.nineteen_ninety_nine.ui.fragments.pages.Upcoming;

public class FragmentManager extends FragmentStateAdapter {

    private String[] tabNames = new String[]{"Map", "Hosting", "Upcoming", "Settings"};

    public FragmentManager(@NonNull FragmentActivity fragActivity)
    {
        super(fragActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        // Make sure tabNames is mapped accordingly to this
        switch (position) {
            case 0:
                return new Map();
            case 1:
                return new Hosting();
            case 2:
                return new Upcoming();
            default:
                return new Settings();
        }
    }

    public String[] getTabNames() {
        return tabNames;
    }

    @Override
    public int getItemCount() {return 4; }
}