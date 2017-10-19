package org.floodexplorer.floodexplorer;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

/**
 * Created by mgilge on 10/17/17.
 */

public class OmekaRestLoader extends AsyncTaskLoader
{
    private ArrayList<String> omekaRestResults; //this is the result of our rest connection
    private String omekaRestGeoResults;
    private String omekaRestItemResults;
    private String omekaRestFilesResults;
    private String sqlQueryHomeResults;


    public OmekaRestLoader(Context context)
    {
        super(context);
    }

    @Override
    protected void onStartLoading()
    {

        if (omekaRestResults != null)
        {
            // Use cached data
            deliverResult(omekaRestResults);
        }
        else
        {
            // We have no data, so kick off loading it
            forceLoad();
        }
    }

    @Override
    public ArrayList<String> loadInBackground()
    {
        HttpRequestHandler rh = new HttpRequestHandler();
        omekaRestGeoResults = rh.sendGetRequest(RESTConfig.REST_URL_GEOLOC); //Using Omeka Rest
        omekaRestItemResults = rh.sendGetRequest(RESTConfig.REST_URL_ITEMS); //Using Omeka Rest
        omekaRestFilesResults = rh.sendGetRequest(RESTConfig.REST_URL_FILES); //not using REST, our own SQL and JSON
        sqlQueryHomeResults = rh.sendGetRequest(RESTConfig.REST_SIMPLE_PAGES); //not using REST, our own SQL and JSON

        omekaRestResults = new ArrayList<String>();
        omekaRestResults.add(omekaRestGeoResults);
        omekaRestResults.add(omekaRestItemResults);
        omekaRestResults.add(omekaRestFilesResults);
        omekaRestResults.add(sqlQueryHomeResults);

        return omekaRestResults;
    }



}
