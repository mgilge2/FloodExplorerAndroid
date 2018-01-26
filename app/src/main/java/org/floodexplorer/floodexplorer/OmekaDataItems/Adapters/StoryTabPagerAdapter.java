package org.floodexplorer.floodexplorer.OmekaDataItems.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.floodexplorer.floodexplorer.Activities.StoryTab.StoryTabImages;
import org.floodexplorer.floodexplorer.Activities.StoryTab.StoryTabMap;
import org.floodexplorer.floodexplorer.Activities.StoryTab.StoryTabStory;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;

/**
 * Created by mgilge on 10/22/17.
 */

public class StoryTabPagerAdapter extends FragmentStatePagerAdapter
{

    private String tabtitles[] = new String[] { "Story", "Pictures", "Map" };
    private StoryTabStory storyTabStory;
    private StoryTabMap storyTabMap;
    private StoryTabImages storyImagesList;

    public StoryTabPagerAdapter(FragmentManager fm, CustomMapMarker customMapMarker, Context context)
    {
        super(fm);
        this.storyTabStory = StoryTabStory.newInstance(customMapMarker);
        this.storyImagesList = StoryTabImages.newInstance(customMapMarker);
        this.storyTabMap = StoryTabMap.newInstance(customMapMarker);
    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        switch (position)
        {
            case 0:
                fragment = storyTabStory;
                break;
            case 1:
                fragment = storyImagesList;
                break;
            case 2:
                fragment = storyTabMap;
                break;
        }
        return fragment;
    }


    @Override
    public int getCount()
    {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return tabtitles[position];
    }
}
