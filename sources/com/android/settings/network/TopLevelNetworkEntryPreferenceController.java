package com.android.settings.network;

import android.content.Context;
import android.content.IntentFilter;
import android.icu.text.ListFormatter;
import android.text.BidiFormatter;
import android.text.TextUtils;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.wifi.WifiPrimarySwitchPreferenceController;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;

public class TopLevelNetworkEntryPreferenceController extends BasePreferenceController {
    private final MobileNetworkPreferenceController mMobileNetworkPreferenceController = new MobileNetworkPreferenceController(this.mContext);
    private final TetherPreferenceController mTetherPreferenceController = new TetherPreferenceController(this.mContext, (Lifecycle) null);
    private final WifiPrimarySwitchPreferenceController mWifiPreferenceController = new WifiPrimarySwitchPreferenceController(this.mContext, (MetricsFeatureProvider) null);

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

    public TopLevelNetworkEntryPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return Utils.isDemoUser(this.mContext) ? 3 : 0;
    }

    public CharSequence getSummary() {
        String unicodeWrap = BidiFormatter.getInstance().unicodeWrap(this.mContext.getString(R.string.wifi_settings_title));
        String string = this.mContext.getString(R.string.network_dashboard_summary_mobile);
        String string2 = this.mContext.getString(R.string.network_dashboard_summary_data_usage);
        String string3 = this.mContext.getString(R.string.network_dashboard_summary_hotspot);
        ArrayList arrayList = new ArrayList();
        if (this.mWifiPreferenceController.isAvailable() && !TextUtils.isEmpty(unicodeWrap)) {
            arrayList.add(unicodeWrap);
        }
        if (this.mMobileNetworkPreferenceController.isAvailable() && !TextUtils.isEmpty(string)) {
            arrayList.add(string);
        }
        if (!TextUtils.isEmpty(string2)) {
            arrayList.add(string2);
        }
        if (this.mTetherPreferenceController.isAvailable() && !TextUtils.isEmpty(string3)) {
            arrayList.add(string3);
        }
        return ListFormatter.getInstance().format(arrayList);
    }
}
