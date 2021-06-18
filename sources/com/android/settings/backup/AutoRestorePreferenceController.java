package com.android.settings.backup;

import android.app.backup.IBackupManager;
import android.content.Context;
import android.content.IntentFilter;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class AutoRestorePreferenceController extends TogglePreferenceController {
    private static final String TAG = "AutoRestorePrefCtrler";
    private PrivacySettingsConfigData mPSCD = PrivacySettingsConfigData.getInstance();
    private Preference mPreference;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AutoRestorePreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        if (!PrivacySettingsUtils.isAdminUser(this.mContext)) {
            return 4;
        }
        return PrivacySettingsUtils.isInvisibleKey(this.mContext, "auto_restore") ? 3 : 0;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        this.mPreference = preference;
        preference.setEnabled(this.mPSCD.isBackupEnabled());
    }

    public boolean isChecked() {
        if (Settings.Secure.getInt(this.mContext.getContentResolver(), "backup_auto_restore", 1) == 1) {
            return true;
        }
        return false;
    }

    public boolean setChecked(boolean z) {
        try {
            IBackupManager.Stub.asInterface(ServiceManager.getService("backup")).setAutoRestore(z);
            return true;
        } catch (RemoteException e) {
            ((SwitchPreference) this.mPreference).setChecked(!z);
            Log.e(TAG, "Error can't set setAutoRestore", e);
            return false;
        }
    }
}
