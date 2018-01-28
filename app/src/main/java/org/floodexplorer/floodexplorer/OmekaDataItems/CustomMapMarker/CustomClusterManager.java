package org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import org.floodexplorer.floodexplorer.Activities.StoryTab.StoryTabFragment;
import org.floodexplorer.floodexplorer.OmekaDataItems.Adapters.MapPinInfoAdapter;
import org.floodexplorer.floodexplorer.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mgilge on 11/14/17.
 */

public class CustomClusterManager<ClusterItem> extends ClusterManager implements Serializable
{

    private LatLng myDest;
    private Marker selectedMarker;
    private GoogleMap googleMap;
    private Context context;
    private CustomMapMarker customMapMarker;
    private CustomClusterRenderer customClusterRenderer;
    private FragmentManager fragmentManager;
    private HashMap<String, CustomMapMarker> omekaDataMap;
    private String title ="";
    private MarkerManager markerManager;
    private GoogleMap.OnInfoWindowClickListener onInfoWindowClickListener;
    private MapPinInfoAdapter mapPinInfoAdapter;

    public CustomClusterManager(Context context, GoogleMap map, MarkerManager markerManager)
    {
        super(context, map, markerManager);
        this.context = context;
        this.googleMap = map;
        this.customClusterRenderer = new CustomClusterRenderer(context, googleMap, this);
        this.customClusterRenderer.setMinClusterSize(2);
        this.setRenderer(customClusterRenderer); //see CustomColorRender class for more, this is used to control cluster icon
        this.markerManager = markerManager;

        this.initMap();
    }

    public CustomClusterManager(Context context, GoogleMap map, MarkerManager markerManager, CustomMapMarker customMapMarker, FragmentManager fragmentManager)
    {
        this(context, map, markerManager);
        this.customMapMarker = customMapMarker;
        this.fragmentManager = fragmentManager;
        this.setRenderer(customClusterRenderer); //see CustomColorRender class for more, this is used to control cluster icon
        this.populateMapWithoutInfoHandler();
    }


    public CustomClusterManager(Context context, GoogleMap map, MarkerManager markerManager, HashMap<String, CustomMapMarker> omekaDataItems, FragmentManager fragmentManager, String title)
    {
        this(context, map, markerManager);
        this.omekaDataMap = omekaDataItems;
        this.fragmentManager = fragmentManager;
        this.title = title;
        this.setRenderer(customClusterRenderer); //see CustomColorRender class for more, this is used to control cluster icon
        this.populateMapWithInfoHandler();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LatLng getMyDest()
    {
        return myDest;
    }

    public Marker getSelectedMarker()
    {
        return selectedMarker;
    }

    public void setMyDest(LatLng myDest)
    {
        this.myDest = myDest;
    }


    //reinflate an infowindow if its selected on rotation
    public void displaySelectedMarkerWindow(String title)
    {
        CustomMapMarker customMapMarker = omekaDataMap.get(title);
        myDest = customMapMarker.getPosition();
        Marker marker = this.customClusterRenderer.getMarker(customMapMarker);
        if(marker != null)
        {
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            marker.showInfoWindow();
            selectedMarker = marker;
        }
    }

    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

    private void initMap()
    {
        this.googleMap.setOnCameraIdleListener(this);
        this.googleMap.setOnMarkerClickListener(this);

        UiSettings uiSettings = this.googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true); //adds zoom buttons on map
        uiSettings.setMapToolbarEnabled(true);

        try
        {
            this.googleMap.setMyLocationEnabled(true);
        }
        catch (SecurityException e)
        {
            Log.e("CustomClusterManager", e.getMessage());
        }
    }

    private void populateMapWithInfoHandler()
    {
        mapPinInfoAdapter = new MapPinInfoAdapter(LayoutInflater.from(context), omekaDataMap);
        this.addClickListeners();
        this.getMarkerCollection().setOnInfoWindowAdapter(mapPinInfoAdapter);
        googleMap.setInfoWindowAdapter(markerManager);
        this.addFloodPointsToMap();
        this.addMarkerInfoHandler();
    }

