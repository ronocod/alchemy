package com.colmcoughlan.colm.alchemy;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Adapter;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by colm on 28/04/17.
 */

public class DataReader extends AsyncTask<String, List<Charity>, List<Charity>> {

    private Context context;
    private GridView t;

    public DataReader(Context context, GridView t) {
        this.context = context;
        this.t = t;
    }

    protected List<Charity> doInBackground(String... urlString) {

        List<String> categories = new ArrayList<String>();

        BufferedReader reader = null;
        HttpURLConnection urlConnection = null;
        List<Charity> charityList = new ArrayList<Charity>();

        try {
            URL url = new URL(urlString[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                //Log.d("Response: ", "> " + line);
            }
            String result = buffer.toString();

            JSONObject payload = new JSONObject(result);

            // first get the categories
            JSONArray categories_list  = payload.getJSONArray("categories");
            for(int i = 0;i< categories_list.length();i++){
                categories.add( categories_list.getString(i));
            }

            // now get the charities
            JSONObject charities = payload.getJSONObject("charities");
            Iterator<?> keys = charities.keys();
            // for each charity
            while( keys.hasNext() ) {
                String key = (String)keys.next(); // get the charity name
                if ( charities.get(key) instanceof JSONObject ) {
                    //Log.d("Response: ", "> " + key);
                    String category = ((JSONObject) charities.get(key)).getString("category"); // category
                    String link = ((JSONObject) charities.get(key)).getString("logo_url"); // logo link
                    String description = ((JSONObject) charities.get(key)).getString("description"); // description link
                    String number = ((JSONObject) charities.get(key)).getString("number"); // lphone number


                    Map<String,String> donation_keys_strings = new HashMap<String, String>();
                    JSONObject donation_list = new JSONObject(((JSONObject) charities.get(key)).getString("donation_options"));
                    Iterator<?> donation_keys = donation_list.keys();
                    while( donation_keys.hasNext() ) {
                        String donation_key = (String)donation_keys.next(); // donation keys and values
                        donation_keys_strings.put(donation_key, donation_list.getString(donation_key));
                    }

                    Charity newCharity = new Charity(key, category, description, link, number, donation_keys_strings);

                    charityList.add(newCharity);
                }
            }

            //Log.d("Response: ", "> " + charityList.toString());


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        // now set up the image adapater

        return charityList;
    }

    protected void onPostExecute(List<Charity> result) {
        Log.w("test", result.toString());
        t.setAdapter(new ImageAdapter(this.context, result));
    }
}