package com.android.settings.network.telephony;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.wifi.WifiPickerTrackerHelper;
import com.android.wifitrackerlib.WifiPickerTracker;

public class MobileDataDialogFragment extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
    private int mSubId;
    private SubscriptionManager mSubscriptionManager;
    private int mType;
    private WifiPickerTrackerHelper mWifiPickerTrackerHelper;

    public int getMetricsCategory() {
        return 1582;
    }

    public static MobileDataDialogFragment newInstance(int i, int i2) {
        MobileDataDialogFragment mobileDataDialogFragment = new MobileDataDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("dialog_type", i);
        bundle.putInt("subId", i2);
        mobileDataDialogFragment.setArguments(bundle);
        return mobileDataDialogFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mSubscriptionManager = (SubscriptionManager) getContext().getSystemService(SubscriptionManager.class);
        this.mWifiPickerTrackerHelper = new WifiPickerTrackerHelper(getSettingsLifecycle(), getContext(), (WifiPickerTracker.WifiPickerTrackerCallback) null);
    }

    public Dialog onCreateDialog(Bundle bundle) {
        String str;
        String str2;
        Bundle arguments = getArguments();
        Context context = getContext();
        this.mType = arguments.getInt("dialog_type");
        int i = arguments.getInt("subId");
        this.mSubId = i;
        int i2 = this.mType;
        if (i2 == 0) {
            return new AlertDialog.Builder(context).setMessage((int) R.string.data_usage_disable_mobile).setPositiveButton(17039370, (DialogInterface.OnClickListener) this).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
        }
        if (i2 == 1) {
            SubscriptionInfo activeSubscriptionInfo = this.mSubscriptionManager.getActiveSubscriptionInfo(i);
            SubscriptionInfo activeSubscriptionInfo2 = this.mSubscriptionManager.getActiveSubscriptionInfo(SubscriptionManager.getDefaultDataSubscriptionId());
            if (activeSubscriptionInfo2 == null) {
                str = getContext().getResources().getString(R.string.sim_selection_required_pref);
            } else {
                str = SubscriptionUtil.getUniqueSubscriptionDisplayName(activeSubscriptionInfo2, getContext()).toString();
            }
            if (activeSubscriptionInfo == null) {
                str2 = getContext().getResources().getString(R.string.sim_selection_required_pref);
            } else {
                str2 = SubscriptionUtil.getUniqueSubscriptionDisplayName(activeSubscriptionInfo, getContext()).toString();
            }
            return new AlertDialog.Builder(context).setTitle((CharSequence) context.getString(R.string.sim_change_data_title, new Object[]{str2})).setMessage((CharSequence) context.getString(R.string.sim_change_data_message, new Object[]{str2, str})).setPositiveButton((CharSequence) context.getString(R.string.sim_change_data_ok, new Object[]{str2}), (DialogInterface.OnClickListener) this).setNegativeButton((int) R.string.cancel, (DialogInterface.OnClickListener) null).create();
        }
        throw new IllegalArgumentException("unknown type " + this.mType);
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        int i2 = this.mType;
        if (i2 == 0) {
            MobileNetworkUtils.setMobileDataEnabled(getContext(), this.mSubId, false, false);
            WifiPickerTrackerHelper wifiPickerTrackerHelper = this.mWifiPickerTrackerHelper;
            if (wifiPickerTrackerHelper != null && !wifiPickerTrackerHelper.isCarrierNetworkProvisionEnabled(this.mSubId)) {
                this.mWifiPickerTrackerHelper.setCarrierNetworkEnabled(false);
            }
        } else if (i2 == 1) {
            this.mSubscriptionManager.setDefaultDataSubId(this.mSubId);
            MobileNetworkUtils.setMobileDataEnabled(getContext(), this.mSubId, true, true);
            WifiPickerTrackerHelper wifiPickerTrackerHelper2 = this.mWifiPickerTrackerHelper;
            if (wifiPickerTrackerHelper2 != null && !wifiPickerTrackerHelper2.isCarrierNetworkProvisionEnabled(this.mSubId)) {
                this.mWifiPickerTrackerHelper.setCarrierNetworkEnabled(true);
            }
        } else {
            throw new IllegalArgumentException("unknown type " + this.mType);
        }
    }
}
