package com.android.settings.fuelgauge;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settingslib.Utils;

public class AdvancedPowerUsageDetailActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Uri uri;
        super.onCreate(bundle);
        Intent intent = getIntent();
        String str = null;
        if (intent == null) {
            uri = null;
        } else {
            uri = intent.getData();
        }
        if (uri != null) {
            str = uri.getSchemeSpecificPart();
        }
        if (str != null) {
            Bundle bundle2 = new Bundle(4);
            PackageManager packageManager = getPackageManager();
            bundle2.putString("extra_package_name", str);
            bundle2.putString("extra_power_usage_percent", Utils.formatPercentage(0));
            if (intent.getBooleanExtra("request_ignore_background_restriction", false)) {
                bundle2.putString(":settings:fragment_args_key", "background_activity");
            }
            try {
                bundle2.putInt("extra_uid", packageManager.getPackageUid(str, 0));
            } catch (PackageManager.NameNotFoundException e) {
                Log.w("AdvancedPowerDetailActivity", "Cannot find package: " + str, e);
            }
            new SubSettingLauncher(this).setDestination(AdvancedPowerUsageDetail.class.getName()).setTitleRes(R.string.battery_details_title).setArguments(bundle2).setSourceMetricsCategory(20).launch();
        }
        finish();
    }
}
