package com.colmcoughlan.colm.alchemy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by colm on 10/10/17.
 */

public class DonationsAdapter extends BaseAdapter {
    private Context mContext;
    private Map<String, Integer> donations;
    private List<String> charities;

    private static <K, V extends Comparable<? super V>> ArrayList<K>
    sortKeysByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        });

        ArrayList<K> result = new ArrayList<K>();
        for (Map.Entry<K, V> entry : list) {
            result.add(entry.getKey());
        }
        return result;
    }

    public DonationsAdapter(Context c, Map<String, Integer> donations) {
        mContext = c;
        this.donations = donations;
        this.charities = sortKeysByValue(donations);
    }

    public int getCount() {
        return donations.size();
    }

    public String getItem(int position) {
        return this.charities.get(position);
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
            textView = convertView.findViewById(R.id.my_donations_gridview_text);
            convertView.setTag(textView);
        } else {
            textView = (TextView) convertView.getTag();
        }

        String charityName = this.charities.get(position);
        textView.setText(charityName + ": €" + donations.get(charityName).toString());

        return convertView;
    }

}
