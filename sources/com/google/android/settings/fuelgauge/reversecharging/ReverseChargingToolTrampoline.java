package com.google.android.settings.fuelgauge.reversecharging;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.core.SettingsBaseActivity;

public class ReverseChargingToolTrampoline extends SettingsBaseActivity {
    @VisibleForTesting
    static final String EXTRA_REVERSE_CHARGING_IS_SUPPORTED = "extra_reverse_charging_is_supported";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getIntent() == null) {
            Log.w("RCToolTrampoline", "Intent is null!");
            finish();
        }
        handleIntent(getIntent());
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void handleIntent(Intent intent) {
        if (intent.getBooleanExtra(EXTRA_REVERSE_CHARGING_IS_SUPPORTED, false)) {
            Intent intent2 = new Intent();
            intent2.putExtra(EXTRA_REVERSE_CHARGING_IS_SUPPORTED, ReverseChargingManager.getInstance(getBaseContext()).isSupportedReverseCharging());
            setResult(10, intent2);
            finish();
        }
    }
}
