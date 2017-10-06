package org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import android.widget.TextView;

import org.floodexplorer.floodexplorer.Activities.MainActivity;
import org.floodexplorer.floodexplorer.Activities.StoryTab.StoryTabActivity;
import org.floodexplorer.floodexplorer.OmekaDataItems.POJO.RetrofitImageAPI;
import org.floodexplorer.floodexplorer.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by mgilge on 7/21/17.
 */

public class CustomMarkerAdapter extends ArrayAdapter<CustomMapMarker>
{
    private TextView storyLabel;
    private ImageView mImageView;
    private GoogleMap mMap;
    private LruCache<String, Bitmap> mMemoryCache;
    private Context context;



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
        }

        this.setupMemoryCache();
        storyLabel = (TextView) convertView.findViewById(R.id.storyLblTxt);
        storyLabel.setText(marker.getTitle());

        mImageView = (ImageView) convertView.findViewById(R.id.imageButton);
        mImageView.setTag(position);

        //String url = "http://floodexplorer.com/rockhammer.png";
       // String url = "http://floodexplorer.com/curatescape/files/fullsize/4e7bbd22388d2a84bb1eed6a4588bc33.jpg";
          String url = marker.getStoryImgageUrl();

       this.loadBitmap(position, mImageView,url);

         this.setButtonListener();

        return convertView;
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

    public void addBitmapToMemoryCache(String key, Bitmap bitmap)
    {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key)
    {
        return mMemoryCache.get(key);
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

                Fragment fragment = new StoryTabActivity(customMapMarker);
                FragmentManager fm =  ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragmentFrame, fragment);
                ft.commit();
            }
        });
    }

    public void setmMap(GoogleMap mMap)
    {
        this.mMap = mMap;
    }

    public Bitmap getBitmapFromURL(String src)
    {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight
    ) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    //this is trial of stuff located at https://www.androidtutorialpoint.com/networking/android-retrofit-2-0-tutorial-retrofit-android-example-download-image-url-display-android-device-screen/

    void getRetrofitImage(String url)
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitImageAPI service = retrofit.create(RetrofitImageAPI.class);

        Call<ResponseBody> call = service.getImageDetails();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {

                try {

                    Log.d("onResponse", "Response came from server");

                    boolean FileDownloaded = DownloadImage(response.body());

                    Log.d("onResponse", "Image is downloaded and saved ? " + FileDownloaded);

                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    private boolean DownloadImage(ResponseBody body) {

        try {
            Log.d("DownloadImage", "Reading and writing file");
            InputStream in = null;
            FileOutputStream out = null;

            try {
                in = body.byteStream();
                out = new FileOutputStream(getContext().getExternalFilesDir(null) + File.separator + "AndroidTutorialPoint.jpg");
                int c;

                while ((c = in.read()) != -1) {
                    out.write(c);
                }
            }
            catch (IOException e) {
                Log.d("DownloadImage",e.toString());
                return false;
            }
            finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }

            int width, height;
            ImageView image = (ImageView) mImageView.findViewById(R.id.imageButton);
            Bitmap bMap = BitmapFactory.decodeFile(getContext().getExternalFilesDir(null) + File.separator + "AndroidTutorialPoint.jpg");
            width = 2*bMap.getWidth();
            height = 6*bMap.getHeight();
            Bitmap bMap2 = Bitmap.createScaledBitmap(bMap, width, height, false);
            image.setImageBitmap(bMap2);

            return true;

        } catch (IOException e) {
            Log.d("DownloadImage",e.toString());
            return false;
        }
    }

}
