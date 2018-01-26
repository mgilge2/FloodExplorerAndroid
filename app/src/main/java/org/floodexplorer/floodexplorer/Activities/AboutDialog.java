package org.floodexplorer.floodexplorer.Activities;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.floodexplorer.floodexplorer.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Created by mgilge on 11/21/17.
 * controls the about page
 */

public class AboutDialog extends DialogFragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE); //this gets rid of title on older phones specifically...has to be done before inflation
        View aboutPage = new AboutPage(getContext())
                .isRTL(false)
                .setImage(R.drawable.elton)
                .setDescription("This app was written by Mike Gilge and Taptej Sidhu in collaboration with Dr. Chad Pritchard of the Eastern Washington University Geology Department.")
                .addItem(getVersionElement())
                .addGroup("Connect with us")
                .addEmail("cpritchard@ewu.edu")
                .addWebsite("http://www.floodexplorer.org/")
                .addPlayStore("org.floodexplorer.floodexplorer")
               // .addGitHub("mgilge2")
                .addItem(getExitElement())
                .create();

        return aboutPage;
    }

    //add animation to dialog inflation
    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            addFadeInAnimation();
        }
    }

    //size the dialog to be other than standard
    @Override
    public void onResume()
    {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    private void addFadeInAnimation()
    {
        View view = getDialog().getWindow().getDecorView();
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("scaleX", 0.0f, 1.0f),
                PropertyValuesHolder.ofFloat("scaleY", 0.0f, 1.0f),
                PropertyValuesHolder.ofFloat("alpha", 0.0f, 1.0f));
        scaleDown.setDuration(800);
        scaleDown.start();
    }

    private Element getExitElement()
    {
        Element element = new Element("Close", null);
        element.setGravity(Gravity.CENTER);
        element.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });
        return element;
    }

    private Element getVersionElement()
    {
        Element versionElement = new Element();
        versionElement.setTitle("Version 1.0b");

        return  versionElement;
    }
}
