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

/**
 * Created by mgilge on 7/21/17.
 */

public class MapPinInfoAdapter implements GoogleMap.InfoWindowAdapter
{
    private final LayoutInflater mInflater;
    private ArrayList<CustomMapMarker> omekaDataItems;

    public MapPinInfoAdapter(LayoutInflater inflater)
    {
        this.mInflater = inflater;
    }

    public MapPinInfoAdapter(LayoutInflater inflater, ArrayList<CustomMapMarker> omekaDataItems)
    {
        this.mInflater = inflater;
        this.omekaDataItems = omekaDataItems;
    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        View popup = mInflater.inflate(R.layout.pin_info_layout, null);
        ImageView imageView = (ImageView) popup.findViewById(R.id.pinImageView);
        ((TextView) popup.findViewById(R.id.title)).setText(marker.getTitle());
        ((TextView) popup.findViewById(R.id.authorTxt)).setText("by: " + marker.getSnippet());

        if(omekaDataItems != null)
        {
            CustomMapMarker customMarker = findMapMarker(marker);
            String url = customMarker.getStoryImgageUrl();
            Picasso.with(popup.getContext()).load(url)
                    .error(R.drawable.rockhammer)
                    .into(imageView, new CustomMarkerCallback(marker));
        }
        return popup;
    }

    @Override
    public View getInfoContents(Marker marker)
    {
       return null;
    }

    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

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
}
