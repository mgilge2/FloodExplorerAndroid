package org.floodexplorer.floodexplorer.Activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.floodexplorer.floodexplorer.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment
{

    public static AboutFragment newInstance()
    {
        AboutFragment aboutFragment = new AboutFragment();
        return aboutFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        this.setNavigationTitle();
        return view;
    }

    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

    private void setNavigationTitle()
    {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        View view = actionBar.getCustomView();
        TextView textView = (TextView) view.findViewById(R.id.navTitleTxt);
        textView.setText("About");
    }
}
