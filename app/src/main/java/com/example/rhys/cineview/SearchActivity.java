package com.example.rhys.cineview;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class SearchActivity extends ActionBarActivity {

    Button btnSearch;
    EditText SearchBox;

    // GPSTracker class
    GPSTracker gps;
    RtRequest film;
    CinemaSearch Cinema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Checks for Touch on text box
        SearchBox  = (EditText) findViewById(R.id.inputSearchEditText);
        SearchBox.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //film = new RtRequest(SearchActivity.this);
                return false;
            }
        });

        //checks to see search button is pushed
        btnSearch  = (Button) findViewById(R.id.btnShowLocation);
        // Register button click event
        btnSearch.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v) {
                SearchBox  = (EditText) findViewById(R.id.inputSearchEditText);
                gps = new GPSTracker(SearchActivity.this);
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
                Cinema = new CinemaSearch(SearchActivity.this);
                Intent i = new Intent(v.getContext(), CinemaTimesActivity.class);
                i.putExtra("title",SearchBox.getText().toString());
                startActivity(i);


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
