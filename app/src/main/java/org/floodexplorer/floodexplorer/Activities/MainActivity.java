package org.floodexplorer.floodexplorer.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.SupportingFiles.AppConfiguration;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMarkerListBuilder;
import org.floodexplorer.floodexplorer.SupportingFiles.OmekaRestLoader;
import org.floodexplorer.floodexplorer.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mehdi.sakout.aboutpage.AboutPage;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<String>>
{
    //This is the model for MVC....
    private static HashMap<String, CustomMapMarker> omekaDataMap;

    private String homeRestResults;
    private BottomNavigationView navigation;
    private boolean restFinished;
    private static final int locationAccess = 0;
    private static final int internetAccess = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestPerms();
        setContentView(R.layout.activity_main);
        this.customizeActionBar();
        this.omekaDataMap = new HashMap<String, CustomMapMarker>();
        this.navigation = (BottomNavigationView) findViewById(R.id.navigation);
        this.initBottomNavigationView();
        this.dealWithSavedInstanceState(savedInstanceState);
        if(savedInstanceState == null)
        {
            getSupportLoaderManager().initLoader(0,null,this);
        }
    }

    //just used for crash testing....
    @Override
    protected void onPause()
    {
        try
        {
            super.onPause();
        }
        catch (Exception e)
        {
            StackTraceElement element = e.getStackTrace()[0];
            e.printStackTrace();
        }
        super.onPause();
    }



    //For handling the back button when pressed within a fragment....
    @Override
    public void onBackPressed()
    {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            getSupportFragmentManager().popBackStack();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public Loader<ArrayList<String>> onCreateLoader(int id, Bundle args)
    {
        if(!isNetworkAvailable())
        {
            noInternetDialog();
        }

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
        this.omekaDataMap = customMarkerListBuilder.buildOmekaDataMap(data);
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
                this.omekaDataMap = (HashMap<String, CustomMapMarker>) savedInstanceState.getSerializable(AppConfiguration.BUNDLE_TAG_OMEKA_DATA_ITEMS);
                this.homeRestResults = savedInstanceState.getString(AppConfiguration.BUNDLE_TAG_HOME_RESULTS);
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        try
        {
            super.onSaveInstanceState(outState);
            int selected = navigation.getSelectedItemId();
            outState.putInt(AppConfiguration.BUNDLE_TAG_NAVIGATION_LOCATION, selected);
            outState.putSerializable(AppConfiguration.BUNDLE_TAG_OMEKA_DATA_ITEMS, omekaDataMap);
            outState.putString(AppConfiguration.BUNDLE_TAG_HOME_RESULTS, homeRestResults);
            outState.putBoolean("restFinished", restFinished);
        }
        catch (Exception e)
        {
            e.getStackTrace();
        }
    }

    public static HashMap<String, CustomMapMarker> getOmekaDataMap()
    {
        return omekaDataMap;
    }


    // Menu...with about.....should add setting to it for things like text size in the app
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.aboutMenuItem)
        {
           AboutDialog aboutDialog = new AboutDialog();
           aboutDialog.show(getSupportFragmentManager(), "About Dialog");
           return true;
        }
        return super.onOptionsItemSelected(item);
    }

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

        //here we can set the color
        actionBar.setBackgroundDrawable(AppConfiguration.ActionBarColor);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_action_bar_layout); //use custom XML layout for ActionBar
        View view = getSupportActionBar().getCustomView();
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
                    fragment = new StoriesFragment();
                    break;
                case R.id.navigation_map:
                    fragment = new MapsFragment();
                    break;
            }

            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); //clear the back stack

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

    //ADDED PERMISSIONS BY TEJ IN CASE NEED TO CHANGE

    private void noInternetDialog()
    {
        AlertDialog.Builder internetDialog = new AlertDialog.Builder(this);
        internetDialog.setMessage("The device is not connected to the Internet \nPlease connect to the internet and reopen Flood Explorer").setTitle("No Internet Connection");
        internetDialog.setNeutralButton("Settings", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                System.exit(0);
            }
        });
        internetDialog.setNegativeButton("Exit", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                System.exit(0);
            }
        });
        AlertDialog dialog = internetDialog.create();
        dialog.show();
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void requestPerms()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},locationAccess);

        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},internetAccess);
        }
    }

    //this method is only for testing purposes
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == locationAccess)
        {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //Toast.makeText(this, "LOCATION GRANTED", Toast.LENGTH_LONG).show();
            }
            else
            {
                //Toast.makeText(this, "location permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == internetAccess)
        {
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                //Toast.makeText(this, "INTERNET GRANTED", Toast.LENGTH_LONG).show();
            }
            else
            {
                //Toast.makeText(this, "Internet permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //END PERMISSIONS BY TEJ IN CASE NEED TO CHANGE
}
