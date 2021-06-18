package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.text.TextUtils;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.security.ConfirmSimDeletionPreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.wifi.dpp.WifiDppUtils;
import java.util.Iterator;

public class DeleteSimProfilePreferenceController extends BasePreferenceController {
    private boolean mConfirmationDefaultOn;
    private Fragment mParentFragment;
    private int mRequestCode;
    private SubscriptionInfo mSubscriptionInfo;

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

    public DeleteSimProfilePreferenceController(Context context, String str) {
        super(context, str);
        this.mConfirmationDefaultOn = context.getResources().getBoolean(R.bool.config_sim_deletion_confirmation_default_on);
    }

    public void init(int i, Fragment fragment, int i2) {
        this.mParentFragment = fragment;
        Iterator<SubscriptionInfo> it = SubscriptionUtil.getAvailableSubscriptions(this.mContext).iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            SubscriptionInfo next = it.next();
            if (next.getSubscriptionId() == i && next.isEmbedded()) {
                this.mSubscriptionInfo = next;
                break;
            }
        }
        this.mRequestCode = i2;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        boolean z = false;
        if (!TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return false;
        }
        if (Settings.Global.getInt(this.mContext.getContentResolver(), ConfirmSimDeletionPreferenceController.KEY_CONFIRM_SIM_DELETION, this.mConfirmationDefaultOn ? 1 : 0) == 1) {
            z = true;
        }
        if (z) {
            WifiDppUtils.showLockScreen(this.mContext, new DeleteSimProfilePreferenceController$$ExternalSyntheticLambda0(this));
        } else {
            lambda$handlePreferenceTreeClick$0();
        }
        return true;
    }

    /* access modifiers changed from: private */
    /* renamed from: deleteSim */
    public void lambda$handlePreferenceTreeClick$0() {
        SubscriptionUtil.startDeleteEuiccSubscriptionDialogActivity(this.mContext, this.mSubscriptionInfo.getSubscriptionId());
    }

    public int getAvailabilityStatus() {
        return this.mSubscriptionInfo != null ? 0 : 2;
    }
}
