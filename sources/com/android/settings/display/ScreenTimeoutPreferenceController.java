package com.android.settings.display;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.os.UserHandle;
import android.provider.Settings;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedPreference;

public class ScreenTimeoutPreferenceController extends BasePreferenceController {
    public static String PREF_NAME = "screen_timeout";
    private final CharSequence[] mTimeoutEntries;
    private final CharSequence[] mTimeoutValues;

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

    public ScreenTimeoutPreferenceController(Context context, String str) {
        super(context, str);
        this.mTimeoutEntries = context.getResources().getStringArray(R.array.screen_timeout_entries);
        this.mTimeoutValues = context.getResources().getStringArray(R.array.screen_timeout_values);
    }

    public void updateState(Preference preference) {
        long longValue = getMaxScreenTimeout().longValue();
        RestrictedLockUtils.EnforcedAdmin preferenceDisablingAdmin = getPreferenceDisablingAdmin(longValue);
        if (preferenceDisablingAdmin != null) {
            preference.setEnabled(false);
            preference.setSummary(this.mContext.getText(R.string.disabled_by_policy_title));
            ((RestrictedPreference) preference).setDisabledByAdmin(preferenceDisablingAdmin);
            return;
        }
        preference.setSummary(getTimeoutSummary(longValue));
    }

    private CharSequence getTimeoutSummary(long j) {
        CharSequence timeoutDescription = getTimeoutDescription(getCurrentScreenTimeout(), j);
        return this.mContext.getString(R.string.screen_timeout_summary, new Object[]{timeoutDescription});
    }

    private Long getMaxScreenTimeout() {
        DevicePolicyManager devicePolicyManager;
        if (RestrictedLockUtilsInternal.checkIfMaximumTimeToLockIsSet(this.mContext) == null || (devicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class)) == null) {
            return Long.MAX_VALUE;
        }
        return Long.valueOf(devicePolicyManager.getMaximumTimeToLock((ComponentName) null, UserHandle.myUserId()));
    }

    private RestrictedLockUtils.EnforcedAdmin getPreferenceDisablingAdmin(long j) {
        if (((DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class)) == null) {
            return null;
        }
        RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this.mContext, "no_config_screen_timeout", UserHandle.myUserId());
        return (checkIfRestrictionEnforced == null && getLargestTimeout(j) == null) ? RestrictedLockUtilsInternal.checkIfMaximumTimeToLockIsSet(this.mContext) : checkIfRestrictionEnforced;
    }

    private long getCurrentScreenTimeout() {
        return Settings.System.getLong(this.mContext.getContentResolver(), "screen_off_timeout", 30000);
    }

    private CharSequence getTimeoutDescription(long j, long j2) {
        CharSequence[] charSequenceArr;
        CharSequence[] charSequenceArr2;
        if (j < 0 || (charSequenceArr = this.mTimeoutEntries) == null || (charSequenceArr2 = this.mTimeoutValues) == null || charSequenceArr2.length != charSequenceArr.length) {
            return null;
        }
        if (j > j2) {
            return getLargestTimeout(j2);
        }
        return getCurrentTimeout(j);
    }

    private CharSequence getCurrentTimeout(long j) {
        int i = 0;
        while (true) {
            CharSequence[] charSequenceArr = this.mTimeoutValues;
            if (i >= charSequenceArr.length) {
                return null;
            }
            if (j == Long.parseLong(charSequenceArr[i].toString())) {
                return this.mTimeoutEntries[i];
            }
            i++;
        }
    }

    private CharSequence getLargestTimeout(long j) {
        CharSequence charSequence = null;
        int i = 0;
        while (true) {
            CharSequence[] charSequenceArr = this.mTimeoutValues;
            if (i >= charSequenceArr.length) {
                return charSequence;
            }
            if (Long.parseLong(charSequenceArr[i].toString()) <= j) {
                charSequence = this.mTimeoutEntries[i];
            }
            i++;
        }
    }
}
