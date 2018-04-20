package com.csci448.tparry.mycastlenotyours;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class TitleActivity extends AppCompatActivity {
    private Button mStartButton;
    private Button mHighScoreButton;
    private Button mExitButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_title);

        mStartButton = (Button) findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TitleActivity.this, GameActivity.class);
                startActivity(i);
            }
        });

        mHighScoreButton = (Button) findViewById(R.id.high_score_button);
        mHighScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog highScoreDialog = createHighScoreDialog();
                highScoreDialog.show();
            }
        });

        mExitButton = (Button) findViewById(R.id.exit_button);
        mExitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });
    }

    public Dialog createHighScoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TitleActivity.this);
        builder.setMessage("High Score: " + "9999999999")
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
        return builder.create();
    }
}
