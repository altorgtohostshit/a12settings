package com.android.settings.display;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class AmbientDisplayAlwaysOnPreferenceController extends TogglePreferenceController {
    private static final String AOD_SUPPRESSED_TOKEN = "winddown";
    private static final int MY_USER = UserHandle.myUserId();
    private static final String PROP_AWARE_AVAILABLE = "ro.vendor.aware_available";
    private final int OFF = 0;

    /* renamed from: ON */
    private final int f61ON = 1;
    private AmbientDisplayConfiguration mConfig;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public boolean isPublicSlice() {
        return true;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AmbientDisplayAlwaysOnPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        if (!isAvailable(getConfig()) || SystemProperties.getBoolean(PROP_AWARE_AVAILABLE, false)) {
            return 3;
        }
        return 0;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        refreshSummary(preference);
    }

    public boolean isSliceable() {
        return TextUtils.equals(getPreferenceKey(), "ambient_display_always_on");
    }

    public boolean isChecked() {
        return getConfig().alwaysOnEnabled(MY_USER);
    }

    public boolean setChecked(boolean z) {
        Settings.Secure.putInt(this.mContext.getContentResolver(), "doze_always_on", z ? 1 : 0);
        return true;
    }

    public CharSequence getSummary() {
        Context context = this.mContext;
        return context.getText(isAodSuppressedByBedtime(context) ? R.string.aware_summary_when_bedtime_on : R.string.doze_always_on_summary);
    }

    public AmbientDisplayAlwaysOnPreferenceController setConfig(AmbientDisplayConfiguration ambientDisplayConfiguration) {
        this.mConfig = ambientDisplayConfiguration;
        return this;
    }

    public static boolean isAvailable(AmbientDisplayConfiguration ambientDisplayConfiguration) {
        return ambientDisplayConfiguration.alwaysOnAvailableForUser(MY_USER);
    }

    private AmbientDisplayConfiguration getConfig() {
        if (this.mConfig == null) {
            this.mConfig = new AmbientDisplayConfiguration(this.mContext);
        }
        return this.mConfig;
    }

    public static boolean isAodSuppressedByBedtime(Context context) {
        try {
            return ((PowerManager) context.getSystemService(PowerManager.class)).isAmbientDisplaySuppressedForTokenByApp(AOD_SUPPRESSED_TOKEN, context.getPackageManager().getApplicationInfo(context.getString(17039913), 0).uid);
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }
}
