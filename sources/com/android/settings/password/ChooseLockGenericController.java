package com.android.settings.password;

import android.app.admin.PasswordMetrics;
import android.content.Context;
import android.os.UserManager;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.R;
import java.util.ArrayList;
import java.util.List;

public class ChooseLockGenericController {
    private final int mAppRequestedMinComplexity;
    private final Context mContext;
    private final boolean mDevicePasswordRequirementOnly;
    private final boolean mHideInsecureScreenLockTypes;
    private final LockPatternUtils mLockPatternUtils;
    private final ManagedLockPasswordProvider mManagedPasswordProvider;
    private final int mUnificationProfileId;
    private final int mUserId;

    public ChooseLockGenericController(Context context, int i, ManagedLockPasswordProvider managedLockPasswordProvider, LockPatternUtils lockPatternUtils, boolean z, int i2, boolean z2, int i3) {
        this.mContext = context;
        this.mUserId = i;
        this.mManagedPasswordProvider = managedLockPasswordProvider;
        this.mLockPatternUtils = lockPatternUtils;
        this.mHideInsecureScreenLockTypes = z;
        this.mAppRequestedMinComplexity = i2;
        this.mDevicePasswordRequirementOnly = z2;
        this.mUnificationProfileId = i3;
    }

    public static class Builder {
        private int mAppRequestedMinComplexity;
        private final Context mContext;
        private boolean mDevicePasswordRequirementOnly;
        private boolean mHideInsecureScreenLockTypes;
        private final LockPatternUtils mLockPatternUtils;
        private final ManagedLockPasswordProvider mManagedPasswordProvider;
        private int mUnificationProfileId;
        private final int mUserId;

        public Builder(Context context, int i) {
            this(context, i, new LockPatternUtils(context));
        }

        public Builder(Context context, int i, LockPatternUtils lockPatternUtils) {
            this(context, i, ManagedLockPasswordProvider.get(context, i), lockPatternUtils);
        }

        Builder(Context context, int i, ManagedLockPasswordProvider managedLockPasswordProvider, LockPatternUtils lockPatternUtils) {
            this.mHideInsecureScreenLockTypes = false;
            this.mAppRequestedMinComplexity = 0;
            this.mDevicePasswordRequirementOnly = false;
            this.mUnificationProfileId = -10000;
            this.mContext = context;
            this.mUserId = i;
            this.mManagedPasswordProvider = managedLockPasswordProvider;
            this.mLockPatternUtils = lockPatternUtils;
        }

        public Builder setAppRequestedMinComplexity(int i) {
            this.mAppRequestedMinComplexity = i;
            return this;
        }

        public Builder setEnforceDevicePasswordRequirementOnly(boolean z) {
            this.mDevicePasswordRequirementOnly = z;
            return this;
        }

        public Builder setProfileToUnify(int i) {
            this.mUnificationProfileId = i;
            return this;
        }

        public Builder setHideInsecureScreenLockTypes(boolean z) {
            this.mHideInsecureScreenLockTypes = z;
            return this;
        }

        public ChooseLockGenericController build() {
            return new ChooseLockGenericController(this.mContext, this.mUserId, this.mManagedPasswordProvider, this.mLockPatternUtils, this.mHideInsecureScreenLockTypes, this.mAppRequestedMinComplexity, this.mDevicePasswordRequirementOnly, this.mUnificationProfileId);
        }
    }

    public boolean isScreenLockVisible(ScreenLockType screenLockType) {
        boolean isManagedProfile = ((UserManager) this.mContext.getSystemService(UserManager.class)).isManagedProfile(this.mUserId);
        switch (C11961.$SwitchMap$com$android$settings$password$ScreenLockType[screenLockType.ordinal()]) {
            case 1:
                if (this.mHideInsecureScreenLockTypes || this.mContext.getResources().getBoolean(R.bool.config_hide_none_security_option) || isManagedProfile) {
                    return false;
                }
                return true;
            case 2:
                if (this.mHideInsecureScreenLockTypes || this.mContext.getResources().getBoolean(R.bool.config_hide_swipe_security_option) || isManagedProfile) {
                    return false;
                }
                return true;
            case 3:
                return this.mManagedPasswordProvider.isManagedPasswordChoosable();
            case 4:
            case 5:
            case 6:
                return this.mLockPatternUtils.hasSecureLockScreen();
            default:
                return true;
        }
    }

