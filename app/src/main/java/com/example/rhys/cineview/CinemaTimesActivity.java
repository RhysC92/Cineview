package com.example.rhys.cineview;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

import static com.example.rhys.cineview.R.id.FilmInfobutton;

import com.example.rhys.cineview.CinemaSearch;



public class CinemaTimesActivity extends ActionBarActivity {

    GPSTracker gps;
    CinemaSearch Cinema;
    CinemaIDs CinemaID;
    Button btnTask;
    TextView TitleDisplay;
    TextView CinemaVenue;
    TextView CinemaTime;
    private ListView CineTimesList;
    public List<String> CinemaTimesList = new ArrayList<String>();
    public List<String> CinemaIDsList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_times);
        ArrayList<String> idlist = getIntent().getStringArrayListExtra("idlist");

        Bundle b = getIntent().getExtras();
        final String total = b.getString("title");

        //gps = new GPSTracker(CinemaTimesActivity.this);
        //double latitude = gps.getLatitude();
        //double longitude = gps.getLongitude();

        //Cinema = new CinemaSearch(CinemaTimesActivity.this);
        //Cinema.CinemaSearch(latitude,longitude,total);

        String NTitle = total.replaceAll("\\s","+");



        //List<String> CinemaIDList = new ArrayList<String>();
        //CinemaIDList = CinemaID.getID();
        //moviesList = (ListView) findViewById(R.id.list_movies);
        for(int i = 0; i < idlist.size(); i++) {
            String ID = idlist.get(i);
            Log .i("Cinema id is", ID);
            CinemaIDsList.add(ID);

            //new RequestTask().execute("http://api.entertainment.ie/entertainme/cinemas.asp?county=10");
            Log.i("Title Searched", NTitle);
            new RequestTask().execute("http://cinematime.azurewebsites.net/movie/id/"+ ID +"/movieName/" + NTitle);
            //new RequestTask().execute("http://api.entertainment.ie/entertainme/movietimes.asp?id="+ ID +"&fid=" + NTitle);
            //new RequestTask().execute("http://api.entertainment.ie/entertainme/movietimes.asp?id="+ ID +"&fid=focus");

        }
        CineTimesList = (ListView) findViewById(R.id.listView);
        btnTask  = (Button) findViewById(FilmInfobutton);
        // Register button click event
        btnTask.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(), FilmInfoActivity.class);
                i.putExtra("title",total);
                startActivity(i);
            }
        });
        CineTimesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = ((TextView)view).getText().toString();

                    Intent i = new Intent(view.getContext(), SmsActivity.class);
                    i.putExtra("title",total);
                    i.putExtra("item", item);
                    startActivity(i);



            }
        });
    }

    private void refreshMoviesList(List movieTitles)
    {
        CineTimesList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, movieTitles));




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


                    String venue = jsonResponse.getString("CinemaName");

                    // fetch the array of movies in the response
                    String film = jsonResponse.getString("FilmTimes");




                    //gets the title,age,runtime & synopsis of pulled in data

                    Log.i("Cinema", venue);
                    Log.i("Times", film);
                    String FilmInfo = venue + " \n " + film;

                    CinemaTimesList.add(FilmInfo);
                    //CinemaTimesList.add(times);
                    refreshMoviesList(CinemaTimesList);






                }
                catch (JSONException e)
                {
                    Log.d("Test", "Failed to parse the JSON response!");
                }
            }
        }
    }
}
