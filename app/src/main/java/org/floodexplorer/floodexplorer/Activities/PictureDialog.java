package org.floodexplorer.floodexplorer.Activities;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.floodexplorer.floodexplorer.OmekaDataItems.Adapters.RecyclerAdapters.StoryItemsRecycler.StoryItemRecycler;
import org.floodexplorer.floodexplorer.SupportingFiles.TouchImageView;
import org.floodexplorer.floodexplorer.SupportingFiles.AppConfiguration;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.StoryItemDetails;
import org.floodexplorer.floodexplorer.R;

/**
 * Created by mgilge on 10/3/17.
 */

public class PictureDialog extends DialogFragment
{
    private TouchImageView dialogImageView;
    private TextView txtImageTitle;
    private TextView txtImageCaption;
    private StoryItemDetails storyItem;
    private RelativeLayout.LayoutParams layoutParams;
    private StoryItemRecycler storyItemRecycler;
    private ProgressBar progressBar;

    public static PictureDialog newInstance(StoryItemDetails storyItemDetails, StoryItemRecycler storyItemRecycler)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConfiguration.BUNDLE_TAG_PICTURE_DIALOGUE_ITEM_DETAILS, storyItemDetails);
        bundle.putSerializable("adapter", storyItemRecycler);
        PictureDialog pictureDialog = new PictureDialog();
        pictureDialog.setArguments(bundle);
        return pictureDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.readArgumentsBundle(getArguments());
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE); //this gets rid of title on older phones specifically...has to be done before inflation
        View rootView = inflater.inflate(R.layout.dialog_picture, container, false);

        ImageButton dismiss = (ImageButton) rootView.findViewById(R.id.dismissBtn);
        dismiss.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                storyItemRecycler.setSelectedStoryTitle(null);
                dismiss();
            }
        });

        this.layoutParams = new RelativeLayout.LayoutParams(1000, 1000);
        this.dialogImageView = (TouchImageView) rootView.findViewById(R.id.dialogImage);
        this.txtImageTitle = (TextView) rootView.findViewById(R.id.txtImageTitle);
        this.txtImageCaption = (TextView) rootView.findViewById(R.id.txtImageCaption);
        this.progressBar = (ProgressBar) rootView.findViewById(R.id.picassoProg);
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
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

   @Override
   public void onPause()
   {
       //dismiss the dialog when the app goes to the background....
       super.onPause();
       this.dismiss();
   }


    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

    private void readArgumentsBundle(Bundle bundle)
    {
        if(bundle != null)
        {
            StoryItemDetails storyItemPassed = (StoryItemDetails) bundle.getSerializable(AppConfiguration.BUNDLE_TAG_PICTURE_DIALOGUE_ITEM_DETAILS);
            this.storyItem = storyItemPassed;
            this.storyItemRecycler = (StoryItemRecycler) bundle.getSerializable("adapter");
        }
    }

    private void buildImagePicasso()
    {

        String fileName = storyItem.getFileName();
        Point point = getDisplaySize();
        Picasso.with(getContext())
                .load(AppConfiguration.URL_IMAGES_ORIGINAL + fileName)
                .resize(point.x, point.y)
                .centerInside()
                .into(dialogImageView, new Callback()
                {
                    @Override
                    public void onSuccess()
                    {
                        progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onError()
                    {

                    }
                });
    }


    private Point getDisplaySize()
    {
        Display display = getDialog().getWindow().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    private void setStoryItemData()
    {
        buildImagePicasso();
        this.configureImageView();
        this.txtImageTitle.setText(storyItem.getFileTitle());
        this.txtImageCaption.setText(storyItem.getFileCaption());
    }

    private void configureImageView()
    {
        //older sdk's have issues displaying the dialog with the given layout parameters
        if (Build.VERSION.SDK_INT < 21)
        {
            layoutParams = new RelativeLayout.LayoutParams(700, 700);
        }
        this.dialogImageView.setLayoutParams(layoutParams);
    }
}
