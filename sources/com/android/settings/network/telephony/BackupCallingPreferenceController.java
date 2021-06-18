package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.ims.ImsException;
import android.telephony.ims.ImsManager;
import android.telephony.ims.ImsMmTelManager;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.slices.SliceBackgroundWorker;
import java.util.List;
import java.util.Objects;

public class BackupCallingPreferenceController extends TelephonyTogglePreferenceController {
    private static final String LOG_TAG = "BackupCallingPrefCtrl";
    private Preference mPreference;

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

    public BackupCallingPreferenceController(Context context, String str) {
        super(context, str);
    }

    public BackupCallingPreferenceController init(int i) {
        this.mSubId = i;
        return this;
    }

    public int getAvailabilityStatus(int i) {
        if (!hasBackupCallingFeature(i)) {
            return 2;
        }
        List<SubscriptionInfo> activeSubscriptionList = getActiveSubscriptionList();
        if (getSubscriptionInfoFromList(activeSubscriptionList, i) != null && activeSubscriptionList.size() > 1) {
            return 0;
        }
        return 2;
    }

    public boolean setChecked(boolean z) {
        ImsMmTelManager imsMmTelManager = getImsMmTelManager(this.mSubId);
        if (imsMmTelManager == null) {
            return false;
        }
        try {
            imsMmTelManager.setCrossSimCallingEnabled(z);
            return true;
        } catch (ImsException e) {
            Log.w(LOG_TAG, "fail to change cross SIM calling configuration: " + z, e);
            return false;
        }
    }

    public boolean isChecked() {
        ImsMmTelManager imsMmTelManager = getImsMmTelManager(this.mSubId);
        if (imsMmTelManager == null) {
            return false;
        }
        try {
            return imsMmTelManager.isCrossSimCallingEnabled();
        } catch (ImsException e) {
            Log.w(LOG_TAG, "fail to get cross SIM calling configuration", e);
            return false;
        }
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference != null && (preference instanceof SwitchPreference)) {
            SubscriptionInfo subscriptionInfoFromActiveList = getSubscriptionInfoFromActiveList(this.mSubId);
            this.mPreference = preference;
            ((SwitchPreference) preference).setChecked(subscriptionInfoFromActiveList != null ? isChecked() : false);
            updateSummary(getLatestSummary(subscriptionInfoFromActiveList));
        }
    }

    private String getLatestSummary(SubscriptionInfo subscriptionInfo) {
        return Objects.toString(subscriptionInfo == null ? null : SubscriptionUtil.getUniqueSubscriptionDisplayName(subscriptionInfo, this.mContext), "");
    }

    private void updateSummary(String str) {
        Preference preference = this.mPreference;
        if (preference != null) {
            preference.setSummary((CharSequence) String.format(getResourcesForSubId().getString(R.string.backup_calling_setting_summary), new Object[]{str}).toString());
        }
    }

    private boolean hasBackupCallingFeature(int i) {
        PersistableBundle carrierConfigForSubId = getCarrierConfigForSubId(i);
        Boolean valueOf = carrierConfigForSubId != null ? Boolean.valueOf(carrierConfigForSubId.getBoolean("carrier_cross_sim_ims_available_bool", false)) : null;
        Log.d(LOG_TAG, "config carrier_cross_sim_ims_available_bool=" + valueOf + " for subId=" + i);
        if (valueOf == null || !valueOf.booleanValue()) {
            return false;
        }
        return true;
    }

    private ImsMmTelManager getImsMmTelManager(int i) {
        ImsManager imsManager;
        if (SubscriptionManager.isUsableSubscriptionId(i) && (imsManager = (ImsManager) this.mContext.getSystemService(ImsManager.class)) != null) {
            return imsManager.getImsMmTelManager(i);
        }
        return null;
    }

    private List<SubscriptionInfo> getActiveSubscriptionList() {
        return SubscriptionUtil.getActiveSubscriptions((SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class));
    }

    private SubscriptionInfo getSubscriptionInfoFromList(List<SubscriptionInfo> list, int i) {
        for (SubscriptionInfo next : list) {
            if (next != null && next.getSubscriptionId() == i) {
                return next;
            }
        }
        return null;
    }

    private SubscriptionInfo getSubscriptionInfoFromActiveList(int i) {
        if (!SubscriptionManager.isUsableSubscriptionId(i)) {
            return null;
        }
        return getSubscriptionInfoFromList(getActiveSubscriptionList(), i);
    }
}
