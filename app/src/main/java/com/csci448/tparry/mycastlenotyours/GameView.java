package com.csci448.tparry.mycastlenotyours;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Owner on 4/26/2018.
 */

public class GameView extends View {
    //score
    private int score = 0;
    private Paint scorePaint;
    private Paint backgroundPaint;
    private Paint groundPaint;
    private float storedX;
    private float storedY;
    private float gravity = 1;
    private boolean pressed = false;
    private boolean loaded = true;
    //pictures
    private Bitmap castle;
    private Bitmap moon;
    private Bitmap catapult;
    // moving objects
    private MovingEnemy dino;
    private MovingCannonball cannonball;
    // animation specifications
    private int fps = 60;
    private long animationDuration = 10000;
    private long startTime;

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
        scorePaint = new Paint();
        scorePaint.setColor(getResources().getColor(R.color.yellow));
        scorePaint.setTextSize(100);
        //setup bitmaps
        Resources res = getResources();
        castle = BitmapFactory.decodeResource(res, R.drawable.castle);
        castle = resizeBitmap(castle, 550, 650);
        moon = BitmapFactory.decodeResource(res, R.drawable.moon);
        moon = resizeBitmap(moon, 350, 350);
        catapult = BitmapFactory.decodeResource(res, R.drawable.catapult_);
        catapult = resizeBitmap(catapult, 160, 240);
        //dino setup
        Bitmap dinoImage = BitmapFactory.decodeResource(res, R.drawable.tyrannosaurus);
        dinoImage = resizeBitmap(dinoImage, 250, 260);
        dino = new MovingEnemy(dinoImage);
        //cannonball setup
        Bitmap ballImage = BitmapFactory.decodeResource(res, R.drawable.cannonball);
        ballImage = resizeBitmap(ballImage, 120, 120);
        cannonball = new MovingCannonball(ballImage);

        //start animation
        this.startTime = System.currentTimeMillis();
        this.postInvalidate();
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
                if (loaded) {
                    loaded = false;
                    releaseBow(event.getX(), event.getY());
                }
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
        //draw score
        canvas.drawText("Score: " + String.valueOf(score), 20, 100, scorePaint);
        //draw back objects
        canvas.drawBitmap(castle,canvas.getWidth() - 700, canvas.getHeight()-600, null);
        canvas.drawBitmap(moon, 0, 110, null);
        // moving dino
        if (dino.yValue == 0) {
            dino.yValue = canvas.getHeight() - 300;
        }
        //moving cannonball
        if (cannonball.xValue == 0 && cannonball.yValue == 0) {
            cannonball.xValue = canvas.getWidth() - 360;
            cannonball.yValue = canvas.getHeight() - 480;
            cannonball.initY = canvas.getHeight() - 480;
            cannonball.initX = canvas.getWidth() - 360;
        }
        canvas.drawBitmap(cannonball.image, cannonball.xValue, cannonball.yValue, null);
        canvas.drawBitmap(catapult, canvas.getWidth() - 490, canvas.getHeight() - 440, null);
        if (cannonball.xVelocity != 0 || cannonball.yVelocity != 0) {
            cannonball.xValue += cannonball.xVelocity;
            cannonball.yValue += cannonball.yVelocity;
            cannonball.yVelocity += gravity;
        }
        //check collision
        if ((cannonball.yValue + 100) > dino.yValue && (cannonball.xValue+100) > dino.xValue && cannonball.xValue < dino.xValue && dino.yValue > cannonball.yValue) {
            dino.xValue -= 250;
            score++;
            resetBall();
        }
        if ((cannonball.yValue + 100) > dino.yValue && (cannonball.xValue+100) > (dino.xValue+200) && cannonball.xValue < (dino.xValue+200) && dino.yValue > cannonball.yValue) {
            dino.xValue -= 250;
            score++;
            resetBall();
        }
        if ((cannonball.yValue + 100) > (dino.yValue+200) && (cannonball.xValue+100) > (dino.xValue+200) && cannonball.xValue < (dino.xValue+200) && (dino.yValue+200) > cannonball.yValue) {
            dino.xValue -= 250;
            score++;
            resetBall();
        }
        if ((cannonball.yValue + 100) > dino.yValue && (cannonball.xValue+100) > dino.xValue && cannonball.xValue < dino.xValue && (dino.yValue+200) > cannonball.yValue) {
            dino.xValue -= 250;
            score++;
            resetBall();
        }

        //animate
        this.postInvalidateDelayed(1000/ fps);

        //when dino reaches castle, it should stop and start attacking castle
        if (dino.xValue >= canvas.getWidth() - 700) {
            dino.speed = 0;
            //canvas.rotate(1);
        }
        canvas.drawBitmap(dino.image, dino.xValue, dino.yValue, null);
        dino.xValue += dino.speed;
        //return when ball passes screen
        if (cannonball.xValue + 120 < 0) {
            resetBall();
        }
        if (cannonball.yValue > canvas.getHeight()) {
            resetBall();
        }

        canvas.restore();
    }

    private void resetBall() {
        cannonball.xValue = cannonball.initX;
        cannonball.yValue = cannonball.initY;
        cannonball.yVelocity = 0;
        cannonball.xVelocity = 0;
        loaded = true;
    }

    public void releaseBow(float x, float y) {
        float deltaY = storedY - y;
        float avgY = (y + storedY) / 2;
        float deltaX = storedX - x;
        float avgX = (x + storedX) / 2;
        deltaY /= 50;
        deltaX /= 50;
        cannonball.yVelocity = -deltaY;
        cannonball.xVelocity = -deltaX;
    }

    public Bitmap resizeBitmap(Bitmap old, int height, int width) {
        int oldWidth = old.getWidth();
        int oldHeight = old.getHeight();
        float scaleWidth = ((float) width) / oldWidth;
        float scaleHeight = ((float) height) / oldHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resized = Bitmap.createBitmap(old, 0, 0, oldWidth, oldHeight, matrix, false);
        old.recycle();
        return resized;
    }
}
