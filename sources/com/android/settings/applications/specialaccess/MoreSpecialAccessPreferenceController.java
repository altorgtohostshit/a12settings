package com.android.settings.applications.specialaccess;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import androidx.preference.Preference;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class MoreSpecialAccessPreferenceController extends BasePreferenceController {
    private final Intent mIntent;

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

    public MoreSpecialAccessPreferenceController(Context context, String str) {
        super(context, str);
        PackageManager packageManager = context.getPackageManager();
        String permissionControllerPackageName = packageManager.getPermissionControllerPackageName();
        Intent intent = null;
        if (permissionControllerPackageName != null) {
            Intent intent2 = new Intent("android.intent.action.MANAGE_SPECIAL_APP_ACCESSES").setPackage(permissionControllerPackageName);
            this.mIntent = packageManager.resolveActivity(intent2, 65536) != null ? intent2 : intent;
            return;
        }
        this.mIntent = null;
    }

    public int getAvailabilityStatus() {
        return this.mIntent != null ? 1 : 3;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), this.mPreferenceKey)) {
            return false;
        }
        Intent intent = this.mIntent;
        if (intent == null) {
            return true;
        }
        this.mContext.startActivity(intent);
        return true;
    }
}
