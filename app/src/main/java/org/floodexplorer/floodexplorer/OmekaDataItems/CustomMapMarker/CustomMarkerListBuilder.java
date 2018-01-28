package org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.StoryItemDetails;
import org.floodexplorer.floodexplorer.SupportingFiles.RESTConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mgilge on 10/18/17.
 * This should be thought of as the builder for my custom data class as the clustering markers built for this app will work for anything
 * with similar data and is not specific to Omeka itself.  This class should be rewritten once a better api for REST is made as we are using standard
 * Omeka rest api which isnt well formed
 */

public class CustomMarkerListBuilder
{
    private String omekaRestGeoResults;
    private String omekaRestItemResults;
    private String omekaRestFilesResults;
    private ArrayList<CustomMapMarker> omekaDataItems;
    private HashMap<String, CustomMapMarker> omekaDataMap;

    public  ArrayList<CustomMapMarker> buildOmekaDataItems(ArrayList<String> restResults)
    {
        this.omekaRestGeoResults = restResults.get(0);
        this.omekaRestItemResults = restResults.get(1);
        this.omekaRestFilesResults = restResults.get(2);

        this.omekaDataItems = new ArrayList<CustomMapMarker>();

        this.parseRESTJsonResults();

        this.buildMap();

        return omekaDataItems;
    }

    public HashMap<String, CustomMapMarker> buildOmekaDataMap(ArrayList<String> restResults)
    {
        this.omekaDataItems = this.buildOmekaDataItems(restResults);
        this.buildMap();
        return omekaDataMap;
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
                results += object.getString(RESTConfiguration.TAG_TEXT);
            }
            Spanned htmlAsSpanned = Html.fromHtml(results); // used by TextView
            ret = htmlAsSpanned.toString();
        }
        catch (Exception e)
        {
            Log.e("CustomMarkerListBuilder", e.getMessage());
        }
        return ret;
    }

    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

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
            Log.e("CustomMarkerListBuilder", e.getMessage());
        }
    }

    private void buildCustomMarkerList(JSONArray locationsArray, JSONArray itemTextsArray, JSONArray filesArray)
    {
        try
        {
            for(int x = 0; x <  locationsArray.length(); x ++)
            {
                JSONObject locObject = locationsArray.getJSONObject(x);
                double latitude = locObject.getDouble(RESTConfiguration.TAG_LAT);
                double longitude = locObject.getDouble(RESTConfiguration.TAG_LONG);
                JSONObject locItem = locObject.getJSONObject(RESTConfiguration.TAG_ITEM);
                float zoom = locObject.getInt(RESTConfiguration.TAG_ZOOM);
                int locId = locItem.getInt(RESTConfiguration.TAG_ID);
                String title = "";
                String storyText = "\t\t\t\t";
                String snippet = "";
                String resources = "";

                for(int y = 0; y < itemTextsArray.length(); y ++)
                {
                    JSONObject itemTextObject = itemTextsArray.getJSONObject(y);
                    int itemElementID = itemTextObject.getInt(RESTConfiguration.TAG_ID);
                    JSONArray itemElementTexts = itemTextObject.getJSONArray(RESTConfiguration.TAG_ELEMENT_TEXTS);
                    for(int index = 0; index < itemElementTexts.length(); index ++)
                    {
                        JSONObject itemElement = itemElementTexts.getJSONObject(index);
                        JSONObject obj = itemElement.getJSONObject(RESTConfiguration.TAG_ELEMENT_SET);
                        if(itemElementID == locId)
                        {
                            if(index == 0)
                            {
                                title = itemElement.getString(RESTConfiguration.TAG_TEXT);
                            }
                            else if(index == 3)
                            {
                                resources += itemElement.getString(RESTConfiguration.TAG_TEXT);
                            }
                            else
                            {
                                String str =  itemElement.getString(RESTConfiguration.TAG_TEXT) +"\n\n";
                                String split[] = str.split("\\s+");
                                if(split.length <= 1 )
                                {
                                   continue;
                                }
                                else
                                {
                                    if(split.length <= 5)
                                    {
                                        snippet = itemElement.getString(RESTConfiguration.TAG_TEXT) +"\n\n";
                                    }
                                    else
                                    {
                                        storyText += itemElement.getString(RESTConfiguration.TAG_TEXT) +"\n\n";  //this is where we need to deal with getting things specific such as author and sources
                                    }
                                }
                            }
                        }
                    }
                }
                storyText = storyText.replaceAll("\n","\n\t\t\t\t");
                if(resources.length() > 1)
                {
                    resources = "Resources: \n" + resources;
                }
                CustomMapMarker addMarker = new CustomMapMarker(latitude, longitude, title, snippet, zoom, storyText, locId, resources);
                this.omekaDataItems.add(addMarker);
            }
            this.addStoryItemsToCustomMarkers(filesArray);
        }
        catch (Exception e)
        {
            Log.e("CustomMarkerListBuilder", e.getMessage());
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
                String fileName = object.getString(RESTConfiguration.TAG_FILENAME);

                JSONObject item = object.getJSONObject(RESTConfiguration.TAG_ITEM);
                int id = item.getInt(RESTConfiguration.TAG_ID);

                JSONArray array = object.getJSONArray(RESTConfiguration.TAG_ELEMENT_TEXTS);
                String picTitle = "";
                ArrayList<String> list = new ArrayList<String>();
                for(int y = 0; y < array.length(); y++)
                {
                    JSONObject obj = array.getJSONObject(y);
                    list.add(obj.getString(RESTConfiguration.TAG_TEXT));
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
            Log.e("CustomMarkerListBuilder", e.getMessage());
        }
    }

    private void  buildMap()
    {
        this.omekaDataMap = new HashMap<String, CustomMapMarker>() ;

        for(CustomMapMarker customMarker : omekaDataItems)
        {
            this.omekaDataMap.put(customMarker.getTitle(), customMarker);
        }
    }

    //Eventually want to move this to a factory class to generate story items other than images, but only images were available at the time this was written
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
        if(fileName.matches("(.*)PNG"))
        {
            ret = true;
        }

        return ret;
    }
}
