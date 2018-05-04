package com.csci448.tparry.mycastlenotyours;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by Owner on 4/26/2018.
 */

public class GameFragment extends Fragment {
    public static GameFragment newInstance() {
        return new GameFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game, containter, false);
        FrameLayout.LayoutParams lay = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        //lay.gravity = FrameLayout.Layout
        //lay.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lay.gravity = Gravity.RIGHT;
        lay.setMargins(50,50,50,50);

        FrameLayout mContainer = new FrameLayout(getActivity());
        mContainer.addView(v);
        return mContainer;
    }
}
