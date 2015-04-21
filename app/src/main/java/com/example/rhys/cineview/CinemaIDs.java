package com.example.rhys.cineview;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by Rhys on 26/03/2015.
 */
public class CinemaIDs {

    private final Context mContext;


    public List<String> GooglePlaceList = new ArrayList<String>();
    String title;



    public CinemaIDs(Context context) {
        this.mContext = context;

    }

    public void CinemaIDS(Context context, String title, List GPLoc)
    {
        GooglePlaceList = GPLoc;
        this.title = title;

        new RequestTask().execute("http://api.entertainment.ie/entertainme/cinemas.asp?county=10");
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
                    List<String> CinemaListIDs = new ArrayList<String>();


                    // convert the String response to a JSON object,
                    // because JSON is the response format Rotten Tomatoes uses
                    JSONObject jsonResponse = new JSONObject(response);
                    // fetch the array of movies in the response
                    JSONArray cinema = jsonResponse.getJSONArray("cinemas");
                    for (int i = 0; i < cinema.length(); i++)
                    {
                        boolean CinemaFound = false;
                        JSONObject movie = cinema.getJSONObject(i);
                        String name = movie.getString("ll");
                        String[] NameSplit = name.split(",");
                        String ElatSplit = NameSplit[0];
                        String ElongSplit = NameSplit[1];

                        //Parsing Entertainment long and lat into double
                        double EdLong = Double.parseDouble(ElongSplit);
                        double EdLat = Double.parseDouble(ElatSplit);

                        Location Elocation = new Location("Elocation");
                        Elocation.setLatitude(EdLat);
                        Elocation.setLongitude(EdLong);




                        //Log.i("Entertainment Loc",EnterLoc);

                        for(int j =0; j < GooglePlaceList.size(); j++) {
                            String GooglePLoc = GooglePlaceList.get(j);

                            String[] GNameSplit = GooglePLoc.split(",");
                            String glatSplit = GNameSplit[0];
                            String glongSplit = GNameSplit[1];

                            double GdLong = Double.parseDouble(glongSplit);
                            double GdLat = Double.parseDouble(glatSplit);

                            Location Glocation = new Location("Glocation");
                            Glocation.setLatitude(GdLat);
                            Glocation.setLongitude(GdLong);




                            float distanceAway = Elocation.distanceTo(Glocation);


                            //Log.i("Distance to", Float.toString(distanceAway));
                            if(distanceAway <= 500 && !CinemaFound==true ) {
                                String CineName = movie.getString("name");
                                String id = movie.getString("id");
                                String time = "21:00 22:00";
                                //sending names for demo purpose
                                CinemaListIDs.add(id);
                                //CinemaListIDs.add("21:00 22:00");
                                CinemaFound = true;
                            }

                        }


                    }
                    //Test to see can an activity be called from a java class
                    Intent i = new Intent(mContext, CinemaTimesActivity.class);
                    i.putStringArrayListExtra("idlist",(ArrayList<String>)CinemaListIDs);
                    i.putExtra("title", title);
                    mContext.startActivity(i);


                }
                catch (JSONException e)
                {
                    Log.d("Test", "Failed to parse the JSON response!");
                }
            }
        }
    }
}
