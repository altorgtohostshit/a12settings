package com.android.settings.accounts;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.widget.LayoutPreference;
import java.io.IOException;

public class RemoveAccountPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, View.OnClickListener {
    private Account mAccount;
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private Fragment mParentFragment;
    private LayoutPreference mRemoveAccountPreference;
    private UserHandle mUserHandle;

    public String getPreferenceKey() {
        return "remove_account";
    }

    public boolean isAvailable() {
        return true;
    }

    public RemoveAccountPreferenceController(Context context, Fragment fragment) {
        super(context);
        this.mParentFragment = fragment;
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        LayoutPreference layoutPreference = (LayoutPreference) preferenceScreen.findPreference("remove_account");
        this.mRemoveAccountPreference = layoutPreference;
        ((Button) layoutPreference.findViewById(R.id.button)).setOnClickListener(this);
    }

    public void onClick(View view) {
        RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced;
        MetricsFeatureProvider metricsFeatureProvider = this.mMetricsFeatureProvider;
        metricsFeatureProvider.logClickedPreference(this.mRemoveAccountPreference, metricsFeatureProvider.getMetricsCategory(this.mParentFragment));
        UserHandle userHandle = this.mUserHandle;
        if (userHandle == null || (checkIfRestrictionEnforced = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this.mContext, "no_modify_accounts", userHandle.getIdentifier())) == null) {
            ConfirmRemoveAccountDialog.show(this.mParentFragment, this.mAccount, this.mUserHandle);
        } else {
            RestrictedLockUtils.sendShowAdminSupportDetailsIntent(this.mContext, checkIfRestrictionEnforced);
        }
    }

    public void init(Account account, UserHandle userHandle) {
        this.mAccount = account;
        this.mUserHandle = userHandle;
    }

    public static class ConfirmRemoveAccountDialog extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
        private Account mAccount;
        private UserHandle mUserHandle;

        public int getMetricsCategory() {
            return 585;
        }

        public static ConfirmRemoveAccountDialog show(Fragment fragment, Account account, UserHandle userHandle) {
            if (!fragment.isAdded()) {
                return null;
            }
            ConfirmRemoveAccountDialog confirmRemoveAccountDialog = new ConfirmRemoveAccountDialog();
            Bundle bundle = new Bundle();
            bundle.putParcelable("account", account);
            bundle.putParcelable("android.intent.extra.USER", userHandle);
            confirmRemoveAccountDialog.setArguments(bundle);
            confirmRemoveAccountDialog.setTargetFragment(fragment, 0);
            confirmRemoveAccountDialog.show(fragment.getFragmentManager(), "confirmRemoveAccount");
            return confirmRemoveAccountDialog;
        }

        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            Bundle arguments = getArguments();
            this.mAccount = (Account) arguments.getParcelable("account");
            this.mUserHandle = (UserHandle) arguments.getParcelable("android.intent.extra.USER");
        }

        public Dialog onCreateDialog(Bundle bundle) {
            return new AlertDialog.Builder(getActivity()).setTitle((int) R.string.really_remove_account_title).setMessage((int) R.string.really_remove_account_message).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).setPositiveButton((int) R.string.remove_account_label, (DialogInterface.OnClickListener) this).create();
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            FragmentActivity activity = getTargetFragment().getActivity();
            AccountManager.get(activity).removeAccountAsUser(this.mAccount, activity, new C0638x609c4744(this), (Handler) null, this.mUserHandle);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onClick$0(AccountManagerFuture accountManagerFuture) {
            FragmentActivity activity = getTargetFragment().getActivity();
            if (activity == null || activity.isFinishing()) {
                Log.w("PrefControllerMixin", "Activity is no longer alive, skipping results");
                return;
            }
            boolean z = true;
            try {
                z = true ^ ((Bundle) accountManagerFuture.getResult()).getBoolean("booleanResult");
            } catch (AuthenticatorException | OperationCanceledException | IOException unused) {
            }
            if (z) {
                RemoveAccountFailureDialog.show(getTargetFragment());
            } else {
                activity.finish();
            }
        }
    }

    public static class RemoveAccountFailureDialog extends InstrumentedDialogFragment {
        public int getMetricsCategory() {
            return 586;
        }

        public static void show(Fragment fragment) {
            if (fragment.isAdded()) {
                RemoveAccountFailureDialog removeAccountFailureDialog = new RemoveAccountFailureDialog();
                removeAccountFailureDialog.setTargetFragment(fragment, 0);
                try {
                    removeAccountFailureDialog.show(fragment.getFragmentManager(), "removeAccountFailed");
                } catch (IllegalStateException e) {
                    Log.w("PrefControllerMixin", "Can't show RemoveAccountFailureDialog. " + e.getMessage());
                }
            }
        }

        public Dialog onCreateDialog(Bundle bundle) {
            return new AlertDialog.Builder(getActivity()).setTitle((int) R.string.really_remove_account_title).setMessage((int) R.string.remove_account_failed).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).create();
        }
    }
}
