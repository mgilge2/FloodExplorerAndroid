package org.floodexplorer.floodexplorer.OmekaDataItems.CustomMapMarker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by mgilge on 7/22/17.
 * attempt to smoothith the imageviews in story list
 * go here for info
 * https://dzone.com/articles/android-listview-optimizations-0
 */

public class BlockingImageView extends AppCompatImageView
{
    private boolean mBlockLayout;

    public BlockingImageView(Context context) {
        super(context);
    }

    public BlockingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BlockingImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void requestLayout() {
        if (!mBlockLayout) {
            super.requestLayout();
        }
    }

    @Override
    public void setImageResource(int resId) {
        mBlockLayout = true;
        super.setImageResource(resId);
        mBlockLayout = false;
    }

    @Override
    public void setImageURI(Uri uri) {
        mBlockLayout = true;
        super.setImageURI(uri);
        mBlockLayout = false;
    }

    @Override
    public void setImageDrawable(Drawable drawable)
    {
        mBlockLayout = true;
        super.setImageDrawable(drawable);
        mBlockLayout = false;
    }
}
