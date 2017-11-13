package org.floodexplorer.floodexplorer.OmekaDataItems.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.floodexplorer.floodexplorer.Activities.PictureDialog;
import org.floodexplorer.floodexplorer.SupportingFiles.AppConfiguration;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.StoryItemDetails;
import org.floodexplorer.floodexplorer.R;

import java.util.ArrayList;

/**
 * Created by mgilge on 11/2/17.
 */

public class StoryImagesListAdapter extends ArrayAdapter<StoryItemDetails>
{
    private View view;
    private ImageView storyImage;
    private TextView storyImageText;
    private Context context;
    private ArrayList<StoryItemDetails> storyItemDetails;
    private ArrayList<ImageView> imageList;


    public StoryImagesListAdapter(@NonNull Context context, ArrayList inFileList)
    {
        super(context,0, inFileList);
        this.context = context;
        this.storyItemDetails = inFileList;
        this.imageList = new ArrayList<ImageView>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View retView;
        StoryItemDetails storyItemDetails = getItem(position);
        if(convertView == null)
        {
            retView = LayoutInflater.from(getContext()).inflate(R.layout.story_images_row, parent, false);
            view = retView;

            this.storyImageText = retView.findViewById(R.id.storyImageTxt);
            this.storyImage = retView.findViewById(R.id.storyImg);
            this.storyImage.setTag(storyItemDetails);
            String url = AppConfiguration.URL_IMAGES_SQUARE_THUMBNAILS + storyItemDetails.getFileName();
            this.loadBitmapPicasso(position, storyImage,url);
            this.storyImageText.setText(storyItemDetails.getFileTitle());
            this.setItemRowListener(storyImage);
        }
        else
        {
            retView = (View) convertView;
        }

        return retView;
    }

    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

    private void setItemRowListener(final ImageView imageView)
    {
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                launchImageDialog(imageView);
            }
        });
    }

    private void launchImageDialog(ImageView imageView)
    {
        FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
        PictureDialog pictureDialog = PictureDialog.newInstance(imageView);
        pictureDialog.show(fm, "Dialog Show");
    }

    private void loadBitmapPicasso(int resId, ImageView imageView, String url)
    {

        Bitmap bitmap = null;
        if (bitmap != null)
        {
            storyImage.setImageBitmap(bitmap);
        }
        else
        {
            storyImage.setImageResource(R.drawable.rockhammer);
            try
            {

                Picasso.with(getContext())
                        .load(url)
                        .resize(250,250)
                        .into(storyImage);

            }
            catch (Exception e)
            {
                e.getStackTrace();
            }
        }
    }
}
