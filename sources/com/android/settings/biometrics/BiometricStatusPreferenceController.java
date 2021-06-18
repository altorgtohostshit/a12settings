package com.android.settings.biometrics;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import androidx.preference.Preference;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;

public abstract class BiometricStatusPreferenceController extends BasePreferenceController {
    protected final LockPatternUtils mLockPatternUtils;
    protected final int mProfileChallengeUserId;
    protected final UserManager mUm;
    private final int mUserId;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    /* access modifiers changed from: protected */
    public abstract String getEnrollClassName();

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    /* access modifiers changed from: protected */
    public abstract String getSettingsClassName();

    /* access modifiers changed from: protected */
    public abstract String getSummaryTextEnrolled();

    /* access modifiers changed from: protected */
    public abstract String getSummaryTextNoneEnrolled();

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    /* access modifiers changed from: protected */
    public abstract boolean hasEnrolledBiometrics();

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    /* access modifiers changed from: protected */
    public abstract boolean isDeviceSupported();

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    /* access modifiers changed from: protected */
    public boolean isUserSupported() {
        return true;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public BiometricStatusPreferenceController(Context context, String str) {
        super(context, str);
        int myUserId = UserHandle.myUserId();
        this.mUserId = myUserId;
        UserManager userManager = (UserManager) context.getSystemService("user");
        this.mUm = userManager;
        this.mLockPatternUtils = FeatureFactory.getFactory(context).getSecurityFeatureProvider().getLockPatternUtils(context);
        this.mProfileChallengeUserId = Utils.getManagedProfileId(userManager, myUserId);
    }

    public int getAvailabilityStatus() {
        if (!isDeviceSupported()) {
            return 3;
        }
        return isUserSupported() ? 0 : 4;
    }

    public void updateState(Preference preference) {
        String str;
        if (isAvailable()) {
            preference.setVisible(true);
            if (hasEnrolledBiometrics()) {
                str = getSummaryTextEnrolled();
            } else {
                str = getSummaryTextNoneEnrolled();
            }
            preference.setSummary((CharSequence) str);
        } else if (preference != null) {
            preference.setVisible(false);
        }
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        String str;
        if (!TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return super.handlePreferenceTreeClick(preference);
        }
        Context context = preference.getContext();
        UserManager userManager = UserManager.get(context);
        int userId = getUserId();
        if (Utils.startQuietModeDialogIfNecessary(context, userManager, userId)) {
            return false;
        }
        Intent intent = new Intent();
        if (hasEnrolledBiometrics()) {
            str = getSettingsClassName();
        } else {
            str = getEnrollClassName();
        }
        intent.setClassName("com.android.settings", str);
        if (!preference.getExtras().isEmpty()) {
            intent.putExtras(preference.getExtras());
        }
        intent.putExtra("android.intent.extra.USER_ID", userId);
        intent.putExtra("from_settings_summary", true);
        context.startActivity(intent);
        return true;
    }

    /* access modifiers changed from: protected */
    public int getUserId() {
        return this.mUserId;
    }
}
