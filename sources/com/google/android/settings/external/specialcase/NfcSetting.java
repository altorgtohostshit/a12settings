package com.google.android.settings.external.specialcase;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.nfc.NfcAdapter;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.connecteddevice.ConnectedDeviceDashboardFragment;
import com.android.settings.nfc.NfcPreferenceController;
import com.android.settings.slices.SliceData;
import com.google.android.settings.external.ExternalSettingsContract;
import com.google.android.settings.external.Queryable;

public class NfcSetting implements Queryable {
    private int getIconResource() {
        return R.drawable.ic_settings_wireless;
    }

    public Cursor getAccessCursor(Context context, SliceData sliceData) {
        NfcAdapter defaultAdapter = NfcAdapter.getDefaultAdapter(context);
        int nfcStatus = getNfcStatus(defaultAdapter);
        int availability = getAvailability(context, defaultAdapter);
        String intentString = getIntentString(context, "nfc", ConnectedDeviceDashboardFragment.class, getScreenTitle(context));
        int iconResource = getIconResource();
        MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", Integer.valueOf(nfcStatus)).add("availability", Integer.valueOf(availability)).add("intent", intentString).add("icon", Integer.valueOf(iconResource)).add("dependent_setting", "toggle_airplane");
        return matrixCursor;
    }

    public Cursor getUpdateCursor(Context context, SliceData sliceData, int i) {
        NfcAdapter defaultAdapter = NfcAdapter.getDefaultAdapter(context);
        int nfcStatus = getNfcStatus(defaultAdapter);
        int availability = getAvailability(context, defaultAdapter);
        String intentString = getIntentString(context, "nfc", ConnectedDeviceDashboardFragment.class, getScreenTitle(context));
        int iconResource = getIconResource();
        if (!shouldChangeValue(availability, nfcStatus, i) || defaultAdapter == null || ((i != 1 || !defaultAdapter.enable()) && (i != 0 || !defaultAdapter.disable()))) {
            i = nfcStatus;
        }
        MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", Integer.valueOf(i)).add("existing_value", Integer.valueOf(nfcStatus)).add("availability", Integer.valueOf(availability)).add("intent", intentString).add("icon", Integer.valueOf(iconResource)).add("dependent_setting", "toggle_airplane");
        return matrixCursor;
    }

    private static int getNfcStatus(NfcAdapter nfcAdapter) {
        if (nfcAdapter != null) {
            return nfcAdapter.isEnabled() ? 1 : 0;
        }
        return 0;
    }

    private static int getAvailability(Context context, NfcAdapter nfcAdapter) {
        if (nfcAdapter == null) {
            return 2;
        }
        if (Settings.Global.getInt(context.getContentResolver(), "airplane_mode_on", 0) != 1 || NfcPreferenceController.isToggleableInAirplaneMode(context)) {
            return 0;
        }
        return 1;
    }

    private String getScreenTitle(Context context) {
        return context.getString(R.string.connected_devices_dashboard_title);
    }
}
