package org.floodexplorer.floodexplorer.SupportingFiles;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

import org.floodexplorer.floodexplorer.R;

/**
 * Created by mgilge on 7/19/17.
 * this file contains the info used to connect and query the remote database for floodexplorer.org
 * it also contains the tags that are used in parsing the jSON array that is returned from the server
 * Enum anyone say enum?
 */

public class AppConfiguration
{
    //General seettings....
    public static final String APP_LANGUAGE =  "english";

    //Details for layouts such as color for mav bars, images, etc.
   // public static final String BtmNavBarColor = "#23B0FC"; //dodger blue color
    public static final String BtmNavBarIconColor = "#FFFFFF"; //icon color white
    public static final ColorDrawable BtmNavBarColor = new ColorDrawable(Color.parseColor("#176130"));
    public static final ColorDrawable ActionBarColor = new ColorDrawable(Color.parseColor("#176130"));

    //Web addresses for loading images and other things...
    public static final String URL_IMAGES_SQUARE_THUMBNAILS = "http://floodexplorer.org//files/square_thumbnails/";
    public static final String URL_IMAGES_ORIGINAL = "http://floodexplorer.org//files/original/";
    public static final String URL_DEFAULT_IMAGE = "http://floodexplorer.com/rockhammer.png";
    public static final String URL_GOOGLE_MAPS_ROUTING = "https://maps.googleapis.com/maps/";

    //Tags for saving things into bundles....
    public static final String BUNDLE_TAG_OMEKA_DATA_ITEMS = "omekaDataList";
    public static final String BUNDLE_TAG_NAVIGATION_LOCATION = "selectedNav";
    public static final String BUNDLE_TAG_HOME_RESULTS = "homeQueryResults";
    public static final String BUNDLE_TAG_HOME_ABOUT = "aboutText";
    public static final String BUNDLE_TAG_PICTURE_DIALOGUE_IMAGE = "image";
    public static final String BUNDLE_TAG_PICTURE_DIALOGUE_ITEM_DETAILS ="storyItem";
    public static final String BUNDLE_TAG_CUSTOM_MAP_MARKER = "customMarker";

    //initial google map settings specific to data collection....
    public static final LatLng MAP_STARTING_POINT = new LatLng(47.47398, -118.5489564);
    public static final float MAP_STARTING_ZOOM =  6.8f;

    //App Colors
    public static final ColorDrawable LIST_BACK_COLOR = new ColorDrawable(Color.TRANSPARENT);
    public static final ColorDrawable LIST_SELECT_COLOR = new ColorDrawable(Color.CYAN);


}