package org.floodexplorer.floodexplorer.OmekaDataItems.Adapters.RecyclerAdapters.StoryRecycler;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mgilge on 11/13/17.
 */

public class StoryRecyclerAdapter extends RecyclerView.Adapter<StoryHolder>
{
    private Context context;
   private ArrayList<CustomMapMarker> markerList;
    private HashMap<String, CustomMapMarker> omekaDataMap;
    private int itemResource;
    private final static int FADE_DURATION = 1000; // in milliseconds put into appConfig

    public StoryRecyclerAdapter(Context context, HashMap<String, CustomMapMarker> omekaDataMap, int itemResource)
    {
        this.context = context;
        this.omekaDataMap = omekaDataMap;
        this.itemResource = itemResource;
        this.buildArrayList();
    }

    @Override
    public StoryHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.itemResource, parent, false);
        return new StoryHolder(this.context, view);
    }

    @Override
    public void onBindViewHolder(StoryHolder holder, int position)
    {
        CustomMapMarker customMapMarker =  this.markerList.get(position);
        holder.bindStory(customMapMarker, position);
        //setScaleAnimation(holder.itemView);
        setFadeAnimation(holder.itemView);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount()
    {
        return this.markerList.size();
    }

    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

    private void buildArrayList()
    {
        this.markerList = new ArrayList<CustomMapMarker>();
        for(Map.Entry<String, CustomMapMarker> entry : omekaDataMap.entrySet())
        {
            CustomMapMarker customMarker = entry.getValue();
            this.markerList.add(customMarker);
        }
    }

    private void setFadeAnimation(View view)
    {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    private void setScaleAnimation(View view)
    {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }
}
