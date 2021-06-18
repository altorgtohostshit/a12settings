package com.android.settings.development;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class WaitForDebuggerPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin, OnActivityResultListener {
    static final int SETTING_VALUE_OFF = 0;
    static final int SETTING_VALUE_ON = 1;

    public String getPreferenceKey() {
        return "wait_for_debugger";
    }

    public WaitForDebuggerPreferenceController(Context context) {
        super(context);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        writeDebuggerAppOptions(Settings.Global.getString(this.mContext.getContentResolver(), "debug_app"), ((Boolean) obj).booleanValue(), true);
        return true;
    }

    public void updateState(Preference preference) {
        updateState(this.mPreference, Settings.Global.getString(this.mContext.getContentResolver(), "debug_app"));
    }

    public boolean onActivityResult(int i, int i2, Intent intent) {
        if (i != 1 || i2 != -1) {
            return false;
        }
        updateState(this.mPreference, intent.getAction());
        return true;
    }

    private void updateState(Preference preference, String str) {
        SwitchPreference switchPreference = (SwitchPreference) preference;
        boolean z = false;
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "wait_for_debugger", 0) != 0) {
            z = true;
        }
        writeDebuggerAppOptions(str, z, true);
        switchPreference.setChecked(z);
        switchPreference.setEnabled(!TextUtils.isEmpty(str));
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        writeDebuggerAppOptions((String) null, false, false);
        ((SwitchPreference) this.mPreference).setChecked(false);
    }

    /* access modifiers changed from: package-private */
    public IActivityManager getActivityManagerService() {
        return ActivityManager.getService();
    }

    private void writeDebuggerAppOptions(String str, boolean z, boolean z2) {
        try {
            getActivityManagerService().setDebugApp(str, z, z2);
        } catch (RemoteException unused) {
        }
    }
}
