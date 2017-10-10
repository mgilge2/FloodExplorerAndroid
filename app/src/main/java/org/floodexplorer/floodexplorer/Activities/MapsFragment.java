package org.floodexplorer.floodexplorer.Activities;


import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import org.floodexplorer.floodexplorer.Activities.StoryTab.StoryTabActivity;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomClusterRenderer;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.PinInfoViewAdapter;
import org.floodexplorer.floodexplorer.OmekaDataItems.POJO.Route.RouteExample;
import org.floodexplorer.floodexplorer.R;
import org.floodexplorer.floodexplorer.OmekaDataItems.RetrofitMapsRoute;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback
{
    private GoogleMap googleMap;
    private ArrayList<CustomMapMarker> omekaDataItems;
    private ClusterManager<CustomMapMarker> mClusterManager;
    private Marker prevMarker;
    private Button changeButton;
    private Button routeButton;
    private LatLng myDest;
    private LatLng origin;
    private Location location;
    private Polyline line;
    private TextView ShowDistanceDuration;

    public static MapsFragment newInstance(ArrayList<CustomMapMarker> omekaDataItems)
    {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("omekaDataList", omekaDataItems);

        MapsFragment mapsFragment = new MapsFragment();
        mapsFragment.setArguments(bundle);
        return mapsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        readArgumentsBundle(getArguments());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        this.setNavigationTitle();
        final SupportMapFragment myMAPF = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        myMAPF.getMapAsync(this);
        this.ShowDistanceDuration = (TextView) view.findViewById(R.id.txtDist);
        this.changeButton = (Button) view.findViewById(R.id.btnChangeMap);
        this.routeButton = (Button) view.findViewById(R.id.btnDriveWalk);
        routeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Button button = (Button) v;
                switch (button.getText().toString())
                {
                    case "Driving":
                        buildRoute("driving");
                        button.setText("Walking");
                        break;
                    case "Walking":
                        buildRoute("walking");
                        button.setText("Driving");
                        break;
                }
                //mapLogic.buildRoute("walking");
            }
        });
        setRetainInstance(true); //this is why rotation is currently working it might not be the best way to do this
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
            this.omekaDataItems = bundle.getParcelableArrayList("omekaDataList");
        }
    }

    private void setNavigationTitle()
    {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        View view = actionBar.getCustomView();
        TextView textView = (TextView) view.findViewById(R.id.navTitleTxt);
        textView.setText("Map");
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
        this.addChangeButtonHandler();
        this.addMarkerInfoHandler();
    }

    private void initClusterManager()
    {
        this.mClusterManager = new ClusterManager<>(getActivity().getApplicationContext(), googleMap, new MarkerManager(googleMap)
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                myDest = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                try
                {
                    if(prevMarker != null)
                    {
                        prevMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    }
                    if(marker != null)
                    {
                        myDest = marker.getPosition();
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        prevMarker = marker;
                    }
                }
                catch (Exception e)
                {
                    //todo work on handling clicks both for cluster and indicating icon clicked, this is a temp fixish
                    prevMarker = null;
                    e.getStackTrace();
                }
                // marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
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

        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new PinInfoViewAdapter(LayoutInflater.from(getContext()), omekaDataItems));
        googleMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
    }

    private void addFloodPointsToMap()
    {
        for (CustomMapMarker coord : omekaDataItems)
        {
            this.mClusterManager.addItem(coord);
        }
        googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(47.47398, -118.5489564) , 6.8f) ); //set initial map zoom and location
    }

    private void addChangeButtonHandler()
    {
        changeButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                switch (googleMap.getMapType())
                {
                    case GoogleMap.MAP_TYPE_NORMAL:
                        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case GoogleMap.MAP_TYPE_SATELLITE:
                        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    case GoogleMap.MAP_TYPE_TERRAIN:
                        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    default:
                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }

            }
        });
    }

    private void addMarkerInfoHandler()
    {
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
        {
            @Override
            public void onInfoWindowClick(Marker marker)
            {

                CustomMapMarker mapMarker = findMapMarker(marker);
                Fragment fragment = StoryTabActivity.newInstance(mapMarker);
                FragmentManager fm =  getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frameLayout, fragment);
                ft.commit();
            }
        });
    }

    private CustomMapMarker findMapMarker(Marker marker)
    {
        String title = marker.getTitle();
        CustomMapMarker retMarker = null;
        for(CustomMapMarker customMarker : omekaDataItems )
        {
            if(customMarker.getTitle().equalsIgnoreCase(title))
            {
                retMarker = customMarker;
            }
        }
        return retMarker;
    }

   //below is for addying polyline routing via the button click...
   private void buildRoute(String type)
   {
       this.setCurrentLocation();

       if(myDest == null)
       {
           myDest  = new LatLng(47.473985, -118.0);
       }

       String url = "https://maps.googleapis.com/maps/";

       Retrofit retrofit = new Retrofit.Builder()
               .baseUrl(url)
               .addConverterFactory(GsonConverterFactory.create())
               .build();

       RetrofitMapsRoute service = retrofit.create(RetrofitMapsRoute.class);

       Call<RouteExample> call = service.getDistanceDuration("english", origin.latitude + "," + origin.longitude, myDest.latitude + "," + myDest.longitude, type);

       call.enqueue(new Callback<RouteExample>()
       {
           @Override
           public void onResponse(Response<RouteExample> response, Retrofit retrofit)
           {

               try
               {
                   //Remove previous line from map
                   if (line != null)
                   {
                       line.remove();
                   }
                   // This loop will go through all the results and add marker on each location.
                   for (int i = 0; i < response.body().getRoutes().size(); i++)
                   {
                       String distance = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();
                       String time = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();
                       ShowDistanceDuration.setText("Distance:" + distance + ", Duration:" + time);
                       String encodedString = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                       List<LatLng> list = decodePoly(encodedString);
                       line = googleMap.addPolyline(new PolylineOptions()
                               .addAll(list)
                               .width(10)
                               .color(Color.RED)
                               .geodesic(true)
                       );
                   }
               }
               catch (Exception e)
               {
                   Log.d("onResponse", "There is an error");
                   e.printStackTrace();
               }
           }

           @Override
           public void onFailure(Throwable t)
           {
               Log.d("onFailure", t.toString());
           }
       });
   }

    private List<LatLng> decodePoly(String encoded)
    {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len)
        {
            int b, shift = 0, result = 0;
            do
            {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do
            {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    private void setCurrentLocation()
    {
        this.location = this.googleMap.getMyLocation();
        origin = new LatLng(location.getLatitude(),  location.getLongitude());
    }
}
