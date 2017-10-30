package org.floodexplorer.floodexplorer.Activities.StoryTab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import org.floodexplorer.floodexplorer.AppConfiguration;
import org.floodexplorer.floodexplorer.OmekaDataItems.Adapters.PinInfoViewAdapter;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomClusterRenderer;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.R;


public class StoryTabMap extends Fragment implements OnMapReadyCallback
{
    private GoogleMap googleMap;
    private ClusterManager<CustomMapMarker> mClusterManager;
    private CustomMapMarker customMapMarker;


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

    private void readArgumentsBundle(Bundle bundle)
    {
        if(bundle != null)
        {
            this.customMapMarker = bundle.getParcelable(AppConfiguration.BUNDLE_TAG_CUSTOM_MAP_MARKER);
        }
    }

    private void initMap()
    {
        this.googleMap.setMyLocationEnabled(true);
        UiSettings uiSettings = this.googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true); //adds zoom buttons on map

        //set the map to what the initial settings for FloodExplorer.org are, this should be placed in a settings file at the least not hardcoded here...
        googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(AppConfiguration.MAP_STARTING_POINT , ((float) customMapMarker.getZoom())) ); //set initial map zoom and location
        this.initClusterManager();
        this.googleMap.setOnCameraIdleListener(mClusterManager);
        this.googleMap.setOnMarkerClickListener(mClusterManager);
        this.addFloodPointsToMap();
    }

    private void initClusterManager()
    {
        this.mClusterManager = new ClusterManager<>(getActivity().getApplicationContext(), googleMap, new MarkerManager(googleMap)
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                //   if(prevMarker != null)
                // {
                //      prevMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                //  }
                //   if(!marker.equals(prevMarker))
                //  {
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                //      prevMarker = marker;
                //     }
                //  prevMarker = marker;
                return super.onMarkerClick(marker);
            }
        });
        mClusterManager
                .setOnClusterClickListener(new ClusterManager.OnClusterClickListener<CustomMapMarker>()
                {
                    @Override
                    public boolean onClusterClick(final Cluster<CustomMapMarker> cluster)
                    {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                cluster.getPosition(), (float) Math.floor(googleMap
                                        .getCameraPosition().zoom + 1)), 300,
                                null);
                        return true;
                    }
                });

        //this shouldnt be needed
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<CustomMapMarker>() {
            @Override
            public boolean onClusterItemClick(CustomMapMarker customMapMarker)
            {
                //dest = new LatLng(customMapMarker.getPosition().latitude, customMapMarker.getPosition().longitude);
                return false;
            }
        });

        mClusterManager.setRenderer(new CustomClusterRenderer(getContext(), googleMap, mClusterManager)); //see CustomColorRender class for more, this is used to control cluster icon

        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new PinInfoViewAdapter(LayoutInflater.from(getContext())));
        googleMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
    }

    private void addFloodPointsToMap()
    {
        this.mClusterManager.addItem(customMapMarker);
        googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(customMapMarker.getPosition() , ((float) customMapMarker.getZoom())) ); //set initial map zoom and location
    }
}