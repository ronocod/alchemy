package com.colmcoughlan.colm.alchemy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by colm on 29/04/17.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Charity> charityList;

    public ImageAdapter(Context c, List<Charity> charityList) {
        mContext = c;
        this.charityList = charityList;
    }

    public int getCount() {
        return charityList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            gridView = new View(mContext);
            gridView = inflater.inflate(R.layout.gridview_layout, null);
            TextView textView = (TextView) gridView.findViewById(R.id.gridview_text);
            ImageView imageView = (ImageView) gridView.findViewById(R.id.gridview_image);
            textView.setText("Test");
            imageView.setImageResource(mThumbIds[position]);
        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.alchemy
    };
}
