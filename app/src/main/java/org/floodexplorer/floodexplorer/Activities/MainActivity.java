package org.floodexplorer.floodexplorer.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.StoryItemDetails;
import org.floodexplorer.floodexplorer.OmekaDataItems.OmekaDataItems;
import org.floodexplorer.floodexplorer.OmekaDataItems.SQL.SqlConfig;
import org.floodexplorer.floodexplorer.OmekaDataItems.SQL.SqlRequestHandler;
import org.floodexplorer.floodexplorer.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.support.design.R.styleable.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private String queryResults;
    private String storyItemsQueryResults;
    private OmekaDataItems<CustomMapMarker> omekaDataItems;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            Fragment fragment = null;
            switch (item.getItemId())
            {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.navigation_stories:
                    StoriesFragment storiesFragment = new StoriesFragment();
                    storiesFragment.setOmekaDataItems(omekaDataItems);
                    fragment = storiesFragment;
                    break;
                case R.id.navigation_map:
                    MapsFragment mapsFragment = new MapsFragment();
                    mapsFragment.setOmekaDataItems(omekaDataItems);
                    fragment = mapsFragment;
                    break;
                case R.id.navigation_about:
                    fragment = new AboutFragment();
                    break;
            }
            try
            {
                final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentFrame, fragment);
                transaction.commit();
                //return true;
            }
            catch (Exception e)
            {
                //toast for error?
                System.out.println(e.getCause());
            }
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        omekaDataItems = new OmekaDataItems();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        this.getLocations(); //perform db query...
        this.getStoryItems();
        this.setInitialNavgationTab();
    }

    //Private implementation......

    private void setInitialNavgationTab()
    {
        MapsFragment fragment = new MapsFragment();
        fragment.setOmekaDataItems(omekaDataItems);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentFrame, fragment);
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
                String s = rh.sendGetRequest(SqlConfig.URL_GET_LOCATIONS);
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
            JSONArray result = jsonObject.getJSONArray(SqlConfig.TAG_JSON_ARRAY);

            for(int x = 0; x < result.length(); x ++)
            {
                JSONObject jo = result.getJSONObject(x);
                double latitude = jo.getDouble(SqlConfig.TAG_LAT);
                double longitude = jo.getDouble(SqlConfig.TAG_LONG);
                String title = this.dealWithEscapeChars(jo.getString(SqlConfig.TAG_TITLE));
                String text = this.dealWithEscapeChars(jo.getString(SqlConfig.TAG_TEXT));
                double zoom = jo.getInt(SqlConfig.TAG_ZOOM);
                int id = jo.getInt(SqlConfig.TAG_ID);
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
            JSONArray result = jsonObject.getJSONArray(SqlConfig.TAG_JSON_ARRAY);

            for(int x = 0; x < result.length(); x ++)
            {
                JSONObject jo = result.getJSONObject(x);
                int id = jo.getInt(SqlConfig.TAG_ID);
                String fileName = jo.getString(SqlConfig.TAG_FILE).replace("\"","");;
                String title = jo.getString(SqlConfig.TAG_ITEM_TITLE);
                String caption = jo.getString(SqlConfig.TAG_CAPTION);
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
                String s = rh.sendGetRequest(SqlConfig.URL_GET_STORYITEMS);
                return s;
            }
        }
        StoryItems ge = new StoryItems();
        ge.execute();
    }

    private String dealWithEscapeChars(String inString)
    {
        String retString = inString
                .replace("\"","") //the replace removed the "" around the title as returned by json
                .replaceAll("(\\\\r)?\\\\n", System.getProperty("line.separator"))
                .replace("\\t",  ""); //deal with tab

        return retString;
    }

}
