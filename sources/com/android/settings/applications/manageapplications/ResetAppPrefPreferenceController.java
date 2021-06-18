package com.android.settings.applications.manageapplications;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.preference.Preference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnCreate;
import com.android.settingslib.core.lifecycle.events.OnSaveInstanceState;

public class ResetAppPrefPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, LifecycleObserver, OnCreate, OnSaveInstanceState {
    private ResetAppsHelper mResetAppsHelper;

    public String getPreferenceKey() {
        return "reset_app_prefs";
    }

    public boolean isAvailable() {
        return true;
    }

    public ResetAppPrefPreferenceController(Context context, Lifecycle lifecycle) {
        super(context);
        this.mResetAppsHelper = new ResetAppsHelper(context);
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return false;
        }
        this.mResetAppsHelper.buildResetDialog();
        return true;
    }

    public void onCreate(Bundle bundle) {
        this.mResetAppsHelper.onRestoreInstanceState(bundle);
    }

    public void onSaveInstanceState(Bundle bundle) {
        this.mResetAppsHelper.onSaveInstanceState(bundle);
    }
}
