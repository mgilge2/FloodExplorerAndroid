package org.floodexplorer.floodexplorer.Activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.floodexplorer.floodexplorer.AppConfiguration;
import org.floodexplorer.floodexplorer.R;


public class HomeFragment extends Fragment
{
    private String aboutTxt;
    private TextView textView;

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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
       // this.setNavigationTitle();
        this.textView = (TextView) view.findViewById(R.id.homeTxtView);
        this.textView.setText(aboutTxt);
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

    private void setNavigationTitle()
    {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        View view = actionBar.getCustomView();
        //TextView textView = (TextView) view.findViewById(R.id.navTitleTxt);
        //textView.setText("Home");
    }
}
