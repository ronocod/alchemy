package com.colmcoughlan.colm.alchemy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by colm on 01/05/17.
 */

public class Charity {
    private String name;
    private String category;
    private String logo_url;
    private String number;
    private String description;

    private Map<String,String> keywords = new HashMap<>();
    private Map<String,String> freqs = new HashMap<>();


    public Charity(String name, String category, String description, String logo_url, String number, Map<String,String> keywords, Map<String,String> freqs) {
        this.name = name;
        this.category = category;
        this.logo_url = logo_url;
        this.number = number;
        this.keywords = keywords;
        this.freqs = freqs;
        this.description = description;
    }

    public String getName(){
        return this.name;
    }

    public String getCategory(){
        return this.category;
    }

    public String getDescription(){
        return this.description;
    }

    public List<String>  getKeys(){
        List<String> keys = new ArrayList<String>(keywords.keySet());

        return keys;
    }

    public Map<String,String> getFreqs(){
        return freqs;
    }

    public String getCost(String key) {
        return keywords.get(key);
    }

    public CharSequence[] getKeywords(List<String> keys){

        List<String> keys_values = new ArrayList<String>();

        for(int i = 0; i< keys.size();i++){
            keys_values.add(getKeywordDescription(freqs.get(keys.get(i))) + ": " + keys.get(i) + " - " + keywords.get(keys.get(i)));
        }

        return keys_values.toArray(new CharSequence[keys_values.size()]);
    }

    public String getLogoURL(){
        return this.logo_url;
    }

    public String getNumber(){
        return this.number;
    }

    // get keyword text

    private String getKeywordDescription(String freq){
        if(freq.equals("once")){
            return "One time donation";
        } else if (freq.equals("week")){
            return "Weekly donation";
        } else if (freq.equals("month")){
            return "Monthly donation";
        }
        else{
            return "Error! Please report this if you see it.";
        }
    }

}
