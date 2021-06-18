package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsMmTelManager;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.network.MobileDataEnabledListener;
import com.android.settings.network.ims.VolteQueryImsState;
import com.android.settings.network.ims.VtQueryImsState;
import com.android.settings.network.telephony.Enhanced4gBasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class VideoCallingPreferenceController extends TelephonyTogglePreferenceController implements LifecycleObserver, OnStart, OnStop, MobileDataEnabledListener.Client, Enhanced4gBasePreferenceController.On4gLteUpdateListener {
    private static final String TAG = "VideoCallingPreference";
    Integer mCallState;
    private CarrierConfigManager mCarrierConfigManager;
    private MobileDataEnabledListener mDataContentObserver;
    /* access modifiers changed from: private */
    public Preference mPreference;
    private PhoneTelephonyCallback mTelephonyCallback = new PhoneTelephonyCallback();

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

    public VideoCallingPreferenceController(Context context, String str) {
        super(context, str);
        this.mCarrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        this.mDataContentObserver = new MobileDataEnabledListener(context, this);
    }

    public int getAvailabilityStatus(int i) {
        return (!SubscriptionManager.isValidSubscriptionId(i) || !isVideoCallEnabled(i)) ? 2 : 0;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public void onStart() {
        this.mTelephonyCallback.register(this.mContext, this.mSubId);
        this.mDataContentObserver.start(this.mSubId);
    }

    public void onStop() {
        this.mTelephonyCallback.unregister();
        this.mDataContentObserver.stop();
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (this.mCallState == null || preference == null) {
            Log.d(TAG, "Skip update under mCallState=" + this.mCallState);
            return;
        }
        SwitchPreference switchPreference = (SwitchPreference) preference;
        boolean isVideoCallEnabled = isVideoCallEnabled(this.mSubId);
        switchPreference.setVisible(isVideoCallEnabled);
        if (isVideoCallEnabled) {
            boolean z = true;
            boolean z2 = queryVoLteState(this.mSubId).isEnabledByUser() && queryImsState(this.mSubId).isAllowUserControl();
            preference.setEnabled(z2 && this.mCallState.intValue() == 0);
            if (!z2 || !isChecked()) {
                z = false;
            }
            switchPreference.setChecked(z);
        }
    }

    public boolean setChecked(boolean z) {
        ImsMmTelManager createForSubscriptionId;
        if (!SubscriptionManager.isValidSubscriptionId(this.mSubId) || (createForSubscriptionId = ImsMmTelManager.createForSubscriptionId(this.mSubId)) == null) {
            return false;
        }
        try {
            createForSubscriptionId.setVtSettingEnabled(z);
            return true;
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "Unable to set VT status " + z + ". subId=" + this.mSubId, e);
            return false;
        }
    }

    public boolean isChecked() {
        return queryImsState(this.mSubId).isEnabledByUser();
    }

    public VideoCallingPreferenceController init(int i) {
        this.mSubId = i;
        return this;
    }

    /* access modifiers changed from: package-private */
    public boolean isVideoCallEnabled(int i) {
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            return false;
        }
        if (this.mCarrierConfigManager == null) {
            Log.e(TAG, "CarrierConfigManager set to null.");
            CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class);
            this.mCarrierConfigManager = carrierConfigManager;
            if (carrierConfigManager == null) {
                Log.e(TAG, "Unable to reinitialize CarrierConfigManager.");
                return false;
            }
        }
        PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(i);
        if (configForSubId == null) {
            return false;
        }
        if (configForSubId.getBoolean("ignore_data_enabled_changed_for_video_calls") || ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(i).isDataEnabled()) {
            return queryImsState(i).isReadyToVideoCall();
        }
        return false;
    }

    public void on4gLteUpdated() {
        updateState(this.mPreference);
    }

    private class PhoneTelephonyCallback extends TelephonyCallback implements TelephonyCallback.CallStateListener {
        private TelephonyManager mTelephonyManager;

        private PhoneTelephonyCallback() {
        }

        public void onCallStateChanged(int i) {
            VideoCallingPreferenceController.this.mCallState = Integer.valueOf(i);
            VideoCallingPreferenceController videoCallingPreferenceController = VideoCallingPreferenceController.this;
            videoCallingPreferenceController.updateState(videoCallingPreferenceController.mPreference);
        }

        public void register(Context context, int i) {
            this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
            if (SubscriptionManager.isValidSubscriptionId(i)) {
                this.mTelephonyManager = this.mTelephonyManager.createForSubscriptionId(i);
            }
            VideoCallingPreferenceController.this.mCallState = Integer.valueOf(this.mTelephonyManager.getCallState(i));
            this.mTelephonyManager.registerTelephonyCallback(context.getMainExecutor(), this);
        }

        public void unregister() {
            VideoCallingPreferenceController.this.mCallState = null;
            this.mTelephonyManager.unregisterTelephonyCallback(this);
        }
    }

    public void onMobileDataEnabledChange() {
        updateState(this.mPreference);
    }

    /* access modifiers changed from: package-private */
    public VtQueryImsState queryImsState(int i) {
        return new VtQueryImsState(this.mContext, i);
    }

    /* access modifiers changed from: package-private */
    public VolteQueryImsState queryVoLteState(int i) {
        return new VolteQueryImsState(this.mContext, i);
    }
}
