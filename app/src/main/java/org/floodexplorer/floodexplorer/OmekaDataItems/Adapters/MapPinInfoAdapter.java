package org.floodexplorer.floodexplorer.OmekaDataItems.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMarkerCallback;
import org.floodexplorer.floodexplorer.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mgilge on 7/21/17.
 */

public class MapPinInfoAdapter implements GoogleMap.InfoWindowAdapter
{
    private final LayoutInflater mInflater;
    private CustomMapMarker customMapMarker;
    private HashMap<String, CustomMapMarker> omekaDataMap;


    public MapPinInfoAdapter(LayoutInflater inflater)
    {
        this.mInflater = inflater;
    }

    public MapPinInfoAdapter(LayoutInflater inflater, HashMap<String, CustomMapMarker> omekaDataMap)
    {
        this(inflater);
        this.omekaDataMap = omekaDataMap;
    }

    public MapPinInfoAdapter(LayoutInflater inflater, CustomMapMarker customMapMarker)
    {
        this(inflater);
        this.customMapMarker = customMapMarker;
    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        View popup = mInflater.inflate(R.layout.pin_info_layout, null);
        ImageView imageView = (ImageView) popup.findViewById(R.id.pinImageView);
        ((TextView) popup.findViewById(R.id.title)).setText(marker.getTitle());
        ((TextView) popup.findViewById(R.id.authorTxt)).setText("by: " + marker.getSnippet());

        if(omekaDataMap != null)
        {
            CustomMapMarker customMarker = findMapMarker(marker);
            String url = customMarker.getStoryImgageUrl();
            Picasso.with(popup.getContext()).load(url)
                    .error(R.drawable.rockhammer)
                    .into(imageView, new CustomMarkerCallback(marker));
        }
        else
        {
            String url = customMapMarker.getStoryImgageUrl();
            Picasso.with(popup.getContext()).load(url)
                    .error(R.drawable.rockhammer)
                    .into(imageView, new CustomMarkerCallback(marker));
        }
        return null; //swapping the return of this method and the one below will give a different window style...
    }

    @Override
    public View getInfoContents(Marker marker)
    {
        View popup = mInflater.inflate(R.layout.pin_info_layout, null);
        ImageView imageView = (ImageView) popup.findViewById(R.id.pinImageView);
        ((TextView) popup.findViewById(R.id.title)).setText(marker.getTitle());
        ((TextView) popup.findViewById(R.id.authorTxt)).setText("by: " + marker.getSnippet());
        CustomMapMarker customMarker = null;
        if(omekaDataMap != null)
        {
            customMarker = findMapMarker(marker);
        }
        else if(customMapMarker != null)
        {
            customMarker = customMapMarker;
        }
        if(customMarker != null)
        {
            String url = customMarker.getStoryImgageUrl();
            Picasso.with(popup.getContext()).load(url)
                    .error(R.drawable.rockhammer)
                    .into(imageView, new CustomMarkerCallback(marker));
        }
        return popup;
    }

    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

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
}
