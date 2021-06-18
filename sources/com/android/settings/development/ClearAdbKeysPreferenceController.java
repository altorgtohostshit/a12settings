package com.android.settings.development;

import android.content.Context;
import android.debug.IAdbManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserManager;
import android.sysprop.AdbProperties;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.Utils;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class ClearAdbKeysPreferenceController extends DeveloperOptionsPreferenceController implements PreferenceControllerMixin {
    private final IAdbManager mAdbManager = IAdbManager.Stub.asInterface(ServiceManager.getService("adb"));
    private final DevelopmentSettingsDashboardFragment mFragment;

    public String getPreferenceKey() {
        return "clear_adb_keys";
    }

    public ClearAdbKeysPreferenceController(Context context, DevelopmentSettingsDashboardFragment developmentSettingsDashboardFragment) {
        super(context);
        this.mFragment = developmentSettingsDashboardFragment;
    }

    public boolean isAvailable() {
        return ((Boolean) AdbProperties.secure().orElse(Boolean.FALSE)).booleanValue();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        if (this.mPreference != null && !isAdminUser()) {
            this.mPreference.setEnabled(false);
        }
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (Utils.isMonkeyRunning() || !TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return false;
        }
        ClearAdbKeysWarningDialog.show(this.mFragment);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchEnabled() {
        if (isAdminUser()) {
            this.mPreference.setEnabled(true);
        }
    }

    public void onClearAdbKeysConfirmed() {
        try {
            this.mAdbManager.clearDebuggingKeys();
        } catch (RemoteException e) {
            Log.e("ClearAdbPrefCtrl", "Unable to clear adb keys", e);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isAdminUser() {
        return ((UserManager) this.mContext.getSystemService("user")).isAdminUser();
    }
}
