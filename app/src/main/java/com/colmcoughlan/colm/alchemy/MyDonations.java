package com.colmcoughlan.colm.alchemy;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import java.util.HashMap;
import java.util.Map;

public class MyDonations extends AppCompatActivity {

    GridView gridView = null;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_donations);
        setupActionBar();
        Map<String,Integer> donations = get_donations();

        gridView = (GridView) findViewById(R.id.my_donations_gridview);
        gridView.setAdapter(new DonationsAdapter(this, donations));

    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private Map<String, Integer> get_donations(){


        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, "address='50300'", null, null);

        Map<String,Integer> donations = new HashMap<String, Integer>();

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                //Log.d("New message", "msg");
                for(int idx=0;idx<cursor.getColumnCount();idx++)
                {
                    if(cursor.getColumnName(idx).contentEquals("body")){
                        String msgData = cursor.getString(idx);

                        if(msgData.startsWith("Thank you")){
                            String charity_name = (msgData.split(" donation to ")[1]).split("\\.")[0];
                            Integer donation_amount = Integer.parseInt(msgData.split(" for your ")[1].split(" Euro")[0]);
                            //Log.d("charity_name", charity_name);
                            //Log.d("donation_amount", donation_amount.toString());
                            if(donations.containsKey(charity_name)){
                                donations.put(charity_name, donations.get(charity_name) + donation_amount);
                            } else {
                                donations.put(charity_name, donation_amount);
                            }
                        }
                    }
                }
            } while (cursor.moveToNext());
        }

        if (donations.size() == 0){
            donations.put("No donations yet",0);
        }

        return donations;
    }

}
