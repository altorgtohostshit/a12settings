package com.android.settings.gestures;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.slices.SliceBackgroundWorker;

public class DoubleTwistPreferenceController extends GesturePreferenceController {
    private static final String PREF_KEY_VIDEO = "gesture_double_twist_video";
    private final int OFF = 0;

    /* renamed from: ON */
    private final int f70ON = 1;
    private final String mDoubleTwistPrefKey;
    private final UserManager mUserManager;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    /* access modifiers changed from: protected */
    public String getVideoPrefKey() {
        return PREF_KEY_VIDEO;
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

    public DoubleTwistPreferenceController(Context context, String str) {
        super(context, str);
        this.mDoubleTwistPrefKey = str;
        this.mUserManager = (UserManager) context.getSystemService("user");
    }

    public static boolean isSuggestionComplete(Context context, SharedPreferences sharedPreferences) {
        if (!isGestureAvailable(context) || sharedPreferences.getBoolean("pref_double_twist_suggestion_complete", false)) {
            return true;
        }
        return false;
    }

    public static boolean isGestureAvailable(Context context) {
        Resources resources = context.getResources();
        String string = resources.getString(R.string.gesture_double_twist_sensor_name);
        String string2 = resources.getString(R.string.gesture_double_twist_sensor_vendor);
        if (TextUtils.isEmpty(string) || TextUtils.isEmpty(string2)) {
            return false;
        }
        for (Sensor next : ((SensorManager) context.getSystemService("sensor")).getSensorList(-1)) {
            if (string.equals(next.getName()) && string2.equals(next.getVendor())) {
                return true;
            }
        }
        return false;
    }

    public int getAvailabilityStatus() {
        return isGestureAvailable(this.mContext) ? 0 : 3;
    }

    public boolean isSliceable() {
        return TextUtils.equals(getPreferenceKey(), "gesture_double_twist");
    }

    public String getPreferenceKey() {
        return this.mDoubleTwistPrefKey;
    }

    public boolean setChecked(boolean z) {
        setDoubleTwistPreference(this.mContext, this.mUserManager, z ? 1 : 0);
        return true;
    }

    public static void setDoubleTwistPreference(Context context, UserManager userManager, int i) {
        Settings.Secure.putInt(context.getContentResolver(), "camera_double_twist_to_flip_enabled", i);
        int managedProfileId = getManagedProfileId(userManager);
        if (managedProfileId != -10000) {
            Settings.Secure.putIntForUser(context.getContentResolver(), "camera_double_twist_to_flip_enabled", i, managedProfileId);
        }
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "camera_double_twist_to_flip_enabled", 1) != 0;
    }

    public static int getManagedProfileId(UserManager userManager) {
        return Utils.getManagedProfileId(userManager, UserHandle.myUserId());
    }
}
