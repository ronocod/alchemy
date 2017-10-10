package com.colmcoughlan.colm.alchemy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by colm on 10/10/17.
 */

public class DonationsAdapter extends BaseAdapter {
    private Context mContext;
    private Map<String, Integer> donations;
    private List<String> charties;

    public DonationsAdapter(Context c, Map<String, Integer> donations) {
        mContext = c;
        this.donations = donations;
        this.charties = new ArrayList<String>(donations.keySet());
    }

    public int getCount() {
        return donations.size();
    }

    public String getItem(int position) {
        return this.charties.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.my_donations_layout, null);
            textView = new TextView(mContext);
            textView = (TextView) convertView.findViewById(R.id.my_donations_gridview_text);
            convertView.setTag(textView);
        } else {
            textView = (TextView) convertView.getTag();
        }

        String charityName = this.charties.get(position);
        textView.setText(charityName + ": €" + donations.get(charityName).toString());

        return convertView;
    }

}