package org.floodexplorer.floodexplorer.OmekaDataItems.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.floodexplorer.floodexplorer.Activities.PictureDialog;
import org.floodexplorer.floodexplorer.Activities.StoryTab.PictureFragment;
import org.floodexplorer.floodexplorer.AppConfiguration;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.StoryItemDetails;
import org.floodexplorer.floodexplorer.R;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Tej on 8/1/2017.
 */

public class PicRayAdapter extends ArrayAdapter<ImageView> implements Serializable
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

        this.buildImageListPicasso();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
        {
            gridViewAndroid = inflater.inflate(R.layout.gridview_layout, null);
            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.android_gridview_image);


            ImageView imageView = (imageList.get(position));
            imageViewAndroid.setImageDrawable(imageView.getDrawable());
            //imageView.setDrawingCacheEnabled(true);
            //Bitmap bitmap = imageView.getDrawingCache(true);
            //imageViewAndroid.setImageBitmap(bitmap);
            Object obj =  imageView.getTag();
            StoryItemDetails details = (StoryItemDetails) obj;
            textViewAndroid.setText(details.getFileTitle());

            imageViewAndroid.setTag(details);
            imageViewAndroid.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageViewAndroid.setOnClickListener(picRayListener);

        }
        else
        {
            gridViewAndroid = (View) convertView;
        }
        return gridViewAndroid;
    }

    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

    private View.OnClickListener onClickListenerGenerator()
    {
        View.OnClickListener onClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ImageView imageView = (ImageView) v;


                PictureFragment pictureFragment = PictureFragment.newInstance(imageView);

                /*
                FragmentManager fm =  ((AppCompatActivity)mContext).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frameLayout, pictureFragment); //.addToBackStack("fragment");
                ft.commit();
                */


                FragmentManager fm = ((AppCompatActivity)mContext).getSupportFragmentManager();
                PictureDialog pictureDialog = PictureDialog.newInstance(imageView);
                pictureDialog.show(fm, "Dialog Show");


            }
        };
        return onClickListener;
    }


    private void buildImageListPicasso() //need to handle data for images here...
    {

        for(StoryItemDetails storyItem: storyItemDetails)
        {
            String fileName = storyItem.getFileName();
            ImageView newImageView = new ImageView(mContext);

            Picasso.with(getContext())
                    .load(AppConfiguration.URL_IMAGES_SQUARE_THUMBNAILS + fileName)
                    //.resize(250,250) //will resize the original to square which makes everything render nice.....this needs work, What I want to do next is make two lists, one resized that is displayed, and another that is the original for the picture dialog....
                    //.centerInside()  //goes with aboive....and causes some additional problems
                    .into(newImageView);
            newImageView.setTag(storyItem);
            imageList.remove(newImageView);
            imageList.add(newImageView);

        }
    }

}