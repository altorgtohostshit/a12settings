package com.android.settings.applications.appinfo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.UserHandle;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settings.overlay.FeatureFactory;

public class InstantAppButtonDialogFragment extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
    private String mPackageName;

    public int getMetricsCategory() {
        return 558;
    }

    public static InstantAppButtonDialogFragment newInstance(String str) {
        InstantAppButtonDialogFragment instantAppButtonDialogFragment = new InstantAppButtonDialogFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("packageName", str);
        instantAppButtonDialogFragment.setArguments(bundle);
        return instantAppButtonDialogFragment;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        this.mPackageName = getArguments().getString("packageName");
        return createDialog();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        Context context = getContext();
        PackageManager packageManager = context.getPackageManager();
        FeatureFactory.getFactory(context).getMetricsFeatureProvider().action(context, 923, this.mPackageName);
        packageManager.deletePackageAsUser(this.mPackageName, (IPackageDeleteObserver) null, 0, UserHandle.myUserId());
    }

    private AlertDialog createDialog() {
        return new AlertDialog.Builder(getContext()).setPositiveButton((int) R.string.clear_instant_app_data, (DialogInterface.OnClickListener) this).setNegativeButton((int) R.string.cancel, (DialogInterface.OnClickListener) null).setTitle((int) R.string.clear_instant_app_data).setMessage((int) R.string.clear_instant_app_confirmation).create();
    }
}
