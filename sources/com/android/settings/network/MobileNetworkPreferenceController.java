package com.android.settings.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.telephony.ServiceState;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.network.telephony.MobileNetworkActivity;
import com.android.settings.network.telephony.MobileNetworkUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedPreference;
import com.android.settingslib.Utils;
import com.android.settingslib.core.AbstractPreferenceController;

public class MobileNetworkPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, LifecycleObserver {
    static final String KEY_MOBILE_NETWORK_SETTINGS = "mobile_network_settings";
    private BroadcastReceiver mAirplanModeChangedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            MobileNetworkPreferenceController mobileNetworkPreferenceController = MobileNetworkPreferenceController.this;
            mobileNetworkPreferenceController.updateState(mobileNetworkPreferenceController.mPreference);
        }
    };
    private final boolean mIsSecondaryUser;
    /* access modifiers changed from: private */
    public Preference mPreference;
    MobileNetworkTelephonyCallback mTelephonyCallback;
    private final TelephonyManager mTelephonyManager;
    private final UserManager mUserManager;

    public String getPreferenceKey() {
        return KEY_MOBILE_NETWORK_SETTINGS;
    }

    public MobileNetworkPreferenceController(Context context) {
        super(context);
        UserManager userManager = (UserManager) context.getSystemService("user");
        this.mUserManager = userManager;
        this.mTelephonyManager = (TelephonyManager) context.getSystemService("phone");
        this.mIsSecondaryUser = !userManager.isAdminUser();
    }

    public boolean isAvailable() {
        return !isUserRestricted() && !Utils.isWifiOnly(this.mContext);
    }

    public boolean isUserRestricted() {
        return this.mIsSecondaryUser || RestrictedLockUtilsInternal.hasBaseUserRestriction(this.mContext, "no_config_mobile_networks", UserHandle.myUserId());
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    class MobileNetworkTelephonyCallback extends TelephonyCallback implements TelephonyCallback.ServiceStateListener {
        MobileNetworkTelephonyCallback() {
        }

        public void onServiceStateChanged(ServiceState serviceState) {
            MobileNetworkPreferenceController mobileNetworkPreferenceController = MobileNetworkPreferenceController.this;
            mobileNetworkPreferenceController.updateState(mobileNetworkPreferenceController.mPreference);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        if (isAvailable()) {
            if (this.mTelephonyCallback == null) {
                this.mTelephonyCallback = new MobileNetworkTelephonyCallback();
            }
            this.mTelephonyManager.registerTelephonyCallback(this.mContext.getMainExecutor(), this.mTelephonyCallback);
        }
        BroadcastReceiver broadcastReceiver = this.mAirplanModeChangedReceiver;
        if (broadcastReceiver != null) {
            this.mContext.registerReceiver(broadcastReceiver, new IntentFilter("android.intent.action.AIRPLANE_MODE"));
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        MobileNetworkTelephonyCallback mobileNetworkTelephonyCallback = this.mTelephonyCallback;
        if (mobileNetworkTelephonyCallback != null) {
            this.mTelephonyManager.unregisterTelephonyCallback(mobileNetworkTelephonyCallback);
        }
        BroadcastReceiver broadcastReceiver = this.mAirplanModeChangedReceiver;
        if (broadcastReceiver != null) {
            this.mContext.unregisterReceiver(broadcastReceiver);
        }
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (!(preference instanceof RestrictedPreference) || !((RestrictedPreference) preference).isDisabledByAdmin()) {
            boolean z = false;
            if (Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 0) {
                z = true;
            }
            preference.setEnabled(z);
        }
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!KEY_MOBILE_NETWORK_SETTINGS.equals(preference.getKey())) {
            return false;
        }
        this.mContext.startActivity(new Intent(this.mContext, MobileNetworkActivity.class));
        return true;
    }

    public CharSequence getSummary() {
        return MobileNetworkUtils.getCurrentCarrierNameForDisplay(this.mContext);
    }
}
