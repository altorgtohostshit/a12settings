package com.android.settings.network.telephony;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.PersistableBundle;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.ArraySet;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.Settings;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.network.ims.WifiCallingQueryImsState;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetworkProviderWifiCallingGroup extends AbstractPreferenceController implements LifecycleObserver, SubscriptionsChangeListener.SubscriptionsChangeListenerClient {
    protected CarrierConfigManager mCarrierConfigManager;
    private PreferenceGroup mPreferenceGroup;
    private String mPreferenceGroupKey;
    private Map<Integer, PhoneAccountHandle> mSimCallManagerList = new HashMap();
    private Set<Integer> mSubIdList = new ArraySet();
    private SubscriptionManager mSubscriptionManager;
    private Map<Integer, TelephonyManager> mTelephonyManagerList = new HashMap();
    private Map<Integer, Preference> mWifiCallingForSubPreferences;

    public String getPreferenceKey() {
        return "provider_model_wfc_group";
    }

    public void onAirplaneModeChanged(boolean z) {
    }

    public NetworkProviderWifiCallingGroup(Context context, Lifecycle lifecycle, String str) {
        super(context);
        this.mCarrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        this.mPreferenceGroupKey = str;
        this.mWifiCallingForSubPreferences = new ArrayMap();
        lifecycle.addObserver(this);
        setSubscriptionInfoList(context);
    }

    private void setSubscriptionInfoList(Context context) {
        for (SubscriptionInfo subscriptionId : SubscriptionUtil.getActiveSubscriptions(this.mSubscriptionManager)) {
            int subscriptionId2 = subscriptionId.getSubscriptionId();
            this.mSubIdList.add(Integer.valueOf(subscriptionId2));
            setTelephonyManagerForSubscriptionId(context, subscriptionId2);
            setPhoneAccountHandleForSubscriptionId(context, subscriptionId2);
        }
    }

    private void setTelephonyManagerForSubscriptionId(Context context, int i) {
        this.mTelephonyManagerList.put(Integer.valueOf(i), ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(i));
    }

    private void setPhoneAccountHandleForSubscriptionId(Context context, int i) {
        this.mSimCallManagerList.put(Integer.valueOf(i), ((TelecomManager) context.getSystemService(TelecomManager.class)).getSimCallManagerForSubscription(i));
    }

    private TelephonyManager getTelephonyManagerForSubscriptionId(int i) {
        return this.mTelephonyManagerList.get(Integer.valueOf(i));
    }

    /* access modifiers changed from: protected */
    public PhoneAccountHandle getPhoneAccountHandleForSubscriptionId(int i) {
        return this.mSimCallManagerList.get(Integer.valueOf(i));
    }

    /* access modifiers changed from: protected */
    public WifiCallingQueryImsState queryImsState(int i) {
        return new WifiCallingQueryImsState(this.mContext, i);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        update();
    }

    public boolean isAvailable() {
        return this.mSubIdList.size() >= 1;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        this.mPreferenceGroup = (PreferenceGroup) preferenceScreen.findPreference(this.mPreferenceGroupKey);
        update();
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference != null) {
            update();
        }
    }

    private void update() {
        if (this.mPreferenceGroup != null) {
            setSubscriptionInfoList(this.mContext);
            if (!isAvailable()) {
                for (Preference removePreference : this.mWifiCallingForSubPreferences.values()) {
                    this.mPreferenceGroup.removePreference(removePreference);
                }
                this.mWifiCallingForSubPreferences.clear();
                return;
            }
            Map<Integer, Preference> map = this.mWifiCallingForSubPreferences;
            this.mWifiCallingForSubPreferences = new ArrayMap();
            setSubscriptionInfoForPreference(SubscriptionUtil.getActiveSubscriptions(this.mSubscriptionManager), map);
            for (Preference removePreference2 : map.values()) {
                this.mPreferenceGroup.removePreference(removePreference2);
            }
        }
    }

    private void setSubscriptionInfoForPreference(List<SubscriptionInfo> list, Map<Integer, Preference> map) {
        Intent buildPhoneAccountConfigureIntent;
        int i = 10;
        for (SubscriptionInfo next : list) {
            int subscriptionId = next.getSubscriptionId();
            if (shouldShowWifiCallingForSub(subscriptionId)) {
                Preference remove = map.remove(Integer.valueOf(subscriptionId));
                if (remove == null) {
                    remove = new Preference(this.mPreferenceGroup.getContext());
                    this.mPreferenceGroup.addPreference(remove);
                }
                CharSequence uniqueSubscriptionDisplayName = SubscriptionUtil.getUniqueSubscriptionDisplayName(next, this.mContext);
                boolean z = false;
                if (!(getPhoneAccountHandleForSubscriptionId(subscriptionId) == null || (buildPhoneAccountConfigureIntent = MobileNetworkUtils.buildPhoneAccountConfigureIntent(this.mContext, getPhoneAccountHandleForSubscriptionId(subscriptionId))) == null)) {
                    PackageManager packageManager = this.mContext.getPackageManager();
                    uniqueSubscriptionDisplayName = packageManager.queryIntentActivities(buildPhoneAccountConfigureIntent, 0).get(0).loadLabel(packageManager);
                    remove.setIntent(buildPhoneAccountConfigureIntent);
                }
                remove.setTitle(uniqueSubscriptionDisplayName);
                remove.setOnPreferenceClickListener(new NetworkProviderWifiCallingGroup$$ExternalSyntheticLambda0(this, next));
                if (getTelephonyManagerForSubscriptionId(subscriptionId).getCallState() == 0) {
                    z = true;
                }
                remove.setEnabled(z);
                int i2 = i + 1;
                remove.setOrder(i);
                int i3 = 17041630;
                if (queryImsState(subscriptionId).isEnabledByUser()) {
                    i3 = R.string.calls_sms_wfc_summary;
                }
                remove.setSummary(i3);
                this.mWifiCallingForSubPreferences.put(Integer.valueOf(subscriptionId), remove);
                i = i2;
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setSubscriptionInfoForPreference$0(SubscriptionInfo subscriptionInfo, Preference preference) {
        Intent intent = new Intent(this.mContext, Settings.WifiCallingSettingsActivity.class);
        intent.putExtra("android.provider.extra.SUB_ID", subscriptionInfo.getSubscriptionId());
        this.mContext.startActivity(intent);
        return true;
    }

    public void onSubscriptionsChanged() {
        update();
    }

    /* access modifiers changed from: protected */
    public boolean shouldShowWifiCallingForSub(int i) {
        return SubscriptionManager.isValidSubscriptionId(i) && MobileNetworkUtils.isWifiCallingEnabled(this.mContext, i, queryImsState(i), getPhoneAccountHandleForSubscriptionId(i)) && isWifiCallingAvailableForCarrier(i);
    }

    private boolean isWifiCallingAvailableForCarrier(int i) {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = this.mCarrierConfigManager;
        if (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(i)) == null) {
            return false;
        }
        return configForSubId.getBoolean("carrier_wfc_ims_available_bool");
    }
}
