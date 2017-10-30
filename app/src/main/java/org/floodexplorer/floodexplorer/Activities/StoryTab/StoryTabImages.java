package org.floodexplorer.floodexplorer.Activities.StoryTab;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TabHost;
import android.widget.TextView;

import org.floodexplorer.floodexplorer.AppConfiguration;
import org.floodexplorer.floodexplorer.OmekaDataItems.Adapters.PicRayAdapter;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.StoryItemDetails;
import org.floodexplorer.floodexplorer.R;

import java.util.ArrayList;


public class StoryTabImages extends Fragment
{
    private GridView gridView;
    private CustomMapMarker customMapMarker;

    public static StoryTabImages newInstance(CustomMapMarker marker)
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConfiguration.BUNDLE_TAG_CUSTOM_MAP_MARKER, marker);

        StoryTabImages storyTabImages = new StoryTabImages();
        storyTabImages.setArguments(bundle);
        return storyTabImages;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.readArgumentsBundle(getArguments());
        View view = inflater.inflate(R.layout.story_tab_images, container, false);
        this.gridView = (GridView) view.findViewById(R.id.gridview);
        this.buildPicArrayAdapter();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.buildPicArrayAdapter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null)
        {
            this.customMapMarker = (CustomMapMarker) savedInstanceState.getSerializable("storyItemDetails");
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(AppConfiguration.BUNDLE_TAG_CUSTOM_MAP_MARKER, customMapMarker);
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

    private void buildPicArrayAdapter()
    {
        PicRayAdapter picRayAdapter = new PicRayAdapter(getContext(), customMapMarker.getFileList());
        gridView.setAdapter(picRayAdapter);
    }
}
