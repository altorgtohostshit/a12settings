package com.google.android.settings.gestures.columbus;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class ColumbusPreferenceController extends BasePreferenceController {
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

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public ColumbusPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return isColumbusSupported(this.mContext) ? 0 : 3;
    }

    public CharSequence getSummary() {
        if (!isColumbusEnabled(this.mContext)) {
            return this.mContext.getText(R.string.gesture_setting_off);
        }
        CharSequence text = this.mContext.getText(R.string.gesture_setting_on);
        String columbusAction = ColumbusActionsPreferenceController.getColumbusAction(this.mContext);
        return this.mContext.getString(R.string.columbus_summary, new Object[]{text, columbusAction});
    }

    static boolean isColumbusSupported(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.hasSystemFeature("android.hardware.sensor.accelerometer") && packageManager.hasSystemFeature("android.hardware.sensor.gyroscope");
    }

    static boolean isColumbusEnabled(Context context) {
        if (Settings.Secure.getInt(context.getContentResolver(), "columbus_enabled", 0) != 0) {
            return true;
        }
        return false;
    }
}
