package org.floodexplorer.floodexplorer.OmekaDataItems.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.floodexplorer.floodexplorer.Activities.StoryTabFragment;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.R;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mgilge on 7/21/17.
 */

public class CustomMarkerAdapter extends ArrayAdapter<CustomMapMarker>
{
    private TextView storyLabel;
    private ImageView mImageView;
    private LruCache<String, Bitmap> mMemoryCache;
    private Context context;
    private ListView listView;
    private View view;

    public CustomMarkerAdapter(Context context, ArrayList<CustomMapMarker> users)
    {
        super(context, 0, users);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        CustomMapMarker marker = getItem(position);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.story_list_row, parent, false);
            view = convertView;
        }

        this.setupMemoryCache();
        storyLabel = (TextView) convertView.findViewById(R.id.storyLblTxt);
        storyLabel.setText(marker.getTitle());
        mImageView = (ImageView) convertView.findViewById(R.id.imageButton);
        mImageView.setTag(position);

        String url = marker.getStoryImgageUrl();
        this.loadBitmapPicasso(position, mImageView,url);
        this.setButtonListener();
        this.setItemRowListener();
        return convertView;
    }

    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

    private void setItemRowListener()
    {
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(context, "Click The Picture to go to the story...", Toast.LENGTH_SHORT).show();

                /*
                int position = (Integer) view.getTag();
                CustomMapMarker customMapMarker = getItem(position);
                Fragment fragment = StoryTabFragment.newInstance(customMapMarker);
                FragmentManager fm =  ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frameLayout, fragment);
                ft.commit();
                */
            }
        });
    }

    private void loadBitmapPicasso(int resId, ImageView imageView, String url)
    {
        final String imageKey = String.valueOf(resId);

        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null)
        {
            mImageView.setImageBitmap(bitmap);
        }
        else
        {
            mImageView.setImageResource(R.drawable.rockhammer);
            try
            {

                //new DownloadImageTask(mImageView).execute(url);
                Picasso.with(getContext()).load(url).into(mImageView);

                //    this.getRetrofitImage(url); buggy!!
            }
            catch (Exception e)
            {
                e.getStackTrace();
            }


        }
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap)
    {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key)
    {
        return mMemoryCache.get(key);
    }

    private void setupMemoryCache()
    {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap)
            {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    private void setButtonListener()
    {
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                CustomMapMarker customMapMarker = getItem(position);

                // Do what you want here...
                /*
                Toast.makeText(MainActivity.getInstance().getApplicationContext(),
                        customMapMarker.getSnippet(), Toast.LENGTH_LONG).show();
                        */
                /*
                FragmentManager fm = MainActivity.getInstance().getSupportFragmentManager();
                DisplayStory dialogFragment = new DisplayStory(customMapMarker.getStoryText(), mImageView);
                dialogFragment.show(fm, "Sample Fragment");
*/

                Fragment fragment = StoryTabFragment.newInstance(customMapMarker);
                FragmentManager fm =  ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frameLayout, fragment);
                ft.commit();
            }
        });
    }

    //Below has been deprecated by picasso call.... Will remove before release!

    public void loadBitmap(int resId, ImageView imageView, String url)
    {
        final String imageKey = String.valueOf(resId);

        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null)
        {
            mImageView.setImageBitmap(bitmap);
        }
        else
        {
            mImageView.setImageResource(R.drawable.rockhammer);
            try
            {

                new DownloadImageTask(mImageView)
                        .execute(url);

                //    this.getRetrofitImage(url); buggy!!
            }
            catch (Exception e)
            {
                e.getStackTrace();
            }
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
    {
        private String url;
        ImageView mHeaderPicture;
        public DownloadImageTask(ImageView mHeaderPicture)
        {
            this.mHeaderPicture= mHeaderPicture;
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
            mHeaderPicture.setImageBitmap(result); //Bitmap.createScaledBitmap(result, 100, 100, false)  setImageBitmap(result)
            addBitmapToMemoryCache(String.valueOf(url), result);
        }

    };
}
