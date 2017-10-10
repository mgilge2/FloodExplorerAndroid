package org.floodexplorer.floodexplorer;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

/**
 * Created by mgilge on 7/19/17.
 * this file contains the info used to connect and query the remote database for floodexplorer.org
 * it also contains the tags that are used in parsing the jSON array that is returned from the server
 * Enum anyone say enum?
 */

public class AppConfiguration
{
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

    public static final ColorDrawable BtmNavBarColor = new ColorDrawable(Color.parseColor("#23B0FC"));
    public static final ColorDrawable ActionBarColor = new ColorDrawable(Color.parseColor("#23B0FC"));

}