package org.floodexplorer.floodexplorer.Activities.StoryTab;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;

import org.floodexplorer.floodexplorer.SupportingFiles.AppConfiguration;
import org.floodexplorer.floodexplorer.OmekaDataItems.Adapters.StoryTabPagerAdapter;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.R;


public class StoryTabFragment extends Fragment
{
    private ViewPager viewPager;
    private StoryTabPagerAdapter storyTabPagerAdapter;
    private CustomMapMarker customMapMarker;

    public static StoryTabFragment newInstance(CustomMapMarker marker)
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConfiguration.BUNDLE_TAG_CUSTOM_MAP_MARKER, marker);

        StoryTabFragment storyTab = new StoryTabFragment();
        storyTab.setArguments(bundle);
        return storyTab;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.readArgumentsBundle(getArguments());
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        this.viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        this.storyTabPagerAdapter = new StoryTabPagerAdapter(getChildFragmentManager(), customMapMarker, view.getContext());
        this.viewPager.setAdapter(null);
        this.viewPager.setAdapter(storyTabPagerAdapter);
        this.viewPager.setPageTransformer(true, new RotateUpTransformer()); //changes the standard animation for swiping tabs. gradle addition needed for this...

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        setRetainInstance(true);
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
