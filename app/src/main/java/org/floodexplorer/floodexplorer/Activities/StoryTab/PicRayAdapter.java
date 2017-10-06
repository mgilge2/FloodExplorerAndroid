package org.floodexplorer.floodexplorer.Activities.StoryTab;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.floodexplorer.floodexplorer.Activities.MainActivity;
import org.floodexplorer.floodexplorer.Activities.PictureDialog;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.StoryItemDetails;
import org.floodexplorer.floodexplorer.R;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Tej on 8/1/2017.
 */

class PicRayAdapter extends ArrayAdapter<ImageView>
{
    private Context mContext;
    private ArrayList<StoryItemDetails> storyItemDetails;
    private ArrayList<ImageView> imageList;
    private ArrayList<Bitmap> bitmapList;
    private View.OnClickListener picRayListener;

    public PicRayAdapter(@NonNull Context context, ArrayList inStringList)
    {
        super(context, 0, inStringList);
        this.mContext = context;
        this.storyItemDetails = inStringList;
        this.imageList = new ArrayList<ImageView>();
        this.bitmapList = new ArrayList<Bitmap>();
        this.picRayListener = this.onClickListenerGenerator();
        this.buildImageList();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        this.addHandlersToImages();
        ImageView imageView;
        if (convertView == null)
        {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }
        else
        {
            imageView = (ImageView) convertView;
        }

        imageView = imageList.get(position);
        return imageView;
    }

    private View.OnClickListener onClickListenerGenerator()
    {
        View.OnClickListener onClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ImageView imageView = (ImageView) v;
                FragmentManager fm = ((AppCompatActivity)mContext).getSupportFragmentManager();
                PictureDialog dialogFragment = new PictureDialog(imageView);
                dialogFragment.show(fm, "Dialog Show");

                //Toast.makeText(mContext,"mmahhahhahahahahahaha!!!", Toast.LENGTH_LONG).show();
            }
        };
        return onClickListener;
    }

    private void buildImageList() //need to handle data for images here...
    {
        for(StoryItemDetails storyItem: storyItemDetails)
        {
            String fileName = storyItem.getFileName();
            new DownloadImageTask(storyItem).execute("http://floodexplorer.com/curatescape/files/original/" + fileName);
        }
    }

    public void addHandlersToImages()
    {
        for(ImageView imageView: imageList)
        {
            imageView.setOnClickListener(picRayListener);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private String url;
        ProgressDialog loading;
        StoryItemDetails itemDetails;

        public DownloadImageTask(StoryItemDetails itemDetails)
        {
            this.itemDetails = itemDetails;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            loading = ProgressDialog.show(mContext, "Fetching...", "Wait...", false, false); //showActivity works in fragments, getContext not so much
        }

        protected Bitmap doInBackground(String... urls) {
            url = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                //bitmapList.add(mIcon11);
                //  return mIcon11;
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }


            //    Bitmap returnBitmap = getResizedBitmap(mIcon11, 100, 100);

            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            ImageView newImageView = new ImageView(mContext);
            newImageView.setImageBitmap(result);
            newImageView.setTag(this.itemDetails);
            imageList.add(newImageView);
            loading.dismiss();


        }
    };
}