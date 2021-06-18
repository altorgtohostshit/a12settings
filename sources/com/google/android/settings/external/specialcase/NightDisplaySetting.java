package com.google.android.settings.external.specialcase;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.hardware.display.ColorDisplayManager;
import com.android.settings.R;
import com.android.settings.display.NightDisplaySettings;
import com.android.settings.slices.SliceData;
import com.google.android.settings.external.ExternalSettingsContract;
import com.google.android.settings.external.Queryable;

public class NightDisplaySetting implements Queryable {
    private int getIconResource() {
        return 17302820;
    }

    public Cursor getAccessCursor(Context context, SliceData sliceData) {
        boolean isNightDisplayActivated = ((ColorDisplayManager) context.getSystemService(ColorDisplayManager.class)).isNightDisplayActivated();
        int i = ColorDisplayManager.isNightDisplayAvailable(context) ? 0 : 2;
        String intentString = getIntentString(context, "night_display", NightDisplaySettings.class, getScreenTitle(context));
        int iconResource = getIconResource();
        MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", Integer.valueOf(isNightDisplayActivated ? 1 : 0)).add("availability", Integer.valueOf(i)).add("intent", intentString).add("icon", Integer.valueOf(iconResource));
        return matrixCursor;
    }

    public Cursor getUpdateCursor(Context context, SliceData sliceData, int i) {
        ColorDisplayManager colorDisplayManager = (ColorDisplayManager) context.getSystemService(ColorDisplayManager.class);
        boolean isNightDisplayActivated = colorDisplayManager.isNightDisplayActivated();
        boolean z = false;
        int i2 = ColorDisplayManager.isNightDisplayAvailable(context) ? 0 : 2;
        String intentString = getIntentString(context, "night_display", NightDisplaySettings.class, getScreenTitle(context));
        int iconResource = getIconResource();
        if (i == 1) {
            z = true;
        }
        if (!shouldChangeValue(i2, isNightDisplayActivated ? 1 : 0, i) || !colorDisplayManager.setNightDisplayActivated(z)) {
            i = isNightDisplayActivated;
        }
        MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", Integer.valueOf(i)).add("existing_value", Integer.valueOf(isNightDisplayActivated)).add("availability", Integer.valueOf(i2)).add("intent", intentString).add("icon", Integer.valueOf(iconResource));
        return matrixCursor;
    }

    private String getScreenTitle(Context context) {
        return context.getString(R.string.night_display_title);
    }
}
