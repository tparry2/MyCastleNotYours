package com.csci448.tparry.mycastlenotyours;

import android.graphics.Bitmap;

/**
 * Created by Owner on 4/30/2018.
 */

public class MovingEnemy {
    public Bitmap image;
    public int xValue;
    public int yValue;

    public MovingEnemy(Bitmap i) {
        image = i;
        xValue = 0;
        yValue = 0;
    }
}