    private void populateMapWithoutInfoHandler()
    {
        mapPinInfoAdapter = new MapPinInfoAdapter(LayoutInflater.from(context), customMapMarker);
        this.addClickListeners();
        this.getMarkerCollection().setOnInfoWindowAdapter(mapPinInfoAdapter);
        googleMap.setInfoWindowAdapter(markerManager);
        this.addMarkerToMap();
        this.addMarkerInfoHandler();
    }

    private void addClickListeners()
    {

        this.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {
                if(selectedMarker != null)
                {
                    selectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    selectedMarker = null;
                }
            }
        });

        this.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<CustomMapMarker>()
        {
            @Override
            public boolean onClusterClick(final Cluster<CustomMapMarker> cluster)
            {

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            cluster.getPosition(), (float) Math.floor(googleMap
                                    .getCameraPosition().zoom + 1)), 300,
                            null);

                if(selectedMarker != null)
                {
                    try
                    {
                        selectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                        selectedMarker = null;
                    }
                    catch (Exception e)
                    {
                        selectedMarker = null;
                        //todo take care of the outlier of if someone clicks back after going to a story from a map pin, and then zooms out without clicking a different marker to the point the original marker is re-clustered...
                    }
                }
                return true;
            }
        });

        this.setOnClusterItemClickListener(new OnClusterItemClickListener() {
            @Override
            public boolean onClusterItemClick(com.google.maps.android.clustering.ClusterItem clusterItem)
            {
                myDest = new LatLng(clusterItem.getPosition().latitude, clusterItem.getPosition().longitude);
                try
                {
                    //myDest = clusterItem.getPosition();
                    if(selectedMarker != null)
                    {
                        selectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    }
                    if(clusterItem != null) //need to somehow check if its a marker or if its a cluster we are actually clicking...
                    {
                        myDest = clusterItem.getPosition();
                        Marker marker = customClusterRenderer.getMarker((CustomMapMarker)clusterItem);
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        selectedMarker = marker;
                    }
                }
                catch (Exception e)
                {
                    //todo work on handling clicks both for cluster and indicating icon clicked, this is a temp fixish
                    selectedMarker = null;
                    e.getStackTrace();
                }

                return false;
            }
        });

    }

    private void addFloodPointsToMap()
    {

        for(Map.Entry<String, CustomMapMarker> entry : omekaDataMap.entrySet())
        {
            CustomMapMarker customMarker = entry.getValue();
            this.addItem(customMarker);
        }
        this.cluster();
    }


    private void addMarkerInfoHandler()
    {
         onInfoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                try
                {
                    CustomMapMarker mapMarker = findMapMarker(marker);
                    Fragment fragment = StoryTabFragment.newInstance(mapMarker);
                    FragmentTransaction ft = fragmentManager.beginTransaction().addToBackStack(null);
                    ft.replace(R.id.frameLayout, fragment);
                    ft.commit();
                }
                catch (Exception e)
                {
                    Log.e("CustomClusterManager", e.getMessage());
                }
            }
        };

        googleMap.setOnInfoWindowClickListener(onInfoWindowClickListener);
    }

    private CustomMapMarker findMapMarker(Marker marker)
    {
        String title = marker.getTitle();
        CustomMapMarker retMarker = null;
        for(Map.Entry<String, CustomMapMarker> entry : omekaDataMap.entrySet())
        {
            CustomMapMarker customMarker = entry.getValue();
            if(customMarker.getTitle().equalsIgnoreCase(title))
            {
                retMarker = customMarker;
            }
        }
        return retMarker;
    }

    private void addMarkerToMap()
    {
        this.addItem(customMapMarker);
        googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(customMapMarker.getPosition() , ((float) customMapMarker.getZoom())) ); //set initial map zoom and location
        this.cluster();
    }
}
