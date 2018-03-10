package com.csci448.tparry.mycastlenotyours;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    private int TOTAL_HEALTH = 100;

    private TextView mScoreTextView;
    private int enemiesKilled = 0;

    private ImageView mBowButton;
    private TextView mHealthTextView;
    private int healthRemaining = TOTAL_HEALTH;

    private Button mGameOverButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mScoreTextView = (TextView) findViewById(R.id.score_textview);
        mScoreTextView.setText("Score: " + Integer.toString(enemiesKilled));

        mBowButton = (ImageView) findViewById(R.id.arrow_button);
        mBowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.bow_toast, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        mHealthTextView = (TextView) findViewById(R.id.health_textview);
        mHealthTextView.setText("Health: " + Integer.toString(healthRemaining) + "/" + Integer.toString(TOTAL_HEALTH));

        mGameOverButton = (Button) findViewById(R.id.game_over_button);
        mGameOverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog highScoreDialog = onCreateHighScoreDialog(savedInstanceState);
                highScoreDialog.show();

                Dialog gameOverDialog = onCreateGameOverDialog(savedInstanceState);
                gameOverDialog.show();
            }
        });

    }


    public Dialog onCreateHighScoreDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setMessage("Scores: "
        + "\nYour Score: " + Integer.toString(enemiesKilled)
        + "\nHigh Score: " + "9999999999")
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public Dialog onCreateGameOverDialog(final Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setMessage(R.string.game_over)
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
