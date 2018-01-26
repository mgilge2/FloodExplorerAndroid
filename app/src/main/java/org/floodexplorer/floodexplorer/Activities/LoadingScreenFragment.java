package org.floodexplorer.floodexplorer.Activities;

/*
This is just a loading screen that is displayed until the initial REST request is completed
 */
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.floodexplorer.floodexplorer.R;


public class LoadingScreenFragment extends Fragment
{
    private ProgressBar mBar;

    public static LoadingScreenFragment newInstance()
    {
        LoadingScreenFragment fragment = new LoadingScreenFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loading_screen, container, false);
        BottomNavigationView navigationView = (BottomNavigationView) view.findViewById(R.id.navigation);
        mBar= (ProgressBar) view.findViewById(R.id.progressBar2);
        this.customizeProgressBar();
        return view;
    }

    private void customizeProgressBar()
    {
        //Customize the color of the progress bar
        mBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#80DAEB"), android.graphics.PorterDuff.Mode.MULTIPLY);
    }
}
