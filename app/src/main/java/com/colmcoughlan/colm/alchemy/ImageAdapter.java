package com.colmcoughlan.colm.alchemy;

import android.content.Context;
import android.graphics.Color;
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

    static class ViewHolder
    {
        TextView textView;
        ImageView imageView;
    }

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
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gridview_layout, null);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.gridview_text);
            holder.imageView = convertView.findViewById(R.id.gridview_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            Picasso.get().cancelRequest(holder.imageView);
        }

        if (position % 2 == 0){
            convertView.setBackgroundColor(Color.parseColor("#e0e0e0"));
        }
        else {
            convertView.setBackgroundColor(Color.parseColor("#eeeeee"));
        }

        holder.textView.setText(filteredList.get(position).getName());
        String logo_url = filteredList.get(position).getLogoURL();
        try{
            Picasso.get().load(logo_url).placeholder(R.drawable.alchemy).into(holder.imageView);
        }
        catch (Throwable e){
            Picasso.get().load(R.drawable.alchemy).into(holder.imageView);
        }

        return convertView;
    }


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
                        matchesCategory = (charity.getCategory().toLowerCase().contains(category.toString().toLowerCase()));
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
