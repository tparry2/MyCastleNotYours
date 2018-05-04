package com.csci448.tparry.mycastlenotyours;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private int TOTAL_HEALTH = 100;

    private TextView mScoreTextView;
    private int mHighScoreInt;
    private int enemiesKilled = 0;

    private ImageView mBowButton;
    private ImageView mArrowImage;
    private RectF mArrowBox = new RectF();
    private Canvas mArrowCanvas = new Canvas();
    private ImageView mTRex;
    private RectF mTRexBox = new RectF();
    private Canvas mTRexCanvas = new Canvas();

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
        //drawSomething();
        //moveEnemy(mTRex);
    }

    private void drawSomething() {
        int vWidth = 1000;
        int vHeight = 1000;
        Path tRexPath = new Path();
        Path arrowPath = new Path();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);


        //mTRexBitmap = Bitmap.createBitmap(mTRex.getWidth(), mTRex.getHeight(), Bitmap.Config.ARGB_8888);
        //mArrowBitmap = Bitmap.createBitmap(mArrowImage.getWidth(), mArrowImage.getHeight(), Bitmap.Config.ARGB_8888);
        //mTRex.setImageBitmap(mTRexBitmap);
        mTRex.post(new Runnable() {
            @Override
            public void run() {
                mTRex.setImageBitmap(loadBitmapFromView(mTRex, mTRexCanvas));
            }
        });
        tRexPath.moveTo(mTRex.getX() + 200, mTRex.getY());
        tRexPath.lineTo(mTRex.getX() + 200, mTRex.getY());
        tRexPath.setFillType(Path.FillType.EVEN_ODD);
        mTRexCanvas.drawPath(tRexPath, paint);
        mArrowImage.post(new Runnable() {
            @Override
            public void run() {
                mArrowImage.setImageBitmap(loadBitmapFromView(mArrowImage, mArrowCanvas));
            }
        });
        mTRexCanvas.drawPath(tRexPath, paint);
        loadBitmapFromView(mTRex, mTRexCanvas).setWidth(10);
        mTRexCanvas.drawTextOnPath("HERE", tRexPath, 0, 0, paint);

        Log.i("GameActivity", "drawSomething()");


    }

    public static Bitmap loadBitmapFromView(View v, Canvas c) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        c = new Canvas(b);
        v.layout(0, 0, v.getWidth(), v.getHeight());
        v.draw(c);
        return b;
    }

    public Dialog createHighScoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setMessage("Scores: "
                + "\nYour Score: " + Integer.toString(enemiesKilled)
                + "\nHigh Score: " + Integer.toString(mHighScoreInt))
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        return builder.create();
    }

    public Dialog createGameOverDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);

        // if current score is larger than previous high score, set current score as high score
        if (enemiesKilled > mHighScoreInt) {
            mHighScoreInt = enemiesKilled;
            SharedPreferences.Editor editor = mHighScore.edit();
            editor.putInt(mPrefKey, mHighScoreInt);
            editor.commit();
        }

        builder.setMessage(R.string.game_over)
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }

    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        String action = "";

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(hasReleasedArrow) {
                    action = "ACTION_DOWN";
                    x2 = current.x;
                    y2 = current.y;
                }
                else action = "Not released yet";
                hasReleasedArrow = false;
                break;

            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                x1 = current.x - 1569;
                y1 = current.y - 790;
                //releaseArrow(x1, y1, x2, y2);
                hasReleasedArrow = true;
                break;
        }

        Log.i("GameActivity", action + " at x=" + current.x + ", y=" + current.y);
        return true;

    }

    public void releaseArrow(float x1, float y1, float x2, float y2){
        Log.i("GameActivity", "releaseArrow() called");

        float acc = 10;
        float deltaY = y2 - y1;
        float deltaX = x2 - x1;
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
        tRexAnim.setFillAfter(true);
        enemy.startAnimation(tRexAnim);

    }

    private void collisionDetected() {
        // TODO:
        // bounce TRex back a certain amount
        // return cannonball to original position
        // reset TRex path towards castle
        // increment score

    }

    private void damageCastle() {
        // TODO:
        // animate TRex to show damage being done
        // decrement castle health
        // if castle health == 0: end game, record score, show game over and score dialogs, return to title screen
    }
}
