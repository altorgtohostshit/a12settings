package com.google.android.settings.external.specialcase;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.wifi.WifiManager;
import com.android.settings.R;
import com.android.settings.slices.SliceData;
import com.android.settings.wifi.WifiSettings;
import com.google.android.settings.external.ExternalSettingsContract;
import com.google.android.settings.external.Queryable;

public class WifiSetting implements Queryable {
    private int getIconResource() {
        return R.drawable.ic_settings_wireless;
    }

    public Cursor getAccessCursor(Context context, SliceData sliceData) {
        boolean isWifiEnabled = ((WifiManager) context.getSystemService("wifi")).isWifiEnabled();
        String intentString = getIntentString(context, "master_wifi_toggle", WifiSettings.class, getScreenTitle(context));
        int iconResource = getIconResource();
        MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", Integer.valueOf(isWifiEnabled ? 1 : 0)).add("availability", 0).add("intent", intentString).add("icon", Integer.valueOf(iconResource));
        return matrixCursor;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x002c, code lost:
        if (r6.setWifiEnabled(r4) != false) goto L_0x0030;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.database.Cursor getUpdateCursor(android.content.Context r5, com.android.settings.slices.SliceData r6, int r7) {
        /*
            r4 = this;
            java.lang.String r6 = "wifi"
            java.lang.Object r6 = r5.getSystemService(r6)
            android.net.wifi.WifiManager r6 = (android.net.wifi.WifiManager) r6
            boolean r0 = r6.isWifiEnabled()
            java.lang.Class<com.android.settings.wifi.WifiSettings> r1 = com.android.settings.wifi.WifiSettings.class
            java.lang.String r2 = r4.getScreenTitle(r5)
            java.lang.String r3 = "master_wifi_toggle"
            java.lang.String r5 = r4.getIntentString(r5, r3, r1, r2)
            int r1 = r4.getIconResource()
            r2 = 0
            boolean r4 = r4.shouldChangeValue(r2, r0, r7)
            if (r4 == 0) goto L_0x002f
            r4 = 1
            if (r7 != r4) goto L_0x0027
            goto L_0x0028
        L_0x0027:
            r4 = r2
        L_0x0028:
            boolean r4 = r6.setWifiEnabled(r4)
            if (r4 == 0) goto L_0x002f
            goto L_0x0030
        L_0x002f:
            r7 = r0
        L_0x0030:
            android.database.MatrixCursor r4 = new android.database.MatrixCursor
            java.lang.String[] r6 = com.google.android.settings.external.ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS
            r4.<init>(r6)
            android.database.MatrixCursor$RowBuilder r6 = r4.newRow()
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)
            java.lang.String r3 = "newValue"
            android.database.MatrixCursor$RowBuilder r6 = r6.add(r3, r7)
            java.lang.Integer r7 = java.lang.Integer.valueOf(r0)
            java.lang.String r0 = "existing_value"
            android.database.MatrixCursor$RowBuilder r6 = r6.add(r0, r7)
            java.lang.Integer r7 = java.lang.Integer.valueOf(r2)
            java.lang.String r0 = "availability"
            android.database.MatrixCursor$RowBuilder r6 = r6.add(r0, r7)
            java.lang.String r7 = "intent"
            android.database.MatrixCursor$RowBuilder r5 = r6.add(r7, r5)
            java.lang.Integer r6 = java.lang.Integer.valueOf(r1)
            java.lang.String r7 = "icon"
            r5.add(r7, r6)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.settings.external.specialcase.WifiSetting.getUpdateCursor(android.content.Context, com.android.settings.slices.SliceData, int):android.database.Cursor");
    }

    private String getScreenTitle(Context context) {
        return context.getString(R.string.wifi_settings);
    }
}
