package org.floodexplorer.floodexplorer.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.StoryItemDetails;
import org.floodexplorer.floodexplorer.AppConfiguration;
import org.floodexplorer.floodexplorer.OmekaDataItems.SqlRequestHandler;
import org.floodexplorer.floodexplorer.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
{

    private String queryResults;
    private String storyItemsQueryResults;
    private String homeQueryResults;
    private ArrayList<CustomMapMarker> omekaDataItems; //this is our model for MVC....
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.customizeActionBar();
        this.omekaDataItems = new ArrayList<CustomMapMarker>();
        this.navigation = (BottomNavigationView) findViewById(R.id.navigation);
        this.initBottomNavigationView();
        if(savedInstanceState == null)
        {
            this.getLocations(); //perform db query...
            this.getStoryItems();
            this.getHomeData();
        }
        else
        {
            this.omekaDataItems = savedInstanceState.getParcelableArrayList(AppConfiguration.BUNDLE_TAG_OMEKA_DATA_ITEMS);
            this.homeQueryResults = savedInstanceState.getString(AppConfiguration.BUNDLE_TAG_HOME_RESULTS);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        int selected = navigation.getSelectedItemId();

        outState.putInt(AppConfiguration.BUNDLE_TAG_NAVIGATION_LOCATION, selected);
        outState.putParcelableArrayList(AppConfiguration.BUNDLE_TAG_OMEKA_DATA_ITEMS, omekaDataItems);
        outState.putString(AppConfiguration.BUNDLE_TAG_HOME_RESULTS, homeQueryResults);
    }

    // Menu...if we want it
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.aboutMenuItem)
        {
            Toast.makeText(this,
                    "FloodExplorer.org Android App", Toast.LENGTH_LONG).show();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
*/
    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

    private void initBottomNavigationView()
    {
        navigation.setBackground(AppConfiguration.BtmNavBarColor);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void customizeActionBar()     //This method will customize the bar at the top of the screen
    {
        ActionBar actionBar = getSupportActionBar();
        //this code will set an image as the background
        //BitmapDrawable background = new BitmapDrawable (BitmapFactory.decodeResource(getResources(), R.drawable.logomenu));
       // actionBar.setBackgroundDrawable(background);

        //here we can set the color
        actionBar.setBackgroundDrawable(AppConfiguration.ActionBarColor);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_action_bar_layout); //use custom XML layout for ActionBar
       // actionBar.setDisplayShowTitleEnabled(true);
        View view = getSupportActionBar().getCustomView();
        //can now use view to get at buttons, images and anything else we wish....
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            Fragment fragment = null;
            switch (item.getItemId())
            {
                case R.id.navigation_home:
                    fragment = HomeFragment.newInstance(homeQueryResults);
                    break;
                case R.id.navigation_stories:
                    fragment = StoriesFragment.newInstance(omekaDataItems);
                    break;
                case R.id.navigation_map:
                    fragment = MapsFragment.newInstance(omekaDataItems);
                    break;
                case R.id.navigation_about:
                   fragment = AboutFragment.newInstance();
                    break;
            }

            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();

            return true;
        }

    };

    private void setInitialNavgationTab()
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, HomeFragment.newInstance(homeQueryResults));
        transaction.commit();
    }

    private void getLocations()
    {
        class GetLocations extends AsyncTask<Void,Void,String>
        {
            ProgressDialog loading;
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Fetching...","Wait...",false,false); //showActivity works in fragments, getContext not so much
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                loading.dismiss();
                queryResults = s;
                parseLocationJSON();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                SqlRequestHandler rh = new SqlRequestHandler();
                String s = rh.sendGetRequest(AppConfiguration.URL_GET_LOCATIONS);
                return s;
            }
        }
        GetLocations ge = new GetLocations();
        ge.execute();
    }

    private void parseLocationJSON()
    {
        JSONObject jsonObject = null;
        try
        {
            jsonObject = new JSONObject(queryResults);
            JSONArray result = jsonObject.getJSONArray(AppConfiguration.TAG_JSON_ARRAY);

            for(int x = 0; x < result.length(); x ++)
            {
                JSONObject jo = result.getJSONObject(x);
                double latitude = jo.getDouble(AppConfiguration.TAG_LAT);
                double longitude = jo.getDouble(AppConfiguration.TAG_LONG);
                String title = this.dealWithEscapeChars(jo.getString(AppConfiguration.TAG_TITLE));
                String text = this.dealWithEscapeChars(jo.getString(AppConfiguration.TAG_TEXT));
                double zoom = jo.getInt(AppConfiguration.TAG_ZOOM);
                int id = jo.getInt(AppConfiguration.TAG_ID);
                CustomMapMarker addMarker = new CustomMapMarker(latitude, longitude, title, "", zoom, text, id);
                this.omekaDataItems.add(addMarker);
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void parseStoryItemJSON()
    {
        JSONObject jsonObject = null;
        try
        {
            jsonObject = new JSONObject(storyItemsQueryResults);
            JSONArray result = jsonObject.getJSONArray(AppConfiguration.TAG_JSON_ARRAY);

            for(int x = 0; x < result.length(); x ++)
            {
                JSONObject jo = result.getJSONObject(x);
                int id = jo.getInt(AppConfiguration.TAG_ID);
                String fileName = jo.getString(AppConfiguration.TAG_FILE).replace("\"","");;
                String title = jo.getString(AppConfiguration.TAG_ITEM_TITLE);
                String caption = jo.getString(AppConfiguration.TAG_CAPTION);
                StoryItemDetails storyItemDetails = new StoryItemDetails(fileName, title, caption);
                ArrayList<CustomMapMarker> mapMarkers = omekaDataItems;
                for(CustomMapMarker marker: omekaDataItems)
                {
                    if(marker.getId() == id)
                    {
                        marker.addFileToMarker(storyItemDetails);
                    }
                }
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

    }

    private void getStoryItems()
    {
        class StoryItems extends AsyncTask<Void,Void,String>
        {
            ProgressDialog loading;
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Fetching...","Wait...",false,false); //showActivity works in fragments, getContext not so much
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);

                loading.dismiss();
                storyItemsQueryResults = s;
                parseStoryItemJSON();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                SqlRequestHandler rh = new SqlRequestHandler();
                String s = rh.sendGetRequest(AppConfiguration.URL_GET_STORYITEMS);
                return s;
            }
        }
        StoryItems ge = new StoryItems();
        ge.execute();
    }

    private void getHomeData()
    {
        class HomeData extends AsyncTask<Void,Void,String>
        {
            ProgressDialog loading;
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Fetching...","Wait...",false,false); //showActivity works in fragments, getContext not so much
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);

                loading.dismiss();
                homeQueryResults = s;
                parseHomeItemData();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                SqlRequestHandler rh = new SqlRequestHandler();
                String s = rh.sendGetRequest(AppConfiguration.URL_GET_HOMEITEMS);
                return s;
            }
        }
        HomeData ge = new HomeData();
        ge.execute();
    }

    private void parseHomeItemData()
    {
        JSONObject jsonObject = null;
        try
        {
            jsonObject = new JSONObject(homeQueryResults);
            JSONArray result = jsonObject.getJSONArray(AppConfiguration.TAG_JSON_ARRAY);

            for(int x = 0; x < result.length(); x ++)
            {
                JSONObject jo = result.getJSONObject(x);
                this.homeQueryResults = dealWithEscapeChars(jo.getString(AppConfiguration.TAG_ABOUT).replace("\"",""));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        this.setInitialNavgationTab(); //this function is placed here so that if the query isnt finished, the first run will make sure it is finished before setting the initial view
    }

    private String dealWithEscapeChars(String inString)
    {
        String retString = inString
                .replace("\"","") //the replace removed the "" around the title as returned by json
                .replaceAll("(\\\\r)?\\\\n", System.getProperty(AppConfiguration.FORMAT_LINE_SEPARATOR))
                .replace("\\t",  ""); //deal with tab
        return retString;
    }
}
