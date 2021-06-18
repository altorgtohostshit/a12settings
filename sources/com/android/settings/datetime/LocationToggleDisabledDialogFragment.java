package com.android.settings.datetime;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class LocationToggleDisabledDialogFragment extends InstrumentedDialogFragment {
    private final Context mContext;

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onCreateDialog$1(DialogInterface dialogInterface, int i) {
    }

    public int getMetricsCategory() {
        return 1876;
    }

    public LocationToggleDisabledDialogFragment(Context context) {
        this.mContext = context;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.location_time_zone_detection_location_is_off_dialog_title).setIcon(R.drawable.ic_warning_24dp).setMessage(R.string.location_time_zone_detection_location_is_off_dialog_message).setPositiveButton(R.string.location_time_zone_detection_location_is_off_dialog_ok_button, new LocationToggleDisabledDialogFragment$$ExternalSyntheticLambda0(this)).setNegativeButton(R.string.location_time_zone_detection_location_is_off_dialog_cancel_button, LocationToggleDisabledDialogFragment$$ExternalSyntheticLambda1.INSTANCE).create();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        this.mContext.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
    }
}
