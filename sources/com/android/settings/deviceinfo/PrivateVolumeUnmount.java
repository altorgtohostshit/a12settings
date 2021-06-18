package com.android.settings.deviceinfo;

import android.os.Bundle;
import android.os.storage.DiskInfo;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.android.settings.R;
import com.android.settings.core.InstrumentedFragment;
import com.android.settings.deviceinfo.storage.StorageUtils;
import com.android.settings.search.actionbar.SearchMenuController;

public class PrivateVolumeUnmount extends InstrumentedFragment {
    private final View.OnClickListener mConfirmListener = new View.OnClickListener() {
        public void onClick(View view) {
            new StorageUtils.UnmountTask(PrivateVolumeUnmount.this.getActivity(), PrivateVolumeUnmount.this.mVolume).execute(new Void[0]);
            PrivateVolumeUnmount.this.getActivity().finish();
        }
    };
    private DiskInfo mDisk;
    /* access modifiers changed from: private */
    public VolumeInfo mVolume;

    public int getMetricsCategory() {
        return 42;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
        SearchMenuController.init((InstrumentedFragment) this);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        StorageManager storageManager = (StorageManager) getActivity().getSystemService(StorageManager.class);
        VolumeInfo findVolumeById = storageManager.findVolumeById(getArguments().getString("android.os.storage.extra.VOLUME_ID"));
        this.mVolume = findVolumeById;
        this.mDisk = storageManager.findDiskById(findVolumeById.getDiskId());
        View inflate = layoutInflater.inflate(R.layout.storage_internal_unmount, viewGroup, false);
        ((TextView) inflate.findViewById(R.id.body)).setText(TextUtils.expandTemplate(getText(R.string.storage_internal_unmount_details), new CharSequence[]{this.mDisk.getDescription()}));
        ((Button) inflate.findViewById(R.id.confirm)).setOnClickListener(this.mConfirmListener);
        return inflate;
    }
}
