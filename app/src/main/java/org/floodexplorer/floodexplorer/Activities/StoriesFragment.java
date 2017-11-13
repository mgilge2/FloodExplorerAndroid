package org.floodexplorer.floodexplorer.Activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.floodexplorer.floodexplorer.SupportingFiles.AppConfiguration;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.OmekaDataItems.Adapters.StoryListAdapter;
import org.floodexplorer.floodexplorer.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoriesFragment extends Fragment
{
    private ListView storyListView;
    private ArrayList<CustomMapMarker> omekaDataItems;

    public static StoriesFragment newInstance(ArrayList<CustomMapMarker> omekaDataItems)
    {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AppConfiguration.BUNDLE_TAG_OMEKA_DATA_ITEMS, omekaDataItems);

        StoriesFragment storiesFragment = new StoriesFragment();
        storiesFragment.setArguments(bundle);
        return storiesFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = null;
        try
        {
            readArgumentsBundle(getArguments());
            view = inflater.inflate(R.layout.fragment_stories, container, false);
            this.storyListView = (ListView) view.findViewById(R.id.storyListView);
            this.populateListView();
            setRetainInstance(true); //this is why rotation is currently working it might not be the best way to do this

        }
        catch (Exception e)
        {
            e.getStackTrace();
        }
        return view;
    }

    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

    private void readArgumentsBundle(Bundle bundle)
    {
        if(bundle != null)
        {
            this.omekaDataItems = bundle.getParcelableArrayList(AppConfiguration.BUNDLE_TAG_OMEKA_DATA_ITEMS);
        }
    }

    private void populateListView()
    {
        FragmentManager fragmentManager = getFragmentManager();
        StoryListAdapter customMarkerAdapter = new StoryListAdapter(getContext(), omekaDataItems, fragmentManager);
        storyListView.setAdapter(customMarkerAdapter);
        this.storyListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }
}
