package org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.floodexplorer.floodexplorer.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by mgilge on 7/21/17.
 */

public class PinInfoViewAdapter implements GoogleMap.InfoWindowAdapter
{
    private final LayoutInflater mInflater;
    private ArrayList<CustomMapMarker> omekaDataItems;
    private ImageView mImageView;
    private ImageView markerImageView;
    private Context mContext;

    public PinInfoViewAdapter(LayoutInflater inflater)
    {
        this.mInflater = inflater;
        this.mContext = inflater.getContext();
    }

    public PinInfoViewAdapter(LayoutInflater inflater, ArrayList<CustomMapMarker> omekaDataItems)
    {
        this.mInflater = inflater;
        this.omekaDataItems = omekaDataItems;
        this.mContext =inflater.getContext();
    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        View popup = mInflater.inflate(R.layout.pin_info_layout, null);
        ImageView imageView = popup.findViewById(R.id.pinImageView);
        ((TextView) popup.findViewById(R.id.title)).setText(marker.getTitle());
        if(omekaDataItems != null)
        {
            CustomMapMarker customMarker = findMapMarker(marker);
            String url = customMarker.getStoryImgageUrl();
            Picasso.with(popup.getContext()).load(url)
                  //  .fit()
                //    .centerCrop()
                  //  .placeholder(R.drawable.rockhammer)
                  //  .transform(new RoundedCornersTransformation(12, 0, RoundedCornersTransformation.CornerType.TOP))
                    .error(R.drawable.rockhammer)
                    .into(imageView, new CustomMarkerCallback(marker));
        }

      //  Drawable drawable =   //getResources().getDrawable(<insert your id here>);
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
