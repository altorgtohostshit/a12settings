package com.android.settings.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.accessibility.AccessibilityUtils;

public class AccessibilitySlicePreferenceController extends TogglePreferenceController {
    private static final String EMPTY_STRING = "";
    private final ComponentName mComponentName;

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

    public boolean isPublicSlice() {
        return true;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AccessibilitySlicePreferenceController(Context context, String str) {
        super(context, str);
        ComponentName unflattenFromString = ComponentName.unflattenFromString(getPreferenceKey());
        this.mComponentName = unflattenFromString;
        if (unflattenFromString == null) {
            throw new IllegalArgumentException("Illegal Component Name from: " + str);
        }
    }

    public CharSequence getSummary() {
        AccessibilityServiceInfo accessibilityServiceInfo = getAccessibilityServiceInfo();
        if (accessibilityServiceInfo == null) {
            return EMPTY_STRING;
        }
        return AccessibilitySettings.getServiceSummary(this.mContext, accessibilityServiceInfo, isChecked());
    }

    public boolean isChecked() {
        boolean z = true;
        if (Settings.Secure.getInt(this.mContext.getContentResolver(), "accessibility_enabled", 0) != 1) {
            z = false;
        }
        if (!z) {
            return false;
        }
        return AccessibilityUtils.getEnabledServicesFromSettings(this.mContext).contains(this.mComponentName);
    }

    public boolean setChecked(boolean z) {
        if (getAccessibilityServiceInfo() == null) {
            return false;
        }
        AccessibilityUtils.setAccessibilityServiceState(this.mContext, this.mComponentName, z);
        if (z == isChecked()) {
            return true;
        }
        return false;
    }

    public int getAvailabilityStatus() {
        return getAccessibilityServiceInfo() == null ? 3 : 0;
    }

    private AccessibilityServiceInfo getAccessibilityServiceInfo() {
        for (AccessibilityServiceInfo next : ((AccessibilityManager) this.mContext.getSystemService(AccessibilityManager.class)).getInstalledAccessibilityServiceList()) {
            if (this.mComponentName.equals(next.getComponentName())) {
                return next;
            }
        }
        return null;
    }
}
