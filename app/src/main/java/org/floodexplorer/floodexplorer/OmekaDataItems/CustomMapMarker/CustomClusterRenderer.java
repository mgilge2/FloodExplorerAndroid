package org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import org.floodexplorer.floodexplorer.R;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.HashMap;

/**
 * Created by mgilge on 7/19/17.
 */

public class CustomClusterRenderer extends DefaultClusterRenderer<CustomMapMarker>
{
    private GoogleMap googleMap;

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<CustomMapMarker> clusterManager)
    {
        super(context, map, clusterManager);
        this.googleMap = map;
    }

    // Return any color you want here. You can base it on clusterSize.
    @Override
    protected int getColor(int clusterSize)
    {
        //todo move these colors into the AppConfiguration file if we use them....
       if(clusterSize <= 5)
       {
           return Color.parseColor("#7CFC00");
       }
       else if(clusterSize <= 15)
       {
           return Color.parseColor("#32CD32");
       }
       else if(clusterSize <= 30)
       {
           return Color.parseColor("#00BFFF");
       }
       else
       {
           return Color.parseColor("#1E90FF");
       }
    }

    @Override
    protected void onBeforeClusterItemRendered(CustomMapMarker item, MarkerOptions markerOptions)
    {
        // Use this method to set your own icon for the markers
        super.onBeforeClusterItemRendered(item,markerOptions);
        BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW); //just set color this way
        markerOptions.icon(icon);
    }

    //Below will use a custom drawable instead of default
    /*
    @Override
    protected void onBeforeClusterRendered(Cluster<CustomMapMarker> cluster, MarkerOptions markerOptions)
    {
        // Use this method to set your own icon for the clusters or comment out for stock behavoir

       // final Drawable drawabable = getResources.getD
        mClusterIconGenerator.setBackground(ContextCompat.getDrawable(context, R.drawable.cluster_circle));
        mClusterIconGenerator.setTextAppearance(R.style.AppTheme_WhiteTextAppearance);
        final Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }
    */

}
