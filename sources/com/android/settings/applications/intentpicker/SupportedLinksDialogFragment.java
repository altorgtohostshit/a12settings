package com.android.settings.applications.intentpicker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.pm.verify.domain.DomainVerificationManager;
import android.content.pm.verify.domain.DomainVerificationUserState;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class SupportedLinksDialogFragment extends InstrumentedDialogFragment {
    private String mPackage;
    private List<SupportedLinkWrapper> mSupportedLinkWrapperList;
    private SupportedLinkViewModel mViewModel;

    public int getMetricsCategory() {
        return 0;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mPackage = getArguments().getString("app_package");
        SupportedLinkViewModel supportedLinkViewModel = (SupportedLinkViewModel) ViewModelProviders.m6of(getActivity()).get(SupportedLinkViewModel.class);
        this.mViewModel = supportedLinkViewModel;
        this.mSupportedLinkWrapperList = supportedLinkViewModel.getSupportedLinkWrapperList();
    }

    public Dialog onCreateDialog(Bundle bundle) {
        FragmentActivity activity = getActivity();
        return new AlertDialog.Builder(activity).setTitle((CharSequence) IntentPickerUtils.getCentralizedDialogTitle(getSupportedLinksTitle())).setAdapter(new SupportedLinksAdapter(activity, this.mSupportedLinkWrapperList), (DialogInterface.OnClickListener) null).setCancelable(true).setPositiveButton((int) R.string.app_launch_supported_links_add, (DialogInterface.OnClickListener) new SupportedLinksDialogFragment$$ExternalSyntheticLambda0(this)).setNegativeButton((int) R.string.app_launch_dialog_cancel, (DialogInterface.OnClickListener) null).create();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        doSelectedAction();
    }

    public void showDialog(FragmentManager fragmentManager) {
        show(fragmentManager, "SupportedLinksDialog");
    }

    private String getSupportedLinksTitle() {
        int size = this.mSupportedLinkWrapperList.size();
        return getResources().getQuantityString(R.plurals.app_launch_supported_links_title, size, new Object[]{Integer.valueOf(size)});
    }

    private void doSelectedAction() {
        DomainVerificationManager domainVerificationManager = (DomainVerificationManager) getActivity().getSystemService(DomainVerificationManager.class);
        DomainVerificationUserState domainVerificationUserState = IntentPickerUtils.getDomainVerificationUserState(domainVerificationManager, this.mPackage);
        if (domainVerificationUserState != null && this.mSupportedLinkWrapperList != null) {
            updateUserSelection(domainVerificationManager, domainVerificationUserState);
            displaySelectedItem();
        }
    }

    private void updateUserSelection(DomainVerificationManager domainVerificationManager, DomainVerificationUserState domainVerificationUserState) {
        ArraySet arraySet = new ArraySet();
        for (SupportedLinkWrapper next : this.mSupportedLinkWrapperList) {
            if (next.isChecked()) {
                arraySet.add(next.getHost());
            }
        }
        if (arraySet.size() > 0) {
            setDomainVerificationUserSelection(domainVerificationManager, domainVerificationUserState.getIdentifier(), arraySet, true);
        }
    }

    private void setDomainVerificationUserSelection(DomainVerificationManager domainVerificationManager, UUID uuid, Set<String> set, boolean z) {
        try {
            domainVerificationManager.setDomainVerificationUserSelection(uuid, set, z);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("SupportedLinksDialogFrg", "addSelectedItems : " + e.getMessage());
        }
    }

    private void displaySelectedItem() {
        for (Fragment next : getActivity().getSupportFragmentManager().getFragments()) {
            if (next instanceof AppLaunchSettings) {
                ((AppLaunchSettings) next).addSelectedLinksPreference();
            }
        }
    }
}
