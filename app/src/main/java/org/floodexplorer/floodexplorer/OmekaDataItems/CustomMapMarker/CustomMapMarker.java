package org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import org.floodexplorer.floodexplorer.AppConfiguration;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mgilge on 7/18/17.
 * this class could be extended to store all of the data downloaded from the database
 * in other words we can make an object that follows these guidlines and store all data associated
 * with a sepcific item within this class
 */

public class CustomMapMarker implements ClusterItem, Parcelable, Serializable
{
    private LatLng mPosition;
    private String mTitle;
    private String mSnippet;
    private String storyText;
    private double zoom;
    private ArrayList<StoryItemDetails> fileList;
    private int id;


    private String storyResources;

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
        this.mSnippet = snippet; //disabling snippet = ""
        this.fileList = new ArrayList<StoryItemDetails>();
    }

    public CustomMapMarker(double lat, double lng, String title, String snippet, double zoom, String storyText, int id, String storyResources)
    {

        this.mPosition = new LatLng(lat, lng);
        this.mTitle = title;
        this.mSnippet = snippet; //disabling snippet = ""
        this.storyText = storyText;
        this.zoom = zoom;
        this.fileList = new ArrayList<StoryItemDetails>();
        this.id = id;
        this.storyResources = storyResources;
    }

    public CustomMapMarker(Parcel parcel)
    {
        LatLng latLng = new LatLng(parcel.readDouble(), parcel.readDouble());
        this.mPosition = latLng;
        this.mTitle = parcel.readString();
        this.mSnippet = parcel.readString();
        this.storyText = parcel.readString();
        this.zoom = parcel.readDouble();
        this.fileList = parcel.readArrayList(ClassLoader.getSystemClassLoader());
        this.id = parcel.readInt();
    }

    public static final Creator<CustomMapMarker> CREATOR = new Creator<CustomMapMarker>()
    {
        @Override
        public CustomMapMarker createFromParcel(Parcel in) {
            return new CustomMapMarker(in);
        }

        @Override
        public CustomMapMarker[] newArray(int size) {
            return new CustomMapMarker[size];
        }
    };

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
            return AppConfiguration.URL_IMAGES_SQUARE_THUMBNAILS + fileList.get(0).getFileName();
        }
        else
        {
            return AppConfiguration.URL_DEFAULT_IMAGE;
        }
    }

    public String getStoryResources()
    {
        return storyResources;
    }

    public int getId() {
        return id;
    }

    public ArrayList<StoryItemDetails> getFileList()
    {
        return this.fileList;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeDouble(mPosition.longitude);
        dest.writeDouble(mPosition.latitude);
        dest.writeString(mTitle);
        dest.writeString(mSnippet);
        dest.writeString(storyText);
        dest.writeDouble(zoom);
        dest.writeList(fileList);
        dest.writeInt(id);
    }
}
