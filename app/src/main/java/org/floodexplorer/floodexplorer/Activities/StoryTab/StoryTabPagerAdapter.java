package org.floodexplorer.floodexplorer.Activities.StoryTab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;

/**
 * Created by mgilge on 10/22/17.
 */

public class StoryTabPagerAdapter extends FragmentStatePagerAdapter
{

    private String tabtitles[] = new String[] { "Story", "Pictures", "Map" };
    private CustomMapMarker customMapMarker;
    private StoryTabStory storyTabStory;
    private StoryTabImages storyTabImages;
    private StoryTabMap storyTabMap;

    public StoryTabPagerAdapter(FragmentManager fm, CustomMapMarker customMapMarker)
    {
        super(fm);
        this.customMapMarker = customMapMarker;
        this.storyTabStory = StoryTabStory.newInstance(customMapMarker);
        this.storyTabImages = StoryTabImages.newInstance(customMapMarker);
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
                fragment = storyTabImages;
                break;
            case 2:
                fragment = storyTabMap;
                break;
        }
        return fragment; //error if this happens...it shouldnt
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
