package com.csci448.tparry.mycastlenotyours;

import android.support.v4.app.Fragment;

/**
 * Created by Owner on 4/26/2018.
 */

public class StartActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return StartFragment.newInstance();
    }
}
