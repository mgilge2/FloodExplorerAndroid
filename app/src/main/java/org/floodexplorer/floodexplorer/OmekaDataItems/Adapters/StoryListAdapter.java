package org.floodexplorer.floodexplorer.OmekaDataItems.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.floodexplorer.floodexplorer.Activities.StoryTab.StoryTabFragment;
import org.floodexplorer.floodexplorer.SupportingFiles.AppConfiguration;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.R;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mgilge on 7/21/17.
 */

public class StoryListAdapter extends ArrayAdapter<CustomMapMarker>
{
    private TextView storyLabel;
    private TextView storyAuthorLabel;
    private ImageView mImageView;
    private View view;
    private View selectedView;
    private FragmentManager fm;

    public StoryListAdapter(Context context, ArrayList<CustomMapMarker> users, FragmentManager fm)
    {
        super(context, 0, users);
        this.fm = fm;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        CustomMapMarker marker = getItem(position);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.story_list_row, parent, false);
            view = convertView;
        }

        storyLabel = (TextView) convertView.findViewById(R.id.storyImageTxt);
        storyAuthorLabel = (TextView) convertView.findViewById(R.id.storyListAuthorTxt);
        storyAuthorLabel.setText("by: " + marker.getSnippet());
        storyLabel.setText(marker.getTitle());
        mImageView = (ImageView) convertView.findViewById(R.id.storyImg);
        mImageView.setTag(position);
        convertView.setTag(position);

        String url = marker.getStoryImgageUrl();
        this.loadBitmapPicasso(position, mImageView,url);
        this.setItemRowListener();
        convertView.setBackgroundColor(AppConfiguration.LIST_BACK_COLOR.getColor());
        return convertView;
    }


    //*******************************************************************
    //  Private Implementation Below Here....
    //
    //*******************************************************************

    private void setItemRowListener()
    {
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(selectedView != null)
                {
                    selectedView.setBackgroundColor(AppConfiguration.LIST_BACK_COLOR.getColor());
                }
                view.setBackgroundColor(AppConfiguration.LIST_SELECT_COLOR.getColor());
                selectedView = view;
                launchStoryTabView(view);
            }
        });
    }

    private void launchStoryTabView(View view)
    {
        try
        {
            int position = (Integer) view.getTag();
            // Access the row position here to get the correct data item
            CustomMapMarker customMapMarker = getItem(position);

            Fragment fragment = StoryTabFragment.newInstance(customMapMarker);

            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frameLayout, fragment);
            ft.commit();
        }
        catch (Exception e)
        {
            e.getStackTrace();
        }
    }

    private void loadBitmapPicasso(int resId, ImageView imageView, String url)
    {
        final String imageKey = String.valueOf(resId);

         Bitmap bitmap = null;
        if (bitmap != null)
        {
            mImageView.setImageBitmap(bitmap);
        }
        else
        {
            mImageView.setImageResource(R.drawable.rockhammer);
            try
            {

                //new DownloadImageTask(mImageView).execute(url);
                Picasso.with(getContext())
                        .load(url)
                        .resize(250,250)
                        .into(mImageView);

                //    this.getRetrofitImage(url); buggy!!
            }
            catch (Exception e)
            {
                e.getStackTrace();
            }
        }
    }
}
