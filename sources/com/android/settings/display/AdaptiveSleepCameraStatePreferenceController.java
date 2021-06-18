package com.android.settings.display;

import android.content.Context;
import android.hardware.SensorPrivacyManager;
import android.view.View;
import androidx.preference.PreferenceScreen;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R;
import com.android.settingslib.widget.BannerMessagePreference;

public class AdaptiveSleepCameraStatePreferenceController {
    @VisibleForTesting
    final BannerMessagePreference mPreference;
    private final SensorPrivacyManager mPrivacyManager;

    public AdaptiveSleepCameraStatePreferenceController(Context context) {
        BannerMessagePreference bannerMessagePreference = new BannerMessagePreference(context);
        this.mPreference = bannerMessagePreference;
        bannerMessagePreference.setTitle((int) R.string.auto_rotate_camera_lock_title);
        bannerMessagePreference.setSummary((int) R.string.adaptive_sleep_camera_lock_summary);
        bannerMessagePreference.setPositiveButtonText(R.string.allow);
        SensorPrivacyManager instance = SensorPrivacyManager.getInstance(context);
        this.mPrivacyManager = instance;
        instance.addSensorPrivacyListener(2, new C0879x31edb191(this));
        bannerMessagePreference.setPositiveButtonOnClickListener(new C0880x31edb192(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, boolean z) {
        updateVisibility();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        this.mPrivacyManager.setSensorPrivacy(2, false);
    }

    public void addToScreen(PreferenceScreen preferenceScreen) {
        preferenceScreen.addPreference(this.mPreference);
        updateVisibility();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean isCameraLocked() {
        return this.mPrivacyManager.isSensorPrivacyEnabled(2);
    }

    public void updateVisibility() {
        this.mPreference.setVisible(isCameraLocked());
    }
}
