package org.floodexplorer.floodexplorer.Activities.StoryTab;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.floodexplorer.floodexplorer.Activities.PictureDialog;
import org.floodexplorer.floodexplorer.Activities.StoryTabFragment;
import org.floodexplorer.floodexplorer.AppConfiguration;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.StoryItemDetails;
import org.floodexplorer.floodexplorer.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PictureFragment extends Fragment
{
    private ImageView dialogImageView;
    private Context context;
    private StoryItemDetails storyItem;
    private TextView txtImageTitle;
    private TextView txtImageCaption;


    public static PictureFragment newInstance(ImageView imageView)
    {
        Bundle bundle = new Bundle();
        imageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = imageView.getDrawingCache();
        bundle.putParcelable(AppConfiguration.BUNDLE_TAG_PICTURE_DIALOGUE_IMAGE, bitmap);

        StoryItemDetails storyItem = (StoryItemDetails) imageView.getTag();
        bundle.putSerializable(AppConfiguration.BUNDLE_TAG_PICTURE_DIALOGUE_ITEM_DETAILS, storyItem);

        PictureFragment pictureFragment = new PictureFragment();
        pictureFragment.setArguments(bundle);
        return pictureFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment\\
        View view = inflater.inflate(R.layout.fragment_picture, container, false);

        this.context = view.getContext();
        this.readArgumentsBundle(getArguments());
        this.txtImageTitle = (TextView) view.findViewById(R.id.txtImageTitle);
        this.txtImageTitle.setText(storyItem.getFileTitle());

        setRetainInstance(true);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null)
        {
            Bitmap bitmap = savedInstanceState.getParcelable(AppConfiguration.BUNDLE_TAG_PICTURE_DIALOGUE_IMAGE);
            ImageView passedImageView = new ImageView(getContext());
            passedImageView.setImageBitmap(bitmap);
            this.dialogImageView = passedImageView;

            this.storyItem = (StoryItemDetails) savedInstanceState.getSerializable("storyItemDetails");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);

        dialogImageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = dialogImageView.getDrawingCache();
        savedInstanceState.putParcelable(AppConfiguration.BUNDLE_TAG_PICTURE_DIALOGUE_IMAGE, bitmap);        savedInstanceState.putSerializable("storyItemDetails", AppConfiguration.BUNDLE_TAG_PICTURE_DIALOGUE_ITEM_DETAILS);
    }


    private void readArgumentsBundle(Bundle bundle)
    {
        if(bundle != null)
        {
            Bitmap bitmap = bundle.getParcelable(AppConfiguration.BUNDLE_TAG_PICTURE_DIALOGUE_IMAGE);
            ImageView passedImageView = new ImageView(getContext());
            passedImageView.setImageBitmap(bitmap);
            this.dialogImageView = passedImageView;

            StoryItemDetails storyItemPassed = (StoryItemDetails) bundle.getSerializable(AppConfiguration.BUNDLE_TAG_PICTURE_DIALOGUE_ITEM_DETAILS);
            this.storyItem = storyItemPassed;
        }
    }
}
