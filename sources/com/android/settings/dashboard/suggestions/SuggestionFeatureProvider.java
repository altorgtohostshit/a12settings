package com.android.settings.dashboard.suggestions;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.fragment.app.Fragment;

public interface SuggestionFeatureProvider {
    Class<? extends Fragment> getContextualSuggestionFragment();

    SharedPreferences getSharedPrefs(Context context);

    ComponentName getSuggestionServiceComponent();

    boolean isSuggestionComplete(Context context, ComponentName componentName);
}
