package com.android.settings.deletionhelper;

import android.content.Context;
import android.os.SystemProperties;
import android.provider.Settings;
import android.widget.Switch;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import com.android.internal.util.Preconditions;
import com.android.settings.widget.SettingsMainSwitchBar;
import com.android.settingslib.Utils;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.widget.OnMainSwitchChangeListener;

public class AutomaticStorageManagerSwitchBarController implements OnMainSwitchChangeListener {
    private Context mContext;
    private Preference mDaysToRetainPreference;
    private FragmentManager mFragmentManager;
    private MetricsFeatureProvider mMetrics;
    private SettingsMainSwitchBar mSwitchBar;

    public AutomaticStorageManagerSwitchBarController(Context context, SettingsMainSwitchBar settingsMainSwitchBar, MetricsFeatureProvider metricsFeatureProvider, Preference preference, FragmentManager fragmentManager) {
        this.mContext = (Context) Preconditions.checkNotNull(context);
        this.mSwitchBar = (SettingsMainSwitchBar) Preconditions.checkNotNull(settingsMainSwitchBar);
        this.mMetrics = (MetricsFeatureProvider) Preconditions.checkNotNull(metricsFeatureProvider);
        this.mDaysToRetainPreference = (Preference) Preconditions.checkNotNull(preference);
        this.mFragmentManager = (FragmentManager) Preconditions.checkNotNull(fragmentManager);
        initializeCheckedStatus();
    }

    private void initializeCheckedStatus() {
        this.mSwitchBar.setChecked(Utils.isStorageManagerEnabled(this.mContext));
        this.mSwitchBar.addOnSwitchChangeListener(this);
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        this.mMetrics.action(this.mContext, 489, z);
        this.mDaysToRetainPreference.setEnabled(z);
        Settings.Secure.putInt(this.mContext.getContentResolver(), "automatic_storage_manager_enabled", z ? 1 : 0);
        if (z) {
            maybeShowWarning();
        }
    }

    public void tearDown() {
        this.mSwitchBar.removeOnSwitchChangeListener(this);
    }

    private void maybeShowWarning() {
        if (!SystemProperties.getBoolean("ro.storage_manager.enabled", false)) {
            ActivationWarningFragment.newInstance().show(this.mFragmentManager, "ActivationWarningFragment");
        }
    }
}
