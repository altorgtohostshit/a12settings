package com.android.settings.display;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.SensorPrivacyManager;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.bluetooth.RestrictionUtils;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedSwitchPreference;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;

public class AdaptiveSleepPreferenceController {
    private final Context mContext;
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private final PackageManager mPackageManager;
    private final PowerManager mPowerManager;
    RestrictedSwitchPreference mPreference;
    private final SensorPrivacyManager mPrivacyManager;
    private final RestrictionUtils mRestrictionUtils;

    public AdaptiveSleepPreferenceController(Context context, RestrictionUtils restrictionUtils) {
        this.mContext = context;
        this.mRestrictionUtils = restrictionUtils;
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
        this.mPrivacyManager = SensorPrivacyManager.getInstance(context);
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
        RestrictedSwitchPreference restrictedSwitchPreference = new RestrictedSwitchPreference(context);
        this.mPreference = restrictedSwitchPreference;
        restrictedSwitchPreference.setTitle((int) R.string.adaptive_sleep_title);
        this.mPreference.setSummary((int) R.string.adaptive_sleep_description);
        this.mPreference.setChecked(isChecked());
        this.mPreference.setKey("adaptive_sleep");
        this.mPreference.setOnPreferenceClickListener(new AdaptiveSleepPreferenceController$$ExternalSyntheticLambda1(this, context));
        this.mPackageManager = context.getPackageManager();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0(Context context, Preference preference) {
        boolean isChecked = ((RestrictedSwitchPreference) preference).isChecked();
        this.mMetricsFeatureProvider.action(context, 1755, isChecked);
        Settings.Secure.putInt(context.getContentResolver(), "adaptive_sleep", isChecked ? 1 : 0);
        return true;
    }

    public AdaptiveSleepPreferenceController(Context context) {
        this(context, new RestrictionUtils());
    }

    public void addToScreen(PreferenceScreen preferenceScreen) {
        updatePreference();
        preferenceScreen.addPreference(this.mPreference);
    }

    public void updatePreference() {
        initializePreference();
        RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced = this.mRestrictionUtils.checkIfRestrictionEnforced(this.mContext, "no_config_screen_timeout");
        if (checkIfRestrictionEnforced != null) {
            this.mPreference.setDisabledByAdmin(checkIfRestrictionEnforced);
        } else {
            this.mPreference.setEnabled(hasSufficientPermission(this.mPackageManager) && !isCameraLocked() && !isPowerSaveMode());
        }
    }

    /* access modifiers changed from: package-private */
    public void initializePreference() {
        if (this.mPreference == null) {
            RestrictedSwitchPreference restrictedSwitchPreference = new RestrictedSwitchPreference(this.mContext);
            this.mPreference = restrictedSwitchPreference;
            restrictedSwitchPreference.setTitle((int) R.string.adaptive_sleep_title);
            this.mPreference.setSummary((int) R.string.adaptive_sleep_description);
            this.mPreference.setChecked(isChecked());
            this.mPreference.setKey("adaptive_sleep");
            this.mPreference.setOnPreferenceChangeListener(new AdaptiveSleepPreferenceController$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$initializePreference$1(Preference preference, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        this.mMetricsFeatureProvider.action(this.mContext, 1755, booleanValue);
        Settings.Secure.putInt(this.mContext.getContentResolver(), "adaptive_sleep", booleanValue ? 1 : 0);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean isChecked() {
        if (!hasSufficientPermission(this.mContext.getPackageManager()) || isCameraLocked() || isPowerSaveMode() || Settings.Secure.getInt(this.mContext.getContentResolver(), "adaptive_sleep", 0) == 0) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean isCameraLocked() {
        return this.mPrivacyManager.isSensorPrivacyEnabled(2);
    }

    /* access modifiers changed from: package-private */
    public boolean isPowerSaveMode() {
        return this.mPowerManager.isPowerSaveMode();
    }

    public static int isControllerAvailable(Context context) {
        return (!context.getResources().getBoolean(17891341) || !isAttentionServiceAvailable(context)) ? 3 : 1;
    }

    private static boolean isAttentionServiceAvailable(Context context) {
        ResolveInfo resolveService;
        PackageManager packageManager = context.getPackageManager();
        String attentionServicePackageName = packageManager.getAttentionServicePackageName();
        if (TextUtils.isEmpty(attentionServicePackageName) || (resolveService = packageManager.resolveService(new Intent("android.service.attention.AttentionService").setPackage(attentionServicePackageName), 1048576)) == null || resolveService.serviceInfo == null) {
            return false;
        }
        return true;
    }

    static boolean hasSufficientPermission(PackageManager packageManager) {
        String attentionServicePackageName = packageManager.getAttentionServicePackageName();
        return attentionServicePackageName != null && packageManager.checkPermission("android.permission.CAMERA", attentionServicePackageName) == 0;
    }
}
