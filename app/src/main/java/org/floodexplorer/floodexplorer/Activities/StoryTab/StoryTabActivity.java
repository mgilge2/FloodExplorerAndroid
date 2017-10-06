package org.floodexplorer.floodexplorer.Activities.StoryTab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomClusterRenderer;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.PinInfoViewAdapter;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.StoryItemDetails;
import org.floodexplorer.floodexplorer.R;

import java.util.ArrayList;

/**
 * Created by mgilge on 7/18/17.
 */

public class StoryTabActivity extends Fragment implements OnMapReadyCallback
{
    private ClusterManager<CustomMapMarker> mClusterManager;
    private CustomMapMarker mapMarker;
    private TabHost host;
    private TextView text1;
    private GridView gridView;
    private ArrayList<StoryItemDetails> storyItemDetailes;
    private GoogleMap googleMap;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_story_tab, container, false);
        final SupportMapFragment myMAPF = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        myMAPF.getMapAsync(this);
        return view;
    }


    public StoryTabActivity(CustomMapMarker marker)
    {
        this.mapMarker = marker;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Menu 1");

        this.host = (TabHost) view.findViewById(R.id.tabHost);
        this.text1 = (TextView) view.findViewById(R.id.tab1Txt);
        this.gridView = (GridView) view.findViewById(R.id.gridview);

        this.initTabHost();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;
        this.initMap();
    }

    //Private Implementation....

    private void initTabHost()
    {
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Story");
        this.text1.setText(this.mapMarker.getStoryText());
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Pictures");
        storyItemDetailes = mapMarker.getFileList();
        buildPicArrayAdapter();
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Map");
        host.addTab(spec);
    }

    private void buildPicArrayAdapter()
    {
        PicRayAdapter picRayAdapter = new PicRayAdapter(this.getContext(), storyItemDetailes);
        gridView.setAdapter(picRayAdapter);
    }

    private void initMap()
    {
        this.googleMap.setMyLocationEnabled(true);
        UiSettings uiSettings = this.googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true); //adds zoom buttons on map

        //set the map to what the initial settings for FloodExplorer.org are, this should be placed in a settings file at the least not hardcoded here...
        googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(47.47398, -118.5489564) , 6.8f) ); //set initial map zoom and location
        this.initClusterManager();
        this.googleMap.setOnCameraIdleListener(mClusterManager);
        this.googleMap.setOnMarkerClickListener(mClusterManager);
        this.addFloodPointsToMap();
       // this.addChangeButtonHandler();
       // this.addMarkerInfoHandler();
    }

    private void initClusterManager()
    {
        this.mClusterManager = new ClusterManager<>(getActivity().getApplicationContext(), googleMap, new MarkerManager(googleMap)
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {

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
        this.mClusterManager.addItem(mapMarker);
        googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(47.47398, -118.5489564) , 6.8f) ); //set initial map zoom and location
    }
}
