package com.android.settings.accessibility;

import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.Settings;
import android.view.accessibility.CaptioningManager;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class CaptionMoreOptionsFragment extends DashboardFragment implements Preference.OnPreferenceChangeListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.captioning_more_options);
    private CaptioningManager mCaptioningManager;
    private LocalePreference mLocale;

    public int getHelpResource() {
        return R.string.help_url_caption;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "CaptionMoreOptionsFragment";
    }

    public int getMetricsCategory() {
        return 1820;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.captioning_more_options;
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        super.onCreatePreferences(bundle, str);
        this.mCaptioningManager = (CaptioningManager) getSystemService("captioning");
        initializeAllPreferences();
        updateAllPreferences();
        installUpdateListeners();
    }

    private void initializeAllPreferences() {
        this.mLocale = (LocalePreference) findPreference("captioning_locale");
    }

    private void installUpdateListeners() {
        this.mLocale.setOnPreferenceChangeListener(this);
    }

    private void updateAllPreferences() {
        String rawLocale = this.mCaptioningManager.getRawLocale();
        LocalePreference localePreference = this.mLocale;
        if (rawLocale == null) {
            rawLocale = "";
        }
        localePreference.setValue(rawLocale);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        if (this.mLocale != preference) {
            return true;
        }
        Settings.Secure.putString(contentResolver, "accessibility_captioning_locale", (String) obj);
        return true;
    }
}
