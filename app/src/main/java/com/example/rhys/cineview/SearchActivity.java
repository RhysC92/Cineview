package com.example.rhys.cineview;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;


import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.net.MalformedURLException;
import java.util.List;

public class SearchActivity extends ActionBarActivity {

    Button btnSearch;

//Declaring all classes
    GPSTracker gps;
    RtRequest film;
    CinemaSearch Cinema;
    azureMobDB mobData;

    private MobileServiceClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        film = new RtRequest(SearchActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //checking that the azure mobile service can be communicated with
        try {
            mClient = new MobileServiceClient(
                    "https://cineviewdata.azure-mobile.net/",
                    "FaphWIwqLKFoFKBYIxxRZUjdrZNHkS70",
                    this);
            mobData = new azureMobDB(mClient);
        }catch (MalformedURLException e)
        {}
        // list for the auto correct
        List<String> filmList = film.getList();
        //creating and setting seek bar for distance
        final SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        final TextView seekBarValue = (TextView)findViewById(R.id.seekbarvalue);
        seekBarValue.setText("10");
        // text view with auto complete from film list
        final AutoCompleteTextView autotextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_title);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filmList);
        autotextView.setAdapter(adapter);
        //checks to see search button is pushed
        btnSearch  = (Button) findViewById(R.id.btnShowLocation);
        // Register button click event
        btnSearch.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v) {

                gps = new GPSTracker(SearchActivity.this);
                //will only check data if location is active
                if(gps.canGetLocation()){
                    String title = autotextView.getEditableText().toString();
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    int distance =  seekBar.getProgress();
                    //create and send lat,long,distance and title of movie to cinemaSearch
                    Cinema = new CinemaSearch(SearchActivity.this);
                    Cinema.CinemaSearch(latitude,longitude,title,distance);
                }else{
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                //changes distance displayed every time seek bar is moved
                seekBarValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });
    }

}
