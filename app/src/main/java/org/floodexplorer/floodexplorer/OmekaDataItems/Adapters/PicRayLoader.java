package org.floodexplorer.floodexplorer.OmekaDataItems.Adapters;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.floodexplorer.floodexplorer.AppConfiguration;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.StoryItemDetails;

import java.util.ArrayList;

/**
 * Created by mgilge on 10/22/17.
 */

public class PicRayLoader extends AsyncTaskLoader
{
    private CustomMapMarker customMapMarker;
    private Context context;

    public PicRayLoader(Context context, CustomMapMarker customMapMarker)
    {
        super(context);
        this.context = context;
        this.customMapMarker = customMapMarker;
    }

    @Override
    public ArrayList<ImageView> loadInBackground()
    {
        ArrayList<ImageView> retList = new ArrayList<ImageView>();
        for (StoryItemDetails storyItem : customMapMarker.getFileList()) {
            String fileName = storyItem.getFileName();
            ImageView newImageView = new ImageView(context);

            Picasso.with(getContext())
                    .load(AppConfiguration.URL_IMAGES_SQUARE_THUMBNAILS + fileName)
                    .resize(250, 250) //will resize the original to square which makes everything render nice.....this needs work, What I want to do next is make two lists, one resized that is displayed, and another that is the original for the picture dialog....
                    //.centerInside()  //goes with aboive....and causes some additional problems
                    .into(newImageView);
            newImageView.setTag(storyItem);
            retList.add(newImageView);
        }
        return retList;
    }
}
