package org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker;

/**
 * Created by mgilge on 10/5/17.
 */

public class StoryItemDetails
{
    private String fileName;
    private String fileTitle;
    private String fileCaption;

    public StoryItemDetails(String fileName, String fileTitle, String fileCaption)
    {
        this.fileName = fileName;
        this.fileTitle = fileTitle;
        this.fileCaption = fileCaption;
    }

    public String getFileName()
    {
        return fileName;
    }

    public String getFileTitle()
    {
        return fileTitle;
    }

    public String getFileCaption()
    {
        return fileCaption;
    }

}
