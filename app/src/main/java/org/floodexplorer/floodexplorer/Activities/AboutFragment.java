package org.floodexplorer.floodexplorer.Activities;


import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import org.floodexplorer.floodexplorer.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment
{
    private VideoView videoView;
    private MediaController mediaControls;
    private ProgressDialog progressDialog;
    private int position = 0;

    public static AboutFragment newInstance()
    {
        AboutFragment aboutFragment = new AboutFragment();
        return aboutFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_about, container, false);


        if(mediaControls == null)
        {
            this.mediaControls = new MediaController(view.getContext());
        }

        this.videoView = (VideoView) view.findViewById(R.id.videoView);

        // create a progress bar while the video file is loading
        progressDialog = new ProgressDialog(getContext());
        // set a title for the progress bar
        progressDialog.setTitle("JavaCodeGeeks Android Video View Example");
        // set a message for the progress bar
        progressDialog.setMessage("Loading...");
        //set the progress bar not cancelable on users' touch
        progressDialog.setCancelable(false);
        // show the progress bar
    //    progressDialog.show();

        try
        {
            //set the media controller in the VideoView
            videoView.setMediaController(mediaControls);
            //set the uri of the video to be played

            String videoURL = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.tom;
            videoView.setVideoURI(Uri.parse(videoURL));
        }
        catch (Exception e)
        {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }


        view.requestFocus();
        //we also set an setOnPreparedListener in order to know when the video file is ready for playback
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                // close the progress bar and play the video
                progressDialog.dismiss();
                //if we have a position on savedInstanceState, the video playback should start from here
                videoView.seekTo(position);
                if (position == 0) {
                    videoView.start();
                } else {
                    //if we come from a resumed activity, video playback will be paused
                    videoView.pause();
                }
            }
        });

     //   this.setNavigationTitle();
        setRetainInstance(true); //this is why rotation is currently working it might not be the best way to do this
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        //we use onSaveInstanceState in order to store the video playback position for orientation change
        savedInstanceState.putInt("Position", videoView.getCurrentPosition());
        videoView.pause();

    }


    public void onRestoreState(Bundle savedState)
    {
        position = savedState.getInt("Position");
        videoView.seekTo(position);
        videoView.resume();
    }


    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

    private void setNavigationTitle()
    {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        View view = actionBar.getCustomView();
        //TextView textView = (TextView) view.findViewById(R.id.navTitleTxt);
        //textView.setText("About");
    }
}
