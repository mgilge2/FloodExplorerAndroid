package org.floodexplorer.floodexplorer.Activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.StoryItemDetails;
import org.floodexplorer.floodexplorer.R;

/**
 * Created by mgilge on 10/3/17.
 */

public class PictureDialog extends DialogFragment
{
    private ImageView imageView;
    private ImageView dialogImageView;
    private TextView txtImageTitle;
    private TextView txtImageCaption;
    private StoryItemDetails storyItem;


    public PictureDialog(ImageView imageView) //this should be done in a fragment bundle......
    {
        this.imageView = imageView;
        Object obj = imageView.getTag();
        this.storyItem = (StoryItemDetails) imageView.getTag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.dialog_picture, container, false);
        getDialog().setTitle("Simple Dialog");

        Button dismiss = (Button) rootView.findViewById(R.id.dismissBtn);
        dismiss.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.dialogImageView = (ImageView) rootView.findViewById(R.id.dialogImage);
        this.txtImageTitle = (TextView) rootView.findViewById(R.id.txtImageTitle);
        this.txtImageCaption = (TextView) rootView.findViewById(R.id.txtImageCaption);
        this.setStoryItemData();

        return rootView;
    }

    //Private implementation
    private void setStoryItemData()
    {
        this.dialogImageView.setImageDrawable(imageView.getDrawable());
        this.txtImageTitle.setText(storyItem.getFileTitle());
        this.txtImageCaption.setText(storyItem.getFileCaption());
    }
}
