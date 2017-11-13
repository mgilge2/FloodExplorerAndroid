package org.floodexplorer.floodexplorer.Activities.StoryTab;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.floodexplorer.floodexplorer.SupportingFiles.AppConfiguration;
import org.floodexplorer.floodexplorer.OmekaDataItems.Adapters.StoryImagesListAdapter;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoryTabImages#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoryTabImages extends Fragment
{
    private ListView storyImagesList;
    private CustomMapMarker customMapMarker;

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
        this.storyImagesList = (ListView) view.findViewById(R.id.storyImagesList);
        this.populateListView();
        setRetainInstance(true); //this is why rotation is currently working it might not be the best way to do this
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(AppConfiguration.BUNDLE_TAG_CUSTOM_MAP_MARKER, customMapMarker);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.populateListView();
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

    private void populateListView()
    {
        StoryImagesListAdapter storyImagesListAdapter = new StoryImagesListAdapter(getContext(), customMapMarker.getFileList());
        storyImagesList.setAdapter(storyImagesListAdapter);
        this.storyImagesList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }
}
