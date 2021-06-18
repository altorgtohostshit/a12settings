package com.android.settings.display;

import android.hardware.SensorPrivacyManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.preference.Preference;
import com.android.internal.view.RotationPolicy;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.widget.SettingsMainSwitchBar;
import com.android.settingslib.search.Indexable$SearchIndexProvider;

public class SmartAutoRotatePreferenceFragment extends DashboardFragment {
    public static final Indexable$SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.auto_rotate_settings);
    /* access modifiers changed from: private */
    public PowerManager mPowerManager;
    /* access modifiers changed from: private */
    public SensorPrivacyManager mPrivacyManager;
    private RotationPolicy.RotationPolicyListener mRotationPolicyListener;
    /* access modifiers changed from: private */
    public AutoRotateSwitchBarController mSwitchBarController;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "SmartAutoRotatePreferenceFragment";
    }

    public int getMetricsCategory() {
        return 1867;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.auto_rotate_settings;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        SettingsActivity settingsActivity = (SettingsActivity) getActivity();
        SettingsMainSwitchBar switchBar = settingsActivity.getSwitchBar();
        switchBar.setTitle(getContext().getString(R.string.auto_rotate_settings_primary_switch_title));
        switchBar.show();
        this.mSwitchBarController = new AutoRotateSwitchBarController(settingsActivity, switchBar, getSettingsLifecycle());
        this.mPrivacyManager = SensorPrivacyManager.getInstance(settingsActivity);
        this.mPowerManager = (PowerManager) getSystemService(PowerManager.class);
        Preference findPreference = findPreference("footer_preference");
        if (findPreference != null) {
            findPreference.setTitle((CharSequence) Html.fromHtml(getString(R.string.smart_rotate_text_headline), 63));
            findPreference.setVisible(SmartAutoRotateController.isRotationResolverServiceAvailable(settingsActivity));
        }
        return onCreateView;
    }

    public void onResume() {
        super.onResume();
        if (this.mRotationPolicyListener == null) {
            this.mRotationPolicyListener = new RotationPolicy.RotationPolicyListener() {
                public void onChange() {
                    SmartAutoRotatePreferenceFragment.this.mSwitchBarController.onChange();
                    boolean isRotationLocked = RotationPolicy.isRotationLocked(SmartAutoRotatePreferenceFragment.this.getContext());
                    boolean isSensorPrivacyEnabled = SmartAutoRotatePreferenceFragment.this.mPrivacyManager.isSensorPrivacyEnabled(2);
                    boolean isPowerSaveMode = SmartAutoRotatePreferenceFragment.this.mPowerManager.isPowerSaveMode();
                    Preference findPreference = SmartAutoRotatePreferenceFragment.this.findPreference("face_based_rotate");
                    if (findPreference != null && SmartAutoRotateController.hasSufficientPermission(SmartAutoRotatePreferenceFragment.this.getContext())) {
                        findPreference.setEnabled(!isRotationLocked && !isSensorPrivacyEnabled && !isPowerSaveMode);
                    }
                }
            };
        }
        RotationPolicy.registerRotationPolicyListener(getPrefContext(), this.mRotationPolicyListener);
    }

    public void onPause() {
        super.onPause();
        if (this.mRotationPolicyListener != null) {
            RotationPolicy.unregisterRotationPolicyListener(getPrefContext(), this.mRotationPolicyListener);
        }
    }
}
