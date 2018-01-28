package org.floodexplorer.floodexplorer.Activities;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.squareup.picasso.Picasso;

import org.floodexplorer.floodexplorer.OmekaDataItems.Adapters.RecyclerAdapters.DividerItemDecoration;
import org.floodexplorer.floodexplorer.OmekaDataItems.Adapters.RecyclerAdapters.StoryRecycler.StoryRecyclerAdapter;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.R;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoriesFragment extends Fragment
{
    private RecyclerView storyListView;
    private int itemResource;
    private HashMap<String, CustomMapMarker> omekaDataMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = null;
        try
        {
            view = inflater.inflate(R.layout.fragment_stories, container, false);
            this.storyListView = (RecyclerView) view.findViewById(R.id.storyListView);
            this.itemResource = R.layout.story_list_row;
            this.omekaDataMap = MainActivity.getOmekaDataMap();
            this.populateListView();
            setRetainInstance(true); //this is why rotation is currently working it might not be the best way to do this

        }
        catch (Exception e)
        {
            Log.e("StoriesFragment", e.getMessage());
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

    private void populateListView()
    {
        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.divider2);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_list_from_right);

        StoryRecyclerAdapter storyRecyclerAdapter = new StoryRecyclerAdapter(getContext(), omekaDataMap, itemResource);

        storyListView.setLayoutManager(new LinearLayoutManager(getContext()));
        storyListView.addItemDecoration(dividerItemDecoration);

        storyListView.setLayoutAnimation(animation);
        storyListView.setAdapter(storyRecyclerAdapter);
    }
}
