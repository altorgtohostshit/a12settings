package com.google.android.settings.external.specialcase;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.TetherSettings;
import com.android.settings.network.TetherPreferenceController;
import com.android.settings.slices.SliceData;
import com.google.android.settings.external.ExternalSettingsContract;
import com.google.android.settings.external.Queryable;

public class HotspotSetting implements Queryable {
    private int getIconResource() {
        return R.drawable.ic_hotspot;
    }

    public Cursor getAccessCursor(Context context, SliceData sliceData) {
        return createQueryCursor(getCurrentValue((WifiManager) context.getSystemService("wifi")), getAvailability(context, (ConnectivityManager) context.getSystemService("connectivity")), getIntentString(context, "enable_wifi_ap", TetherSettings.class, getScreenTitle(context)), getIconResource());
    }

    public Cursor getUpdateCursor(Context context, SliceData sliceData, int i) {
        validateInput(i);
        int currentValue = getCurrentValue((WifiManager) context.getSystemService("wifi"));
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        int availability = getAvailability(context, connectivityManager);
        String intentString = getIntentString(context, "enable_wifi_ap", TetherSettings.class, getScreenTitle(context));
        int iconResource = getIconResource();
        if (shouldChangeValue(availability, currentValue, i)) {
            updateHotspot(connectivityManager, i);
        } else {
            i = currentValue;
        }
        return createUpdateCursor(currentValue, availability, intentString, iconResource, i);
    }

    public static Cursor createQueryCursor(int i, int i2, String str, int i3) {
        MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", Integer.valueOf(i)).add("availability", Integer.valueOf(i2)).add("intent", str).add("icon", Integer.valueOf(i3)).add("dependent_setting", "toggle_airplane");
        return matrixCursor;
    }

    public static Cursor createUpdateCursor(int i, int i2, String str, int i3, int i4) {
        MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", Integer.valueOf(i4)).add("existing_value", Integer.valueOf(i)).add("availability", Integer.valueOf(i2)).add("intent", str).add("icon", Integer.valueOf(i3)).add("dependent_setting", "toggle_airplane");
        return matrixCursor;
    }

    private void validateInput(int i) {
        if (i != 1 && i != 0) {
            throw new IllegalArgumentException("Unexpected value for hotspot mode. Expected 0 or 1, but found: " + i);
        }
    }

    private int getAvailability(Context context, ConnectivityManager connectivityManager) {
        if (TetherPreferenceController.isTetherConfigDisallowed(context)) {
            return 6;
        }
        if (Settings.Global.getInt(context.getContentResolver(), "airplane_mode_on", 0) == 1) {
            return 1;
        }
        if (connectivityManager.isTetheringSupported()) {
            return 0;
        }
        return 2;
    }

    private void updateHotspot(ConnectivityManager connectivityManager, int i) {
        if (i == 1) {
            connectivityManager.startTethering(0, true, new ConnectivityManager.OnStartTetheringCallback() {
            });
        } else {
            connectivityManager.stopTethering(0);
        }
    }

    private int getCurrentValue(WifiManager wifiManager) {
        return wifiManager.getWifiApState() == 13 ? 1 : 0;
    }

    private String getScreenTitle(Context context) {
        return context.getString(R.string.network_dashboard_title);
    }
}
