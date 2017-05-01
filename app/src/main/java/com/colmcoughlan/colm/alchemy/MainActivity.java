package com.colmcoughlan.colm.alchemy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridview = (GridView) findViewById(R.id.gridview);

        DataReader dataReader = new DataReader(this, gridview);

        dataReader.execute("http://colmcoughlan.com:5000");
        gridview.setAdapter(new ImageAdapter(this));
    }
}
