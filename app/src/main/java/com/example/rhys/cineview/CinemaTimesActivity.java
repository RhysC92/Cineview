package com.example.rhys.cineview;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import static com.example.rhys.cineview.R.id.FilmInfobutton;


public class CinemaTimesActivity extends ActionBarActivity {

    Button btnTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_times);
        Bundle b = getIntent().getExtras();
        final String total = b.getString("title");
        Log.i("title +", total);
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


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cinema_times, menu);
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
