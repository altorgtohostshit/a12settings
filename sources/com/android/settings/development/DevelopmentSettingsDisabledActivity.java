package com.android.settings.development;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import com.android.settings.R;

public class DevelopmentSettingsDisabledActivity extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Toast.makeText(this, R.string.dev_settings_disabled_warning, 0).show();
        finish();
    }
}
