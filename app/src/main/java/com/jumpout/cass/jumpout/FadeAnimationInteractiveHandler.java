package com.jumpout.cass.jumpout;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.os.Build;
import android.os.CountDownTimer;

import android.widget.TextView;

/**
 * Created by aramide on 09/03/2017.
 */

public class FadeAnimationInteractiveHandler {


    //field is assigned imageView when obj is created
    ImageView imgView;

    //also pass the current view
    View vView;

    //create the object with a textview

    //default no-args
    public FadeAnimationInteractiveHandler(){


    }

    //just passes the imageView (overloaded constructor)
    public FadeAnimationInteractiveHandler(ImageView viewImage){

        this.imgView = viewImage;

    }

    //constructor expects a textview to be called with it
    public FadeAnimationInteractiveHandler(ImageView viewImage, View newView){

        //obj is assigned the views passed to it
        this.imgView = viewImage;
        this.vView = newView;


    }

    //timers handling the transitions (later replace with threads)
    private static CountDownTimer fadeOutTimer;
    private static CountDownTimer fadeInTimer;


    //call the fadeOut method with a textview
    public void fadeOut(ImageView genericImage){

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());

        fadeOut.setStartOffset(100);
        fadeOut.setDuration(2000);

        //indicate the cooba logo
        //final ImageView newImage = (ImageView) findViewById(R.id.coobaSplash);

        final ImageView newImage = genericImage;

        //apply the animation to the logo
        newImage.setAnimation(fadeOut);

        //timer should last duration of the fade out
        fadeOutTimer = new CountDownTimer(2000, 1000){

            @Override
            public void onTick(long millisUntilFinished){


            }

            @Override
            public void onFinish(){

                //make logo invisible once finished
                newImage.setVisibility(View.INVISIBLE);

                //thread.sleep

                //call method to do inverse, make it fade in
                fadeIn(newImage);

            }

        };	//tell timer to begin
        fadeOutTimer.start();


    }


    //call fadeIn method with a textview
    public void fadeIn(ImageView genericImage){

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());

        fadeIn.setStartOffset(1000);
        fadeIn.setDuration(2000);

        //assigns this imageView the image passed
        final ImageView newImage = genericImage;

        //apply the animation to the logo
        newImage.setAnimation(fadeIn);

        //timer should last duration of the fade out
        fadeInTimer = new CountDownTimer(3000, 1000){

            @Override
            public void onTick(long millisUntilFinished){

                //nothing needed here

            }

            @Override
            public void onFinish(){

                //make logo invisible once finished
                newImage.setVisibility(View.VISIBLE);

                //call method to do inverse, make it fade out
                fadeOut(newImage);

            }

        };	//tell timer to begin
        fadeInTimer.start();
    }


}
