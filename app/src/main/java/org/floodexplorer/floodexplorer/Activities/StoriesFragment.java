package org.floodexplorer.floodexplorer.Activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMarkerAdapter;
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
        bundle.putParcelableArrayList("omekaDataList", omekaDataItems);

        StoriesFragment storiesFragment = new StoriesFragment();
        storiesFragment.setArguments(bundle);
        return storiesFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        readArgumentsBundle(getArguments());
        View view = inflater.inflate(R.layout.fragment_stories, container, false);
        this.setNavigationTitle();
        this.storyListView = (ListView) view.findViewById(R.id.storyListView);
        this.populateListView();
        setRetainInstance(true); //this is why rotation is currently working it might not be the best way to do this
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
            this.omekaDataItems = bundle.getParcelableArrayList("omekaDataList");
        }
    }

    private void setNavigationTitle()
    {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        View view = actionBar.getCustomView();
        TextView textView = (TextView) view.findViewById(R.id.navTitleTxt);
        textView.setText("Stories");
    }

    private void populateListView()
    {
        CustomMarkerAdapter customMarkerAdapter = new CustomMarkerAdapter(getContext(), omekaDataItems);
        storyListView.setAdapter(customMarkerAdapter);
    }
}
