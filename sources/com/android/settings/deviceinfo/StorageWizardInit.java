package com.android.settings.deviceinfo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.os.storage.VolumeInfo;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import com.android.settings.R;
import com.android.settings.overlay.FeatureFactory;

public class StorageWizardInit extends StorageWizardBase {
    private Button mInternal;
    private boolean mIsPermittedToAdopt;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (this.mDisk == null) {
            finish();
            return;
        }
        setContentView(R.layout.storage_wizard_init);
        this.mIsPermittedToAdopt = UserManager.get(this).isAdminUser() && !ActivityManager.isUserAMonkey();
        setHeaderText(R.string.storage_wizard_init_v2_title, getDiskShortDescription());
        this.mInternal = (Button) requireViewById(R.id.storage_wizard_init_internal);
        setBackButtonText(R.string.storage_wizard_init_v2_later, new CharSequence[0]);
        setNextButtonVisibility(4);
        if (!this.mDisk.isAdoptable()) {
            this.mInternal.setEnabled(false);
            onNavigateExternal((View) null);
        } else if (!this.mIsPermittedToAdopt) {
            this.mInternal.setEnabled(false);
        }
    }

    public void onNavigateBack(View view) {
        finish();
    }

    public void onNavigateExternal(View view) {
        if (view != null) {
            FeatureFactory.getFactory(this).getMetricsFeatureProvider().action((Context) this, 1407, (Pair<Integer, Object>[]) new Pair[0]);
        }
        VolumeInfo volumeInfo = this.mVolume;
        if (volumeInfo == null || volumeInfo.getType() != 0 || this.mVolume.getState() == 6) {
            StorageWizardFormatConfirm.showPublic(this, this.mDisk.getId());
            return;
        }
        this.mStorage.setVolumeInited(this.mVolume.getFsUuid(), true);
        Intent intent = new Intent(this, StorageWizardReady.class);
        intent.putExtra("android.os.storage.extra.DISK_ID", this.mDisk.getId());
        startActivity(intent);
        finish();
    }

    public void onNavigateInternal(View view) {
        if (view != null) {
            FeatureFactory.getFactory(this).getMetricsFeatureProvider().action((Context) this, 1408, (Pair<Integer, Object>[]) new Pair[0]);
        }
        StorageWizardFormatConfirm.showPrivate(this, this.mDisk.getId());
    }
}
