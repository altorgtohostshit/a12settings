package com.android.settings.accessibility;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.view.accessibility.CaptioningManager;
import android.widget.Switch;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.widget.SettingsMainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;
import com.google.common.primitives.Floats;
import java.util.ArrayList;
import java.util.List;

public class CaptionPropertiesFragment extends DashboardFragment implements Preference.OnPreferenceChangeListener, OnMainSwitchChangeListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.captioning_settings);
    private CaptioningManager mCaptioningManager;
    private float[] mFontSizeValuesArray;
    private Preference mMoreOptions;
    private final List<Preference> mPreferenceList = new ArrayList();
    private SettingsMainSwitchPreference mSwitch;
    private Preference mTextAppearance;

    public int getHelpResource() {
        return R.string.help_url_caption;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "CaptionPropertiesFragment";
    }

    public int getMetricsCategory() {
        return 3;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.captioning_settings;
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        super.onCreatePreferences(bundle, str);
        this.mCaptioningManager = (CaptioningManager) getSystemService("captioning");
        initializeAllPreferences();
        installUpdateListeners();
        initFontSizeValuesArray();
    }

    public void onResume() {
        super.onResume();
        updateAllPreferences();
    }

    private void initializeAllPreferences() {
        this.mSwitch = (SettingsMainSwitchPreference) findPreference("captioning_preference_switch");
        this.mTextAppearance = findPreference("captioning_caption_appearance");
        this.mMoreOptions = findPreference("captioning_more_options");
        this.mPreferenceList.add(this.mTextAppearance);
        this.mPreferenceList.add(this.mMoreOptions);
    }

    private void installUpdateListeners() {
        this.mSwitch.setOnPreferenceChangeListener(this);
        this.mSwitch.addOnSwitchChangeListener(this);
    }

    private void initFontSizeValuesArray() {
        String[] stringArray = getPrefContext().getResources().getStringArray(R.array.captioning_font_size_selector_values);
        int length = stringArray.length;
        this.mFontSizeValuesArray = new float[length];
        for (int i = 0; i < length; i++) {
            this.mFontSizeValuesArray[i] = Float.parseFloat(stringArray[i]);
        }
    }

    private void updateAllPreferences() {
        this.mSwitch.setChecked(this.mCaptioningManager.isEnabled());
        this.mTextAppearance.setSummary(geTextAppearanceSummary(getPrefContext()));
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        if (this.mSwitch != preference) {
            return true;
        }
        Settings.Secure.putInt(contentResolver, "accessibility_captioning_enabled", ((Boolean) obj).booleanValue() ? 1 : 0);
        return true;
    }

    private CharSequence geTextAppearanceSummary(Context context) {
        String[] stringArray = context.getResources().getStringArray(R.array.captioning_font_size_selector_summaries);
        int indexOf = Floats.indexOf(this.mFontSizeValuesArray, this.mCaptioningManager.getFontScale());
        if (indexOf == -1) {
            indexOf = 0;
        }
        return stringArray[indexOf];
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        Settings.Secure.putInt(getActivity().getContentResolver(), "accessibility_captioning_enabled", z ? 1 : 0);
    }
}
