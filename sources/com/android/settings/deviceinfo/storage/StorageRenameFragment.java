package com.android.settings.deviceinfo.storage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.os.storage.VolumeRecord;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class StorageRenameFragment extends InstrumentedDialogFragment {
    public int getMetricsCategory() {
        return 563;
    }

    public static void show(Fragment fragment, VolumeInfo volumeInfo) {
        StorageRenameFragment storageRenameFragment = new StorageRenameFragment();
        storageRenameFragment.setTargetFragment(fragment, 0);
        Bundle bundle = new Bundle();
        bundle.putString("android.os.storage.extra.FS_UUID", volumeInfo.getFsUuid());
        storageRenameFragment.setArguments(bundle);
        storageRenameFragment.show(fragment.getFragmentManager(), "rename");
    }

    public Dialog onCreateDialog(Bundle bundle) {
        FragmentActivity activity = getActivity();
        StorageManager storageManager = (StorageManager) activity.getSystemService(StorageManager.class);
        String string = getArguments().getString("android.os.storage.extra.FS_UUID");
        VolumeRecord findRecordByUuid = storageManager.findRecordByUuid(string);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View inflate = LayoutInflater.from(builder.getContext()).inflate(R.layout.dialog_edittext, (ViewGroup) null, false);
        EditText editText = (EditText) inflate.findViewById(R.id.edittext);
        editText.setText(findRecordByUuid.getNickname());
        editText.requestFocus();
        return builder.setTitle((int) R.string.storage_rename_title).setView(inflate).setPositiveButton((int) R.string.save, (DialogInterface.OnClickListener) new StorageRenameFragment$$ExternalSyntheticLambda0(storageManager, string, editText)).setNegativeButton((int) R.string.cancel, (DialogInterface.OnClickListener) null).create();
    }
}
