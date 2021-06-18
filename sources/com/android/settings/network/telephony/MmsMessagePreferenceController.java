package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import androidx.preference.PreferenceScreen;
import com.android.settings.network.MobileDataContentObserver;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class MmsMessagePreferenceController extends TelephonyTogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    private MobileDataContentObserver mMobileDataContentObserver;
    private PreferenceScreen mScreen;
    private SubscriptionManager mSubscriptionManager;
    private TelephonyManager mTelephonyManager;

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

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public MmsMessagePreferenceController(Context context, String str) {
        super(context, str);
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        MobileDataContentObserver mobileDataContentObserver = new MobileDataContentObserver(new Handler(Looper.getMainLooper()));
        this.mMobileDataContentObserver = mobileDataContentObserver;
        mobileDataContentObserver.setOnMobileDataChangedListener(new MmsMessagePreferenceController$$ExternalSyntheticLambda0(this));
    }

    public int getAvailabilityStatus(int i) {
        TelephonyManager createForSubscriptionId = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(i);
        if (i == -1 || createForSubscriptionId.isDataEnabled() || !createForSubscriptionId.isApnMetered(2)) {
            return 2;
        }
        return 0;
    }

    public void onStart() {
        int i = this.mSubId;
        if (i != -1) {
            this.mMobileDataContentObserver.register(this.mContext, i);
        }
    }

    public void onStop() {
        if (this.mSubId != -1) {
            this.mMobileDataContentObserver.unRegister(this.mContext);
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mScreen = preferenceScreen;
    }

    public void init(int i) {
        this.mSubId = i;
        this.mTelephonyManager = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mSubId);
    }

    public boolean setChecked(boolean z) {
        this.mTelephonyManager.setMobileDataPolicyEnabled(2, z);
        return z == this.mTelephonyManager.isMobileDataPolicyEnabled(2);
    }

    public boolean isChecked() {
        return this.mTelephonyManager.isDataEnabledForApn(2);
    }

    /* access modifiers changed from: private */
    /* renamed from: refreshPreference */
    public void lambda$new$0() {
        PreferenceScreen preferenceScreen = this.mScreen;
        if (preferenceScreen != null) {
            super.displayPreference(preferenceScreen);
        }
    }
}
