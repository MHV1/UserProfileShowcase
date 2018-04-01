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

import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;

import java.util.ArrayList;
import java.util.List;

public class Theme {

    public static final Theme THEME_DEFAULT =
            new Theme(R.string.theme_default, R.style.AppTheme_Default);

    public static final Theme THEME_HALLOWEEN =
            new Theme(R.string.theme_halloween, R.style.AppTheme_Halloween);

    public static final Theme THEME_SAILOR =
            new Theme(R.string.theme_sailor, R.style.AppTheme_Sailor);

    public static final Theme THEME_AUTUMN =
            new Theme(R.string.theme_autumn, R.style.AppTheme_Autumn);

    public static final Theme THEME_FAIRYTALE =
            new Theme(R.string.theme_fairytale, R.style.AppTheme_Fairytale);

    @StringRes
    private int nameResId;

    @StyleRes
    private int themeResId;

    public Theme(@StringRes int nameResId, @StyleRes int themeResId) {
        this.nameResId = nameResId;
        this.themeResId = themeResId;
    }

    public int getNameResId() {
        return nameResId;
    }

    public int getThemeResId() {
        return themeResId;
    }

    public static List<Theme> getThemeList() {
        final List<Theme> themeList = new ArrayList<>();
        themeList.add(THEME_DEFAULT);
        themeList.add(THEME_HALLOWEEN);
        themeList.add(THEME_SAILOR);
        themeList.add(THEME_AUTUMN);
        themeList.add(THEME_FAIRYTALE);
        return themeList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return nameResId == theme.nameResId &&
                themeResId == theme.themeResId;
    }
}
