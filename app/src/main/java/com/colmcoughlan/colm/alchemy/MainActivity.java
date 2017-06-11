package com.colmcoughlan.colm.alchemy;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.util.Log;
import android.telephony.SmsManager;
import android.widget.Spinner;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    static String category = "All";
    GridView gridView = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));

        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d("Query submit: ", "> " + query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        Log.d("Testchange: ", "> " + newText);
        Log.d("Testchangecat: ", "> " + category);


        ImageAdapter imageAdapter = (ImageAdapter) gridView.getAdapter();
        imageAdapter.getFilter().filter(newText+":cat:"+category);

        // use to enable search view popup text
//        if (TextUtils.isEmpty(newText)) {
//            friendListView.clearTextFilter();
//        }
//        else {
//            friendListView.setFilterText(newText.toString());
//        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setSelection(0, false); // need this to stop OnItemSelectedListener being called at start

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                              @Override
                                              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                category = (String) parent.getItemAtPosition(position);
                                                if(gridView != null){
                                                    ImageAdapter imageAdapter = (ImageAdapter) gridView.getAdapter();
                                                    imageAdapter.getFilter().filter(""+":cat:"+category);
                                                }
                                              }

                                              @Override
                                              public void onNothingSelected(AdapterView<?> parent) {
                                                category = "All";
                                              }
                                          }
        );

        gridView = (GridView) findViewById(R.id.gridview);

        DataReader dataReader = new DataReader(this, gridView);

        dataReader.execute("http://colmcoughlan.com:5000/gci");

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
                                sendSms(charity.getNumber(), keywords.get(which));
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
