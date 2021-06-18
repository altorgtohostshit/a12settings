package com.android.settings.applications.specialaccess.notificationaccess;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class FriendlyWarningDialogFragment extends InstrumentedDialogFragment {
    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
    }

    public int getMetricsCategory() {
        return 552;
    }

    public FriendlyWarningDialogFragment setServiceInfo(ComponentName componentName, CharSequence charSequence, Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("c", componentName.flattenToString());
        bundle.putCharSequence("l", charSequence);
        setArguments(bundle);
        setTargetFragment(fragment, 0);
        return this;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        CharSequence charSequence = arguments.getCharSequence("l");
        final ComponentName unflattenFromString = ComponentName.unflattenFromString(arguments.getString("c"));
        final NotificationAccessDetails notificationAccessDetails = (NotificationAccessDetails) getTargetFragment();
        return new AlertDialog.Builder(getContext()).setMessage((CharSequence) getResources().getString(R.string.notification_listener_disable_warning_summary, new Object[]{charSequence})).setCancelable(true).setPositiveButton((int) R.string.notification_listener_disable_warning_confirm, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                notificationAccessDetails.disable(unflattenFromString);
            }
        }).setNegativeButton((int) R.string.notification_listener_disable_warning_cancel, (DialogInterface.OnClickListener) FriendlyWarningDialogFragment$$ExternalSyntheticLambda0.INSTANCE).create();
    }
}
