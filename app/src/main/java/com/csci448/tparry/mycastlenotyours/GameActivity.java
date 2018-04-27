package com.csci448.tparry.mycastlenotyours;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    private Canvas mCanvas;
    private Bitmap mBitmap;
    private static final int OFFSET = 100;
    private int mOffset = OFFSET;

    private int TOTAL_HEALTH = 100;

    private Button mStartButton;

    private TextView mScoreTextView;
    private int mHighScoreInt;
    private int enemiesKilled = 0;

    private ImageView mBowButton;
    private ImageView mArrowImage;
    private RectF mArrowBox = new RectF();
    private ImageView mTRex;
    private RectF mTRexBox = new RectF();

    private TextView mHealthTextView;
    private int healthRemaining = TOTAL_HEALTH;

    private SharedPreferences mHighScore;
    private String mPrefKey = "High_Score";

    private float x1;
    private float y1;
    private float x2;
    private float y2;
    private boolean hasReleasedArrow = true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mHighScore = this.getSharedPreferences(mPrefKey, Context.MODE_PRIVATE);
        mHighScoreInt = mHighScore.getInt(mPrefKey, 0);

        mScoreTextView = (TextView) findViewById(R.id.score_textview);
        mScoreTextView.setText("Score: " + Integer.toString(enemiesKilled));

        mArrowImage = (ImageView) findViewById(R.id.arrow);
        mArrowBox = new RectF(mArrowImage.getX(), mArrowImage.getY(), mArrowImage.getX() + 75, mArrowImage.getY() + 50);

        mBowButton = (ImageView) findViewById(R.id.catapult_img);
        mBowButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onTouchEvent(event);
                return false;
            }
        });

        mTRex = (ImageView) findViewById(R.id.trex);
        mTRexBox = new RectF(mTRex.getX(), mTRex.getY(), mTRex.getX() + 75, mTRex.getY() + 100);

        mHealthTextView = (TextView) findViewById(R.id.health_textview);
        mHealthTextView.setText("Health: " + Integer.toString(healthRemaining) + "/" + Integer.toString(TOTAL_HEALTH));
        drawSomething();
        moveEnemy(mTRex);

        // THIS IS WHERE CANVAS CODE STARTS
//        mPaint.setColor(0x13579B);
//        mPaintText = new Paint(Paint.UNDERLINE_TEXT_FLAG);
//        mPaintText.setColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));
//        mPaintText.setTextSize(70);

    }

//    @Override
//    protected void onResume(){
//        super.onResume();
//
//        moveEnemy(mTRex);
//    }

    private void drawSomething() {
        int vWidth = 1000;
        int vHeight = 1000;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.CYAN);


        mBitmap = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888);
        mTRex.setImageBitmap(mBitmap);
        Log.i("GameActivity", Integer.toString(mBitmap.describeContents()));
        mArrowImage.setImageBitmap(mBitmap);

        mCanvas = new Canvas(mBitmap);
        mCanvas.drawBitmap(mBitmap, vHeight, vHeight, paint);
        //mCanvas.drawRect(mTRexBox, paint);

        //view.invalidate();

    }

    private void collisionDetection() {

    }


    public Dialog createHighScoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setMessage("Scores: "
                + "\nYour Score: " + Integer.toString(enemiesKilled)
                + "\nHigh Score: " + "9999999999")
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        return builder.create();
    }

    public Dialog createGameOverDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setMessage(R.string.game_over)
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }

    public boolean onTouchEvent(MotionEvent event) {
//        PointF current = new PointF(event.getX(), event.getY());
//        String action = "";
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                if(hasReleasedArrow) {
//                    action = "ACTION_DOWN";
//                    x2 = current.x;
//                    y2 = current.y;
//                }
//                else action = "Not released yet";
//                hasReleasedArrow = false;
//                break;
//
//            case MotionEvent.ACTION_UP:
//                action = "ACTION_UP";
//                x1 = current.x - 1569;
//                y1 = current.y - 790;
//                releaseArrow(x1, y1, x2, y2);
//                hasReleasedArrow = true;
//                break;
//        }
//
//        Log.i("GameActivity", action + " at x=" + current.x + ", y=" + current.y);
        return true;

    }

    public void releaseArrow(float x1, float y1, float x2, float y2){
        Log.i("GameActivity", "releaseArrow() called");

        float acc = 10;
        float deltaY = y2 - y1;
        float avgY = (y1 + y2) / 2;
        float deltaX = x2 - x1;
        float avgX = (x1 + x2) / 2;
        float magnitude = (float) Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
        float angle = (float) Math.toDegrees(Math.atan(deltaY / deltaX));

        float time =  2 * (Math.abs(deltaY) / acc);
        float endX = -Math.abs(deltaX) * time;


        // trajectory: y = (tan(angle)*x) - ((9.81 / (2 * deltaX * deltaX)) * x^2)
        // calculate arrow path, detect collision(separate function to handle hitting a stick figure)

        ArcTranslateAnimation arcAnim = new ArcTranslateAnimation(0, endX, 0, 225, mArrowImage, mTRex);
        Log.i("GameActivity", "deltaX = " + deltaX + " deltaY = " + deltaY + " endX = " + endX);

        arcAnim.setDuration((long) time * 250);
        arcAnim.setFillAfter(false);

        mArrowImage.setVisibility(View.VISIBLE);
        mArrowImage.startAnimation(arcAnim);
    }

    public void moveEnemy(ImageView enemy) {
       // mArrowImage.getHitRect(mArrowBox);
        //mTRex.getHitRect(mTRexBox);
        TranslateAnimation tRexAnim= new TranslateAnimation(mTRex.getX(), 1000, mTRex.getY(), 0);
        tRexAnim.setDuration(10000);
        enemy.startAnimation(tRexAnim);

    }
}
