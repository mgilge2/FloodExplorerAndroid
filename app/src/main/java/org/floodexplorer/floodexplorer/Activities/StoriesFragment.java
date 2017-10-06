package org.floodexplorer.floodexplorer.Activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMarkerAdapter;
import org.floodexplorer.floodexplorer.OmekaDataItems.OmekaDataItems;
import org.floodexplorer.floodexplorer.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoriesFragment extends Fragment
{

    private ListView storyListView;
    private OmekaDataItems<CustomMapMarker> omekaDataItems;


    public StoriesFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_stories, container, false);

        this.storyListView = (ListView) view.findViewById(R.id.storyListView);
        this.populateListView();
        return view;
    }

    public void setOmekaDataItems(OmekaDataItems<CustomMapMarker> omekaDataItems)
    {
        this.omekaDataItems = omekaDataItems;
    }

    //private implementation...
    private void populateListView()
    {
        CustomMarkerAdapter customMarkerAdapter = new CustomMarkerAdapter(getContext(), omekaDataItems);
        storyListView.setAdapter(customMarkerAdapter);
    }
}
