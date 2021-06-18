package com.android.settings.deviceinfo.storage;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settings.deviceinfo.PrivateVolumeForget;

public class StorageUtils {
    public static void launchForgetMissingVolumeRecordFragment(Context context, StorageEntry storageEntry) {
        if (storageEntry != null && storageEntry.isVolumeRecordMissed()) {
            Bundle bundle = new Bundle();
            bundle.putString("android.os.storage.extra.FS_UUID", storageEntry.getFsUuid());
            new SubSettingLauncher(context).setDestination(PrivateVolumeForget.class.getCanonicalName()).setTitleRes(R.string.storage_menu_forget).setSourceMetricsCategory(745).setArguments(bundle).launch();
        }
    }

    public static class UnmountTask extends AsyncTask<Void, Void, Exception> {
        private final Context mContext;
        private final String mDescription;
        private final StorageManager mStorageManager;
        private final String mVolumeId;

        public UnmountTask(Context context, VolumeInfo volumeInfo) {
            Context applicationContext = context.getApplicationContext();
            this.mContext = applicationContext;
            StorageManager storageManager = (StorageManager) applicationContext.getSystemService(StorageManager.class);
            this.mStorageManager = storageManager;
            this.mVolumeId = volumeInfo.getId();
            this.mDescription = storageManager.getBestVolumeDescription(volumeInfo);
        }

        /* access modifiers changed from: protected */
        public Exception doInBackground(Void... voidArr) {
            try {
                this.mStorageManager.unmount(this.mVolumeId);
                return null;
            } catch (Exception e) {
                return e;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Exception exc) {
            if (exc == null) {
                Context context = this.mContext;
                Toast.makeText(context, context.getString(R.string.storage_unmount_success, new Object[]{this.mDescription}), 0).show();
                return;
            }
            Log.e("StorageUtils", "Failed to unmount " + this.mVolumeId, exc);
            Context context2 = this.mContext;
            Toast.makeText(context2, context2.getString(R.string.storage_unmount_failure, new Object[]{this.mDescription}), 0).show();
        }
    }

    public static class MountTask extends AsyncTask<Void, Void, Exception> {
        private final Context mContext;
        private final String mDescription;
        private final StorageManager mStorageManager;
        private final String mVolumeId;

        public MountTask(Context context, VolumeInfo volumeInfo) {
            Context applicationContext = context.getApplicationContext();
            this.mContext = applicationContext;
            StorageManager storageManager = (StorageManager) applicationContext.getSystemService(StorageManager.class);
            this.mStorageManager = storageManager;
            this.mVolumeId = volumeInfo.getId();
            this.mDescription = storageManager.getBestVolumeDescription(volumeInfo);
        }

        /* access modifiers changed from: protected */
        public Exception doInBackground(Void... voidArr) {
            try {
                this.mStorageManager.mount(this.mVolumeId);
                return null;
            } catch (Exception e) {
                return e;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Exception exc) {
            if (exc == null) {
                Context context = this.mContext;
                Toast.makeText(context, context.getString(R.string.storage_mount_success, new Object[]{this.mDescription}), 0).show();
                return;
            }
            Log.e("StorageUtils", "Failed to mount " + this.mVolumeId, exc);
            Context context2 = this.mContext;
            Toast.makeText(context2, context2.getString(R.string.storage_mount_failure, new Object[]{this.mDescription}), 0).show();
        }
    }

    public static class SystemInfoFragment extends InstrumentedDialogFragment {
        public int getMetricsCategory() {
            return 565;
        }

        public Dialog onCreateDialog(Bundle bundle) {
            return new AlertDialog.Builder(getActivity()).setMessage((CharSequence) getContext().getString(R.string.storage_detail_dialog_system, new Object[]{Build.VERSION.RELEASE_OR_CODENAME})).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).create();
        }
    }
}
