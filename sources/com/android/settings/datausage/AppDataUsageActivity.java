package com.android.settings.datausage;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settingslib.AppItem;

public class AppDataUsageActivity extends SettingsActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        String schemeSpecificPart = intent.getData().getSchemeSpecificPart();
        try {
            int packageUid = getPackageManager().getPackageUid(schemeSpecificPart, 0);
            Bundle bundle2 = new Bundle();
            AppItem appItem = new AppItem(packageUid);
            appItem.addUid(packageUid);
            bundle2.putParcelable("app_item", appItem);
            intent.putExtra(":settings:show_fragment_args", bundle2);
            intent.putExtra(":settings:show_fragment", AppDataUsage.class.getName());
            intent.putExtra(":settings:show_fragment_title_resid", R.string.data_usage_app_summary_title);
            super.onCreate(bundle);
        } catch (PackageManager.NameNotFoundException unused) {
            Log.w("AppDataUsageActivity", "invalid package: " + schemeSpecificPart);
            try {
                super.onCreate(bundle);
            } catch (Exception unused2) {
            } catch (Throwable th) {
                finish();
                throw th;
            }
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public boolean isValidFragment(String str) {
        return super.isValidFragment(str) || AppDataUsage.class.getName().equals(str);
    }
}
