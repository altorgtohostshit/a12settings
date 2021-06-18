package com.android.settings.network.telephony;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.slices.SliceBackgroundWorker;
import java.util.ArrayList;
import java.util.List;

public abstract class DefaultSubscriptionController extends TelephonyBasePreferenceController implements LifecycleObserver, Preference.OnPreferenceChangeListener, SubscriptionsChangeListener.SubscriptionsChangeListenerClient {
    private static final String EMERGENCY_ACCOUNT_HANDLE_ID = "E";
    private static final ComponentName PSTN_CONNECTION_SERVICE_COMPONENT = new ComponentName("com.android.phone", "com.android.services.telephony.TelephonyConnectionService");
    private static final String TAG = "DefaultSubController";
    protected SubscriptionsChangeListener mChangeListener;
    protected SubscriptionManager mManager;
    protected ListPreference mPreference;
    protected TelecomManager mTelecomManager;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    /* access modifiers changed from: protected */
    public abstract int getDefaultSubscriptionId();

    /* access modifiers changed from: protected */
    public abstract SubscriptionInfo getDefaultSubscriptionInfo();

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    /* access modifiers changed from: protected */
    public boolean isAskEverytimeSupported() {
        return true;
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

    public void onAirplaneModeChanged(boolean z) {
    }

    /* access modifiers changed from: protected */
    public abstract void setDefaultSubscription(int i);

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public DefaultSubscriptionController(Context context, String str) {
        super(context, str);
        this.mManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        this.mChangeListener = new SubscriptionsChangeListener(context, this);
    }

    public void init(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }

    public int getAvailabilityStatus(int i) {
        return (SubscriptionUtil.getActiveSubscriptions(this.mManager).size() > 1 || Utils.isProviderModelEnabled(this.mContext)) ? 0 : 2;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        this.mChangeListener.start();
        updateEntries();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        this.mChangeListener.stop();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (ListPreference) preferenceScreen.findPreference(getPreferenceKey());
        updateEntries();
    }

    public CharSequence getSummary() {
        PhoneAccountHandle defaultCallingAccountHandle = getDefaultCallingAccountHandle();
        if (defaultCallingAccountHandle != null && !isCallingAccountBindToSubscription(defaultCallingAccountHandle)) {
            return getLabelFromCallingAccount(defaultCallingAccountHandle);
        }
        SubscriptionInfo defaultSubscriptionInfo = getDefaultSubscriptionInfo();
        if (defaultSubscriptionInfo != null) {
            return SubscriptionUtil.getUniqueSubscriptionDisplayName(defaultSubscriptionInfo, this.mContext);
        }
        return isAskEverytimeSupported() ? this.mContext.getString(R.string.calls_and_sms_ask_every_time) : "";
    }

    private void updateEntries() {
        if (this.mPreference != null) {
            if (!isAvailable()) {
                this.mPreference.setVisible(false);
                return;
            }
            this.mPreference.setVisible(true);
            this.mPreference.setOnPreferenceChangeListener(this);
            List<SubscriptionInfo> activeSubscriptions = SubscriptionUtil.getActiveSubscriptions(this.mManager);
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            if (!Utils.isProviderModelEnabled(this.mContext) || activeSubscriptions.size() != 1) {
                int defaultSubscriptionId = getDefaultSubscriptionId();
                boolean z = false;
                for (SubscriptionInfo next : activeSubscriptions) {
                    if (!next.isOpportunistic()) {
                        arrayList.add(SubscriptionUtil.getUniqueSubscriptionDisplayName(next, this.mContext));
                        int subscriptionId = next.getSubscriptionId();
                        arrayList2.add(Integer.toString(subscriptionId));
                        if (subscriptionId == defaultSubscriptionId) {
                            z = true;
                        }
                    }
                }
                if (isAskEverytimeSupported()) {
                    arrayList.add(this.mContext.getString(R.string.calls_and_sms_ask_every_time));
                    arrayList2.add(Integer.toString(-1));
                }
                this.mPreference.setEntries((CharSequence[]) arrayList.toArray(new CharSequence[0]));
                this.mPreference.setEntryValues((CharSequence[]) arrayList2.toArray(new CharSequence[0]));
                if (z) {
                    this.mPreference.setValue(Integer.toString(defaultSubscriptionId));
                } else {
                    this.mPreference.setValue(Integer.toString(-1));
                }
            } else {
                this.mPreference.setEnabled(false);
                this.mPreference.setSummary(SubscriptionUtil.getUniqueSubscriptionDisplayName(activeSubscriptions.get(0), this.mContext));
            }
        }
    }

    public PhoneAccountHandle getDefaultCallingAccountHandle() {
        PhoneAccountHandle userSelectedOutgoingPhoneAccount = getTelecomManager().getUserSelectedOutgoingPhoneAccount();
        if (userSelectedOutgoingPhoneAccount == null) {
            return null;
        }
        List<PhoneAccountHandle> callCapablePhoneAccounts = getTelecomManager().getCallCapablePhoneAccounts(false);
        if (userSelectedOutgoingPhoneAccount.equals(new PhoneAccountHandle(PSTN_CONNECTION_SERVICE_COMPONENT, EMERGENCY_ACCOUNT_HANDLE_ID))) {
            return null;
        }
        for (PhoneAccountHandle equals : callCapablePhoneAccounts) {
            if (userSelectedOutgoingPhoneAccount.equals(equals)) {
                return userSelectedOutgoingPhoneAccount;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public TelecomManager getTelecomManager() {
        if (this.mTelecomManager == null) {
            this.mTelecomManager = (TelecomManager) this.mContext.getSystemService(TelecomManager.class);
        }
        return this.mTelecomManager;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public PhoneAccount getPhoneAccount(PhoneAccountHandle phoneAccountHandle) {
        return getTelecomManager().getPhoneAccount(phoneAccountHandle);
    }

    public boolean isCallingAccountBindToSubscription(PhoneAccountHandle phoneAccountHandle) {
        PhoneAccount phoneAccount = getPhoneAccount(phoneAccountHandle);
        if (phoneAccount == null) {
            return false;
        }
        return phoneAccount.hasCapabilities(4);
    }

    public CharSequence getLabelFromCallingAccount(PhoneAccountHandle phoneAccountHandle) {
        PhoneAccount phoneAccount = getPhoneAccount(phoneAccountHandle);
        CharSequence label = phoneAccount != null ? phoneAccount.getLabel() : null;
        if (label != null) {
            label = this.mContext.getPackageManager().getUserBadgedLabel(label, phoneAccountHandle.getUserHandle());
        }
        return label != null ? label : "";
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        setDefaultSubscription(Integer.parseInt((String) obj));
        refreshSummary(this.mPreference);
        return true;
    }

    public void onSubscriptionsChanged() {
        if (this.mPreference != null) {
            updateEntries();
            refreshSummary(this.mPreference);
        }
    }
}
