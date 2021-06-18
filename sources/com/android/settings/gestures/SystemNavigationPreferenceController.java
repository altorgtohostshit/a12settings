package com.android.settings.gestures;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class SystemNavigationPreferenceController extends BasePreferenceController {
    private static final String ACTION_QUICKSTEP = "android.intent.action.QUICKSTEP_SERVICE";
    static final String PREF_KEY_SYSTEM_NAVIGATION = "gesture_system_navigation";

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

    public SystemNavigationPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return isGestureAvailable(this.mContext) ? 0 : 3;
    }

    public CharSequence getSummary() {
        if (isGestureNavigationEnabled(this.mContext)) {
            return this.mContext.getText(R.string.edge_to_edge_navigation_title);
        }
        if (is2ButtonNavigationEnabled(this.mContext)) {
            return this.mContext.getText(R.string.swipe_up_to_switch_apps_title);
        }
        return this.mContext.getText(R.string.legacy_navigation_title);
    }

    static boolean isGestureAvailable(Context context) {
        ComponentName unflattenFromString;
        if (!context.getResources().getBoolean(17891662) || (unflattenFromString = ComponentName.unflattenFromString(context.getString(17039982))) == null) {
            return false;
        }
        if (context.getPackageManager().resolveService(new Intent(ACTION_QUICKSTEP).setPackage(unflattenFromString.getPackageName()), 1048576) == null) {
            return false;
        }
        return true;
    }

    static boolean isOverlayPackageAvailable(Context context, String str) {
        try {
            return context.getPackageManager().getPackageInfo(str, 0) != null;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    static boolean is2ButtonNavigationEnabled(Context context) {
        return 1 == context.getResources().getInteger(17694858);
    }

    static boolean isGestureNavigationEnabled(Context context) {
        return 2 == context.getResources().getInteger(17694858);
    }
}
