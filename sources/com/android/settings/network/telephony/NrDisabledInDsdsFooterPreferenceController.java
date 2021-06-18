package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.utils.AnnotationSpan;
import com.android.settingslib.HelpUtils;

public class NrDisabledInDsdsFooterPreferenceController extends BasePreferenceController {
    private int mSubId = -1;

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

    public NrDisabledInDsdsFooterPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void init(int i) {
        this.mSubId = i;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference != null) {
            preference.setTitle((CharSequence) "");
            preference.setTitle(getFooterText());
        }
    }

    private CharSequence getFooterText() {
        Context context = this.mContext;
        AnnotationSpan.LinkInfo linkInfo = new AnnotationSpan.LinkInfo(this.mContext, "url", HelpUtils.getHelpIntent(context, context.getString(R.string.help_uri_5g_dsds), this.mContext.getClass().getName()));
        if (!linkInfo.isActionable()) {
            return AnnotationSpan.textWithoutLink(this.mContext.getText(R.string.no_5g_in_dsds_text));
        }
        return AnnotationSpan.linkify(this.mContext.getText(R.string.no_5g_in_dsds_text), linkInfo);
    }

    public int getAvailabilityStatus() {
        int i;
        if (this.mSubId == -1) {
            return 2;
        }
        TelephonyManager createForSubscriptionId = ((TelephonyManager) this.mContext.getSystemService("phone")).createForSubscriptionId(this.mSubId);
        int[] activeSubscriptionIdList = ((SubscriptionManager) this.mContext.getSystemService("telephony_subscription_service")).getActiveSubscriptionIdList();
        if (activeSubscriptionIdList == null) {
            i = 0;
        } else {
            i = activeSubscriptionIdList.length;
        }
        if (!createForSubscriptionId.isDataEnabled() || i < 2 || !is5GSupportedByRadio(createForSubscriptionId) || createForSubscriptionId.canConnectTo5GInDsdsMode()) {
            return 2;
        }
        return 0;
    }

    private boolean is5GSupportedByRadio(TelephonyManager telephonyManager) {
        return (telephonyManager.getSupportedRadioAccessFamily() & 524288) > 0;
    }
}
