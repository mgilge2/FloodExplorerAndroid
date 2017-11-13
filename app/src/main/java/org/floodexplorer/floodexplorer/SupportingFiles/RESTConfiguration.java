package org.floodexplorer.floodexplorer.SupportingFiles;

import java.security.PublicKey;

/**
 * Created by mgilge on 10/15/17.
 */

public class RESTConfiguration
{

    /*
    //Web addresses for REST
    public static final String REST_URL_ITEMS = "http://floodexplorer.com/curatescape/api/items";
    public static final String REST_URL_FILES = "http://floodexplorer.com/curatescape/api/files";
    public static final String REST_URL_GEOLOC = "http://floodexplorer.com/curatescape/api/geolocations";
    public static final String REST_SIMPLE_PAGES = "http://floodexplorer.com/curatescape/api/simple_pages";
*/
    //Web addresses for REST
    public static final String REST_URL_ITEMS = "http://floodexplorer.org/api/items";
    public static final String REST_URL_FILES = "http://floodexplorer.org/api/files";
    public static final String REST_URL_GEOLOC = "http://floodexplorer.org/api/geolocations";
    public static final String REST_SIMPLE_PAGES = "http://floodexplorer.org/api/simple_pages";

    //Tags for JSON parsing of REST results
    public static final String TAG_LAT = "latitude";
    public static final String TAG_LONG = "longitude";
    public static final String TAG_ITEM = "item";
    public static final String TAG_ZOOM = "zoom_level";
    public static final String TAG_ID = "id";
    public static final String TAG_ELEMENT_TEXTS = "element_texts";
    public static final String TAG_ELEMENT_SET = "element_set";
    public static final String TAG_TEXT = "text";
    public static final String TAG_FILENAME = "filename";
}
