package com.csci448.tparry.mycastlenotyours;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * Created by Owner on 4/26/2018.
 */

public class StartFragment extends Fragment {
    //widgets
    private Button startButton;

    public static StartFragment newInstance() {
        return new StartFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_game, containter, false);
        startButton = (Button) v.findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GameActivity2.class);
                startActivity(intent);
            }
        });
        return v;
    }
}
