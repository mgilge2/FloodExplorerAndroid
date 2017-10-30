package org.floodexplorer.floodexplorer.Activities.StoryTab;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.squareup.picasso.Picasso;

import org.floodexplorer.floodexplorer.Activities.HomeFragment;
import org.floodexplorer.floodexplorer.Activities.LoadingScreenFragment;
import org.floodexplorer.floodexplorer.Activities.StoryTabFragment;
import org.floodexplorer.floodexplorer.AppConfiguration;
import org.floodexplorer.floodexplorer.OmekaDataItems.Adapters.PicRayAdapter;
import org.floodexplorer.floodexplorer.OmekaDataItems.Adapters.PicRayLoader;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.StoryItemDetails;
import org.floodexplorer.floodexplorer.R;

import java.util.ArrayList;


public class StoryTab extends Fragment
{
    private ViewPager viewPager;
    private StoryTabPagerAdapter storyTabPagerAdapter;
    private CustomMapMarker customMapMarker;
    private Context context;

    public static StoryTab newInstance(CustomMapMarker marker)
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConfiguration.BUNDLE_TAG_CUSTOM_MAP_MARKER, marker);

        StoryTab storyTab = new StoryTab();
        storyTab.setArguments(bundle);
        return storyTab;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.readArgumentsBundle(getArguments());
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        this.viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        this.storyTabPagerAdapter = new StoryTabPagerAdapter(getFragmentManager(), customMapMarker);
        this.viewPager.setAdapter(null);
        this.viewPager.setAdapter(storyTabPagerAdapter);
        this.viewPager.setPageTransformer(true, new RotateUpTransformer()); //changes the standard animation for swiping tabs. gradle addition needed for this...
        this.context = view.getContext();

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        setRetainInstance(true);
        return view;
    }



    private void readArgumentsBundle(Bundle bundle)
    {
        if(bundle != null)
        {
            this.customMapMarker = bundle.getParcelable(AppConfiguration.BUNDLE_TAG_CUSTOM_MAP_MARKER);
        }
    }

    private void buildImageListPicasso() //need to handle data for images here...
    {

        for(StoryItemDetails storyItem: customMapMarker.getFileList())
        {
            String fileName = storyItem.getFileName();

            Picasso.with(getContext())
                    .load(AppConfiguration.URL_IMAGES_SQUARE_THUMBNAILS + fileName)
                    .resize(250,250); //will resize the original to square which makes everything render nice.....this needs work, What I want to do next is make two lists, one resized that is displayed, and another that is the original for the picture dialog....
                    //.centerInside()  //goes with aboive....and causes some additional problems
        }
    }
}
