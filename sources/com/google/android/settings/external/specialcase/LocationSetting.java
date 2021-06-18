package com.google.android.settings.external.specialcase;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.UserManager;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.location.LocationSettings;
import com.android.settings.slices.SliceData;
import com.google.android.settings.external.ExternalSettingsContract;
import com.google.android.settings.external.Queryable;

public class LocationSetting implements Queryable {
    private int getIconResource() {
        return 17302850;
    }

    public Cursor getAccessCursor(Context context, SliceData sliceData) {
        int currentValue = getCurrentValue(context);
        int availability = getAvailability(context);
        String intentString = getIntentString(context, "location", LocationSettings.class, getScreenTitle(context));
        int iconResource = getIconResource();
        MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", Integer.valueOf(currentValue)).add("availability", Integer.valueOf(availability)).add("intent", intentString).add("icon", Integer.valueOf(iconResource));
        return matrixCursor;
    }

    public Cursor getUpdateCursor(Context context, SliceData sliceData, int i) {
        validateInput(i);
        int currentValue = getCurrentValue(context);
        int availability = getAvailability(context);
        String intentString = getIntentString(context, "location", LocationSettings.class, getScreenTitle(context));
        int iconResource = getIconResource();
        if (!shouldChangeValue(availability, currentValue, i) || !updateLocationMode(context, currentValue, i)) {
            i = currentValue;
        }
        MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", Integer.valueOf(i)).add("existing_value", Integer.valueOf(currentValue)).add("availability", Integer.valueOf(availability)).add("intent", intentString).add("icon", Integer.valueOf(iconResource));
        return matrixCursor;
    }

    private int getAvailability(Context context) {
        return isRestricted(context) ? 6 : 0;
    }

    private int getCurrentValue(Context context) {
        if (Settings.Secure.getIntForUser(context.getContentResolver(), "location_mode", 0, ActivityManager.getCurrentUser()) != 0) {
            return 1;
        }
        return 0;
    }

    private String getScreenTitle(Context context) {
        return context.getString(R.string.location_settings_title);
    }

    private void validateInput(int i) {
        if (i != 1 && i != 0) {
            throw new IllegalArgumentException("Unexpected value for location toggle. Expected 0 or 1, but found: " + i);
        }
    }

    private boolean isRestricted(Context context) {
        return ((UserManager) context.getSystemService("user")).hasUserRestriction("no_share_location");
    }

    private boolean updateLocationMode(Context context, int i, int i2) {
        Intent intent = new Intent("com.android.settings.location.MODE_CHANGING");
        intent.putExtra("CURRENT_MODE", i);
        intent.putExtra("NEW_MODE", i2);
        context.sendBroadcast(intent, "android.permission.WRITE_SECURE_SETTINGS");
        return Settings.Secure.putInt(context.getContentResolver(), "location_mode", i2);
    }
}
