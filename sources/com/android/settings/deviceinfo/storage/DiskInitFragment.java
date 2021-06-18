package com.android.settings.deviceinfo.storage;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.storage.DiskInfo;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settings.deviceinfo.StorageWizardInit;

public class DiskInitFragment extends InstrumentedDialogFragment {
    public int getMetricsCategory() {
        return 561;
    }

    public static void show(Fragment fragment, int i, String str) {
        Bundle bundle = new Bundle();
        bundle.putInt("android.intent.extra.TEXT", i);
        bundle.putString("android.os.storage.extra.DISK_ID", str);
        DiskInitFragment diskInitFragment = new DiskInitFragment();
        diskInitFragment.setArguments(bundle);
        diskInitFragment.setTargetFragment(fragment, 0);
        diskInitFragment.show(fragment.getFragmentManager(), "disk_init");
    }

    public Dialog onCreateDialog(Bundle bundle) {
        FragmentActivity activity = getActivity();
        int i = getArguments().getInt("android.intent.extra.TEXT");
        String string = getArguments().getString("android.os.storage.extra.DISK_ID");
        DiskInfo findDiskById = ((StorageManager) activity.getSystemService(StorageManager.class)).findDiskById(string);
        return new AlertDialog.Builder(activity).setMessage(TextUtils.expandTemplate(getText(i), new CharSequence[]{findDiskById.getDescription()})).setPositiveButton((int) R.string.storage_menu_set_up, (DialogInterface.OnClickListener) new DiskInitFragment$$ExternalSyntheticLambda0(this, activity, string)).setNegativeButton((int) R.string.cancel, (DialogInterface.OnClickListener) null).create();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(Context context, String str, DialogInterface dialogInterface, int i) {
        Intent intent = new Intent(context, StorageWizardInit.class);
        intent.putExtra("android.os.storage.extra.DISK_ID", str);
        startActivity(intent);
    }
}
