package com.android.settings.emergency;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class EmergencyGestureEntrypointPreferenceController extends BasePreferenceController {
    static final String ACTION_EMERGENCY_GESTURE_SETTINGS = "com.android.settings.action.emergency_gesture_settings";
    private static final String TAG = "EmergencyGestureEntry";
    Intent mIntent;
    private boolean mUseCustomIntent;

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

    public EmergencyGestureEntrypointPreferenceController(Context context, String str) {
        super(context, str);
        String string = context.getResources().getString(R.string.emergency_gesture_settings_package);
        if (!TextUtils.isEmpty(string)) {
            Intent intent = new Intent(ACTION_EMERGENCY_GESTURE_SETTINGS).setPackage(string);
            if (canResolveIntent(intent)) {
                this.mUseCustomIntent = true;
                this.mIntent = intent;
            }
        }
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        boolean canHandleClicks = canHandleClicks();
        if (preference != null) {
            preference.setEnabled(canHandleClicks);
        }
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        Intent intent;
        if (!TextUtils.equals(getPreferenceKey(), preference.getKey()) || (intent = this.mIntent) == null) {
            return super.handlePreferenceTreeClick(preference);
        }
        intent.setFlags(67108864);
        this.mContext.startActivity(this.mIntent);
        return true;
    }

    public int getAvailabilityStatus() {
        if (this.mContext.getResources().getBoolean(R.bool.config_show_emergency_gesture_settings) && canHandleClicks()) {
            return 0;
        }
        return 3;
    }

    public CharSequence getSummary() {
        if (!this.mUseCustomIntent) {
            return super.getSummary();
        }
        String string = this.mContext.getResources().getString(R.string.emergency_gesture_settings_package);
        try {
            PackageManager packageManager = this.mContext.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(string, 33280);
            return this.mContext.getString(R.string.emergency_gesture_entrypoint_summary, new Object[]{applicationInfo.loadLabel(packageManager)});
        } catch (Exception unused) {
            Log.d(TAG, "Failed to get custom summary, falling back.");
            return super.getSummary();
        }
    }

    public boolean shouldSuppressFromSearch() {
        return this.mUseCustomIntent;
    }

    private boolean canHandleClicks() {
        return !this.mUseCustomIntent || this.mIntent != null;
    }

    private boolean canResolveIntent(Intent intent) {
        if (this.mContext.getPackageManager().resolveActivity(intent, 0) != null) {
            return true;
        }
        return false;
    }
}
