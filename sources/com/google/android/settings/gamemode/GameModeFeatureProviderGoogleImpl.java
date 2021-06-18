package com.google.android.settings.gamemode;

import android.app.GameManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.android.settings.R;
import com.android.settings.applications.appinfo.ExtraAppInfoFeatureProvider;
import com.android.settings.core.SubSettingLauncher;

public class GameModeFeatureProviderGoogleImpl implements ExtraAppInfoFeatureProvider {
    private String mPackageName;

    public boolean isSupported(Context context) {
        if (TextUtils.isEmpty(this.mPackageName)) {
            return false;
        }
        PackageManager packageManager = context.getPackageManager();
        if (!packageManager.hasSystemFeature("com.google.android.feature.GAME_OVERLAY") && !Build.IS_DEBUGGABLE) {
            return false;
        }
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.mPackageName, 0);
            if (applicationInfo == null || applicationInfo.category != 0) {
                return false;
            }
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("GameModeFeatureProviderGoogleImpl", "Fail to get ApplicationInfo for package:" + this.mPackageName, e);
            return false;
        }
    }

    public void launchExtraAppInfoSettings(Context context) {
        Bundle bundle = new Bundle();
        bundle.putString("package", this.mPackageName);
        new SubSettingLauncher(context).setDestination(GameModeSettings.class.getName()).setArguments(bundle).setSourceMetricsCategory(20).launch();
    }

    public void setPackageName(String str) {
        this.mPackageName = str;
    }

    public String getSummary(Context context) {
        int gameMode = ((GameManager) context.getSystemService(GameManager.class)).getGameMode(this.mPackageName);
        if (gameMode == 1) {
            return context.getResources().getText(R.string.game_mode_standard_title).toString();
        }
        if (gameMode == 2) {
            return context.getResources().getText(R.string.game_mode_performance_title).toString();
        }
        return gameMode == 3 ? context.getResources().getText(R.string.game_mode_battery_title).toString() : "";
    }
}
