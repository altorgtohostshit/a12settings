package com.android.settings.display;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;

public class ToggleFontSizePreferenceFragment extends PreviewSeekBarPreferenceFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider() {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return false;
        }
    };
    private float[] mValues;

    /* access modifiers changed from: protected */
    public int getActivityLayoutResId() {
        return R.layout.font_size_activity;
    }

    public int getHelpResource() {
        return R.string.help_url_font_size;
    }

    public int getMetricsCategory() {
        return 340;
    }

    /* access modifiers changed from: protected */
    public int[] getPreviewSampleResIds() {
        return new int[]{R.layout.font_size_preview};
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Resources resources = getContext().getResources();
        ContentResolver contentResolver = getContext().getContentResolver();
        this.mEntries = resources.getStringArray(R.array.entries_font_size);
        String[] stringArray = resources.getStringArray(R.array.entryvalues_font_size);
        this.mInitialIndex = fontSizeValueToIndex(Settings.System.getFloat(contentResolver, "font_scale", 1.0f), stringArray);
        this.mValues = new float[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            this.mValues[i] = Float.parseFloat(stringArray[i]);
        }
        getActivity().setTitle(R.string.title_font_size);
    }

    /* access modifiers changed from: protected */
    public Configuration createConfig(Configuration configuration, int i) {
        Configuration configuration2 = new Configuration(configuration);
        configuration2.fontScale = this.mValues[i];
        return configuration2;
    }

    /* access modifiers changed from: protected */
    public void commit() {
        if (getContext() != null) {
            Settings.System.putFloat(getContext().getContentResolver(), "font_scale", this.mValues[this.mCurrentIndex]);
        }
    }

    public static int fontSizeValueToIndex(float f, String[] strArr) {
        float parseFloat = Float.parseFloat(strArr[0]);
        int i = 1;
        while (i < strArr.length) {
            float parseFloat2 = Float.parseFloat(strArr[i]);
            if (f < parseFloat + ((parseFloat2 - parseFloat) * 0.5f)) {
                return i - 1;
            }
            i++;
            parseFloat = parseFloat2;
        }
        return strArr.length - 1;
    }
}
