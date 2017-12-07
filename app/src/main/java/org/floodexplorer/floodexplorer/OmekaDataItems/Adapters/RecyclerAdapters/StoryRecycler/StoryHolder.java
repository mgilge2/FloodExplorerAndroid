package org.floodexplorer.floodexplorer.OmekaDataItems.Adapters.RecyclerAdapters.StoryRecycler;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import org.floodexplorer.floodexplorer.Activities.StoryTab.StoryTabFragment;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.R;

/**
 * Created by mgilge on 11/13/17.
 */

public class StoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    private TextView storyLabel;
    private TextView storyAuthorLabel;
    private ImageView imageView;
    private Context context;
    private CustomMapMarker customMapMarker;

    public StoryHolder(Context context, View itemView)
    {
        super(itemView);
        this.context = context;
        this.storyLabel = (TextView) itemView.findViewById(R.id.storyImageTxt);
        storyAuthorLabel = (TextView) itemView.findViewById(R.id.storyListAuthorTxt);
        imageView = (ImageView) itemView.findViewById(R.id.storyImg);

        itemView.setOnClickListener(this);
    }

    public void bindStory(CustomMapMarker marker, int position)
    {
        storyAuthorLabel.setText("by: " + marker.getSnippet());
        storyLabel.setText(marker.getTitle());
        TextViewCompat.setAutoSizeTextTypeWithDefaults(storyLabel, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(storyAuthorLabel, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        // TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(storyLabel, 10, 30, 2,1);
        this.customMapMarker = marker;
        String url = marker.getStoryImgageUrl();
        this.loadBitmapPicasso(imageView,url);
        //this.loadBitmapGlide(imageView, url);
    }

    @Override
    public void onClick(View v)
    {
        launchStoryTabView();
    }

    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

    private void launchStoryTabView()
    {
        try
        {
            // Access the row position here to get the correct data item
            Fragment fragment = StoryTabFragment.newInstance(customMapMarker);
            ((AppCompatActivity)context).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); //clear the back stack
            FragmentTransaction ft =  ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().addToBackStack("story");
            ft.replace(R.id.frameLayout, fragment);
            ft.commit();
        }
        catch (Exception e)
        {
            e.getStackTrace();
        }
    }

    private void loadBitmapPicasso(ImageView imageView, String url)
    {

        Bitmap bitmap = null;
        if (bitmap != null)
        {
            imageView.setImageBitmap(bitmap);
        }
        else
        {
            imageView.setImageResource(R.drawable.rockhammer);
            try
            {

                //new DownloadImageTask(mImageView).execute(url);
                Picasso.with(context)
                        .load(url)
                        .resize(250,250)
                        .placeholder(R.drawable.rockhammer)
                        .into(imageView);

            }
            catch (Exception e)
            {
                e.getStackTrace();
            }
        }
    }

    //chose picasso over glide but as this was written this function is usable
    private void loadBitmapGlide(ImageView imageView, String url)
    {
        GlideApp.with(context)
                .load(url)
                .override(250,250)
                .into(imageView);
    }
}
