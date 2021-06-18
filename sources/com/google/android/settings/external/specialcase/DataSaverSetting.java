package com.google.android.settings.external.specialcase;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import com.android.settings.R;
import com.android.settings.datausage.DataSaverBackend;
import com.android.settings.datausage.DataUsageSummary;
import com.android.settings.datausage.DataUsageUtils;
import com.android.settings.slices.SliceData;
import com.google.android.settings.external.ExternalSettingsContract;
import com.google.android.settings.external.Queryable;

public class DataSaverSetting implements Queryable {
    private int getIconResource() {
        return R.drawable.ic_settings_data_usage;
    }

    public Cursor getAccessCursor(Context context, SliceData sliceData) {
        int currentValue = getCurrentValue(new DataSaverBackend(context));
        int availability = getAvailability(context);
        String intentString = getIntentString(context, "data_saver", DataUsageSummary.class, getScreenTitle(context));
        int iconResource = getIconResource();
        MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", Integer.valueOf(currentValue)).add("availability", Integer.valueOf(availability)).add("intent", intentString).add("icon", Integer.valueOf(iconResource));
        return matrixCursor;
    }

    public Cursor getUpdateCursor(Context context, SliceData sliceData, int i) {
        validateInput(i);
        DataSaverBackend dataSaverBackend = new DataSaverBackend(context);
        int currentValue = getCurrentValue(dataSaverBackend);
        int availability = getAvailability(context);
        String intentString = getIntentString(context, "data_saver", DataUsageSummary.class, getScreenTitle(context));
        int iconResource = getIconResource();
        if (shouldChangeValue(availability, currentValue, i)) {
            boolean z = true;
            if (i != 1) {
                z = false;
            }
            dataSaverBackend.setDataSaverEnabled(z);
        } else {
            i = currentValue;
        }
        MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", Integer.valueOf(i)).add("existing_value", Integer.valueOf(currentValue)).add("availability", Integer.valueOf(availability)).add("intent", intentString).add("icon", Integer.valueOf(iconResource));
        return matrixCursor;
    }

    private int getCurrentValue(DataSaverBackend dataSaverBackend) {
        return dataSaverBackend.isDataSaverEnabled() ? 1 : 0;
    }

    private int getAvailability(Context context) {
        return DataUsageUtils.hasMobileData(context) ? 0 : 2;
    }

    private void validateInput(int i) {
        if (i != 1 && i != 0) {
            throw new IllegalArgumentException("Unexpected value for data saver. Expected 0 or 1, but found: " + i);
        }
    }

    private String getScreenTitle(Context context) {
        return context.getString(R.string.data_usage_summary_title);
    }
}
