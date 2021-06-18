package com.android.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.RestrictionsManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.enterprise.ActionDisabledByAdminDialogHelper;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;

@Deprecated
public abstract class RestrictedSettingsFragment extends SettingsPreferenceFragment {
    static final int REQUEST_PIN_CHALLENGE = 12309;
    AlertDialog mActionDisabledDialog;
    /* access modifiers changed from: private */
    public boolean mChallengeRequested;
    /* access modifiers changed from: private */
    public boolean mChallengeSucceeded;
    private TextView mEmptyTextView;
    private RestrictedLockUtils.EnforcedAdmin mEnforcedAdmin;
    private boolean mIsAdminUser;
    private boolean mOnlyAvailableForAdmins = false;
    private final String mRestrictionKey;
    private RestrictionsManager mRestrictionsManager;
    private BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (!RestrictedSettingsFragment.this.mChallengeRequested) {
                boolean unused = RestrictedSettingsFragment.this.mChallengeSucceeded = false;
                boolean unused2 = RestrictedSettingsFragment.this.mChallengeRequested = false;
            }
        }
    };
    private UserManager mUserManager;

    public RestrictedSettingsFragment(String str) {
        this.mRestrictionKey = str;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mRestrictionsManager = (RestrictionsManager) getSystemService("restrictions");
        UserManager userManager = (UserManager) getSystemService("user");
        this.mUserManager = userManager;
        this.mIsAdminUser = userManager.isAdminUser();
        if (bundle != null) {
            this.mChallengeSucceeded = bundle.getBoolean("chsc", false);
            this.mChallengeRequested = bundle.getBoolean("chrq", false);
        }
        IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        getActivity().registerReceiver(this.mScreenOffReceiver, intentFilter);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mEmptyTextView = initEmptyTextView();
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (getActivity().isChangingConfigurations()) {
            bundle.putBoolean("chrq", this.mChallengeRequested);
            bundle.putBoolean("chsc", this.mChallengeSucceeded);
        }
    }

    public void onResume() {
        super.onResume();
        if (shouldBeProviderProtected(this.mRestrictionKey)) {
            ensurePin();
        }
    }

    public void onDestroy() {
        getActivity().unregisterReceiver(this.mScreenOffReceiver);
        super.onDestroy();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i != REQUEST_PIN_CHALLENGE) {
            super.onActivityResult(i, i2, intent);
        } else if (i2 == -1) {
            this.mChallengeSucceeded = true;
            this.mChallengeRequested = false;
            AlertDialog alertDialog = this.mActionDisabledDialog;
            if (alertDialog != null && alertDialog.isShowing()) {
                this.mActionDisabledDialog.setOnDismissListener((DialogInterface.OnDismissListener) null);
                this.mActionDisabledDialog.dismiss();
            }
        } else {
            this.mChallengeSucceeded = false;
        }
    }

    private void ensurePin() {
        Intent createLocalApprovalIntent;
        if (!this.mChallengeSucceeded && !this.mChallengeRequested && this.mRestrictionsManager.hasRestrictionsProvider() && (createLocalApprovalIntent = this.mRestrictionsManager.createLocalApprovalIntent()) != null) {
            this.mChallengeRequested = true;
            this.mChallengeSucceeded = false;
            PersistableBundle persistableBundle = new PersistableBundle();
            persistableBundle.putString("android.request.mesg", getResources().getString(R.string.restr_pin_enter_admin_pin));
            createLocalApprovalIntent.putExtra("android.content.extra.REQUEST_BUNDLE", persistableBundle);
            startActivityForResult(createLocalApprovalIntent, REQUEST_PIN_CHALLENGE);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isRestrictedAndNotProviderProtected() {
        String str = this.mRestrictionKey;
        if (str == null || "restrict_if_overridable".equals(str) || !this.mUserManager.hasUserRestriction(this.mRestrictionKey) || this.mRestrictionsManager.hasRestrictionsProvider()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean hasChallengeSucceeded() {
        boolean z = this.mChallengeRequested;
        return (z && this.mChallengeSucceeded) || !z;
    }

    /* access modifiers changed from: protected */
    public boolean shouldBeProviderProtected(String str) {
        if (str == null) {
            return false;
        }
        if (!("restrict_if_overridable".equals(str) || this.mUserManager.hasUserRestriction(this.mRestrictionKey)) || !this.mRestrictionsManager.hasRestrictionsProvider()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public TextView initEmptyTextView() {
        return (TextView) getActivity().findViewById(16908292);
    }

    public RestrictedLockUtils.EnforcedAdmin getRestrictionEnforcedAdmin() {
        RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(getActivity(), this.mRestrictionKey, UserHandle.myUserId());
        this.mEnforcedAdmin = checkIfRestrictionEnforced;
        if (checkIfRestrictionEnforced != null && checkIfRestrictionEnforced.user == null) {
            checkIfRestrictionEnforced.user = UserHandle.of(UserHandle.myUserId());
        }
        return this.mEnforcedAdmin;
    }

    public TextView getEmptyTextView() {
        return this.mEmptyTextView;
    }

    /* access modifiers changed from: protected */
    public void onDataSetChanged() {
        AlertDialog alertDialog;
        highlightPreferenceIfNeeded();
        if (!isUiRestrictedByOnlyAdmin() || ((alertDialog = this.mActionDisabledDialog) != null && alertDialog.isShowing())) {
            TextView textView = this.mEmptyTextView;
            if (textView != null) {
                setEmptyView(textView);
            }
        } else {
            this.mActionDisabledDialog = new ActionDisabledByAdminDialogHelper(getActivity()).prepareDialogBuilder(this.mRestrictionKey, getRestrictionEnforcedAdmin()).setOnDismissListener(new RestrictedSettingsFragment$$ExternalSyntheticLambda0(this)).show();
            setEmptyView(new View(getContext()));
        }
        super.onDataSetChanged();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onDataSetChanged$0(DialogInterface dialogInterface) {
        getActivity().finish();
    }

    public void setIfOnlyAvailableForAdmins(boolean z) {
        this.mOnlyAvailableForAdmins = z;
    }

    /* access modifiers changed from: protected */
    public boolean isUiRestricted() {
        return isRestrictedAndNotProviderProtected() || !hasChallengeSucceeded() || (!this.mIsAdminUser && this.mOnlyAvailableForAdmins);
    }

    /* access modifiers changed from: protected */
    public boolean isUiRestrictedByOnlyAdmin() {
        return isUiRestricted() && !this.mUserManager.hasBaseUserRestriction(this.mRestrictionKey, UserHandle.of(UserHandle.myUserId())) && (this.mIsAdminUser || !this.mOnlyAvailableForAdmins);
    }
}
