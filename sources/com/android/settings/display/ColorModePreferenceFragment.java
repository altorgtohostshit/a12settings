package com.android.settings.display;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.hardware.display.ColorDisplayManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.widget.RadioButtonPickerFragment;
import com.android.settingslib.widget.CandidateInfo;
import com.android.settingslib.widget.LayoutPreference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ColorModePreferenceFragment extends RadioButtonPickerFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.color_mode_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            int[] intArray = context.getResources().getIntArray(17235992);
            return intArray != null && intArray.length > 0 && !ColorDisplayManager.areAccessibilityTransformsEnabled(context);
        }
    };
    private ColorDisplayManager mColorDisplayManager;
    private ContentObserver mContentObserver;
    private Resources mResources;

    private boolean isValidColorMode(int i) {
        if (i == 0 || i == 1 || i == 2 || i == 3) {
            return true;
        }
        return i >= 256 && i <= 511;
    }

    public int getMetricsCategory() {
        return 1143;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.color_mode_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mColorDisplayManager = (ColorDisplayManager) context.getSystemService(ColorDisplayManager.class);
        this.mResources = context.getResources();
        ContentResolver contentResolver = context.getContentResolver();
        this.mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            public void onChange(boolean z, Uri uri) {
                super.onChange(z, uri);
                if (ColorDisplayManager.areAccessibilityTransformsEnabled(ColorModePreferenceFragment.this.getContext())) {
                    ColorModePreferenceFragment.this.getActivity().finish();
                }
            }
        };
        contentResolver.registerContentObserver(Settings.Secure.getUriFor("accessibility_display_inversion_enabled"), false, this.mContentObserver, this.mUserId);
        contentResolver.registerContentObserver(Settings.Secure.getUriFor("accessibility_display_daltonizer_enabled"), false, this.mContentObserver, this.mUserId);
    }

    public void onDetach() {
        if (this.mContentObserver != null) {
            getContext().getContentResolver().unregisterContentObserver(this.mContentObserver);
            this.mContentObserver = null;
        }
        super.onDetach();
    }

    /* access modifiers changed from: package-private */
    public void configureAndInstallPreview(LayoutPreference layoutPreference, PreferenceScreen preferenceScreen) {
        layoutPreference.setSelectable(false);
        preferenceScreen.addPreference(layoutPreference);
    }

    /* access modifiers changed from: protected */
    public void addStaticPreferences(PreferenceScreen preferenceScreen) {
        configureAndInstallPreview(new LayoutPreference(preferenceScreen.getContext(), (int) R.layout.color_mode_preview), preferenceScreen);
    }

    /* access modifiers changed from: protected */
    public List<? extends CandidateInfo> getCandidates() {
        Map<Integer, String> colorModeMapping = ColorModeUtils.getColorModeMapping(this.mResources);
        ArrayList arrayList = new ArrayList();
        for (int i : this.mResources.getIntArray(17235992)) {
            arrayList.add(new ColorModeCandidateInfo(colorModeMapping.get(Integer.valueOf(i)), getKeyForColorMode(i), true));
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public String getDefaultKey() {
        int colorMode = getColorMode();
        if (isValidColorMode(colorMode)) {
            return getKeyForColorMode(colorMode);
        }
        return getKeyForColorMode(0);
    }

    /* access modifiers changed from: protected */
    public boolean setDefaultKey(String str) {
        int parseInt = Integer.parseInt(str.substring(str.lastIndexOf("_") + 1));
        if (isValidColorMode(parseInt)) {
            setColorMode(parseInt);
        }
        return true;
    }

    public int getColorMode() {
        return this.mColorDisplayManager.getColorMode();
    }

    public void setColorMode(int i) {
        this.mColorDisplayManager.setColorMode(i);
    }

    /* access modifiers changed from: package-private */
    public String getKeyForColorMode(int i) {
        return "color_mode_" + i;
    }

    static class ColorModeCandidateInfo extends CandidateInfo {
        private final String mKey;
        private final CharSequence mLabel;

        public Drawable loadIcon() {
            return null;
        }

        ColorModeCandidateInfo(CharSequence charSequence, String str, boolean z) {
            super(z);
            this.mLabel = charSequence;
            this.mKey = str;
        }

        public CharSequence loadLabel() {
            return this.mLabel;
        }

        public String getKey() {
            return this.mKey;
        }
    }
}
