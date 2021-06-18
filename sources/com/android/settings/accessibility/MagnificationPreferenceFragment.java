package com.android.settings.accessibility;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public final class MagnificationPreferenceFragment extends DashboardFragment {
    static final int OFF = 0;

    /* renamed from: ON */
    static final int f55ON = 1;
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.accessibility_magnification_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return MagnificationPreferenceFragment.isApplicable(context.getResources());
        }
    };
    private boolean mLaunchedFromSuw = false;

    public int getHelpResource() {
        return R.string.help_url_magnification;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "MagnificationPreferenceFragment";
    }

    public int getMetricsCategory() {
        return 922;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.accessibility_magnification_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("from_suw")) {
            this.mLaunchedFromSuw = arguments.getBoolean("from_suw");
        }
        ((MagnificationGesturesPreferenceController) use(MagnificationGesturesPreferenceController.class)).setIsFromSUW(this.mLaunchedFromSuw);
        ((MagnificationNavbarPreferenceController) use(MagnificationNavbarPreferenceController.class)).setIsFromSUW(this.mLaunchedFromSuw);
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        if (this.mLaunchedFromSuw) {
            preference.setFragment(ToggleScreenMagnificationPreferenceFragmentForSetupWizard.class.getName());
        }
        return super.onPreferenceTreeClick(preference);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        if (!this.mLaunchedFromSuw) {
            super.onCreateOptionsMenu(menu, menuInflater);
        }
    }

    static boolean isChecked(ContentResolver contentResolver, String str) {
        return Settings.Secure.getInt(contentResolver, str, 0) == 1;
    }

    static boolean setChecked(ContentResolver contentResolver, String str, boolean z) {
        return Settings.Secure.putInt(contentResolver, str, z ? 1 : 0);
    }

    static boolean isApplicable(Resources resources) {
        return resources.getBoolean(17891624);
    }
}
