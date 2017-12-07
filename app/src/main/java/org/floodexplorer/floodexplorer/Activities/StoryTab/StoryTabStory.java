package org.floodexplorer.floodexplorer.Activities.StoryTab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.floodexplorer.floodexplorer.SupportingFiles.AppConfiguration;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.R;

public class StoryTabStory extends Fragment
{
    private TextView storyText;
    private TextView storyTitle;
    private TextView storyAuthor;
    private TextView storyResources;
    private CustomMapMarker customMapMarker;

    public static StoryTabStory newInstance(CustomMapMarker marker)
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConfiguration.BUNDLE_TAG_CUSTOM_MAP_MARKER, marker);

        StoryTabStory storyTabStory = new StoryTabStory();
        storyTabStory.setArguments(bundle);
        return storyTabStory;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.readArgumentsBundle(getArguments());
        View view = inflater.inflate(R.layout.story_tab_story, container, false);

        this.storyText = (TextView) view.findViewById(R.id.storyBodyTxt);
        this.storyTitle = (TextView) view.findViewById(R.id.storyTitleTxt);
        this.storyAuthor = (TextView) view.findViewById(R.id.storyAuthorTxt);
        this.storyResources = (TextView) view.findViewById(R.id.storyResources);
        this.storyText.setText(customMapMarker.getStoryText());
        this.storyTitle.setText(customMapMarker.getTitle());
        this.storyResources.setText(customMapMarker.getStoryResources());
        this.storyAuthor.setText("By: " + customMapMarker.getSnippet());

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
            this.customMapMarker = bundle.getParcelable(AppConfiguration.BUNDLE_TAG_CUSTOM_MAP_MARKER);
        }
    }
}
