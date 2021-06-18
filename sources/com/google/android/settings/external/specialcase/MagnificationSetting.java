package com.google.android.settings.external.specialcase;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;
import com.android.settings.R;
import com.android.settings.accessibility.AccessibilitySettings;
import com.android.settings.slices.SliceData;
import com.google.android.settings.external.ExternalSettingsContract;
import com.google.android.settings.external.Queryable;

public class MagnificationSetting implements Queryable {
    private final String mKey;
    private final String mSecureKey;

    private int getIconResource() {
        return 0;
    }

    public MagnificationSetting(String str, String str2) {
        this.mKey = str;
        this.mSecureKey = str2;
    }

    public Cursor getAccessCursor(Context context, SliceData sliceData) {
        int i = Settings.Secure.getInt(context.getContentResolver(), this.mSecureKey, 0);
        int availability = getAvailability();
        String intentString = getIntentString(context, "magnification_preference_screen", AccessibilitySettings.class, getScreenTitle(context));
        int iconResource = getIconResource();
        MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", Integer.valueOf(i)).add("availability", Integer.valueOf(availability)).add("intent", intentString).add("icon", Integer.valueOf(iconResource));
        return matrixCursor;
    }

    public Cursor getUpdateCursor(Context context, SliceData sliceData, int i) {
        validateInput(i);
        ContentResolver contentResolver = context.getContentResolver();
        int i2 = Settings.Secure.getInt(contentResolver, this.mSecureKey, 0);
        int availability = getAvailability();
        String intentString = getIntentString(context, "magnification_preference_screen", AccessibilitySettings.class, getScreenTitle(context));
        int iconResource = getIconResource();
        if (!shouldChangeValue(availability, i2, i) || !Settings.Secure.putInt(contentResolver, this.mSecureKey, i)) {
            i = i2;
        }
        MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", Integer.valueOf(i)).add("existing_value", Integer.valueOf(i2)).add("availability", Integer.valueOf(availability)).add("intent", intentString).add("icon", Integer.valueOf(iconResource));
        return matrixCursor;
    }

    private void validateInput(int i) {
        if (i != 1 && i != 0) {
            throw new IllegalArgumentException("Unexpected value for magnification settings. Expected 0 or 1, but found: " + i);
        }
    }

    private int getAvailability() {
        return (this.mKey == "magnify_gesture" || AccessibilityManager.isAccessibilityButtonSupported()) ? 0 : 2;
    }

    private String getScreenTitle(Context context) {
        return context.getString(R.string.accessibility_settings_title);
    }
}
