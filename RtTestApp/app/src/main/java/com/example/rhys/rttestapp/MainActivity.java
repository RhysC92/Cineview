package com.example.rhys.rttestapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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

public class MainActivity extends Activity
{
    // the Rotten Tomatoes API key of your application! get this from their website
    private static final String API_KEY = "mkptd89jc5m8wb8gwxr7h5ey";

    // the number of movies you want to get in a single request to their web server
    private static final int MOVIE_PAGE_LIMIT = 10;

    private EditText searchBox;
    private Button searchButton;
    private ListView moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBox = (EditText) findViewById(R.id.text_search_box);
        searchButton = (Button) findViewById(R.id.button_search);
        searchButton.setOnClickListener(new OnClickListener()
        {
            // send an API request when the button is pressed
            @Override
            public void onClick(View arg0)
            {
                new RequestTask().execute("http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=" + API_KEY + "&q=" + searchBox.getText().toString().trim() + "&page_limit=" + MOVIE_PAGE_LIMIT);
            }
        });
        moviesList = (ListView) findViewById(R.id.list_movies);
    }

    private void refreshMoviesList(String[] movieTitles)
    {
        moviesList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, movieTitles));
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
                    JSONArray movies = jsonResponse.getJSONArray("movies");

                    // add each movie's title to an array
                    String[] movieTitles = new String[movies.length()];
                    for (int i = 0; i < movies.length(); i++)
                    {
                        JSONObject movie = movies.getJSONObject(i);
                        movieTitles[i] = movie.getString("title");
                    }

                    // update the UI
                    refreshMoviesList(movieTitles);
                }
                catch (JSONException e)
                {
                    Log.d("Test", "Failed to parse the JSON response!");
                }
            }
        }
    }
}
