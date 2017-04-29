package com.colmcoughlan.colm.alchemy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textList = (TextView) findViewById(R.id.hello);

        DataReader dataReader = new DataReader(this, textList);

        dataReader.execute("https://api.likecharity.com/charities/");
    }
}
