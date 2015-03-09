package com.example.rhys.cineview;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

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
public class RtRequest {
    // the Rotten Tomatoes API key of your application! get this from their website
    private static final String API_KEY = "mkptd89jc5m8wb8gwxr7h5ey";

    // the number of movies you want to get in a single request to their web server
    private static final int MOVIE_PAGE_LIMIT = 50;

    //country you are requesting from
    private static final String Country_Code = "ie";

    private final Context mContext;

    JSONObject jsonResponse;

    // fetch the array of movies in the response
    JSONArray movies;



    public RtRequest(Context context) {

        this.mContext = context;
        new RequestTask().execute("http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?apikey=" + API_KEY + "&country=" + Country_Code + "&page_limit=" + MOVIE_PAGE_LIMIT);


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
                    jsonResponse = new JSONObject(response);

                    // fetch the array of movies in the response
                    movies = jsonResponse.getJSONArray("movies");

                    for (int i = 0; i < movies.length(); i++)
                    {
                        //gets the title,age,runtime & synopsis of pulled in data
                        JSONObject movie = movies.getJSONObject(i);

                            String title = movie.getString("title");
                            String ageGroup = movie.getString("mpaa_rating");
                            String runTime = movie.getString("runtime");
                            String synopsis = movie.getString("synopsis");

                        if(title.equalsIgnoreCase("Kidnapping Mr. Heineken")) {

                            Log.i("New list", "-");
                            Log.i("Rhys", title);
                            Log.i("Rhys", ageGroup);
                            Log.i("Rhys", runTime);
                            Log.i("Rhys", synopsis);


                            //gets cast list from array within array
                            JSONArray cast = movie.getJSONArray("abridged_cast");
                            for (int j = 0; j < cast.length(); j++) {
                                JSONObject castMember = cast.getJSONObject(j);
                                String Cast = castMember.getString("name");
                                //Log.i("Rhys", Cast);
                            }
                            Log.i("New list", "-");
                        }

                    }
                }
                catch (JSONException e)
                {
                    Log.d("Test", "Failed to parse the JSON response!");
                }
            }
        }
    }

    public String MoreInfoRequest(String MovieName) {
        Log.i("TESTER","Got to exception");
        try
        {
            String MName = MovieName;
        for (int i = 0; i < movies.length(); i++)
        {
            //gets the title,age,runtime & synopsis of pulled in data
            JSONObject movie = movies.getJSONObject(i);

            String title = movie.getString("title");


            if(title.equalsIgnoreCase(MName)) {
                String ageGroup = movie.getString("mpaa_rating");
                String runTime = movie.getString("runtime");
                String synopsis = movie.getString("synopsis");

                Log.i("New list", "-");
                Log.i("Rhys", title);
                Log.i("Rhys", ageGroup);
                Log.i("Rhys", runTime);
                Log.i("Rhys", synopsis);


                //gets cast list from array within array
                JSONArray cast = movie.getJSONArray("abridged_cast");
                for (int j = 0; j < cast.length(); j++) {
                    JSONObject castMember = cast.getJSONObject(j);
                    String Cast = castMember.getString("name");
                    //Log.i("Rhys", Cast);
                }
                Log.i("New list", "-");
            }

        }
        }
        catch (JSONException e)
        {
            Log.d("Test", "Failed to parse the JSON response!");
        }
        return MovieName;
    }
}


