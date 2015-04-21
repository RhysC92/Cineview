package com.example.rhys.cineview;

import android.content.Intent;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class SmsActivity extends ActionBarActivity {

    private Spinner timeSpinner;
    private Button btnSend;
    private Button btnContact;
    String smsItem= "Test";
    String title= "Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        String CinemaName = "";
        String timePicked = "";
        Bundle b = getIntent().getExtras();
        smsItem = b.getString("item");
        title = b.getString("title");
        String[] arr = smsItem.split(" ");
        List<String> timesList = new ArrayList<>();

        for(int i = 0; i < arr.length; i++){
            if (arr[i].matches("[a-zA-Z]+"))
            {
                CinemaName = CinemaName+arr[i]+" ";
            }
            else if(arr[i].matches("[\n]+")){}
            else{
                timesList.add(arr[i]);
            }
        }
        List<String> PhoneList = new ArrayList<String>();
        final String textCinemaName = CinemaName;
        final EditText smsText = (EditText) findViewById(R.id.smsLayout);
        timeSpinner = (Spinner) findViewById(R.id.timeSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, timesList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(dataAdapter);

        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String time = timeSpinner.getSelectedItem().toString();
                String Message = "Hi I am playing to go to the cinema would you like to come? \n Film Name : "+ title + "\nDetails Cinema : " + textCinemaName +" \n Time of Film : " + time + ".";

                smsText.setText(Message);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });



        btnSend  = (Button) findViewById(R.id.SendButton);
        // Register button click event
        btnSend.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Contact.class);
                i.putExtra("Txt",smsText.getText().toString());
                startActivity(i);
/*
                SmsManager smsManager = SmsManager.getDefault();

                smsManager.sendTextMessage("0879213648", null, smsText.getText().toString(), null, null);
*/
                }

        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sms, menu);
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
