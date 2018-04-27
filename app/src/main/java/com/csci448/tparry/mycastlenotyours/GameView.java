package com.csci448.tparry.mycastlenotyours;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Owner on 4/26/2018.
 */

public class GameView extends View {
    private Paint backgroundPaint;
    private Paint groundPaint;
    private float storedX;
    private float storedY;
    private boolean pressed = false;

    public GameView(Context context) {
        this(context, null);
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //set background colors
        backgroundPaint = new Paint();
        backgroundPaint.setColor(getResources().getColor(R.color.colorPrimary));
        groundPaint = new Paint();
        groundPaint.setColor(getResources().getColor(R.color.green));
    }

    //handle swiping
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //PointF current = new PointF(event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!pressed) {
                    storedX = event.getX();
                    storedY = event.getY();
                    pressed = true;
                    break;
                }
            case MotionEvent.ACTION_UP:
                pressed = false;
                releaseBow(event.getX(), event.getY());
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        //draw background
        canvas.drawRect((float)0.0, (float)0.0, canvas.getWidth(), canvas.getHeight() / 2, backgroundPaint);
        canvas.drawRect((float)0.0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight(), groundPaint);

        canvas.restore();
    }

    public void releaseBow(float x, float y) {

    }
}
