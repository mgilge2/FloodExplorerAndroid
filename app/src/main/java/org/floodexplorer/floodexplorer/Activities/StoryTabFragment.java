package org.floodexplorer.floodexplorer.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TabHost;
import android.widget.TextView;

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
import org.floodexplorer.floodexplorer.OmekaDataItems.Adapters.PicRayAdapter;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomClusterRenderer;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.OmekaDataItems.Adapters.PinInfoViewAdapter;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.StoryItemDetails;
import org.floodexplorer.floodexplorer.R;

import java.util.ArrayList;

/**
 * Created by mgilge on 7/18/17.
 */

public class StoryTabFragment extends Fragment implements OnMapReadyCallback
{
    private ClusterManager<CustomMapMarker> mClusterManager;
    private CustomMapMarker mapMarker;
    private TabHost host;
    private TextView storyText;
    private TextView storyTitle;
    private GridView gridView;
    private ArrayList<StoryItemDetails> storyItemDetailes;
    private GoogleMap googleMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        this.readArgumentsBundle(getArguments());
        View view = inflater.inflate(R.layout.fragment_story_tab, container, false);
        final SupportMapFragment myMAPF = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        myMAPF.getMapAsync(this);
        setRetainInstance(true); //this is why rotation is currently working it might not be the best way to do this
        return view;
    }

    public static StoryTabFragment newInstance(CustomMapMarker marker)
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConfiguration.BUNDLE_TAG_CUSTOM_MAP_MARKER, marker);

        StoryTabFragment storyTabActivity = new StoryTabFragment();
        storyTabActivity.setArguments(bundle);
        return storyTabActivity;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(mapMarker.getTitle());

        this.host = (TabHost) view.findViewById(R.id.tabHost);
        this.storyText = (TextView) view.findViewById(R.id.storyBodyTxt);
        this.storyTitle = (TextView) view.findViewById(R.id.storyTitleTxt);
        this.gridView = (GridView) view.findViewById(R.id.gridview);
        setRetainInstance(true);
        this.initTabHost();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;
        this.initMap();
    }

    //Below two methods have to do with saving the selected tab when rotating...
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null)
        {
            this.host.setCurrentTab(savedInstanceState.getInt("selectedTab"));
            this.storyItemDetailes = (ArrayList<StoryItemDetails>)savedInstanceState.getSerializable("storyItemDetails");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("selectedTab", this.host.getCurrentTab());
        savedInstanceState.putSerializable("storyItemDetails", storyItemDetailes);
    }

    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

    private void readArgumentsBundle(Bundle bundle)
    {
        if(bundle != null)
        {
            this.mapMarker = bundle.getParcelable(AppConfiguration.BUNDLE_TAG_CUSTOM_MAP_MARKER);
        }
    }

    private void initTabHost()
    {
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Story");
        this.storyText.setText(this.mapMarker.getStoryText());
        this.storyTitle.setText(this.mapMarker.getTitle());
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
        googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(AppConfiguration.MAP_STARTING_POINT , ((float) mapMarker.getZoom())) ); //set initial map zoom and location
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
        this.mClusterManager.addItem(mapMarker);
        googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(mapMarker.getPosition() , ((float) mapMarker.getZoom())) ); //set initial map zoom and location
    }
}
