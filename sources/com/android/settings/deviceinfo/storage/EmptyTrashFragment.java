package com.android.settings.deviceinfo.storage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class EmptyTrashFragment extends InstrumentedDialogFragment {
    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
    }

    public int getMetricsCategory() {
        return 1875;
    }

    public static void show(Fragment fragment) {
        EmptyTrashFragment emptyTrashFragment = new EmptyTrashFragment();
        emptyTrashFragment.setTargetFragment(fragment, 0);
        emptyTrashFragment.show(fragment.getFragmentManager(), "empty_trash");
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(getActivity()).setTitle((int) R.string.storage_trash_dialog_title).setMessage((int) R.string.storage_trash_dialog_ask_message).setPositiveButton((int) R.string.storage_trash_dialog_confirm, (DialogInterface.OnClickListener) EmptyTrashFragment$$ExternalSyntheticLambda0.INSTANCE).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
    }
}
