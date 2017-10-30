package org.floodexplorer.floodexplorer.OmekaDataItems.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.floodexplorer.floodexplorer.Activities.StoryTab.StoryTab;
import org.floodexplorer.floodexplorer.Activities.StoryTab.StoryTabImages;
import org.floodexplorer.floodexplorer.Activities.StoryTabFragment;
import org.floodexplorer.floodexplorer.AppConfiguration;
import org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker.CustomMapMarker;
import org.floodexplorer.floodexplorer.R;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mgilge on 7/21/17.
 */

public class CustomMarkerAdapter extends ArrayAdapter<CustomMapMarker>
{
    private TextView storyLabel;
    private TextView storyAuthorLabel;
    private ImageView mImageView;
    private LruCache<String, Bitmap> mMemoryCache;
    private Context context;
    private ListView listView;
    private View view;
    private View selectedView;

    public CustomMarkerAdapter(Context context, ArrayList<CustomMapMarker> users)
    {
        super(context, 0, users);
        this.context = context;
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

        storyLabel = (TextView) convertView.findViewById(R.id.storyLblTxt);
        storyAuthorLabel = (TextView) convertView.findViewById(R.id.storyListAuthorTxt);
        storyAuthorLabel.setText("by: " + marker.getSnippet());
        storyLabel.setText(marker.getTitle());
        mImageView = (ImageView) convertView.findViewById(R.id.imageButton);
        mImageView.setTag(position);
        convertView.setTag(position);

        String url = marker.getStoryImgageUrl();
        this.loadBitmapPicasso(position, mImageView,url);
        this.setButtonListener();
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
                //Toast.makeText(context, "Click The Picture to go to the story...", Toast.LENGTH_SHORT).show();

                //need more...
                if(selectedView != null)
                {
                    selectedView.setBackgroundColor(AppConfiguration.LIST_BACK_COLOR.getColor());
                }
                view.setBackgroundColor(AppConfiguration.LIST_SELECT_COLOR.getColor());
                selectedView = view;
                //launchStory(view);
                launchStoryTabView(view);

                /*
                int position = (Integer) view.getTag();
                CustomMapMarker customMapMarker = getItem(position);
                Fragment fragment = StoryTabFragment.newInstance(customMapMarker);
                FragmentManager fm =  ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frameLayout, fragment);
                ft.commit();
                */
            }
        });
    }

    private void launchStoryTabView(View view)
    {
        int position = (Integer) view.getTag();
        // Access the row position here to get the correct data item
        CustomMapMarker customMapMarker = getItem(position);

        // Do what you want here...
                /*
                Toast.makeText(MainActivity.getInstance().getApplicationContext(),
                        customMapMarker.getSnippet(), Toast.LENGTH_LONG).show();
                        */
                /*
                FragmentManager fm = MainActivity.getInstance().getSupportFragmentManager();
                DisplayStory dialogFragment = new DisplayStory(customMapMarker.getStoryText(), mImageView);
                dialogFragment.show(fm, "Sample Fragment");
*/
        PicRayAdapter picRayAdapter = new PicRayAdapter(context, customMapMarker.getFileList()); //this line of code is to for picasso to load thumbs for later useage
        StoryTabImages storyTabImages = StoryTabImages.newInstance(customMapMarker);
        Fragment fragment = StoryTab.newInstance(customMapMarker);
        FragmentManager fm =  ((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, fragment).addToBackStack("fragment");
        ft.commit();
    }

    private void launchStory(View view)
    {
        int position = (Integer) view.getTag();
        // Access the row position here to get the correct data item
        CustomMapMarker customMapMarker = getItem(position);

        // Do what you want here...
                /*
                Toast.makeText(MainActivity.getInstance().getApplicationContext(),
                        customMapMarker.getSnippet(), Toast.LENGTH_LONG).show();
                        */
                /*
                FragmentManager fm = MainActivity.getInstance().getSupportFragmentManager();
                DisplayStory dialogFragment = new DisplayStory(customMapMarker.getStoryText(), mImageView);
                dialogFragment.show(fm, "Sample Fragment");
*/

        Fragment fragment = StoryTabFragment.newInstance(customMapMarker);
        FragmentManager fm =  ((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, fragment).addToBackStack("fragment");
        ft.commit();
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

    private void setButtonListener()
    {
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                CustomMapMarker customMapMarker = getItem(position);

                // Do what you want here...
                /*
                Toast.makeText(MainActivity.getInstance().getApplicationContext(),
                        customMapMarker.getSnippet(), Toast.LENGTH_LONG).show();
                        */
                /*
                FragmentManager fm = MainActivity.getInstance().getSupportFragmentManager();
                DisplayStory dialogFragment = new DisplayStory(customMapMarker.getStoryText(), mImageView);
                dialogFragment.show(fm, "Sample Fragment");
*/

                Fragment fragment = StoryTabFragment.newInstance(customMapMarker);
                FragmentManager fm =  ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frameLayout, fragment).addToBackStack("fragment");
                ft.commit();
            }
        });
    }

}
