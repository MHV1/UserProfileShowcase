/*
 * Copyright (C) 2018 Milan Herrera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mhv.userprofileshowcase;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener {

    private static final float TOOLBAR_TITLE_DISPLAY_THRESHOLD = 0.9f;
    private static final int ANIMATION_DURATION = 200;

    private TextView mToolbarTitle;
    private SharedPreferences mSharedPrefs;
    private boolean mToolbarTitleVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPrefs = getPreferences(Context.MODE_PRIVATE);

        int layoutMode = mSharedPrefs.getInt(Constants.PREF_LAYOUT_MODE,
                Constants.LAYOUT_MODE_FLAT);

        int themeResId = mSharedPrefs
                .getInt(Constants.PREF_CURRENT_THEME, R.style.AppTheme_Default);

        setTheme(themeResId);
        super.onCreate(savedInstanceState);

        switch (layoutMode) {
            case Constants.LAYOUT_MODE_FLAT:
                setContentView(R.layout.activity_main_flat);
                break;
            case Constants.LAYOUT_MODE_CARD:
                setContentView(R.layout.activity_main_card);
                break;
            case Constants.LAYOUT_MODE_FANCY:
                setContentView(R.layout.activity_main_fancy);
                break;
        }

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (layoutMode == Constants.LAYOUT_MODE_FANCY) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                getSupportActionBar().setTitle("");
            }

            mToolbarTitle = findViewById(R.id.profile_toolbar_title);
            final AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
            appBarLayout.addOnOffsetChangedListener(this);

            playAlphaAnimation(mToolbarTitle, 0, View.INVISIBLE);

        } else {

            final Display display = getWindowManager().getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics();
            display.getMetrics(outMetrics);

            final CircleImageView profileAvatarView = findViewById(R.id.profile_avatar_view);
            ViewGroup.LayoutParams layoutParams = profileAvatarView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                layoutParams.width = outMetrics.widthPixels / 2;
            } else {
                layoutParams.width = outMetrics.widthPixels / 4;
            }

            profileAvatarView.requestLayout();

            final CollapsingToolbarLayout  collapsingToolbarLayout =
                    findViewById(R.id.collapsing_toolbar_layout);

            collapsingToolbarLayout
                    .setExpandedTitleGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            collapsingToolbarLayout.setTitle(getString(R.string.profile_name));

            // See: https://issuetracker.google.com/issues/37140811
            collapsingToolbarLayout.post(new Runnable() {

                @Override
                public void run() {
                    collapsingToolbarLayout.requestLayout();
                }
            });
        }

        final FloatingActionButton fab = findViewById(R.id.fab_theme_selection);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AppCompatDialogFragment themeDialogFragment = new ThemeDialogFragment();
                boolean doesFragmentExist = getSupportFragmentManager()
                        .findFragmentByTag(themeDialogFragment.getClass().getName()) != null;

                if (!doesFragmentExist) {
                    themeDialogFragment.show(getSupportFragmentManager(),
                            themeDialogFragment.getClass().getName());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        SharedPreferences.Editor editor = mSharedPrefs.edit();

        switch (itemId) {
            case R.id.action_layout_mode_flat:
                editor.putInt(Constants.PREF_LAYOUT_MODE, Constants.LAYOUT_MODE_FLAT);
                editor.apply();
                recreate();
                return true;
            case R.id.action_layout_mode_card:
                editor.putInt(Constants.PREF_LAYOUT_MODE, Constants.LAYOUT_MODE_CARD);
                editor.apply();
                recreate();
                return true;
            case R.id.action_layout_mode_fancy:
                editor.putInt(Constants.PREF_LAYOUT_MODE, Constants.LAYOUT_MODE_FANCY);
                editor.apply();
                recreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        if (percentage >= TOOLBAR_TITLE_DISPLAY_THRESHOLD) {
            if (!mToolbarTitleVisible) {
                playAlphaAnimation(mToolbarTitle, ANIMATION_DURATION, View.VISIBLE);
                mToolbarTitleVisible = true;
            }
        } else {
            if (mToolbarTitleVisible) {
                playAlphaAnimation(mToolbarTitle, ANIMATION_DURATION, View.INVISIBLE);
                mToolbarTitleVisible = false;
            }
        }
    }

    public static void playAlphaAnimation(View view, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        view.startAnimation(alphaAnimation);
    }
}
