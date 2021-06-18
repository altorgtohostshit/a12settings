package com.android.settings.accounts;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.search.SearchIndexableRaw;
import java.util.List;

public class EmergencyInfoPreferenceController extends BasePreferenceController {
    Intent mIntent;

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

    public EmergencyInfoPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void updateRawDataToIndex(List<SearchIndexableRaw> list) {
        if (isAvailable()) {
            SearchIndexableRaw searchIndexableRaw = new SearchIndexableRaw(this.mContext);
            Resources resources = this.mContext.getResources();
            searchIndexableRaw.title = resources.getString(R.string.emergency_info_title);
            searchIndexableRaw.screenTitle = resources.getString(R.string.emergency_info_title);
            list.add(searchIndexableRaw);
        }
    }

    public void updateState(Preference preference) {
        UserInfo userInfo = ((UserManager) this.mContext.getSystemService(UserManager.class)).getUserInfo(UserHandle.myUserId());
        preference.setSummary((CharSequence) this.mContext.getString(R.string.emergency_info_summary, new Object[]{userInfo.name}));
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        Intent intent;
        if (!TextUtils.equals(getPreferenceKey(), preference.getKey()) || (intent = this.mIntent) == null) {
            return false;
        }
        intent.setFlags(67108864);
        this.mContext.startActivity(this.mIntent);
        return true;
    }

    public int getAvailabilityStatus() {
        if (!this.mContext.getResources().getBoolean(R.bool.config_show_emergency_info_in_device_info)) {
            return 3;
        }
        if (!isEmergencyInfoSupported() && !isAOSPVersionSupported()) {
            return 3;
        }
        return 0;
    }

    private boolean isEmergencyInfoSupported() {
        this.mIntent = new Intent(this.mContext.getResources().getString(R.string.config_emergency_intent_action)).setPackage(this.mContext.getResources().getString(R.string.config_emergency_package_name));
        List<ResolveInfo> queryIntentActivities = this.mContext.getPackageManager().queryIntentActivities(this.mIntent, 0);
        if (queryIntentActivities == null || queryIntentActivities.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean isAOSPVersionSupported() {
        this.mIntent = new Intent(this.mContext.getResources().getString(R.string.config_aosp_emergency_intent_action)).setPackage(this.mContext.getResources().getString(R.string.config_aosp_emergency_package_name));
        List<ResolveInfo> queryIntentActivities = this.mContext.getPackageManager().queryIntentActivities(this.mIntent, 0);
        if (queryIntentActivities == null || queryIntentActivities.isEmpty()) {
            return false;
        }
        return true;
    }
}
