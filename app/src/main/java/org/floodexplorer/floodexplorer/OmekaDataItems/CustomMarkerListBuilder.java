package org.floodexplorer.floodexplorer.OmekaDataItems;

import android.text.Html;
import android.text.Spanned;

import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.StoryItemDetails;
import org.floodexplorer.floodexplorer.RESTConfig;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mgilge on 10/18/17.
 */

public class CustomMarkerListBuilder
{
    private String omekaRestGeoResults;
    private String omekaRestItemResults;
    private String omekaRestFilesResults;
    private ArrayList<CustomMapMarker> omekaDataItems;

    public  ArrayList<CustomMapMarker> buildOmekaDataItems(ArrayList<String> restResults)
    {
        this.omekaRestGeoResults = restResults.get(0);
        this.omekaRestItemResults = restResults.get(1);
        this.omekaRestFilesResults = restResults.get(2);

        this.omekaDataItems = new ArrayList<CustomMapMarker>();

        this.parseRESTJsonResults();
        return omekaDataItems;
    }

    public String parseRestSimplePages(String restResults)
    {
        String ret = "";
        try
        {
            JSONArray jsonArray = new JSONArray(restResults);
            String results = "";
            for(int x = 0; x < jsonArray.length(); x++)
            {
                JSONObject object = jsonArray.getJSONObject(x);
                results += object.getString(RESTConfig.TAG_TEXT);
            }
            //replace all occurrences of one or more HTML tags with optional
            // whitespace inbetween with a single space character
            String strippedText = results.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
            Spanned htmlAsSpanned = Html.fromHtml(results); // used by TextView
            ret = htmlAsSpanned.toString();
        }
        catch (Exception e)
        {
            StackTraceElement element = e.getStackTrace()[0];
            e.printStackTrace();
        }
        return ret;
    }

    private void parseRESTJsonResults()
    {
        try
        {
            JSONArray filesArray = new JSONArray(omekaRestFilesResults);
            JSONArray itemTextsArray = new JSONArray(omekaRestItemResults);
            JSONArray locationsArray = new JSONArray(omekaRestGeoResults);
            this.buildCustomMarkerList(locationsArray, itemTextsArray, filesArray);
        }
        catch (Exception e)
        {
            //toDo handle this if it occurs...
            e.printStackTrace();
        }
    }

    private void buildCustomMarkerList(JSONArray locationsArray, JSONArray itemTextsArray, JSONArray filesArray)
    {
        try
        {
            for(int x = 0; x <  locationsArray.length(); x ++)
            {
                JSONObject locObject = locationsArray.getJSONObject(x);
                double latitude = locObject.getDouble(RESTConfig.TAG_LAT);
                double longitude = locObject.getDouble(RESTConfig.TAG_LONG);
                JSONObject locItem = locObject.getJSONObject(RESTConfig.TAG_ITEM);
                float zoom = locObject.getInt(RESTConfig.TAG_ZOOM);
                int locId = locItem.getInt(RESTConfig.TAG_ID);
                String title = "";
                String storyText = "";

                for(int y = 0; y < itemTextsArray.length(); y ++)
                {
                    JSONObject itemTextObject = itemTextsArray.getJSONObject(y);
                    int itemElementID = itemTextObject.getInt(RESTConfig.TAG_ID);
                    JSONArray itemElementTexts = itemTextObject.getJSONArray(RESTConfig.TAG_ELEMENT_TEXTS);
                    for(int index = 0; index < itemElementTexts.length(); index ++)
                    {
                        JSONObject itemElement = itemElementTexts.getJSONObject(index);
                        JSONObject obj = itemElement.getJSONObject(RESTConfig.TAG_ELEMENT_SET);
                        if(itemElementID == locId)
                        {
                            if(index == 0)
                            {
                                title = itemElement.getString(RESTConfig.TAG_TEXT);
                            }
                            else
                            {
                                storyText += itemElement.getString(RESTConfig.TAG_TEXT) +"\n\n";  //this is where we need to deal with getting things specific such as author and sources
                            }
                        }
                    }
                }
                CustomMapMarker addMarker = new CustomMapMarker(latitude, longitude, title, "", zoom, storyText, locId);
                this.omekaDataItems.add(addMarker);
            }
            this.addStoryItemsToCustomMarkers(filesArray);
        }
        catch (Exception e)
        {
            //// TODO: 10/15/17 need to handle this if it happens.....
            e.printStackTrace();
        }
    }

    private void addStoryItemsToCustomMarkers(JSONArray filesArray)
    {
        try
        {
            for(int x = 0; x < filesArray.length(); x ++)
            {
                String results = "";
                JSONObject object = filesArray.getJSONObject(x);
                String fileName = object.getString(RESTConfig.TAG_FILENAME);

                JSONObject item = object.getJSONObject(RESTConfig.TAG_ITEM);
                int id = item.getInt(RESTConfig.TAG_ID);

                JSONArray array = object.getJSONArray(RESTConfig.TAG_ELEMENT_TEXTS);
                String picTitle = "";
                ArrayList<String> list = new ArrayList<String>();
                for(int y = 0; y < array.length(); y++)
                {
                    JSONObject obj = array.getJSONObject(y);
                    list.add(obj.getString(RESTConfig.TAG_TEXT));
                }

                for(int index = 0; index < list.size(); index ++)
                {
                    if(index == 0)
                    {
                        picTitle = list.get(0);
                    }
                    else
                    {
                        results += list.get(index) + "\n\n";
                    }
                }
                for(CustomMapMarker customMarker: omekaDataItems)
                {
                    if(customMarker.getId() == id && fileExcluder(fileName))
                    {
                        customMarker.addFileToMarker(new StoryItemDetails(fileName, picTitle, results));
                    }
                }
            }
        }
        catch (Exception e)
        {
            StackTraceElement element = e.getStackTrace()[0];
            e.printStackTrace();
        }
    }

    private boolean fileExcluder(String fileName)
    {
        boolean ret = false;

        if(fileName.matches("(.*)jpeg"))
        {
            ret = true;
        }
        if(fileName.matches("(.*)JPG"))
        {
            ret = true;
        }
        if(fileName.matches("(.*)jpg"))
        {
            ret = true;
        }
        if(fileName.matches("(.*)png"))
        {
            ret = true;
        }

        return ret;
    }
}