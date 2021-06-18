package com.android.settings.network;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.network.InternetUpdater;
import com.android.settings.widget.SummaryUpdater;
import com.android.settings.wifi.WifiSummaryUpdater;
import com.android.settingslib.Utils;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.utils.ThreadUtils;
import java.util.HashMap;
import java.util.Map;

public class InternetPreferenceController extends AbstractPreferenceController implements LifecycleObserver, SummaryUpdater.OnSummaryChangeListener, InternetUpdater.InternetChangeListener {
    static Map<Integer, Integer> sIconMap;
    private static Map<Integer, Integer> sSummaryMap;
    private int mInternetType;
    private InternetUpdater mInternetUpdater;
    private Preference mPreference;
    private final WifiSummaryUpdater mSummaryHelper;

    public String getPreferenceKey() {
        return "internet_settings";
    }

    public boolean isAvailable() {
        return true;
    }

    static {
        HashMap hashMap = new HashMap();
        sIconMap = hashMap;
        hashMap.put(0, Integer.valueOf(R.drawable.ic_no_internet_airplane));
        sIconMap.put(1, Integer.valueOf(R.drawable.ic_no_internet_available));
        sIconMap.put(2, Integer.valueOf(R.drawable.ic_wifi_signal_4));
        sIconMap.put(3, Integer.valueOf(R.drawable.ic_network_cell));
        sIconMap.put(4, Integer.valueOf(R.drawable.ic_settings_ethernet));
        HashMap hashMap2 = new HashMap();
        sSummaryMap = hashMap2;
        hashMap2.put(0, Integer.valueOf(R.string.condition_airplane_title));
        sSummaryMap.put(1, Integer.valueOf(R.string.networks_available));
        sSummaryMap.put(2, 0);
        sSummaryMap.put(3, 0);
        sSummaryMap.put(4, Integer.valueOf(R.string.to_switch_networks_disconnect_ethernet));
    }

    public InternetPreferenceController(Context context, Lifecycle lifecycle) {
        super(context);
        if (lifecycle != null) {
            this.mSummaryHelper = new WifiSummaryUpdater(this.mContext, this);
            InternetUpdater internetUpdater = new InternetUpdater(context, lifecycle, this);
            this.mInternetUpdater = internetUpdater;
            this.mInternetType = internetUpdater.getInternetType();
            lifecycle.addObserver(this);
            return;
        }
        throw new IllegalArgumentException("Lifecycle must be set");
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference("internet_settings");
    }

    public void updateState(Preference preference) {
        Drawable drawable;
        if (this.mPreference != null) {
            int intValue = sIconMap.get(Integer.valueOf(this.mInternetType)).intValue();
            if (!(intValue == 0 || (drawable = this.mContext.getDrawable(intValue)) == null)) {
                drawable.setTintList(Utils.getColorAttr(this.mContext, 16843817));
                this.mPreference.setIcon(drawable);
            }
            int i = this.mInternetType;
            if (i == 2) {
                this.mPreference.setSummary((CharSequence) this.mSummaryHelper.getSummary());
            } else if (i == 3) {
                updateCellularSummary();
            } else {
                int intValue2 = sSummaryMap.get(Integer.valueOf(i)).intValue();
                if (intValue2 != 0) {
                    this.mPreference.setSummary(intValue2);
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        this.mSummaryHelper.register(true);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        this.mSummaryHelper.register(false);
    }

    public void onInternetTypeChanged(int i) {
        boolean z = i != this.mInternetType;
        this.mInternetType = i;
        if (z) {
            ThreadUtils.postOnMainThread(new InternetPreferenceController$$ExternalSyntheticLambda1(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onInternetTypeChanged$0() {
        updateState(this.mPreference);
    }

    public void onAirplaneModeChanged(boolean z) {
        ThreadUtils.postOnMainThread(new InternetPreferenceController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onAirplaneModeChanged$1() {
        updateState(this.mPreference);
    }

    public void onSummaryChanged(String str) {
        Preference preference;
        if (this.mInternetType == 2 && (preference = this.mPreference) != null) {
            preference.setSummary((CharSequence) str);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateCellularSummary() {
        SubscriptionInfo defaultDataSubscriptionInfo;
        SubscriptionManager subscriptionManager = (SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class);
        if (subscriptionManager != null && (defaultDataSubscriptionInfo = subscriptionManager.getDefaultDataSubscriptionInfo()) != null) {
            this.mPreference.setSummary(SubscriptionUtil.getUniqueSubscriptionDisplayName(defaultDataSubscriptionInfo, this.mContext));
        }
    }
}
