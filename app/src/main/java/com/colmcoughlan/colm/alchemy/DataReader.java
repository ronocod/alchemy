package com.colmcoughlan.colm.alchemy;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by colm on 28/04/17.
 */

public class DataReader extends AsyncTask<String, Void, Map<String,List<String>>> {

    private Context context;
    private TextView t;

    public DataReader(Context context, TextView t) {
        this.context = context;
        this.t = t;
    }

    protected Map<String,List<String>> doInBackground(String... urls) {

        Map<String,List<String>> map = new HashMap<String,List<String>>();

        Document doc = null;
        try {
            for (String url : urls) {
                Log.w("test", "Running");
                doc = Jsoup.connect(url).get();
                Elements charity_elems = doc.select("h3.charity");
                Elements keywords_elems = doc.select("div.keywords");
                int nitems = charity_elems.size();
                for(int i=0;i<nitems;i++){
                    Elements keywords = keywords_elems.get(i).select("li.keyword");
                    List<String> keyword_list = new ArrayList<String>(keywords.size());
                    for(Element keyword: keywords){
                        keyword_list.add(keyword.text());
                    }
                    map.put(charity_elems.get(i).text(), keyword_list);
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return map;
    }

    protected void onPostExecute(Map<String,List<String>> result) {
        Log.w("test", result.toString());
        t.setText(result.toString());
    }
}