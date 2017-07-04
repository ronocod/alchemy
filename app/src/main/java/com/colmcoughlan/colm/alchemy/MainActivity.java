package com.colmcoughlan.colm.alchemy;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    static String category = "All";
    GridView gridView = null;
    Activity mainActivity = this;
    Boolean rebuildMenu = true;

    // add the search and about sections to the menu. Hook up the search option to the correct searchview


    // this is actually the same as on create, but called after resume to make sure menu is ok

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        if(rebuildMenu){
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
            rebuildMenu = false;
        }

        return true;
    };

    @Override
    public void onResume(){
        super.onResume();
        rebuildMenu = true;
        invalidateOptionsMenu();
    }

    // add ability to select about activity

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // don't really submit searches, but still need function

    @Override
    public boolean onQueryTextSubmit(String query) {
        ImageAdapter imageAdapter = (ImageAdapter) gridView.getAdapter();
        imageAdapter.getFilter().filter(query+":cat:"+category);

        return true;
    }

    // connect search to imageadapter filter method

    @Override
    public boolean onQueryTextChange(String newText) {


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

    // create the main screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // create the category spinner


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


        // create the gridview and get the data

        gridView = (GridView) findViewById(R.id.gridview);
        DataReader dataReader = new DataReader(this, gridView);
        dataReader.execute(getString(R.string.server_url));

        // if this is the first run, display an information box

        if(firstRun()){
            showHelp(this);
        }
        else{
            // whether it's first run or not, we need SMS permissions (not granted by default)
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
            }
        }

        // set up click listener for selection of charities

        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                final Charity charity = (Charity) gridView.getItemAtPosition(position);
                final List<String> keywords = charity.getKeys();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose a keyword.");
                builder.setItems( charity.getKeywords(keywords) , new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                confirmDialog(charity, keywords.get(which));
                            }
                        });
                builder.create().show();
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Charity charity = (Charity) gridView.getItemAtPosition(position);
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(getApplicationContext(), charity.getDescription(), duration);
                toast.show();
                return true; // cancel the single click with true
            }
        });
    }

    // is this the first run?

    private boolean firstRun(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.commit();
        }
        return  !previouslyStarted;
    }

    // show help if needed

    private void showHelp(final Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Welcome!");
        builder.setMessage(R.string.welcome_text);
        builder.setPositiveButton(R.string.welcome_dismiss, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, 0);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // if we get permissions for SMS - great, otherwise - raise an error and quit the app

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Warning: This app needs SMS permissions!");
                    builder.setMessage(R.string.sms_text);
                    builder.setPositiveButton(R.string.welcome_dismiss, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            mainActivity.finish();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        }
    }

    // check with the user if they want to confirm a donation

    private void confirmDialog(final Charity charity, final String keyword) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Donate "+charity.getCost(keyword)+" to "+charity.getName()+"?");
        builder.setMessage(getString(R.string.likecharity_tcs));
        builder.setPositiveButton(R.string.confirm_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                sendSms(charity.getNumber(), keyword);
                dialog.dismiss();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), R.string.toast_confirmation, duration);
                toast.show();
            }
        });
        builder.setNegativeButton(R.string.confirm_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    // actually send the text

    private void sendSms(String phoneNumber, String message){
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
}
