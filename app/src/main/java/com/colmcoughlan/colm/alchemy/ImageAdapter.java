package com.colmcoughlan.colm.alchemy;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by colm on 29/04/17.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Charity> charityList;
    private List<Charity> filteredList;
    private CharityFilter charityFilter;

    public ImageAdapter(Context c, List<Charity> charityList) {
        mContext = c;
        this.charityList = charityList;
        this.filteredList = charityList;
    }

    public int getCount() {
        return filteredList.size();
    }

    public Charity getItem(int position) {
        return filteredList.get(position);
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

        if (position % 2 == 0)
            gridView.setBackgroundColor(Color.parseColor("#e0e0e0"));
        else
            gridView.setBackgroundColor(Color.parseColor("#eeeeee"));


        TextView textView = (TextView) gridView.findViewById(R.id.gridview_text);
        final ImageView imageView = (ImageView) gridView.findViewById(R.id.gridview_image);
        textView.setText(filteredList.get(position).getName());
        String logo_url = filteredList.get(position).getLogoURL();
        //imageView.setImageResource(mThumbIds[0]);

        Picasso.Builder builder = new Picasso.Builder(this.mContext);
        builder.listener(new Picasso.Listener()
        {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
            {
                picasso.load(mThumbIds[0]).into(imageView); // on failure just load the default image
            }
        });

        Picasso picasso= builder.build();

        if ( !logo_url.isEmpty()){
            picasso.load(logo_url).into(imageView);
        }
        else{
            picasso.load(mThumbIds[0]).into(imageView);
        }

        return gridView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.alchemy
    };

    public Filter getFilter() {
        if (charityFilter == null) {
            charityFilter = new CharityFilter();
        }

        return charityFilter;
    }

    private class CharityFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraintCategory){
            FilterResults filterResults = new FilterResults();
            String[] parts = constraintCategory.toString().split(":cat:");
            CharSequence constraint = parts[0];
            CharSequence category = parts[1];
            boolean haveConstraint = false;
            boolean matchesConstraint = false;
            boolean matchesCategory = false;

            if (constraint!=null && constraint.length()>0){
                haveConstraint = true;
            }
            if (haveConstraint || !category.equals("All")) {
                ArrayList<Charity> tempList = new ArrayList<Charity>();

                for (Charity charity : charityList) {
                    if(haveConstraint){
                        matchesConstraint = (charity.getName().toLowerCase().contains(constraint.toString().toLowerCase()));
                    }else{
                        matchesConstraint = true;
                    }

                    if(category.equals("All")){
                        matchesCategory = true;
                    }else{
                        matchesCategory = (charity.getCategory().equals(category.toString()));
                    }

                    if  (matchesConstraint && matchesCategory) {
                        tempList.add(charity);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = charityList.size();
                filterResults.values = charityList;
            }


            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (List<Charity>) results.values;
            notifyDataSetChanged();
        }
    }

}