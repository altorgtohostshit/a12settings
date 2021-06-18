package com.android.settings.display;

import android.content.Context;
import android.os.PowerManager;
import android.view.View;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settingslib.widget.BannerMessagePreference;

public class AdaptiveSleepBatterySaverPreferenceController {
    private final PowerManager mPowerManager;
    final BannerMessagePreference mPreference;

    public AdaptiveSleepBatterySaverPreferenceController(Context context) {
        BannerMessagePreference bannerMessagePreference = new BannerMessagePreference(context);
        this.mPreference = bannerMessagePreference;
        bannerMessagePreference.setTitle((int) R.string.ambient_camera_summary_battery_saver_on);
        bannerMessagePreference.setPositiveButtonText(R.string.disable_text);
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
        bannerMessagePreference.setPositiveButtonOnClickListener(new C0878x9778124f(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        this.mPowerManager.setPowerSaveModeEnabled(false);
    }

    public void addToScreen(PreferenceScreen preferenceScreen) {
        preferenceScreen.addPreference(this.mPreference);
        updateVisibility();
    }

    /* access modifiers changed from: package-private */
    public boolean isPowerSaveMode() {
        return this.mPowerManager.isPowerSaveMode();
    }

    public void updateVisibility() {
        this.mPreference.setVisible(isPowerSaveMode());
    }
}
