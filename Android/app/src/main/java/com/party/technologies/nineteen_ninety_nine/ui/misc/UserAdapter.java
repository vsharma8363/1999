package com.party.technologies.nineteen_ninety_nine.ui.misc;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;
import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {

    // on below line we have created variables
    // for our array list and context.
    private ArrayList<UserProfile> profiles;
    private Context context;

    // on below line we have created constructor for our variables.
    public UserAdapter(ArrayList<UserProfile> profile, Context context) {
        this.profiles = profile;
        this.context = context;
    }

    @Override
    public int getCount() {
        // in get count method we are returning the size of our array list.
        return profiles.size();
    }

    @Override
    public Object getItem(int position) {
        // in get item method we are returning the item from our array list.
        return profiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        // in get item id we are returning the position.
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // in get view method we are inflating our layout on below line.
        View v = convertView;
        if (v == null) {
            // on below line we are inflating our layout.
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profile_card, parent, false);
        }
        // on below line we are initializing our variables and setting data to our variables.
        UserProfile up = profiles.get(position);
        ((TextView) v.findViewById(R.id.fullName)).setText(up.getFullName());
        ((TextView) v.findViewById(R.id.bio)).setText(up.getBio());
        ((TextView) v.findViewById(R.id.age)).setText(up.getAge());
        ((TextView) v.findViewById(R.id.instagram_username)).setText("@" + up.getInstagramUserName());
        ((TextView) v.findViewById(R.id.instagram_username)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://instagram.com/_u/" + up.getInstagramUserName());
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    context.startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/" + up.getInstagramUserName())));
                }
            }
        });
        UserInterface.loadImageToImageView(up.getImage(), (ImageView) v.findViewById(R.id.profile_img), context);
        return v;
    }
}
