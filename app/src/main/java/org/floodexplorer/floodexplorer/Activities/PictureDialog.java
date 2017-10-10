package org.floodexplorer.floodexplorer.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.floodexplorer.floodexplorer.AppConfiguration;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.StoryItemDetails;
import org.floodexplorer.floodexplorer.R;

import java.util.ArrayList;

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
    private ArrayList<ImageView> imageList;
    private RelativeLayout.LayoutParams layoutParams;

    public PictureDialog() //this should be done in a fragment bundle......
    {
        this.layoutParams = new RelativeLayout.LayoutParams(1000,1000);
    }

    public static PictureDialog newInstance(ImageView imageView)
    {

        Bundle bundle = new Bundle();
        imageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = imageView.getDrawingCache();
        bundle.putParcelable(AppConfiguration.BUNDLE_TAG_PICTURE_DIALOGUE_IMAGE, bitmap);

        StoryItemDetails storyItem = (StoryItemDetails) imageView.getTag();
        bundle.putSerializable(AppConfiguration.BUNDLE_TAG_PICTURE_DIALOGUE_ITEM_DETAILS, storyItem);

        PictureDialog pictureDialog = new PictureDialog();
        pictureDialog.setArguments(bundle);
        return pictureDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.readArgumentsBundle(getArguments());
        View rootView = inflater.inflate(R.layout.dialog_picture, container, false);
        getDialog().setTitle("Simple Dialog"); //might want to set this in config
        Button dismiss = (Button) rootView.findViewById(R.id.dismissBtn);
        dismiss.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.layoutParams = new RelativeLayout.LayoutParams(1000,1000);
        this.dialogImageView = (ImageView) rootView.findViewById(R.id.dialogImage);
        this.txtImageTitle = (TextView) rootView.findViewById(R.id.txtImageTitle);
        this.txtImageCaption = (TextView) rootView.findViewById(R.id.txtImageCaption);
        this.setStoryItemData();

        setRetainInstance(true); //this is why rotation is currently working it might not be the best way to do this
        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

    private void readArgumentsBundle(Bundle bundle)
    {
        if(bundle != null)
        {
            Bitmap bitmap = bundle.getParcelable(AppConfiguration.BUNDLE_TAG_PICTURE_DIALOGUE_IMAGE);
            ImageView passedImageView = new ImageView(getContext());
            passedImageView.setImageBitmap(bitmap);
            this.imageView = passedImageView;

            StoryItemDetails storyItemPassed = (StoryItemDetails) bundle.getSerializable(AppConfiguration.BUNDLE_TAG_PICTURE_DIALOGUE_ITEM_DETAILS);
            this.storyItem = storyItemPassed;
        }
    }

    private void setStoryItemData()
    {
        this.dialogImageView.setImageDrawable(imageView.getDrawable());
        this.configureImageView();
        this.txtImageTitle.setText(storyItem.getFileTitle());
        this.txtImageCaption.setText(storyItem.getFileCaption());
    }

    private void configureImageView()
    {
        this.dialogImageView.setLayoutParams(layoutParams);
        this.dialogImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }
}
