package com.android.settings.applications.appinfo;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.provider.DeviceConfig;
import android.text.TextUtils;
import android.util.Slog;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.slices.SliceBackgroundWorker;

public final class HibernationSwitchPreferenceController extends AppInfoPreferenceControllerBase implements LifecycleObserver, AppOpsManager.OnOpChangedListener, Preference.OnPreferenceChangeListener {
    private static final String TAG = "HibernationSwitchPrefController";
    private final AppOpsManager mAppOpsManager;
    private boolean mIsPackageExemptByDefault;
    boolean mIsPackageSet;
    private String mPackageName;
    private int mPackageUid;

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

    public HibernationSwitchPreferenceController(Context context, String str) {
        super(context, str);
        this.mAppOpsManager = (AppOpsManager) context.getSystemService(AppOpsManager.class);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (this.mIsPackageSet) {
            this.mAppOpsManager.startWatchingMode("android:auto_revoke_permissions_if_unused", this.mPackageName, this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        this.mAppOpsManager.stopWatchingMode(this);
    }

    public int getAvailabilityStatus() {
        return (!isHibernationEnabled() || !this.mIsPackageSet) ? 2 : 0;
    }

    /* access modifiers changed from: package-private */
    public void setPackage(String str) {
        this.mPackageName = str;
        PackageManager packageManager = this.mContext.getPackageManager();
        int i = packageManager.hasSystemFeature("android.hardware.type.automotive") ? 30 : 29;
        try {
            this.mPackageUid = packageManager.getPackageUid(str, 0);
            this.mIsPackageExemptByDefault = packageManager.getTargetSdkVersion(str) <= i;
            this.mIsPackageSet = true;
        } catch (PackageManager.NameNotFoundException unused) {
            Slog.w(TAG, "Package [" + this.mPackageName + "] is not found!");
            this.mIsPackageSet = false;
        }
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        ((SwitchPreference) preference).setChecked(!isPackageHibernationExemptByUser());
    }

    /* access modifiers changed from: package-private */
    public boolean isPackageHibernationExemptByUser() {
        if (!this.mIsPackageSet) {
            return true;
        }
        int unsafeCheckOpNoThrow = this.mAppOpsManager.unsafeCheckOpNoThrow("android:auto_revoke_permissions_if_unused", this.mPackageUid, this.mPackageName);
        if (unsafeCheckOpNoThrow == 3) {
            return this.mIsPackageExemptByDefault;
        }
        if (unsafeCheckOpNoThrow != 0) {
            return true;
        }
        return false;
    }

    public void onOpChanged(String str, String str2) {
        if ("android:auto_revoke_permissions_if_unused".equals(str) && TextUtils.equals(this.mPackageName, str2)) {
            updateState(this.mPreference);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        try {
            this.mAppOpsManager.setUidMode("android:auto_revoke_permissions_if_unused", this.mPackageUid, ((Boolean) obj).booleanValue() ? 0 : 1);
            return true;
        } catch (RuntimeException unused) {
            return false;
        }
    }

    private static boolean isHibernationEnabled() {
        return DeviceConfig.getBoolean("app_hibernation", "app_hibernation_enabled", false);
    }
}
