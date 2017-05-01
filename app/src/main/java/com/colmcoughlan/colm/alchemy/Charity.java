package com.colmcoughlan.colm.alchemy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by colm on 01/05/17.
 */

public class Charity {
    private String name;
    private String category;
    private String logo_url;

    private Map<String,String> keywords = new HashMap<>();

    public Charity(String name, String category, String logo_url, Map<String,String> keywords) {
        this.name = name;
        this.category = category;
        this.logo_url = logo_url;
        this.keywords = keywords;
    }

    public String getName(){
        return this.name;
    }

    public String getCategory(){
        return this.category;
    }
    public Map<String,String> getKeywords(){
        return this.keywords;
    }
}
