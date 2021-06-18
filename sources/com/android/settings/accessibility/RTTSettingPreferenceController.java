package com.android.settings.accessibility;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import java.util.List;

public class RTTSettingPreferenceController extends BasePreferenceController {
    private static final String DIALER_RTT_CONFIGURATION = "dialer_rtt_configuration";
    private final Context mContext;
    private final String mDialerPackage;
    private final CharSequence[] mModes;
    private final PackageManager mPackageManager;
    Intent mRTTIntent;
    private final TelecomManager mTelecomManager;

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

    public RTTSettingPreferenceController(Context context, String str) {
        super(context, str);
        this.mContext = context;
        this.mModes = context.getResources().getTextArray(R.array.rtt_setting_mode);
        this.mDialerPackage = context.getString(R.string.config_rtt_setting_package_name);
        this.mPackageManager = context.getPackageManager();
        this.mTelecomManager = (TelecomManager) context.getSystemService(TelecomManager.class);
        this.mRTTIntent = new Intent(context.getString(R.string.config_rtt_setting_intent_action));
    }

    public int getAvailabilityStatus() {
        List<ResolveInfo> queryIntentActivities = this.mPackageManager.queryIntentActivities(this.mRTTIntent, 0);
        if (queryIntentActivities == null || queryIntentActivities.isEmpty() || !isDialerSupportRTTSetting()) {
            return 3;
        }
        return 0;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        preferenceScreen.findPreference(getPreferenceKey()).setIntent(this.mRTTIntent);
    }

    public CharSequence getSummary() {
        return this.mModes[Settings.Secure.getInt(this.mContext.getContentResolver(), DIALER_RTT_CONFIGURATION, 1)];
    }

    private boolean isDialerSupportRTTSetting() {
        if (!(createTelephonyManagerFromSubId().isRttSupported() && getBooleanCarrierConfig("ignore_rtt_mode_setting_bool")) || !TextUtils.equals(this.mTelecomManager.getDefaultDialerPackage(), this.mDialerPackage)) {
            return false;
        }
        return true;
    }

    private boolean getBooleanCarrierConfig(String str) {
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager == null) {
            return CarrierConfigManager.getDefaultConfig().getBoolean(str);
        }
        PersistableBundle configForSubId = carrierConfigManager.getConfigForSubId(SubscriptionManager.getDefaultVoiceSubscriptionId());
        if (configForSubId != null) {
            return configForSubId.getBoolean(str);
        }
        return CarrierConfigManager.getDefaultConfig().getBoolean(str);
    }

    private TelephonyManager createTelephonyManagerFromSubId() {
        return ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(SubscriptionManager.getDefaultVoiceSubscriptionId());
    }
}
