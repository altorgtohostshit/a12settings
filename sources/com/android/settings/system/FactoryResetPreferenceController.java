package com.android.settings.system;

import android.content.Context;
import android.content.Intent;
import android.os.UserManager;
import androidx.preference.Preference;
import com.android.settings.Settings;
import com.android.settings.Utils;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;

public class FactoryResetPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    private final UserManager mUm;

    public String getPreferenceKey() {
        return "factory_reset";
    }

    public FactoryResetPreferenceController(Context context) {
        super(context);
        this.mUm = (UserManager) context.getSystemService("user");
    }

    public boolean isAvailable() {
        return this.mUm.isAdminUser() || Utils.isDemoUser(this.mContext);
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!"factory_reset".equals(preference.getKey())) {
            return false;
        }
        this.mContext.startActivity(new Intent(this.mContext, Settings.FactoryResetActivity.class));
        return true;
    }
}
