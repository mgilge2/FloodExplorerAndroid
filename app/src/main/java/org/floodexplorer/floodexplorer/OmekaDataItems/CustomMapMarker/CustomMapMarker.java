package org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterItem;

import org.floodexplorer.floodexplorer.Activities.StoryTab.StoryTabActivity;
import org.floodexplorer.floodexplorer.R;

import java.util.ArrayList;

/**
 * Created by mgilge on 7/18/17.
 * this class could be extended to store all of the data downloaded from the database
 * in other words we can make an object that follows these guidlines and store all data associated
 * with a sepcific item within this class
 */

public class CustomMapMarker implements ClusterItem
{
    private LatLng mPosition;
    private String mTitle;
    private String mSnippet;
    private String storyText;
    private double zoom;
    private ArrayList<StoryItemDetails> fileList;
    private int id;

    public CustomMapMarker(double lat, double lng)
    {
        super();
        this.mPosition = new LatLng(lat, lng);
        this.fileList = new ArrayList<StoryItemDetails>();
    }


    public CustomMapMarker(double lat, double lng, String title, String snippet)
    {

        this.mPosition = new LatLng(lat, lng);
        this.mTitle = title;
        this.mSnippet = ""; //disabling snippet = ""
        this.fileList = new ArrayList<StoryItemDetails>();
    }

    public CustomMapMarker(double lat, double lng, String title, String snippet, double zoom, String storyText, int id)
    {

        this.mPosition = new LatLng(lat, lng);
        this.mTitle = title;
  //      this.mSnippet = ""; //disabling snippet = ""
        this.storyText = storyText;
        this.zoom = zoom;
        this.fileList = new ArrayList<StoryItemDetails>();
        this.id = id;
    }

    public void addFileToMarker(StoryItemDetails storyItem)
    {
        this.fileList.add(storyItem);
    }

    @Override
    public LatLng getPosition()
    {
        return this.mPosition;
    }

    @Override
    public String getTitle()
    {
        return this.mTitle;
    }

    @Override
    public String getSnippet()
    {
        return this.mSnippet;
    }

    public double getZoom() {
        return zoom;
    }

    public String getStoryText() {
        return storyText;
    }

    public String getStoryImgageUrl()
    {

        //todo move url string to config file
        if(fileList.size() != 0)
        {
            return "http://floodexplorer.com/curatescape/files/square_thumbnails/" + fileList.get(0).getFileName();
        }
        else
        {
            return "http://floodexplorer.com/rockhammer.png";
        }

       // return "http://floodexplorer.com/rockhammer.png";
    }

    public int getId() {
        return id;
    }

    public ArrayList<StoryItemDetails> getFileList()
    {
        return this.fileList;
    }



}
