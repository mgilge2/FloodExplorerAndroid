package org.floodexplorer.floodexplorer.OmekaDataItems.Adapters.RecyclerAdapters.StoryItemsRecycler;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import org.floodexplorer.floodexplorer.Activities.PictureDialog;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.StoryItemDetails;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mgilge on 11/24/17.
 */

public class StoryItemRecycler extends RecyclerView.Adapter<StoryItemHolder> implements Serializable
{
    private int itemResource;
    private ArrayList<StoryItemDetails> storyItemDetails;
    private Context context;
    private int selectedStoryItem = - 1;
    private String selectedStoryTitle;

    public String getSelectedStoryTitle()
    {
        return selectedStoryTitle;
    }

    public void setSelectedStoryTitle(String selectedStoryTitle)
    {
        this.selectedStoryTitle = selectedStoryTitle;
    }

    //the below method ideally should be in the itemdetails only....
    public void launchImageDialog()
    {
        try
        {
            StoryItemDetails storyItemDetails = this.findSelectedStoryItem();
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            PictureDialog pictureDialog =  PictureDialog.newInstance(storyItemDetails, this);
            pictureDialog.show(fragmentManager, "");
        }
        catch (Exception e)
        {
            e.getStackTrace();
            String s = "";
        }
    }

    public StoryItemRecycler(Context context, ArrayList<StoryItemDetails> storyItemDetails, int itemResource)
    {
        this.context = context;
        this.storyItemDetails = storyItemDetails;
        this.itemResource = itemResource;
    }

    public int getSelectedStoryItem()
    {
        return selectedStoryItem;
    }

    public void setSelectedStoryItem(int selectedStoryItem)
    {
        this.selectedStoryItem = selectedStoryItem;
    }

    @Override
    public StoryItemHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.itemResource, parent, false);
        return new StoryItemHolder(this.context, view, this);
    }

    @Override
    public void onBindViewHolder(StoryItemHolder holder, int position)
    {
        StoryItemDetails storyItem = storyItemDetails.get(position);
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        holder.bindStory(storyItem, position, fragmentManager);
    }

    @Override
    public int getItemCount()
    {
        return this.storyItemDetails.size();
    }

    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

    private StoryItemDetails findSelectedStoryItem()
    {
        StoryItemDetails storyItemDetails = null; //bad!
        if(this.selectedStoryTitle != null)
        {
            for(StoryItemDetails storyItem : this.storyItemDetails)
            {
                if(this.selectedStoryTitle.equals(storyItem.getFileTitle()))
                {
                    storyItemDetails = storyItem;
                }
            }
        }
        return storyItemDetails;
    }
}
