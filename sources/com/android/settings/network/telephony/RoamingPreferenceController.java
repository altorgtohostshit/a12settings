package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.network.GlobalSettingsChangeListener;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.RestrictedSwitchPreference;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class RoamingPreferenceController extends TelephonyTogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    private static final String DIALOG_TAG = "MobileDataDialog";
    private static final String TAG = "RoamingController";
    private CarrierConfigManager mCarrierConfigManager;
    FragmentManager mFragmentManager;
    private GlobalSettingsChangeListener mListener;
    private GlobalSettingsChangeListener mListenerForSubId;
    /* access modifiers changed from: private */
    public RestrictedSwitchPreference mSwitchPreference;
    private TelephonyManager mTelephonyManager;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus(int i) {
        return i != -1 ? 0 : 1;
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

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public RoamingPreferenceController(Context context, String str) {
        super(context, str);
        this.mCarrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
    }

    public void onStart() {
        if (this.mListener == null) {
            this.mListener = new GlobalSettingsChangeListener(this.mContext, "data_roaming") {
                public void onChanged(String str) {
                    RoamingPreferenceController roamingPreferenceController = RoamingPreferenceController.this;
                    roamingPreferenceController.updateState(roamingPreferenceController.mSwitchPreference);
                }
            };
        }
        stopMonitorSubIdSpecific();
        if (this.mSubId != -1) {
            Context context = this.mContext;
            this.mListenerForSubId = new GlobalSettingsChangeListener(context, "data_roaming" + this.mSubId) {
                public void onChanged(String str) {
                    RoamingPreferenceController.this.stopMonitor();
                    RoamingPreferenceController roamingPreferenceController = RoamingPreferenceController.this;
                    roamingPreferenceController.updateState(roamingPreferenceController.mSwitchPreference);
                }
            };
        }
    }

    public void onStop() {
        stopMonitor();
        stopMonitorSubIdSpecific();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mSwitchPreference = (RestrictedSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public boolean setChecked(boolean z) {
        if (isDialogNeeded()) {
            showDialog();
            return false;
        }
        this.mTelephonyManager.setDataRoamingEnabled(z);
        return true;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        RestrictedSwitchPreference restrictedSwitchPreference = (RestrictedSwitchPreference) preference;
        if (!restrictedSwitchPreference.isDisabledByAdmin()) {
            restrictedSwitchPreference.setEnabled(this.mSubId != -1);
            restrictedSwitchPreference.setChecked(isChecked());
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isDialogNeeded() {
        boolean isDataRoamingEnabled = this.mTelephonyManager.isDataRoamingEnabled();
        PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(this.mSubId);
        if (!isDataRoamingEnabled) {
            return configForSubId == null || !configForSubId.getBoolean("disable_charge_indication_bool");
        }
        return false;
    }

    public boolean isChecked() {
        return this.mTelephonyManager.isDataRoamingEnabled();
    }

    public void init(FragmentManager fragmentManager, int i) {
        this.mFragmentManager = fragmentManager;
        this.mSubId = i;
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        this.mTelephonyManager = telephonyManager;
        int i2 = this.mSubId;
        if (i2 != -1) {
            TelephonyManager createForSubscriptionId = telephonyManager.createForSubscriptionId(i2);
            if (createForSubscriptionId == null) {
                Log.w(TAG, "fail to init in sub" + this.mSubId);
                this.mSubId = -1;
                return;
            }
            this.mTelephonyManager = createForSubscriptionId;
        }
    }

    private void showDialog() {
        RoamingDialogFragment.newInstance(this.mSubId).show(this.mFragmentManager, DIALOG_TAG);
    }

    /* access modifiers changed from: private */
    public void stopMonitor() {
        GlobalSettingsChangeListener globalSettingsChangeListener = this.mListener;
        if (globalSettingsChangeListener != null) {
            globalSettingsChangeListener.close();
            this.mListener = null;
        }
    }

    private void stopMonitorSubIdSpecific() {
        GlobalSettingsChangeListener globalSettingsChangeListener = this.mListenerForSubId;
        if (globalSettingsChangeListener != null) {
            globalSettingsChangeListener.close();
            this.mListenerForSubId = null;
        }
    }
}