    /* renamed from: com.android.settings.password.ChooseLockGenericController$1 */
    static /* synthetic */ class C11961 {
        static final /* synthetic */ int[] $SwitchMap$com$android$settings$password$ScreenLockType;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|14) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.android.settings.password.ScreenLockType[] r0 = com.android.settings.password.ScreenLockType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$android$settings$password$ScreenLockType = r0
                com.android.settings.password.ScreenLockType r1 = com.android.settings.password.ScreenLockType.NONE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$android$settings$password$ScreenLockType     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.settings.password.ScreenLockType r1 = com.android.settings.password.ScreenLockType.SWIPE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$android$settings$password$ScreenLockType     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.settings.password.ScreenLockType r1 = com.android.settings.password.ScreenLockType.MANAGED     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$android$settings$password$ScreenLockType     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.settings.password.ScreenLockType r1 = com.android.settings.password.ScreenLockType.PIN     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$com$android$settings$password$ScreenLockType     // Catch:{ NoSuchFieldError -> 0x003e }
                com.android.settings.password.ScreenLockType r1 = com.android.settings.password.ScreenLockType.PATTERN     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$com$android$settings$password$ScreenLockType     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.android.settings.password.ScreenLockType r1 = com.android.settings.password.ScreenLockType.PASSWORD     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settings.password.ChooseLockGenericController.C11961.<clinit>():void");
        }
    }

    public boolean isScreenLockEnabled(ScreenLockType screenLockType) {
        return screenLockType.maxQuality >= upgradeQuality(0);
    }

    public int upgradeQuality(int i) {
        return Math.max(i, Math.max(LockPatternUtils.credentialTypeToPasswordQuality(getAggregatedPasswordMetrics().credType), PasswordMetrics.complexityLevelToMinQuality(getAggregatedPasswordComplexity())));
    }

    public CharSequence getTitle(ScreenLockType screenLockType) {
        switch (C11961.$SwitchMap$com$android$settings$password$ScreenLockType[screenLockType.ordinal()]) {
            case 1:
                return this.mContext.getText(R.string.unlock_set_unlock_off_title);
            case 2:
                return this.mContext.getText(R.string.unlock_set_unlock_none_title);
            case 3:
                return this.mManagedPasswordProvider.getPickerOptionTitle(false);
            case 4:
                return this.mContext.getText(R.string.unlock_set_unlock_pin_title);
            case 5:
                return this.mContext.getText(R.string.unlock_set_unlock_pattern_title);
            case 6:
                return this.mContext.getText(R.string.unlock_set_unlock_password_title);
            default:
                return null;
        }
    }

    public List<ScreenLockType> getVisibleAndEnabledScreenLockTypes() {
        ArrayList arrayList = new ArrayList();
        for (ScreenLockType screenLockType : ScreenLockType.values()) {
            if (isScreenLockVisible(screenLockType) && isScreenLockEnabled(screenLockType)) {
                arrayList.add(screenLockType);
            }
        }
        return arrayList;
    }

    public PasswordMetrics getAggregatedPasswordMetrics() {
        PasswordMetrics requestedPasswordMetrics = this.mLockPatternUtils.getRequestedPasswordMetrics(this.mUserId, this.mDevicePasswordRequirementOnly);
        int i = this.mUnificationProfileId;
        if (i != -10000) {
            requestedPasswordMetrics.maxWith(this.mLockPatternUtils.getRequestedPasswordMetrics(i));
        }
        return requestedPasswordMetrics;
    }

    public int getAggregatedPasswordComplexity() {
        int max = Math.max(this.mAppRequestedMinComplexity, this.mLockPatternUtils.getRequestedPasswordComplexity(this.mUserId, this.mDevicePasswordRequirementOnly));
        int i = this.mUnificationProfileId;
        return i != -10000 ? Math.max(max, this.mLockPatternUtils.getRequestedPasswordComplexity(i)) : max;
    }

    public boolean isScreenLockRestrictedByAdmin() {
        return getAggregatedPasswordMetrics().credType != -1 || isComplexityProvidedByAdmin();
    }

    public boolean isComplexityProvidedByAdmin() {
        int aggregatedPasswordComplexity = getAggregatedPasswordComplexity();
        return aggregatedPasswordComplexity > this.mAppRequestedMinComplexity && aggregatedPasswordComplexity > 0;
    }
}
