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

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ThemeDialogFragment extends AppCompatDialogFragment
        implements DialogInterface.OnClickListener {

    @StyleRes
    private int mCurrentThemeResId;

    private Theme mCurrentTheme;
    private SharedPreferences mSharedPrefs;
    private WeakReference<FragmentActivity> mActivityRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityRef = new WeakReference<>(getActivity());

        mSharedPrefs = mActivityRef.get().getPreferences(Context.MODE_PRIVATE);
        mCurrentThemeResId =
                mSharedPrefs.getInt(Constants.PREF_CURRENT_THEME, R.style.AppTheme_Default);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        List<String> themeNameList = new ArrayList<>();

        for (Theme theme : Theme.getThemeList()) {
            themeNameList.add(getString(theme.getNameResId()));

            if (theme.getThemeResId() == mCurrentThemeResId) {
                mCurrentTheme = theme;
            }
        }

        int currentSelectedThemeIndex = Theme.getThemeList().indexOf(mCurrentTheme);
        String[] themeNameArray = themeNameList.toArray(new String[themeNameList.size()]);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivityRef.get())
                .setTitle(R.string.theme_dialog_title)
                .setNegativeButton(R.string.theme_dialog_negative, this)
                .setSingleChoiceItems(themeNameArray, currentSelectedThemeIndex, this);

        Dialog dialog = alertDialogBuilder.create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_NEGATIVE) {
            dialog.dismiss();
        } else {
            final Theme selectedTheme = Theme.getThemeList().get(which);

            SharedPreferences.Editor editor = mSharedPrefs.edit();
            editor.putInt(Constants.PREF_CURRENT_THEME, selectedTheme.getThemeResId());
            editor.apply();

            dialog.dismiss();
            mActivityRef.get().recreate();
        }
    }
}