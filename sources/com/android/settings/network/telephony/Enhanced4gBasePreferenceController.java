package com.android.settings.network.telephony;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsMmTelManager;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.internal.telephony.util.ArrayUtils;
import com.android.settings.R;
import com.android.settings.network.ims.VolteQueryImsState;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.util.ArrayList;
import java.util.List;

public class Enhanced4gBasePreferenceController extends TelephonyTogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    protected static final int MODE_4G_CALLING = 2;
    protected static final int MODE_ADVANCED_CALL = 1;
    protected static final int MODE_NONE = -1;
    protected static final int MODE_VOLTE = 0;
    private static final String TAG = "Enhanced4g";
    private int m4gCurrentMode = MODE_NONE;
    private final List<On4gLteUpdateListener> m4gLteListeners = new ArrayList();
    /* access modifiers changed from: private */
    public Integer mCallState;
    /* access modifiers changed from: private */
    public boolean mHas5gCapability;
    boolean mIsNrEnabledFromCarrierConfig;
    Preference mPreference;
    private boolean mShow5gLimitedDialog;
    /* access modifiers changed from: private */
    public PhoneCallStateTelephonyCallback mTelephonyCallback;

    public interface On4gLteUpdateListener {
        void on4gLteUpdated();
    }

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    /* access modifiers changed from: protected */
    public int getMode() {
        return MODE_NONE;
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

    public Enhanced4gBasePreferenceController(Context context, String str) {
        super(context, str);
    }

    public Enhanced4gBasePreferenceController init(int i) {
        if (this.mTelephonyCallback == null) {
            this.mTelephonyCallback = new PhoneCallStateTelephonyCallback();
        }
        if (this.mSubId == i) {
            return this;
        }
        this.mSubId = i;
        PersistableBundle carrierConfigForSubId = getCarrierConfigForSubId(i);
        if (carrierConfigForSubId == null) {
            return this;
        }
        boolean z = carrierConfigForSubId.getBoolean("show_4g_for_lte_data_icon_bool");
        int i2 = carrierConfigForSubId.getInt("enhanced_4g_lte_title_variant_int");
        this.m4gCurrentMode = i2;
        if (i2 != 1) {
            this.m4gCurrentMode = z ? 2 : 0;
        }
        this.mShow5gLimitedDialog = carrierConfigForSubId.getBoolean("volte_5g_limited_alert_dialog_bool");
        this.mIsNrEnabledFromCarrierConfig = !ArrayUtils.isEmpty(carrierConfigForSubId.getIntArray("carrier_nr_availabilities_int_array"));
        return this;
    }

    public int getAvailabilityStatus(int i) {
        init(i);
        if (!isModeMatched()) {
            return 2;
        }
        VolteQueryImsState queryImsState = queryImsState(i);
        if (queryImsState.isVoImsOptInEnabled()) {
            return 0;
        }
        PersistableBundle carrierConfigForSubId = getCarrierConfigForSubId(i);
        if (carrierConfigForSubId == null || carrierConfigForSubId.getBoolean("hide_enhanced_4g_lte_bool") || !queryImsState.isReadyToVoLte()) {
            return 2;
        }
        if (!isUserControlAllowed(carrierConfigForSubId) || !queryImsState.isAllowUserControl()) {
            return 1;
        }
        return 0;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public void onStart() {
        PhoneCallStateTelephonyCallback phoneCallStateTelephonyCallback;
        if (isModeMatched() && (phoneCallStateTelephonyCallback = this.mTelephonyCallback) != null) {
            phoneCallStateTelephonyCallback.register(this.mContext, this.mSubId);
        }
    }

    public void onStop() {
        PhoneCallStateTelephonyCallback phoneCallStateTelephonyCallback = this.mTelephonyCallback;
        if (phoneCallStateTelephonyCallback != null) {
            phoneCallStateTelephonyCallback.unregister();
        }
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference != null) {
            SwitchPreference switchPreference = (SwitchPreference) preference;
            VolteQueryImsState queryImsState = queryImsState(this.mSubId);
            boolean z = true;
            switchPreference.setEnabled(isUserControlAllowed(getCarrierConfigForSubId(this.mSubId)) && queryImsState.isAllowUserControl());
            if (!queryImsState.isEnabledByUser() || !queryImsState.isAllowUserControl()) {
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
        if (!isDialogNeeded() || z) {
            return setAdvancedCallingSettingEnabled(createForSubscriptionId, z);
        }
        show5gLimitedDialog(createForSubscriptionId);
        return false;
    }

    public boolean isChecked() {
        return queryImsState(this.mSubId).isEnabledByUser();
    }

    public Enhanced4gBasePreferenceController addListener(On4gLteUpdateListener on4gLteUpdateListener) {
        this.m4gLteListeners.add(on4gLteUpdateListener);
        return this;
    }

    private boolean isModeMatched() {
        return this.m4gCurrentMode == getMode();
    }

    /* access modifiers changed from: protected */
    public VolteQueryImsState queryImsState(int i) {
        return new VolteQueryImsState(this.mContext, i);
    }

    /* access modifiers changed from: protected */
    public boolean isCallStateIdle() {
        Integer num = this.mCallState;
        return num != null && num.intValue() == 0;
    }

    private boolean isUserControlAllowed(PersistableBundle persistableBundle) {
        return isCallStateIdle() && persistableBundle != null && persistableBundle.getBoolean("editable_enhanced_4g_lte_bool");
    }

    private class PhoneCallStateTelephonyCallback extends TelephonyCallback implements TelephonyCallback.CallStateListener {
        private TelephonyManager mTelephonyManager;

        private PhoneCallStateTelephonyCallback() {
        }

        public void onCallStateChanged(int i) {
            Integer unused = Enhanced4gBasePreferenceController.this.mCallState = Integer.valueOf(i);
            Enhanced4gBasePreferenceController enhanced4gBasePreferenceController = Enhanced4gBasePreferenceController.this;
            enhanced4gBasePreferenceController.updateState(enhanced4gBasePreferenceController.mPreference);
        }

        public void register(Context context, int i) {
            this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
            if (SubscriptionManager.isValidSubscriptionId(i)) {
                this.mTelephonyManager = this.mTelephonyManager.createForSubscriptionId(i);
            }
            Integer unused = Enhanced4gBasePreferenceController.this.mCallState = Integer.valueOf(this.mTelephonyManager.getCallState(i));
            this.mTelephonyManager.registerTelephonyCallback(Enhanced4gBasePreferenceController.this.mContext.getMainExecutor(), Enhanced4gBasePreferenceController.this.mTelephonyCallback);
            boolean unused2 = Enhanced4gBasePreferenceController.this.mHas5gCapability = (this.mTelephonyManager.getSupportedRadioAccessFamily() & 524288) > 0;
        }

        public void unregister() {
            Integer unused = Enhanced4gBasePreferenceController.this.mCallState = null;
            TelephonyManager telephonyManager = this.mTelephonyManager;
            if (telephonyManager != null) {
                telephonyManager.unregisterTelephonyCallback(this);
            }
        }
    }

    private boolean isDialogNeeded() {
        Log.d(TAG, "Has5gCapability:" + this.mHas5gCapability);
        return this.mShow5gLimitedDialog && this.mHas5gCapability && this.mIsNrEnabledFromCarrierConfig;
    }

    private void show5gLimitedDialog(final ImsMmTelManager imsMmTelManager) {
        Log.d(TAG, "show5gLimitedDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setTitle(R.string.volte_5G_limited_title).setMessage(R.string.volte_5G_limited_text).setNeutralButton(this.mContext.getResources().getString(R.string.cancel), (DialogInterface.OnClickListener) null).setPositiveButton(this.mContext.getResources().getString(R.string.condition_turn_off), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(Enhanced4gBasePreferenceController.TAG, "onClick,isChecked:false");
                boolean unused = Enhanced4gBasePreferenceController.this.setAdvancedCallingSettingEnabled(imsMmTelManager, false);
                Enhanced4gBasePreferenceController enhanced4gBasePreferenceController = Enhanced4gBasePreferenceController.this;
                enhanced4gBasePreferenceController.updateState(enhanced4gBasePreferenceController.mPreference);
            }
        }).create().show();
    }

    /* access modifiers changed from: private */
    public boolean setAdvancedCallingSettingEnabled(ImsMmTelManager imsMmTelManager, boolean z) {
        try {
            imsMmTelManager.setAdvancedCallingSettingEnabled(z);
            for (On4gLteUpdateListener on4gLteUpdated : this.m4gLteListeners) {
                on4gLteUpdated.on4gLteUpdated();
            }
            return true;
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "fail to set VoLTE=" + z + ". subId=" + this.mSubId, e);
            return false;
        }
    }
}
