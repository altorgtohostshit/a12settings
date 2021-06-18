package com.android.settings.deviceinfo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.UserManager;
import android.os.storage.DiskInfo;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.fragment.app.Fragment;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.deviceinfo.storage.StorageEntry;
import com.android.settings.deviceinfo.storage.StorageRenameFragment;
import com.android.settings.deviceinfo.storage.StorageUtils;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnCreateOptionsMenu;
import com.android.settingslib.core.lifecycle.events.OnOptionsItemSelected;
import com.android.settingslib.core.lifecycle.events.OnPrepareOptionsMenu;
import java.util.Objects;

public class VolumeOptionMenuController implements LifecycleObserver, OnCreateOptionsMenu, OnPrepareOptionsMenu, OnOptionsItemSelected {
    private final Context mContext;
    MenuItem mForget;
    MenuItem mFormat;
    MenuItem mFormatAsInternal;
    MenuItem mFormatAsPortable;
    private final Fragment mFragment;
    MenuItem mFree;
    MenuItem mMigrate;
    MenuItem mMount;
    private final PackageManager mPackageManager;
    MenuItem mRename;
    private StorageEntry mStorageEntry;
    private final StorageManager mStorageManager;
    MenuItem mUnmount;

    public VolumeOptionMenuController(Context context, Fragment fragment, StorageEntry storageEntry) {
        this.mContext = context;
        this.mFragment = fragment;
        this.mPackageManager = context.getPackageManager();
        this.mStorageManager = (StorageManager) context.getSystemService(StorageManager.class);
        this.mStorageEntry = storageEntry;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.storage_volume, menu);
    }

    public void onPrepareOptionsMenu(Menu menu) {
        this.mRename = menu.findItem(R.id.storage_rename);
        this.mMount = menu.findItem(R.id.storage_mount);
        this.mUnmount = menu.findItem(R.id.storage_unmount);
        this.mFormat = menu.findItem(R.id.storage_format);
        this.mFormatAsPortable = menu.findItem(R.id.storage_format_as_portable);
        this.mFormatAsInternal = menu.findItem(R.id.storage_format_as_internal);
        this.mMigrate = menu.findItem(R.id.storage_migrate);
        this.mFree = menu.findItem(R.id.storage_free);
        this.mForget = menu.findItem(R.id.storage_forget);
        boolean z = false;
        this.mRename.setVisible(false);
        this.mMount.setVisible(false);
        this.mUnmount.setVisible(false);
        this.mFormat.setVisible(false);
        this.mFormatAsPortable.setVisible(false);
        this.mFormatAsInternal.setVisible(false);
        this.mMigrate.setVisible(false);
        this.mFree.setVisible(false);
        this.mForget.setVisible(false);
        if (this.mStorageEntry.isDiskInfoUnsupported()) {
            this.mFormat.setVisible(true);
        } else if (this.mStorageEntry.isVolumeRecordMissed()) {
            this.mForget.setVisible(true);
        } else if (this.mStorageEntry.isUnmounted()) {
            this.mMount.setVisible(true);
        } else if (this.mStorageEntry.isMounted()) {
            if (this.mStorageEntry.isPrivate()) {
                if (!this.mStorageEntry.isDefaultInternalStorage()) {
                    this.mRename.setVisible(true);
                    this.mUnmount.setVisible(true);
                    this.mFormatAsPortable.setVisible(true);
                }
                VolumeInfo primaryStorageCurrentVolume = this.mPackageManager.getPrimaryStorageCurrentVolume();
                VolumeInfo volumeInfo = this.mStorageEntry.getVolumeInfo();
                MenuItem menuItem = this.mMigrate;
                if (primaryStorageCurrentVolume != null && primaryStorageCurrentVolume.getType() == 1 && !Objects.equals(volumeInfo, primaryStorageCurrentVolume) && primaryStorageCurrentVolume.isMountedWritable()) {
                    z = true;
                }
                menuItem.setVisible(z);
            } else if (this.mStorageEntry.isPublic()) {
                this.mRename.setVisible(true);
                this.mUnmount.setVisible(true);
                this.mFormat.setVisible(true);
                DiskInfo findDiskById = this.mStorageManager.findDiskById(this.mStorageEntry.getDiskId());
                MenuItem menuItem2 = this.mFormatAsInternal;
                if (findDiskById != null && findDiskById.isAdoptable() && UserManager.get(this.mContext).isAdminUser() && !ActivityManager.isUserAMonkey()) {
                    z = true;
                }
                menuItem2.setVisible(z);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (!this.mFragment.isAdded()) {
            return false;
        }
        int itemId = menuItem.getItemId();
        if (itemId == R.id.storage_mount) {
            if (!this.mStorageEntry.isUnmounted()) {
                return false;
            }
            new StorageUtils.MountTask(this.mFragment.getActivity(), this.mStorageEntry.getVolumeInfo()).execute(new Void[0]);
            return true;
        } else if (itemId == R.id.storage_unmount) {
            if (this.mStorageEntry.isMounted()) {
                if (this.mStorageEntry.isPublic()) {
                    new StorageUtils.UnmountTask(this.mFragment.getActivity(), this.mStorageEntry.getVolumeInfo()).execute(new Void[0]);
                    return true;
                } else if (this.mStorageEntry.isPrivate() && !this.mStorageEntry.isDefaultInternalStorage()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("android.os.storage.extra.VOLUME_ID", this.mStorageEntry.getId());
                    new SubSettingLauncher(this.mContext).setDestination(PrivateVolumeUnmount.class.getCanonicalName()).setTitleRes(R.string.storage_menu_unmount).setSourceMetricsCategory(42).setArguments(bundle).launch();
                    return true;
                }
            }
            return false;
        } else if (itemId == R.id.storage_rename) {
            if ((!this.mStorageEntry.isPrivate() || this.mStorageEntry.isDefaultInternalStorage()) && !this.mStorageEntry.isPublic()) {
                return false;
            }
            StorageRenameFragment.show(this.mFragment, this.mStorageEntry.getVolumeInfo());
            return true;
        } else if (itemId == R.id.storage_format) {
            if (!this.mStorageEntry.isDiskInfoUnsupported() && !this.mStorageEntry.isPublic()) {
                return false;
            }
            StorageWizardFormatConfirm.showPublic(this.mFragment.getActivity(), this.mStorageEntry.getDiskId());
            return true;
        } else if (itemId == R.id.storage_format_as_portable) {
            if (!this.mStorageEntry.isPrivate()) {
                return false;
            }
            Bundle bundle2 = new Bundle();
            bundle2.putString("android.os.storage.extra.VOLUME_ID", this.mStorageEntry.getId());
            new SubSettingLauncher(this.mContext).setDestination(PrivateVolumeFormat.class.getCanonicalName()).setTitleRes(R.string.storage_menu_format).setSourceMetricsCategory(42).setArguments(bundle2).launch();
            return true;
        } else if (itemId == R.id.storage_format_as_internal) {
            if (!this.mStorageEntry.isPublic()) {
                return false;
            }
            StorageWizardFormatConfirm.showPrivate(this.mFragment.getActivity(), this.mStorageEntry.getDiskId());
            return true;
        } else if (itemId == R.id.storage_migrate) {
            if (!this.mStorageEntry.isPrivate()) {
                return false;
            }
            Intent intent = new Intent(this.mContext, StorageWizardMigrateConfirm.class);
            intent.putExtra("android.os.storage.extra.VOLUME_ID", this.mStorageEntry.getId());
            this.mContext.startActivity(intent);
            return true;
        } else if (itemId != R.id.storage_forget || !this.mStorageEntry.isVolumeRecordMissed()) {
            return false;
        } else {
            StorageUtils.launchForgetMissingVolumeRecordFragment(this.mContext, this.mStorageEntry);
            return true;
        }
    }

    public void setSelectedStorageEntry(StorageEntry storageEntry) {
        this.mStorageEntry = storageEntry;
    }
}
