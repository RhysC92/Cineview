package com.example.rhys.cineview;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class FilmInfoActivity extends ActionBarActivity {

    // the Rotten Tomatoes API key of your application! get this from their website
    private static final String API_KEY = "mkptd89jc5m8wb8gwxr7h5ey";
    private static final String Country_Code = "ie";


    // the number of movies you want to get in a single request to their web server
    private static final int MOVIE_PAGE_LIMIT = 50;

    Button btnBack;
    TextView TitleDisplay;
    TextView RatingDisplay;
    TextView runTimeDisplay;
    TextView plotDisplay;
    TextView CastDisplay;

    String text = " blank ";
    String title = " " ;
    String ageGroup= " " ;
    String runTime= " " ;
    String synopsis= " " ;
    String Cast = " ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_info);

        Bundle b = getIntent().getExtras();
         text = b.getString("title");

        //reguests info from rottentomatoes on page startup
        new RequestTask().execute("http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?apikey=" + API_KEY + "&country=" + Country_Code + "&page_limit=" + MOVIE_PAGE_LIMIT);
        btnBack  = (Button) findViewById(R.id.BackButton);






        // Register button click event
        btnBack.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), CinemaTimesActivity.class);
                startActivity(i);
            }
        });
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



                    for (int i = 0; i < movies.length(); i++)
                    {

                        //gets the title,age,runtime & synopsis of pulled in data
                        JSONObject movie = movies.getJSONObject(i);

                            String temp = movie.getString("title");

                        if(temp.equalsIgnoreCase(text)) {
                            title = movie.getString("title");
                            ageGroup = movie.getString("mpaa_rating");
                            runTime = movie.getString("runtime");
                            synopsis = movie.getString("synopsis");




                            //gets cast list from array within array
                            JSONArray cast = movie.getJSONArray("abridged_cast");
                            for (int j = 0; j < cast.length(); j++) {
                                JSONObject castMember = cast.getJSONObject(j);
                                String tempCast = castMember.getString("name");
                                if(Cast.equals(" ")) {
                                    Cast = Cast + tempCast;
                                }
                                else
                                {
                                    Cast = Cast + ",";
                                    Cast = Cast + tempCast;
                                }
                            }
                        }
                        TitleDisplay = (TextView) findViewById(R.id.titleDisplay);
                        TitleDisplay.setText(title);
                        RatingDisplay = (TextView) findViewById(R.id.ratingDisplay);
                        RatingDisplay.setText(ageGroup);
                        runTimeDisplay = (TextView) findViewById(R.id.runTimeDisplay);
                        runTimeDisplay.setText(runTime);
                        plotDisplay = (TextView) findViewById(R.id.plotDisplay);
                        plotDisplay.setText(synopsis);
                        CastDisplay = (TextView) findViewById(R.id.CastDisplay);
                        CastDisplay.setText(Cast);



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
