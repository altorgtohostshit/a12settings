package com.android.settings.privacy;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.IntentFilter;
import android.view.accessibility.AccessibilityManager;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import java.util.List;

public class AccessibilityUsagePreferenceController extends BasePreferenceController {
    private final AccessibilityManager mAccessibilityManager;
    private List<AccessibilityServiceInfo> mEnabledServiceInfos;

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

    public AccessibilityUsagePreferenceController(Context context, String str) {
        super(context, str);
        AccessibilityManager accessibilityManager = (AccessibilityManager) this.mContext.getSystemService(AccessibilityManager.class);
        this.mAccessibilityManager = accessibilityManager;
        this.mEnabledServiceInfos = accessibilityManager.getEnabledAccessibilityServiceList(-1);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        List<AccessibilityServiceInfo> enabledAccessibilityServiceList = this.mAccessibilityManager.getEnabledAccessibilityServiceList(-1);
        this.mEnabledServiceInfos = enabledAccessibilityServiceList;
        if (enabledAccessibilityServiceList.isEmpty()) {
            preference.setVisible(false);
        }
    }

    public int getAvailabilityStatus() {
        return this.mEnabledServiceInfos.isEmpty() ? 3 : 0;
    }

    public CharSequence getSummary() {
        return this.mContext.getResources().getQuantityString(R.plurals.accessibility_usage_summary, this.mEnabledServiceInfos.size(), new Object[]{Integer.valueOf(this.mEnabledServiceInfos.size())});
    }
}
