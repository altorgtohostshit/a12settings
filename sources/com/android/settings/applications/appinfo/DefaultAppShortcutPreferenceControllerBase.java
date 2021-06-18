package com.android.settings.applications.appinfo;

import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserManager;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.util.CollectionUtils;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import java.util.concurrent.Executor;

public abstract class DefaultAppShortcutPreferenceControllerBase extends BasePreferenceController {
    private boolean mAppVisible;
    protected final String mPackageName;
    private PreferenceScreen mPreferenceScreen;
    private final RoleManager mRoleManager;
    private final String mRoleName;
    private boolean mRoleVisible;

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

    public DefaultAppShortcutPreferenceControllerBase(Context context, String str, String str2, String str3) {
        super(context, str);
        this.mRoleName = str2;
        this.mPackageName = str3;
        RoleManager roleManager = (RoleManager) context.getSystemService(RoleManager.class);
        this.mRoleManager = roleManager;
        Executor mainExecutor = this.mContext.getMainExecutor();
        roleManager.isRoleVisible(str2, mainExecutor, new C0691x5368185e(this));
        roleManager.isApplicationVisibleForRole(str2, str3, mainExecutor, new C0690x5368185d(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Boolean bool) {
        this.mRoleVisible = bool.booleanValue();
        refreshAvailability();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(Boolean bool) {
        this.mAppVisible = bool.booleanValue();
        refreshAvailability();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceScreen = preferenceScreen;
    }

    private void refreshAvailability() {
        Preference findPreference;
        PreferenceScreen preferenceScreen = this.mPreferenceScreen;
        if (preferenceScreen != null && (findPreference = preferenceScreen.findPreference(getPreferenceKey())) != null) {
            findPreference.setVisible(isAvailable());
            updateState(findPreference);
        }
    }

    public int getAvailabilityStatus() {
        if (((UserManager) this.mContext.getSystemService(UserManager.class)).isManagedProfile()) {
            return 4;
        }
        return (!this.mRoleVisible || !this.mAppVisible) ? 3 : 0;
    }

    public CharSequence getSummary() {
        return this.mContext.getText(isDefaultApp() ? R.string.yes : R.string.no);
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(this.mPreferenceKey, preference.getKey())) {
            return false;
        }
        this.mContext.startActivity(new Intent("android.intent.action.MANAGE_DEFAULT_APP").putExtra("android.intent.extra.ROLE_NAME", this.mRoleName));
        return true;
    }

    private boolean isDefaultApp() {
        return TextUtils.equals(this.mPackageName, (String) CollectionUtils.firstOrNull(this.mRoleManager.getRoleHolders(this.mRoleName)));
    }
}
