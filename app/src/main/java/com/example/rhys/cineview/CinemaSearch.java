package com.example.rhys.cineview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.rhys.cineview.GPSTracker;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by Rhys on 24/02/2015.
 */
public class CinemaSearch extends Activity
{
    //google places api key
    private static final String API_KEY = "AIzaSyCKg5KZhi5UK2CbeaAK3r7-_3CmJMN51AE";

    private double latitude;
    private double longitude;
    private String title;
    private int distance;
    CinemaIDs CineID;

    private final Context mContext;

    public CinemaSearch(Context context) {
        this.mContext = context;
        }

    public void CinemaSearch(double latitude, double longitude, String title, int distance) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.title = title;
        //km - meters
        this.distance = distance*1000;
        // request to google places
        new RequestTask().execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + this.latitude  + "," + this.longitude + "&radius="+this.distance+"&types=movie_theater&key=" + API_KEY + "");
    }

    private class RequestTask extends AsyncTask<String, String, String>
    {
        // make a request to the specified url
        @Override
        protected String doInBackground(String... uri)
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try
            {
                // make a HTTP request
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK)
                {
                    // request successful - read the response and close the connection
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    responseString = out.toString();
                }
                else
                {
                    // request failed - close the connection
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            }
            catch (Exception e)
            {
                Log.d("Test", "Couldn't make a successful request! here");
            }
            return responseString;
        }

        // if the request above completed successfully, this method will
        // automatically run so you can do something with the response
        @Override
        protected void onPostExecute(String response)
        {
            super.onPostExecute(response);

            if (response != null)
            {
                try
                {
                    // convert the String response to a JSON object,
                    // because JSON is the response format Rotten Tomatoes uses
                    JSONObject jsonResponse = new JSONObject(response);
                    // fetch the array of movies in the response
                    JSONArray movies = jsonResponse.getJSONArray("results");

                    //Creates a list to store all the locations from the googlePlaces Api
                    List<String> GoogleCinemaList = new ArrayList<String>();

                    //Goes through all returns from the api
                    for (int i = 0; i < movies.length(); i++)
                    {

                        JSONObject movie = movies.getJSONObject(i);
                        String name = movie.getString("name");
                        JSONObject geometry = movie.getJSONObject("geometry");
                        JSONObject loco = geometry.getJSONObject("location");
                        //Geting Long & Lat from google places
                        String Long = loco.getString("lng");
                        String Lat = loco.getString("lat");

                        //Combines into one location string
                        String Loc = Lat+","+Long;


                        //Adds it to the list to be passed though
                        GoogleCinemaList.add(Loc);

                    }

                    //Passes through list of google places to be compared against entertainment cinemas
                    CineID = new CinemaIDs(mContext);

                    CineID.CinemaIDS(mContext, title, GoogleCinemaList);


                }
                catch (JSONException e)
                {
                    Log.d("Test", "Failed to parse the JSON response!");
                }
            }
        }
    }
}
