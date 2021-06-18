package com.android.settings.security;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import android.util.Pair;
import androidx.preference.Preference;
import androidx.preference.TwoStatePreference;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.network.telephony.MobileNetworkUtils;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.wifi.dpp.WifiDppUtils;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;

public class ConfirmSimDeletionPreferenceController extends BasePreferenceController implements Preference.OnPreferenceChangeListener {
    public static final String KEY_CONFIRM_SIM_DELETION = "confirm_sim_deletion";
    private boolean mConfirmationDefaultOn;
    private MetricsFeatureProvider mMetricsFeatureProvider;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public ConfirmSimDeletionPreferenceController(Context context, String str) {
        super(context, str);
        this.mConfirmationDefaultOn = context.getResources().getBoolean(R.bool.config_sim_deletion_confirmation_default_on);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    public int getAvailabilityStatus() {
        return MobileNetworkUtils.showEuiccSettings(this.mContext) ? 0 : 3;
    }

    private boolean getGlobalState() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), KEY_CONFIRM_SIM_DELETION, this.mConfirmationDefaultOn ? 1 : 0) == 1;
    }

    public boolean isChecked() {
        return getGlobalState();
    }

    public boolean setChecked(boolean z) {
        Settings.Global.putInt(this.mContext.getContentResolver(), KEY_CONFIRM_SIM_DELETION, z ? 1 : 0);
        return true;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (!preference.getKey().equals(getPreferenceKey())) {
            return false;
        }
        if (!isChecked()) {
            this.mMetricsFeatureProvider.action(this.mContext, 1738, (Pair<Integer, Object>[]) new Pair[0]);
            setChecked(true);
            return true;
        }
        WifiDppUtils.showLockScreen(this.mContext, new ConfirmSimDeletionPreferenceController$$ExternalSyntheticLambda0(this, preference));
        return false;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onPreferenceChange$0(Preference preference) {
        this.mMetricsFeatureProvider.action(this.mContext, 1739, (Pair<Integer, Object>[]) new Pair[0]);
        setChecked(false);
        ((TwoStatePreference) preference).setChecked(false);
    }

    public void updateState(Preference preference) {
        if (!((KeyguardManager) this.mContext.getSystemService(KeyguardManager.class)).isKeyguardSecure()) {
            preference.setEnabled(false);
            if (preference instanceof TwoStatePreference) {
                ((TwoStatePreference) preference).setChecked(false);
            }
            preference.setSummary((int) R.string.disabled_because_no_backup_security);
            return;
        }
        preference.setEnabled(true);
        if (preference instanceof TwoStatePreference) {
            ((TwoStatePreference) preference).setChecked(getGlobalState());
        }
        preference.setSummary((int) R.string.confirm_sim_deletion_description);
    }
}
