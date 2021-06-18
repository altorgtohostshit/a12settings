package com.android.settings.users;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.UserInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.android.settings.core.SubSettingLauncher;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedPreference;

public class UserDetailsSettings extends SettingsPreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    private static final String TAG = UserDetailsSettings.class.getSimpleName();
    Preference mAppAndContentAccessPref;
    private Bundle mDefaultGuestRestrictions;
    private SwitchPreference mPhonePref;
    Preference mRemoveUserPref;
    RestrictedPreference mSwitchUserPref;
    private UserCapabilities mUserCaps;
    UserInfo mUserInfo;
    private UserManager mUserManager;

    public int getDialogMetricsCategory(int i) {
        if (i == 1) {
            return 591;
        }
        if (i == 2) {
            return 592;
        }
        if (i != 3) {
            return i != 4 ? 0 : 596;
        }
        return 593;
    }

    public int getMetricsCategory() {
        return 98;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FragmentActivity activity = getActivity();
        this.mUserManager = (UserManager) activity.getSystemService("user");
        this.mUserCaps = UserCapabilities.create(activity);
        addPreferencesFromResource(R.xml.user_details_settings);
        initialize(activity, getArguments());
    }

    public void onResume() {
        super.onResume();
        this.mSwitchUserPref.setEnabled(canSwitchUserNow());
    }

    public boolean onPreferenceClick(Preference preference) {
        if (preference == this.mRemoveUserPref) {
            if (canDeleteUser()) {
                showDialog(1);
                return true;
            }
        } else if (preference == this.mSwitchUserPref) {
            if (canSwitchUserNow()) {
                if (shouldShowSetupPromptDialog()) {
                    showDialog(4);
                } else {
                    switchUser();
                }
                return true;
            }
        } else if (preference == this.mAppAndContentAccessPref) {
            openAppAndContentAccessScreen(false);
            return true;
        }
        return false;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (Boolean.TRUE.equals(obj)) {
            showDialog(this.mUserInfo.isGuest() ? 2 : 3);
            return false;
        }
        enableCallsAndSms(false);
        return true;
    }

    public Dialog onCreateDialog(int i) {
        if (getActivity() == null) {
            return null;
        }
        if (i == 1) {
            return UserDialogs.createRemoveDialog(getActivity(), this.mUserInfo.id, new UserDetailsSettings$$ExternalSyntheticLambda3(this));
        }
        if (i == 2) {
            return UserDialogs.createEnablePhoneCallsDialog(getActivity(), new UserDetailsSettings$$ExternalSyntheticLambda2(this));
        }
        if (i == 3) {
            return UserDialogs.createEnablePhoneCallsAndSmsDialog(getActivity(), new UserDetailsSettings$$ExternalSyntheticLambda1(this));
        }
        if (i == 4) {
            return UserDialogs.createSetupUserDialog(getActivity(), new UserDetailsSettings$$ExternalSyntheticLambda0(this));
        }
        throw new IllegalArgumentException("Unsupported dialogId " + i);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        removeUser();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$1(DialogInterface dialogInterface, int i) {
        enableCallsAndSms(true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$2(DialogInterface dialogInterface, int i) {
        enableCallsAndSms(true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$3(DialogInterface dialogInterface, int i) {
        if (canSwitchUserNow()) {
            switchUser();
        }
    }

    /* access modifiers changed from: protected */
    public void showDialog(int i) {
        super.showDialog(i);
    }

    /* access modifiers changed from: package-private */
    public void initialize(Context context, Bundle bundle) {
        int i = bundle != null ? bundle.getInt("user_id", -10000) : -10000;
        if (i != -10000) {
            boolean z = bundle.getBoolean("new_user", false);
            this.mUserInfo = this.mUserManager.getUserInfo(i);
            this.mSwitchUserPref = (RestrictedPreference) findPreference("switch_user");
            this.mPhonePref = (SwitchPreference) findPreference("enable_calling");
            this.mRemoveUserPref = findPreference("remove_user");
            this.mAppAndContentAccessPref = findPreference("app_and_content_access");
            this.mSwitchUserPref.setTitle((CharSequence) context.getString(R.string.user_switch_to_user, new Object[]{UserSettings.getUserName(context, this.mUserInfo)}));
            if (this.mUserCaps.mDisallowSwitchUser) {
                this.mSwitchUserPref.setDisabledByAdmin(RestrictedLockUtilsInternal.getDeviceOwner(context));
            } else {
                this.mSwitchUserPref.setDisabledByAdmin((RestrictedLockUtils.EnforcedAdmin) null);
                this.mSwitchUserPref.setSelectable(true);
                this.mSwitchUserPref.setOnPreferenceClickListener(this);
            }
            if (!this.mUserManager.isAdminUser()) {
                removePreference("enable_calling");
                removePreference("remove_user");
                removePreference("app_and_content_access");
                return;
            }
            if (!Utils.isVoiceCapable(context)) {
                removePreference("enable_calling");
            }
            if (this.mUserInfo.isRestricted()) {
                removePreference("enable_calling");
                if (z) {
                    openAppAndContentAccessScreen(true);
                }
            } else {
                removePreference("app_and_content_access");
            }
            if (this.mUserInfo.isGuest()) {
                this.mPhonePref.setTitle((int) R.string.user_enable_calling);
                Bundle defaultGuestRestrictions = this.mUserManager.getDefaultGuestRestrictions();
                this.mDefaultGuestRestrictions = defaultGuestRestrictions;
                this.mPhonePref.setChecked(!defaultGuestRestrictions.getBoolean("no_outgoing_calls"));
                this.mRemoveUserPref.setTitle((int) R.string.user_exit_guest_title);
            } else {
                this.mPhonePref.setChecked(!this.mUserManager.hasUserRestriction("no_outgoing_calls", new UserHandle(i)));
                this.mRemoveUserPref.setTitle((int) R.string.user_remove_user);
            }
            if (RestrictedLockUtilsInternal.hasBaseUserRestriction(context, "no_remove_user", UserHandle.myUserId())) {
                removePreference("remove_user");
            }
            this.mRemoveUserPref.setOnPreferenceClickListener(this);
            this.mPhonePref.setOnPreferenceChangeListener(this);
            this.mAppAndContentAccessPref.setOnPreferenceClickListener(this);
            return;
        }
        throw new IllegalStateException("Arguments to this fragment must contain the user id");
    }

    /* access modifiers changed from: package-private */
    public boolean canDeleteUser() {
        FragmentActivity activity;
        if (!this.mUserManager.isAdminUser() || (activity = getActivity()) == null) {
            return false;
        }
        RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(activity, "no_remove_user", UserHandle.myUserId());
        if (checkIfRestrictionEnforced == null) {
            return true;
        }
        RestrictedLockUtils.sendShowAdminSupportDetailsIntent(activity, checkIfRestrictionEnforced);
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean canSwitchUserNow() {
        return this.mUserManager.getUserSwitchability() == 0;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:6|7) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002f, code lost:
        finishFragment();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0032, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0022, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
        android.util.Log.e(TAG, "Error while switching to other user.");
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x0024 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void switchUser() {
        /*
            r4 = this;
            android.content.pm.UserInfo r0 = r4.mUserInfo     // Catch:{ RemoteException -> 0x0024 }
            boolean r0 = r0.isGuest()     // Catch:{ RemoteException -> 0x0024 }
            if (r0 == 0) goto L_0x0016
            com.android.settingslib.core.instrumentation.MetricsFeatureProvider r0 = r4.mMetricsFeatureProvider     // Catch:{ RemoteException -> 0x0024 }
            androidx.fragment.app.FragmentActivity r1 = r4.getActivity()     // Catch:{ RemoteException -> 0x0024 }
            r2 = 1765(0x6e5, float:2.473E-42)
            r3 = 0
            android.util.Pair[] r3 = new android.util.Pair[r3]     // Catch:{ RemoteException -> 0x0024 }
            r0.action((android.content.Context) r1, (int) r2, (android.util.Pair<java.lang.Integer, java.lang.Object>[]) r3)     // Catch:{ RemoteException -> 0x0024 }
        L_0x0016:
            android.app.IActivityManager r0 = android.app.ActivityManager.getService()     // Catch:{ RemoteException -> 0x0024 }
            android.content.pm.UserInfo r1 = r4.mUserInfo     // Catch:{ RemoteException -> 0x0024 }
            int r1 = r1.id     // Catch:{ RemoteException -> 0x0024 }
            r0.switchUser(r1)     // Catch:{ RemoteException -> 0x0024 }
            goto L_0x002b
        L_0x0022:
            r0 = move-exception
            goto L_0x002f
        L_0x0024:
            java.lang.String r0 = TAG     // Catch:{ all -> 0x0022 }
            java.lang.String r1 = "Error while switching to other user."
            android.util.Log.e(r0, r1)     // Catch:{ all -> 0x0022 }
        L_0x002b:
            r4.finishFragment()
            return
        L_0x002f:
            r4.finishFragment()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.users.UserDetailsSettings.switchUser():void");
    }

    private void enableCallsAndSms(boolean z) {
        this.mPhonePref.setChecked(z);
        if (this.mUserInfo.isGuest()) {
            this.mDefaultGuestRestrictions.putBoolean("no_outgoing_calls", !z);
            this.mDefaultGuestRestrictions.putBoolean("no_sms", true);
            this.mUserManager.setDefaultGuestRestrictions(this.mDefaultGuestRestrictions);
            for (UserInfo userInfo : this.mUserManager.getAliveUsers()) {
                if (userInfo.isGuest()) {
                    UserHandle of = UserHandle.of(userInfo.id);
                    for (String str : this.mDefaultGuestRestrictions.keySet()) {
                        this.mUserManager.setUserRestriction(str, this.mDefaultGuestRestrictions.getBoolean(str), of);
                    }
                }
            }
            return;
        }
        UserHandle of2 = UserHandle.of(this.mUserInfo.id);
        this.mUserManager.setUserRestriction("no_outgoing_calls", !z, of2);
        this.mUserManager.setUserRestriction("no_sms", !z, of2);
    }

    private void removeUser() {
        this.mUserManager.removeUser(this.mUserInfo.id);
        finishFragment();
    }

    private void openAppAndContentAccessScreen(boolean z) {
        Bundle bundle = new Bundle();
        bundle.putInt("user_id", this.mUserInfo.id);
        bundle.putBoolean("new_user", z);
        new SubSettingLauncher(getContext()).setDestination(AppRestrictionsFragment.class.getName()).setArguments(bundle).setTitleRes(R.string.user_restrictions_title).setSourceMetricsCategory(getMetricsCategory()).launch();
    }

    private boolean isSecondaryUser(UserInfo userInfo) {
        return "android.os.usertype.full.SECONDARY".equals(userInfo.userType);
    }

    private boolean shouldShowSetupPromptDialog() {
        return isSecondaryUser(this.mUserInfo) && !this.mUserInfo.isInitialized();
    }
}
