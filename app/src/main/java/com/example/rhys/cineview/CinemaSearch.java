package com.example.rhys.cineview;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by Rhys on 24/02/2015.
 */
public class CinemaSearch
{
    private static final String API_KEY = "AIzaSyCKg5KZhi5UK2CbeaAK3r7-_3CmJMN51AE";

    private final Context mContext;

    public CinemaSearch(Context context) {
        this.mContext = context;
        //new RequestTask().execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + Lat +"," + Long + "&radius=" + radius +"&types=movie_theater&key=" + API_KEY ");
        new RequestTask().execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=53.287059491564335,-6.367207612255925&radius=500&types=movie_theater&key=" + API_KEY + "");

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
                Log.d("Test", "Couldn't make a successful request!");
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



                    for (int i = 0; i < movies.length(); i++)
                    {
                        //gets the title,age,runtime & synopsis of pulled in data
                        JSONObject movie = movies.getJSONObject(i);
                        String name = movie.getString("name");

                    }
                }
                catch (JSONException e)
                {
                    Log.d("Test", "Failed to parse the JSON response!");
                }
            }
        }
    }
}
