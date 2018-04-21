package com.csci448.tparry.mycastlenotyours;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    private int TOTAL_HEALTH = 100;

    private TextView mScoreTextView;
    private int enemiesKilled = 0;

    private ImageView mBowButton;
    private ImageView mArrowImage;
    private TextView mHealthTextView;
    private int healthRemaining = TOTAL_HEALTH;

    private float x1;
    private float y1;
    private float x2;
    private float y2;
    private boolean hasReleasedArrow = true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

//        SurfaceView surface = (SurfaceView) findViewById(R.id.surface);
//        surface.getHolder().addCallback(new SurfaceHolder.Callback() {
//
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//                // Do some drawing when surface is ready
//                Canvas canvas = holder.lockCanvas();
//                canvas.drawColor(Color.RED);
//                holder.unlockCanvasAndPost(canvas);
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            }
//        });

        mScoreTextView = (TextView) findViewById(R.id.score_textview);
        mScoreTextView.setText("Score: " + Integer.toString(enemiesKilled));

        mArrowImage = (ImageView) findViewById(R.id.arrow);
        //mArrowImage.setVisibility(View.INVISIBLE);

        mBowButton = (ImageView) findViewById(R.id.catapult_img);
        mBowButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mArrowImage.setVisibility(View.VISIBLE);
                onTouchEvent(event);
                //mArrowImage.setVisibility(View.INVISIBLE);
                return false;
            }
        });

        mHealthTextView = (TextView) findViewById(R.id.health_textview);
        mHealthTextView.setText("Health: " + Integer.toString(healthRemaining) + "/" + Integer.toString(TOTAL_HEALTH));

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
                releaseArrow(x1, y1, x2, y2);
                hasReleasedArrow = true;
                break;
        }

        Log.i("GameActivity", action + " at x=" + current.x + ", y=" + current.y);
        return true;

    }

    public void releaseArrow(float x1, float y1, float x2, float y2){
        Log.i("GameActivity", "releaseArrow() called");
        ImageView arrowImage = (ImageView) findViewById(R.id.arrow);

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

        int abs    = ArcTranslateAnimation.ABSOLUTE;

        ArcTranslateAnimation arcAnim = new ArcTranslateAnimation(0, endX / 30, 0, 275);
        Log.i("GameActivity", "deltaX = " + deltaX + " deltaY = " + deltaY + " endX = " + endX);

        arcAnim.setDuration((long) time*50);
        arcAnim.setFillAfter(true);
        System.out.print(arcAnim);

        arrowImage.setVisibility(View.VISIBLE);
        arrowImage.startAnimation(arcAnim);
        arrowImage.setVisibility(View.INVISIBLE);
    }
}
