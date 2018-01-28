package org.floodexplorer.floodexplorer.Activities.StoryTab;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.maps.android.MarkerManager;

import org.floodexplorer.floodexplorer.Activities.MainActivity;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomClusterManager;
import org.floodexplorer.floodexplorer.SupportingFiles.AppConfiguration;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.R;


public class StoryTabMap extends Fragment implements OnMapReadyCallback
{
    private GoogleMap googleMap;
    private CustomClusterManager<CustomMapMarker> mClusterManager;
    private CustomMapMarker customMapMarker;
    private Context context;


    public static StoryTabMap newInstance(CustomMapMarker marker)
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConfiguration.BUNDLE_TAG_CUSTOM_MAP_MARKER, marker);

        StoryTabMap storyTabMap = new StoryTabMap();
        storyTabMap.setArguments(bundle);
        return storyTabMap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.readArgumentsBundle(getArguments());
        View view = inflater.inflate(R.layout.story_tab_map, container, false);

        this.context = view.getContext();
        final SupportMapFragment myMAPF = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        myMAPF.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;
        this.initMap();
    }

    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

    private void readArgumentsBundle(Bundle bundle)
    {
        if(bundle != null)
        {
            this.customMapMarker = bundle.getParcelable(AppConfiguration.BUNDLE_TAG_CUSTOM_MAP_MARKER);
        }
    }

    private void initMap()
    {
        try
        {
            this.googleMap.setMyLocationEnabled(true);
        }
        catch (SecurityException e)
        {
            Log.e("StoryTabMap", e.getMessage());
        }

        //set the map to what the initial settings for FloodExplorer.org are, this should be placed in a settings file at the least not hardcoded here...
        googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(customMapMarker.getPosition() , ((float) customMapMarker.getZoom())) ); //set initial map zoom and location
        this.initClusterManager();
    }

    private void initClusterManager()
    {
        MarkerManager markerManager = new MarkerManager(googleMap);
        markerManager.newCollection("collection");
        this.mClusterManager = new CustomClusterManager(getActivity().getApplicationContext(), googleMap, markerManager, customMapMarker,  ((MainActivity) context).getSupportFragmentManager());
    }
}