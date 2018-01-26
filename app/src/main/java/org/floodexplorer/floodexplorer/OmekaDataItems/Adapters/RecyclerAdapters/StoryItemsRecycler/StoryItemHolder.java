package org.floodexplorer.floodexplorer.OmekaDataItems.Adapters.RecyclerAdapters.StoryItemsRecycler;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.floodexplorer.floodexplorer.Activities.PictureDialog;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.StoryItemDetails;
import org.floodexplorer.floodexplorer.R;
import org.floodexplorer.floodexplorer.SupportingFiles.AppConfiguration;

import java.io.Serializable;

/**
 * Created by mgilge on 11/24/17.
 */

public class StoryItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Serializable
{
    private Context context;
    private ImageView storyImage;
    private TextView storyImageText;
    private FragmentManager fragmentManager;
    private  StoryItemRecycler storyItemRecycler;

    public StoryItemHolder(Context context, View itemView, final StoryItemRecycler storyItemRecycler)
    {
        super(itemView);
        this.context = context;
        this.storyItemRecycler = storyItemRecycler;
        this.storyImageText = itemView.findViewById(R.id.storyImageTxt);
        this.storyImage = itemView.findViewById(R.id.storyImg);
        itemView.setOnClickListener(this);

        ImageButton dismiss = (ImageButton) itemView.findViewById(R.id.dismissBtn);
    }


    @Override
    public void onClick(View v)
    {
        launchDialog();
    }

    public void launchDialog()
    {
        int selected = getAdapterPosition();
        storyItemRecycler.setSelectedStoryItem(selected);
        StoryItemDetails storyItemDetails =  (StoryItemDetails)storyImage.getTag();
        storyItemRecycler.setSelectedStoryTitle(storyItemDetails.getFileTitle());
        launchImageDialog(storyItemDetails, storyItemRecycler);
    }

    public void launchImageDialog(StoryItemDetails storyItemDetails, StoryItemRecycler storyItemRecycler)
    {
        try
        {
            PictureDialog pictureDialog =  PictureDialog.newInstance(storyItemDetails, storyItemRecycler);
            pictureDialog.show(fragmentManager, "");
        }
        catch (Exception e)
        {
            e.getStackTrace();
            String s = "";
        }
    }

    public void bindStory(StoryItemDetails storyItemDetails, int position, FragmentManager fragmentManager)
    {
        this.fragmentManager = fragmentManager;
        String url = AppConfiguration.URL_IMAGES_SQUARE_THUMBNAILS + storyItemDetails.getFileName();
        this.loadBitmapPicasso(position, storyImage,url);
        this.storyImage.setTag(storyItemDetails);
        this.storyImageText.setText(storyItemDetails.getFileTitle());
    }

    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

    private void loadBitmapPicasso(int resId, ImageView imageView, String url)
    {

        Bitmap bitmap = null;
        if (bitmap != null)
        {
            storyImage.setImageBitmap(bitmap);
        }
        else
        {
            storyImage.setImageResource(R.drawable.rockhammer);
            try
            {

                Picasso.with(context)
                        .load(url)
                        .resize(250,250)
                        .into(storyImage);
                //this.imageList.add(storyImage);
            }
            catch (Exception e)
            {
                e.getStackTrace();
            }
        }
    }
}
