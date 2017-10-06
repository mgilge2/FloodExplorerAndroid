package org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.floodexplorer.floodexplorer.OmekaDataItems.OmekaDataItems;
import org.floodexplorer.floodexplorer.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by mgilge on 7/21/17.
 */

public class PinInfoViewAdapter implements GoogleMap.InfoWindowAdapter
{
    private final LayoutInflater mInflater;
    private OmekaDataItems<CustomMapMarker> omekaDataItems;
    private ImageView mImageView;
    private Context mContext;
    private  ImageView imageView;
    private HashMap<String, Uri> images=null;
    private int iconWidth = -1;
    private int iconHeight = -1;

    public PinInfoViewAdapter(LayoutInflater inflater)
    {
        this.mInflater = inflater;
        this.mContext = inflater.getContext();
    }

    public PinInfoViewAdapter(LayoutInflater inflater, OmekaDataItems<CustomMapMarker> omekaDataItems)
    {
        this.mInflater = inflater;
        this.omekaDataItems = omekaDataItems;
        this.mContext =inflater.getContext();
    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        final View popup = mInflater.inflate(R.layout.pin_info_layout, null);
        if(omekaDataItems != null)
        {
            imageView = new ImageView(mContext);
            mImageView = (ImageView) popup.findViewById(R.id.pinImageView);

            CustomMapMarker customMarker = findMapMarker(marker);
           //
            // String url = customMarker.getStoryImgageUrl();
            String img_url = customMarker.getStoryImgageUrl();
            if (!img_url.equalsIgnoreCase(""))
                Picasso.with(mContext).load(img_url)
                        .placeholder(R.drawable.rockhammer)// Place holder image from drawable folder
                        .into(mImageView, new MarkerCallback(marker));
            /*
        //    Uri image = new

            Picasso.with(mContext).load(url).resize(iconWidth, iconHeight)
                    .centerCrop().noFade()
                    .placeholder(R.drawable.rockhammer)
                    .into(mImageView, new MarkerCallback(marker));
            //this.loadBitmap(0, imageView, url);
          //  mImageView.setImageDrawable(imageView.getDrawable());
            //mImageView.setImageResource(R.drawable.logomenu);
            */
        }

      //  Drawable drawable =   //getResources().getDrawable(<insert your id here>);
        ((TextView) popup.findViewById(R.id.title)).setText(marker.getTitle());
        return popup;
    }

    @Override
    public View getInfoContents(Marker marker)
    {
        final View popup = mInflater.inflate(R.layout.pin_info_layout, null);
        ((TextView) popup.findViewById(R.id.title)).setText(marker.getTitle());
        return popup;
    }

    //Private implementation...

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

    public void loadBitmap(int resId, ImageView imageView, String url)
    {
        final String imageKey = String.valueOf(resId);
        final Bitmap bitmap;
     //   imageView.setImageResource(R.drawable.rockhammer);
            try
            {

                new DownloadImageTask(imageView)
                        .execute(url);

                //    this.getRetrofitImage(url); buggy!!
            }
            catch (Exception e)
            {
                e.getStackTrace();
            }


    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
    {
        private String url;
        ImageView mHeaderPicture;
        ProgressDialog loading;

        public DownloadImageTask(ImageView mHeaderPicture)
        {
            this.mHeaderPicture= mHeaderPicture;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            loading = ProgressDialog.show(mContext,"Fetching...","Wait...",false,false); //showActivity works in fragments, getContext not so much
        }

        protected Bitmap doInBackground(String... urls)
        {
            url = urls[0];
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try
            {
                InputStream in = new URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                return mIcon11;
            }
            catch (Exception e)
            {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            //    Bitmap returnBitmap = getResizedBitmap(mIcon11, 100, 100);

            return mIcon11;
        }

        protected void onPostExecute(Bitmap result)
        {
            loading.dismiss();
            imageView.setImageBitmap(result); //Bitmap.createScaledBitmap(result, 100, 100, false)  setImageBitmap(result)


            /*
            lots of ways to set the scaling on an image, this is not the best approach
            https://stackoverflow.com/questions/4837715/how-to-resize-a-bitmap-in-android
            for more info

            resizing is causing a crash probably due to url and delay in loading we should try to just convert the images during the query phase to actual images if possible
            and see what the hit is in performance (it isnt good as is)

            https://www.androidtutorialpoint.com/networking/android-retrofit-2-0-tutorial-retrofit-android-example-download-image-url-display-android-device-screen/

            is likely a good solution as that site has proven to be very useful
             */
        }
    };

    static class MarkerCallback implements Callback
    {
        Marker marker=null;

        MarkerCallback(Marker marker)
        {
            this.marker=marker;
        }

        @Override
        public void onError()
        {
            Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
        }

        @Override
        public void onSuccess()
        {
            if (marker != null && marker.isInfoWindowShown())
            {
                marker.showInfoWindow();
            }
        }
    }
}
