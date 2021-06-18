package com.android.settings.applications;

import android.app.role.RoleManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.icu.text.ListFormatter;
import android.text.TextUtils;
import androidx.core.text.BidiFormatter;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.applications.AppUtils;
import java.util.ArrayList;
import java.util.List;

public class DefaultAppsPreferenceController extends BasePreferenceController {
    private final PackageManager mPackageManager;
    private final RoleManager mRoleManager;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
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

    public DefaultAppsPreferenceController(Context context, String str) {
        super(context, str);
        this.mPackageManager = context.getPackageManager();
        this.mRoleManager = (RoleManager) context.getSystemService(RoleManager.class);
    }

    public CharSequence getSummary() {
        ArrayList arrayList = new ArrayList();
        CharSequence defaultAppLabel = getDefaultAppLabel("android.app.role.BROWSER");
        if (!TextUtils.isEmpty(defaultAppLabel)) {
            arrayList.add(defaultAppLabel);
        }
        CharSequence defaultAppLabel2 = getDefaultAppLabel("android.app.role.DIALER");
        if (!TextUtils.isEmpty(defaultAppLabel2)) {
            arrayList.add(defaultAppLabel2);
        }
        CharSequence defaultAppLabel3 = getDefaultAppLabel("android.app.role.SMS");
        if (!TextUtils.isEmpty(defaultAppLabel3)) {
            arrayList.add(defaultAppLabel3);
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return ListFormatter.getInstance().format(arrayList);
    }

    private CharSequence getDefaultAppLabel(String str) {
        List roleHolders = this.mRoleManager.getRoleHolders(str);
        if (roleHolders.isEmpty()) {
            return null;
        }
        return BidiFormatter.getInstance().unicodeWrap(AppUtils.getApplicationLabel(this.mPackageManager, (String) roleHolders.get(0)));
    }
}
