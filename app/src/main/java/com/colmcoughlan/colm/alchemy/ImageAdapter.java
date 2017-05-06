package com.colmcoughlan.colm.alchemy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

    public Charity getItem(int position) {
        return charityList.get(position);
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
        } else {
            gridView = (View) convertView;
        }

        TextView textView = (TextView) gridView.findViewById(R.id.gridview_text);
        ImageView imageView = (ImageView) gridView.findViewById(R.id.gridview_image);
        textView.setText(charityList.get(position).getName());
        String logo_url = charityList.get(position).getLogoURL();
        //imageView.setImageResource(mThumbIds[0]);
        if ( logo_url != ""){
            Picasso.with(this.mContext).load("http://i.imgur.com/DvpvklR.png").into(imageView);
        }
        else{
            Picasso.with(this.mContext).load(mThumbIds[0]).into(imageView);
        }

        return gridView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.alchemy
    };
}
