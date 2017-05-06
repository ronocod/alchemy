package com.colmcoughlan.colm.alchemy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.util.Log;
import android.telephony.SmsManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GridView gridView = (GridView) findViewById(R.id.gridview);

        DataReader dataReader = new DataReader(this, gridView);

        dataReader.execute("http://colmcoughlan.com:5000");

        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                final Charity charity = (Charity) gridView.getItemAtPosition(position);
                final List<String> keywords = charity.getKeys();
                Log.w("touch", charity.getName());
                // DO something

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose a keyword");
                builder.setItems( charity.getKeywords(keywords) , new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.w("choice:", keywords.get(which));
                                sendSms("50300", keywords.get(which));
                            }
                        });
                builder.create().show();
            }
        });
    }

    private void sendSms(String phoneNumber, String message){
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
}
