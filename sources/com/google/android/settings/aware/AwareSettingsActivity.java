package com.google.android.settings.aware;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.system.SystemDashboardFragment;

public class AwareSettingsActivity extends Activity {
    public static boolean isSuggestionComplete(Context context) {
        AwareHelper awareHelper = new AwareHelper(context);
        return !awareHelper.isAvailable() || awareHelper.isEnabled();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        launchSubSettings(getIntent().getIntExtra("show_aware_dialog", -1));
        finish();
    }

    private void launchSubSettings(int i) {
        String str;
        String str2;
        if (i == -1) {
            str = AwareSettings.class.getName();
            str2 = "";
        } else if (i == 0) {
            str = SystemDashboardFragment.class.getName();
            str2 = "show_aware_dialog_disabled";
        } else if (i == 1) {
            str = AwareSettings.class.getName();
            str2 = "show_aware_dialog_enabled";
        } else {
            return;
        }
        SubSettingLauncher subSettingLauncher = new SubSettingLauncher(this);
        if (!TextUtils.isEmpty(str2)) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(str2, true);
            subSettingLauncher.setArguments(bundle);
        }
        subSettingLauncher.setDestination(str).setSourceMetricsCategory(0).launch();
    }
}
