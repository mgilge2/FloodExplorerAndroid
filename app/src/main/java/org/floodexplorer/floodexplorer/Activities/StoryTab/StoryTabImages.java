package org.floodexplorer.floodexplorer.Activities.StoryTab;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.floodexplorer.floodexplorer.OmekaDataItems.Adapters.RecyclerAdapters.StoryItemsRecycler.StoryItemRecycler;
import org.floodexplorer.floodexplorer.OmekaDataItems.Adapters.RecyclerAdapters.DividerItemDecoration;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.StoryItemDetails;
import org.floodexplorer.floodexplorer.SupportingFiles.AppConfiguration;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoryTabImages#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoryTabImages extends Fragment
{
    private RecyclerView storyImagesList;
    private CustomMapMarker customMapMarker;
    private StoryItemRecycler storyItemRecycler;
    private String selectedStoryItem;

    public static StoryTabImages newInstance(CustomMapMarker marker)
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConfiguration.BUNDLE_TAG_CUSTOM_MAP_MARKER, marker);
        StoryTabImages storyImagesList = new StoryTabImages();
        storyImagesList.setArguments(bundle);

        return storyImagesList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        readArgumentsBundle(getArguments());
        View view = inflater.inflate(R.layout.fragment_story_images_list, container, false);
        this.storyImagesList = (RecyclerView) view.findViewById(R.id.storyImagesList);

        //this.populateRecyclerView();
        setRetainInstance(true); //this is why rotation is currently working it might not be the best way to do this
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(AppConfiguration.BUNDLE_TAG_CUSTOM_MAP_MARKER, customMapMarker);
        if(storyItemRecycler.getSelectedStoryTitle() != null)
        {
            savedInstanceState.putString("selectedStoryItem", storyItemRecycler.getSelectedStoryTitle());
        }
        else
        {
            savedInstanceState.putSerializable("selectedStoryItem", null);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null)
        {
            this.customMapMarker = (CustomMapMarker) savedInstanceState.getSerializable(AppConfiguration.BUNDLE_TAG_CUSTOM_MAP_MARKER);
            selectedStoryItem = savedInstanceState.getString("selectedStoryItem");
        }
        if(customMapMarker != null)
        {
            this.populateRecyclerView();
        }
        setRetainInstance(true);
    }



    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

    private void readArgumentsBundle(Bundle bundle)
    {
        if(bundle != null)
        {
            this.customMapMarker = bundle.getParcelable(AppConfiguration.BUNDLE_TAG_CUSTOM_MAP_MARKER);
        }
    }

    private void populateRecyclerView()
    {
        if(storyItemRecycler == null)
        {
            storyItemRecycler = new StoryItemRecycler(getContext(), customMapMarker.getFileList(), R.layout.story_images_row);
        }
        else
        {
            storyItemRecycler = new StoryItemRecycler(getContext(), customMapMarker.getFileList(), R.layout.story_images_row);
        }

        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.divider2
        );
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);

        storyImagesList.setLayoutManager(new LinearLayoutManager(getContext()));
        storyImagesList.addItemDecoration(dividerItemDecoration);
        storyImagesList.setAdapter(storyItemRecycler);

        if(selectedStoryItem != null)
        {
            storyItemRecycler.setSelectedStoryTitle(selectedStoryItem);
            storyItemRecycler.launchImageDialog();
        }
    }
}
