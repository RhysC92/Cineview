package com.example.rhys.cineview;

/**
 * Created by Rhys on 17/04/2015.
 */
import android.content.Context;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.microsoft.windowsazure.mobileservices.*;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;

public class azureMobDB {

    private MobileServiceClient mobClient;

    private ProgressBar mProgressBar;


    public azureMobDB(MobileServiceClient mClient) {
        mobClient = mClient;
    }



}
