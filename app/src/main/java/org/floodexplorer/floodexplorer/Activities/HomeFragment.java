package org.floodexplorer.floodexplorer.Activities;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.floodexplorer.floodexplorer.SupportingFiles.AppConfiguration;
import org.floodexplorer.floodexplorer.R;


public class HomeFragment extends Fragment
{
    private String aboutTxt;
    private TextView textView;
    private CarouselView carouselView;
    private ImageListener imageListener;
    private int[] sampleImages;



    public static HomeFragment newInstance(String aboutTxt)
    {
        Bundle bundle = new Bundle();
        bundle.putString(AppConfiguration.BUNDLE_TAG_HOME_ABOUT, aboutTxt);

        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.readArgumentsBundle(getArguments());

        //some phones have issues with more images being loaded into the carousel....keep under a meg of images if at all possible

        sampleImages = new int[] {R.drawable.home1, R.drawable.home2, R.drawable.home3, R.drawable.home4, R.drawable.home5, R.drawable.home6, R.drawable.home7, R.drawable.home8, R.drawable.home9, R.drawable.home10};


        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.textView = (TextView) view.findViewById(R.id.homeTxtView);
        this.textView.setText(aboutTxt);
        imageListener = new ImageListener()
        {
            @Override
            public void setImageForPosition(int position, ImageView imageView)
            {
                imageView.setImageResource(sampleImages[position]);
            }
        };
        carouselView = (CarouselView)view.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

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
            this.aboutTxt = bundle.getString(AppConfiguration.BUNDLE_TAG_HOME_ABOUT);
        }
    }
}
