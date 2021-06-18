package com.google.android.settings.external.specialcase;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import com.android.settings.R;
import com.android.settings.datausage.DataUsageSummary;
import com.android.settings.slices.SliceData;
import com.android.settingslib.net.DataUsageController;
import com.google.android.settings.external.ExternalSettingsContract;
import com.google.android.settings.external.Queryable;

public class MobileDataSetting implements Queryable {
    private final int OFF = 0;

    /* renamed from: ON */
    private final int f127ON = 1;

    private int getIconResource() {
        return R.drawable.ic_network_cell;
    }

    public Cursor getAccessCursor(Context context, SliceData sliceData) {
        DataUsageController dataUsageController = new DataUsageController(context);
        int currentValue = getCurrentValue(dataUsageController);
        int availability = getAvailability(dataUsageController);
        String intentString = getIntentString(context, "mobile_data", DataUsageSummary.class, getScreenTitle(context));
        int iconResource = getIconResource();
        MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", Integer.valueOf(currentValue)).add("availability", Integer.valueOf(availability)).add("intent", intentString).add("icon", Integer.valueOf(iconResource));
        return matrixCursor;
    }

    public Cursor getUpdateCursor(Context context, SliceData sliceData, int i) {
        validateInput(i);
        DataUsageController dataUsageController = new DataUsageController(context);
        int currentValue = getCurrentValue(dataUsageController);
        int availability = getAvailability(dataUsageController);
        String intentString = getIntentString(context, "mobile_data", DataUsageSummary.class, getScreenTitle(context));
        int iconResource = getIconResource();
        if (shouldChangeValue(availability, currentValue, i)) {
            boolean z = true;
            if (i != 1) {
                z = false;
            }
            dataUsageController.setMobileDataEnabled(z);
        } else {
            i = currentValue;
        }
        MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", Integer.valueOf(i)).add("existing_value", Integer.valueOf(currentValue)).add("availability", Integer.valueOf(availability)).add("intent", intentString).add("icon", Integer.valueOf(iconResource));
        return matrixCursor;
    }

    private int getAvailability(DataUsageController dataUsageController) {
        return dataUsageController.isMobileDataSupported() ? 0 : 2;
    }

    private int getCurrentValue(DataUsageController dataUsageController) {
        return dataUsageController.isMobileDataEnabled() ? 1 : 0;
    }

    private void validateInput(int i) {
        if (i != 1 && i != 0) {
            throw new IllegalArgumentException("Unexpected value for mobile data mode. Expected 0 or 1, but found: " + i);
        }
    }

    private String getScreenTitle(Context context) {
        return context.getString(R.string.data_usage_summary_title);
    }
}
