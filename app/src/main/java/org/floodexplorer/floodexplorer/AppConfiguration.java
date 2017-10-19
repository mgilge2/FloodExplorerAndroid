package org.floodexplorer.floodexplorer;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.google.android.gms.maps.model.LatLng;

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

    //Address of our scripts of queries to the Omeka database
    public static final String URL_ADD="http://www.floodexplorer.com/addEmp.php";
    public static final String URL_GET_LOCATIONS = "http://www.floodexplorer.com/getAllLocations.php";
    public static final String URL_GET_STORYITEMS = "http://www.floodexplorer.com/getAllStoryItems.php";
    public static final String URL_GET_HOMEITEMS = "http://www.floodexplorer.com/getAboutPage.php";
    public static final String URL_UPDATE_EMP = "http://www.floodexplorer.com/updateEmp.php";
    public static final String URL_DELETE_EMP = "http://www.floodexplorer.com/deleteEmp.php?id=";

    //Keys that can be used to send the request to php scripts
    public static final String KEY_EMP_ID = "id";
    public static final String KEY_EMP_NAME = "name";
    public static final String KEY_EMP_DESG = "desg";
    public static final String KEY_EMP_SAL = "salary";

    //JSON Tags for Location query
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_LAT = "latitude";
    public static final String TAG_LONG = "longitude";
    public static final String TAG_TITLE = "title";
    public static final String TAG_TEXT = "text";
    public static final String TAG_ZOOM = "zoom";

    //JSON Tags for Story Items query
    public static final String TAG_ID="id";
    public static final String TAG_FILE = "file";
    public static final String TAG_ITEM_TITLE = "title";
    public static final String TAG_CAPTION = "caption";

    //JSON Tags for Home Items query
    public static final String TAG_ABOUT="about";

    //Details for layouts such as color for mav bars, images, etc.
   // public static final String BtmNavBarColor = "#23B0FC"; //dodger blue color
    public static final String BtmNavBarIconColor = "#FFFFFF"; //icon color white
    public static final ColorDrawable BtmNavBarColor = new ColorDrawable(Color.parseColor("#176130"));
    public static final ColorDrawable ActionBarColor = new ColorDrawable(Color.parseColor("#176130"));

    /*
    //Web addresses for loading images and other things...
    public static final String URL_IMAGES_SQUARE_THUMBNAILS = "http://floodexplorer.com/curatescape/files/square_thumbnails/";
    public static final String URL_IMAGES_ORIGINAL = "http://floodexplorer.com/curatescape/files/original/";
    public static final String URL_DEFAULT_IMAGE = "http://floodexplorer.com/rockhammer.png";
    public static final String URL_GOOGLE_MAPS_ROUTING = "https://maps.googleapis.com/maps/";
*/

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

    //Stuff to do with formatting
    public static final String FORMAT_LINE_SEPARATOR = "line.separator";
}