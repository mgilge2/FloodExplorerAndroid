package org.floodexplorer.floodexplorer.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.AppConfiguration;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMarkerListBuilder;
import org.floodexplorer.floodexplorer.OmekaRestLoader;
import org.floodexplorer.floodexplorer.R;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<String>>
{
    private String homeRestResults;
    private ArrayList<CustomMapMarker> omekaDataItems; //this is our model for MVC....
    private BottomNavigationView navigation;
    private boolean restFinished;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.customizeActionBar();
        if(omekaDataItems == null);
        this.omekaDataItems = new ArrayList<CustomMapMarker>();
        this.navigation = (BottomNavigationView) findViewById(R.id.navigation);
        this.initBottomNavigationView();
        this.dealWithSavedInstanceState(savedInstanceState);
        if(savedInstanceState == null)
        {
            getSupportLoaderManager().initLoader(0,null,this);
        }
    }

    //For handling the back button when pressed within a fragment....
    @Override
    public void onBackPressed()
    {
        if (getFragmentManager().getBackStackEntryCount() > 0)
        {
            getFragmentManager().popBackStack();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public Loader<ArrayList<String>> onCreateLoader(int id, Bundle args)
    {
        navigation.setVisibility(View.GONE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, LoadingScreenFragment.newInstance());
        transaction.commitAllowingStateLoss();
        return new OmekaRestLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<String>> loader, ArrayList<String> data)
    {
        CustomMarkerListBuilder customMarkerListBuilder = new CustomMarkerListBuilder();
        this.homeRestResults = data.get(3); //make parse be in markerbuilder... rename marker builder to be more appropriate...
        this.homeRestResults = customMarkerListBuilder.parseRestSimplePages(homeRestResults);
        this.omekaDataItems = customMarkerListBuilder.buildOmekaDataItems(data);
        this.setInitialNavgationTab();
        this.restFinished = true;
        navigation.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<String>> loader)
    {
       // loader = null;
    }

    private void dealWithSavedInstanceState(Bundle savedInstanceState)
    {
        if(savedInstanceState != null)
        {
            restFinished = savedInstanceState.getBoolean("restFinished");
            if(!restFinished)
            {
                getSupportLoaderManager().initLoader(0,null,this);
            }
            else
            {
                this.omekaDataItems = savedInstanceState.getParcelableArrayList(AppConfiguration.BUNDLE_TAG_OMEKA_DATA_ITEMS);
                this.homeRestResults = savedInstanceState.getString(AppConfiguration.BUNDLE_TAG_HOME_RESULTS);
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        int selected = navigation.getSelectedItemId();
        outState.putInt(AppConfiguration.BUNDLE_TAG_NAVIGATION_LOCATION, selected);
        outState.putParcelableArrayList(AppConfiguration.BUNDLE_TAG_OMEKA_DATA_ITEMS, omekaDataItems);
        outState.putString(AppConfiguration.BUNDLE_TAG_HOME_RESULTS, homeRestResults);
        outState.putBoolean("restFinished", restFinished);
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            Fragment fragment = null;
            switch (item.getItemId())
            {
                case R.id.navigation_home:
                    fragment = HomeFragment.newInstance(homeRestResults);
                    break;
                case R.id.navigation_stories:
                    fragment = StoriesFragment.newInstance(omekaDataItems);
                    break;
                case R.id.navigation_map:
                    fragment = MapsFragment.newInstance(omekaDataItems);
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
        transaction.replace(R.id.frameLayout, HomeFragment.newInstance(homeRestResults));
        transaction.commitAllowingStateLoss();
    }
}
