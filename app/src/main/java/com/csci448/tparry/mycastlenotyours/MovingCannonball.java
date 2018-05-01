package com.csci448.tparry.mycastlenotyours;

import android.graphics.Bitmap;

/**
 * Created by Owner on 4/30/2018.
 */

public class MovingCannonball {
    public Bitmap image;
    public int xValue;
    public int yValue;
    public float xVelocity;
    public float yVelocity;

    public MovingCannonball(Bitmap i) {
        image = i;
        xValue = 0;
        yValue = 0;
        xVelocity = 0;
        yVelocity = 0;
    }
}
