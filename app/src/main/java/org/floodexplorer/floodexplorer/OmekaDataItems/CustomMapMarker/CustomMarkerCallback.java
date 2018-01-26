package org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker;

import android.util.Log;

import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;

/**
 * Created by mgilge on 10/7/17.
 */

public class CustomMarkerCallback implements Callback
{
    private Marker marker;

    public CustomMarkerCallback(Marker marker)
    {
        this.marker = marker;
    }

    @Override
    public void onSuccess()
    {
        if (marker != null && marker.isInfoWindowShown())
        {
            marker.hideInfoWindow();
            marker.showInfoWindow();
        }
    }

    @Override
    public void onError()
    {
        Log.d("TAG", "CustomMarkerCallback - onError");
    }
}
