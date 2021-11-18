package com.party.technologies.nineteen_ninety_nine.ui.misc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.party.technologies.nineteen_ninety_nine.R;
/**
public class PartyAdapter extends ArrayAdapter<String> {

    String[] partyNames;
    int[] partyImages;
    Context mContext;

    public PartyAdapter(Context context, String[] partyNames, int[] partyImages) {
        super(context, R.layout.listview_item);
        this.partyNames = partyNames;
        this.partyImages = partyImages;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return partyNames.length;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listview_item, parent, false);
            mViewHolder.image = (ImageView) convertView.findViewById(R.id.imageView);
            mViewHolder.name = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.image.setImageResource(partyImages[position]);
        mViewHolder.name.setText(partyNames[position]);

        return convertView;
    }

    static class ViewHolder {
        ImageView image;
        TextView name;
    }
}**/