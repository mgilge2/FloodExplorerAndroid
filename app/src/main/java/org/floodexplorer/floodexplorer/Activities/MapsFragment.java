package org.floodexplorer.floodexplorer.Activities;


import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.MarkerManager;

import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomClusterManager;
import org.floodexplorer.floodexplorer.SupportingFiles.AppConfiguration;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.SupportingFiles.POJO.Route.RouteExample;
import org.floodexplorer.floodexplorer.R;
import org.floodexplorer.floodexplorer.SupportingFiles.RetrofitMapsRoute;

import java.util.ArrayList;
import java.util.HashMap;
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
    private CustomClusterManager<CustomMapMarker> mClusterManager;
    private Button changeButton;
    private Button routeButton;
    private LatLng origin;
    private Polyline line;
    private boolean firstRun = true;
    private int selectedMapType = GoogleMap.MAP_TYPE_NORMAL;
    private String selectedMarkerTitle = "";
    private Context  context;
    private  HashMap<String, CustomMapMarker> omekaDataMap;
    private Runnable runnable;
    private Handler handler;
    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        this.handler = new Handler();
        runnable = new Runnable()
        {
            public void run()
            {
                mClusterManager.displaySelectedMarkerWindow(selectedMarkerTitle);
            }
        };

        final SupportMapFragment myMAPF = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        myMAPF.getMapAsync(this);

        this.context = view.getContext();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        this.setCurrentLocation();
        this.changeButton = (Button) view.findViewById(R.id.btnChangeMap);
        this.routeButton = (Button) view.findViewById(R.id.btnDriveWalk);
        this.omekaDataMap = MainActivity.getOmekaDataMap();
        routeButton.setOnClickListener(new View.OnClickListener()
        {
                @Override
                public void onClick(View v)
                {
                    if(origin != null)
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
                    }
                }
            });
        return view;
    }



    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        if(this.googleMap == null)
        {
            this.googleMap = googleMap;
            this.initNewMap();
        }
        else
        {
            this.addChangeButtonHandler();
        }
    }


    //Below two methods have to do with saving the selected tab when rotating...
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null)
        {
            firstRun = savedInstanceState.getBoolean("firstRun");
            selectedMapType = savedInstanceState.getInt("selectedMapType");
            selectedMarkerTitle = savedInstanceState.getString("selectedMarkerTitle");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("firstRun", firstRun);
        savedInstanceState.putInt("selectedMapType", selectedMapType);

        if(mClusterManager.getSelectedMarker() != null)
        {
            savedInstanceState.putString("selectedMarkerTitle", this.mClusterManager.getSelectedMarker().getTitle());
        }
        else
        {
            savedInstanceState.putString("selectedMarkerTitle", "");
        }
    }

    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

    private void initNewMap()
    {
        if(firstRun)
        {
            googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(47.47398, -118.5489564) , 6.8f) ); //set initial map zoom and location
            firstRun = false;
        }
        initExistingMap();
    }

    private void initExistingMap()
    {
        try
        {

            this.googleMap.setMapType(selectedMapType);
            this.initClusterManager();
            this.addChangeButtonHandler();
        }
        catch(Exception e)
        {
            e.getStackTrace();
        }
    }

    private void initClusterManager()
    {
        MarkerManager markerManager = new MarkerManager(googleMap);
        markerManager.newCollection("collection");

        this.mClusterManager = new CustomClusterManager(getActivity().getApplicationContext(), googleMap, markerManager, omekaDataMap,  ((MainActivity) context).getSupportFragmentManager(), selectedMarkerTitle);
        if(!(selectedMarkerTitle.equals("")))
        {
            mClusterManager.setTitle(selectedMarkerTitle);
            handler.postDelayed(runnable, 200);
        }
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
                        selectedMapType = GoogleMap.MAP_TYPE_SATELLITE;
                        break;
                    case GoogleMap.MAP_TYPE_SATELLITE:
                        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        selectedMapType = GoogleMap.MAP_TYPE_TERRAIN;
                        break;
                    case GoogleMap.MAP_TYPE_TERRAIN:
                        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        selectedMapType = GoogleMap.MAP_TYPE_HYBRID;
                        break;
                    default:
                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        selectedMapType = GoogleMap.MAP_TYPE_NORMAL;
                }

            }
        });

    }

   //below is for addying polyline routing via the button click...
    //todo need to save mydest in clustermanager for rotations...
   private void buildRoute(String type)
   {
       this.setCurrentLocation();

       if(mClusterManager.getMyDest() == null)
       {
           mClusterManager.setMyDest(AppConfiguration.MAP_STARTING_POINT);
       }

       String url = AppConfiguration.URL_GOOGLE_MAPS_ROUTING;

       Retrofit retrofit = new Retrofit.Builder()
               .baseUrl(url)
               .addConverterFactory(GsonConverterFactory.create())
               .build();

       RetrofitMapsRoute service = retrofit.create(RetrofitMapsRoute.class);

       Call<RouteExample> call = service.getDistanceDuration(AppConfiguration.APP_LANGUAGE , origin.latitude + "," + origin.longitude, mClusterManager.getMyDest().latitude + "," + mClusterManager.getMyDest().longitude, type);

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
                       displayRouteToast(distance,time);
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

    private void displayRouteToast(String distance, String time)
    {
        Toast.makeText(getActivity(), "Distance: " + distance + ", Duration: " + time, Toast.LENGTH_LONG).show();
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
        try
        {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>()
                    {
                        @Override
                        public void onSuccess(Location location)
                        {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                origin = new LatLng(location.getLatitude(), location.getLongitude());
                            }
                        }
                    });

        }
        catch (SecurityException e)
        {
            //todo handle this...it should be unreachable
        }
    }
}
