package com.example.rhys.cineview;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {

    Button btnShowLocation;

    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(MainActivity.this);

                // check if GPS enabled
                if (gps.canGetLocation()) {

                    double latitude1 = gps.getLatitude();
                    double longitude1 = gps.getLongitude();
                    double longitude2 = -6.364815;
                    double latitude2 = 53.289944;
                    float [] dist = new float[1];

                    Location.distanceBetween(latitude1, longitude1, latitude2, longitude2, dist);
                    float distance = dist[0]*0.000621371192f;


                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Locations are " + distance + " meters away from each other" , Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });

    }
}