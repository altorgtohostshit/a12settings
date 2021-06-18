package com.android.settings.display;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.display.DisplayDensityConfiguration;
import com.android.settingslib.display.DisplayDensityUtils;

public class ScreenZoomSettings extends PreviewSeekBarPreferenceFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider() {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return false;
        }
    };
    private int mDefaultDensity;
    private int[] mValues;

    /* access modifiers changed from: protected */
    public int getActivityLayoutResId() {
        return R.layout.screen_zoom_activity;
    }

    public int getHelpResource() {
        return R.string.help_url_display_size;
    }

    public int getMetricsCategory() {
        return 339;
    }

    /* access modifiers changed from: protected */
    public int[] getPreviewSampleResIds() {
        if (getContext().getResources().getBoolean(R.bool.config_enable_extra_screen_zoom_preview)) {
            return new int[]{R.layout.screen_zoom_preview_1, R.layout.screen_zoom_preview_2, R.layout.screen_zoom_preview_settings};
        }
        return new int[]{R.layout.screen_zoom_preview_1};
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        DisplayDensityUtils displayDensityUtils = new DisplayDensityUtils(getContext());
        int currentIndex = displayDensityUtils.getCurrentIndex();
        if (currentIndex < 0) {
            int i = getResources().getDisplayMetrics().densityDpi;
            this.mValues = new int[]{i};
            this.mEntries = new String[]{getString(DisplayDensityUtils.SUMMARY_DEFAULT)};
            this.mInitialIndex = 0;
            this.mDefaultDensity = i;
        } else {
            this.mValues = displayDensityUtils.getValues();
            this.mEntries = displayDensityUtils.getEntries();
            this.mInitialIndex = currentIndex;
            this.mDefaultDensity = displayDensityUtils.getDefaultDensity();
        }
        getActivity().setTitle(R.string.screen_zoom_title);
    }

    /* access modifiers changed from: protected */
    public Configuration createConfig(Configuration configuration, int i) {
        Configuration configuration2 = new Configuration(configuration);
        configuration2.densityDpi = this.mValues[i];
        return configuration2;
    }

    /* access modifiers changed from: protected */
    public void commit() {
        int i = this.mValues[this.mCurrentIndex];
        if (i == this.mDefaultDensity) {
            DisplayDensityConfiguration.clearForcedDisplayDensity(0);
        } else {
            DisplayDensityConfiguration.setForcedDisplayDensity(0, i);
        }
    }
}
