package com.google.android.settings.external.specialcase;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;
import com.android.settings.R;
import com.android.settings.accessibility.AccessibilitySettings;
import com.android.settings.slices.SliceData;
import com.android.settingslib.accessibility.AccessibilityUtils;
import com.google.android.settings.external.ExternalSettingsContract;
import com.google.android.settings.external.Queryable;
import java.util.List;
import java.util.Set;

public class AccessibilitySetting implements Queryable {
    private final ComponentName mComponentName;
    private final String mKey;
    private final String mServiceName;

    private int getIconResource() {
        return 0;
    }

    public AccessibilitySetting(String str, String str2) {
        this.mServiceName = str2;
        this.mKey = str;
        this.mComponentName = ComponentName.unflattenFromString(str2);
    }

    public Cursor getAccessCursor(Context context, SliceData sliceData) {
        int currentValue = getCurrentValue(context);
        int availability = getAvailability(context);
        int iconResource = getIconResource();
        String intentString = getIntentString(context, this.mServiceName, AccessibilitySettings.class, getScreenTitle(context));
        MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", Integer.valueOf(currentValue)).add("availability", Integer.valueOf(availability)).add("intent", intentString).add("icon", Integer.valueOf(iconResource));
        return matrixCursor;
    }

    public Cursor getUpdateCursor(Context context, SliceData sliceData, int i) {
        validateInput(i);
        int currentValue = getCurrentValue(context);
        int availability = getAvailability(context);
        String intentString = getIntentString(context, this.mServiceName, AccessibilitySettings.class, getScreenTitle(context));
        int iconResource = getIconResource();
        if (shouldChangeValue(availability, currentValue, i)) {
            boolean z = true;
            if (i != 1) {
                z = false;
            }
            setTalkback(context, z);
        } else {
            i = currentValue;
        }
        MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", Integer.valueOf(i)).add("existing_value", Integer.valueOf(currentValue)).add("availability", Integer.valueOf(availability)).add("intent", intentString).add("icon", Integer.valueOf(iconResource));
        return matrixCursor;
    }

    private void validateInput(int i) {
        if (i != 1 && i != 0) {
            throw new IllegalArgumentException("Unexpected value for Accessibility:" + this.mKey + ". Expected 0 or 1, but found: " + i);
        }
    }

    private void setTalkback(Context context, boolean z) {
        AccessibilityUtils.setAccessibilityServiceState(context, this.mComponentName, z);
    }

    private int getCurrentValue(Context context) {
        boolean z = Settings.Secure.getInt(context.getContentResolver(), "accessibility_enabled", 0) == 1;
        Set<ComponentName> enabledServicesFromSettings = AccessibilityUtils.getEnabledServicesFromSettings(context);
        if (!z || !enabledServicesFromSettings.contains(this.mComponentName)) {
            return 0;
        }
        return 1;
    }

    private int getAvailability(Context context) {
        List<AccessibilityServiceInfo> installedAccessibilityServiceList = ((AccessibilityManager) context.getSystemService("accessibility")).getInstalledAccessibilityServiceList();
        int size = installedAccessibilityServiceList.size();
        for (int i = 0; i < size; i++) {
            if (this.mComponentName.equals(installedAccessibilityServiceList.get(i).getComponentName())) {
                return 0;
            }
        }
        return 4;
    }

    private String getScreenTitle(Context context) {
        return context.getString(R.string.accessibility_settings);
    }
}
