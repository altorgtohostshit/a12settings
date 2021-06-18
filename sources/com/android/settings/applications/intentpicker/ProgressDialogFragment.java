package com.android.settings.applications.intentpicker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.verify.domain.DomainVerificationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settingslib.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

public class ProgressDialogFragment extends InstrumentedDialogFragment {
    private DomainVerificationManager mDomainVerificationManager;
    private Handler mHandle;
    private String mPackage;
    private ProgressAlertDialog mProgressAlertDialog;
    private List<SupportedLinkWrapper> mSupportedLinkWrapperList;
    private SupportedLinkViewModel mViewModel;

    public int getMetricsCategory() {
        return 0;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mViewModel = (SupportedLinkViewModel) ViewModelProviders.m6of(getActivity()).get(SupportedLinkViewModel.class);
    }

    public Dialog onCreateDialog(Bundle bundle) {
        this.mPackage = getArguments().getString("app_package");
        this.mDomainVerificationManager = (DomainVerificationManager) getActivity().getSystemService(DomainVerificationManager.class);
        this.mHandle = new Handler(Looper.getMainLooper());
        ProgressAlertDialog createProgressAlertDialog = createProgressAlertDialog();
        this.mProgressAlertDialog = createProgressAlertDialog;
        return createProgressAlertDialog;
    }

    private ProgressAlertDialog createProgressAlertDialog() {
        FragmentActivity activity = getActivity();
        ProgressAlertDialog progressAlertDialog = new ProgressAlertDialog(activity);
        progressAlertDialog.setTitle(IntentPickerUtils.getCentralizedDialogTitle(activity.getResources().getString(R.string.app_launch_checking_links_title)));
        progressAlertDialog.setButton(-2, activity.getText(R.string.app_launch_dialog_cancel), ProgressDialogFragment$$ExternalSyntheticLambda0.INSTANCE);
        progressAlertDialog.setCanceledOnTouchOutside(true);
        return progressAlertDialog;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$createProgressAlertDialog$0(DialogInterface dialogInterface, int i) {
        if (i == -2) {
            dialogInterface.cancel();
        }
    }

    public void showDialog(FragmentManager fragmentManager) {
        show(fragmentManager, "ProgressDialog");
    }

    public void onResume() {
        super.onResume();
        generateProgressAlertDialog();
    }

    public void onDestroy() {
        super.onDestroy();
        ProgressAlertDialog progressAlertDialog = this.mProgressAlertDialog;
        if (progressAlertDialog != null && progressAlertDialog.isShowing()) {
            this.mProgressAlertDialog.cancel();
        }
    }

    private void generateProgressAlertDialog() {
        ThreadUtils.postOnBackgroundThread((Runnable) new ProgressDialogFragment$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$generateProgressAlertDialog$2() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        queryLinksInBackground();
        IntentPickerUtils.logd("queryLinksInBackground take time: " + (SystemClock.elapsedRealtime() - elapsedRealtime));
        if (this.mProgressAlertDialog.isShowing()) {
            this.mHandle.post(new ProgressDialogFragment$$ExternalSyntheticLambda1(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$generateProgressAlertDialog$1() {
        synchronized (this.mHandle) {
            if (this.mProgressAlertDialog.isShowing()) {
                this.mProgressAlertDialog.dismiss();
                IntentPickerUtils.logd("mProgressAlertDialog.dismiss() and isShowing: " + this.mProgressAlertDialog.isShowing());
                launchSupportedLinksDialogFragment();
            }
        }
    }

    private void queryLinksInBackground() {
        int i = 0;
        List<String> linksList = IntentPickerUtils.getLinksList(this.mDomainVerificationManager, this.mPackage, 0);
        int size = linksList.size();
        this.mSupportedLinkWrapperList = new ArrayList();
        Iterator<String> it = linksList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String next = it.next();
            SortedSet ownersForDomain = this.mDomainVerificationManager.getOwnersForDomain(next);
            this.mSupportedLinkWrapperList.add(new SupportedLinkWrapper(getActivity(), next, ownersForDomain));
            i++;
            if (!this.mProgressAlertDialog.isShowing()) {
                Log.w("ProgressDialogFragment", "Exit the background thread!!!");
                this.mSupportedLinkWrapperList.clear();
                break;
            }
            this.mHandle.post(new ProgressDialogFragment$$ExternalSyntheticLambda3(this, (i * 100) / size));
            if (ownersForDomain.size() == 0) {
                SystemClock.sleep(20);
            }
        }
        IntentPickerUtils.logd("queryLinksInBackground : SupportedLinkWrapperList size=" + this.mSupportedLinkWrapperList.size());
        Collections.sort(this.mSupportedLinkWrapperList);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$queryLinksInBackground$3(int i) {
        synchronized (this.mHandle) {
            if (!this.mProgressAlertDialog.isShowing()) {
                Log.w("ProgressDialogFragment", "Exit the UI thread");
            } else {
                this.mProgressAlertDialog.getProgressBar().setProgress(i);
            }
        }
    }

    private void launchSupportedLinksDialogFragment() {
        if (this.mSupportedLinkWrapperList.size() > 0) {
            this.mViewModel.setSupportedLinkWrapperList(this.mSupportedLinkWrapperList);
            Bundle bundle = new Bundle();
            bundle.putString("app_package", this.mPackage);
            SupportedLinksDialogFragment supportedLinksDialogFragment = new SupportedLinksDialogFragment();
            supportedLinksDialogFragment.setArguments(bundle);
            supportedLinksDialogFragment.showDialog(getActivity().getSupportFragmentManager());
        }
    }

    static class ProgressAlertDialog extends AlertDialog {
        private ProgressBar mProgressBar;

        protected ProgressAlertDialog(Context context) {
            this(context, 0);
        }

        protected ProgressAlertDialog(Context context, int i) {
            super(context, i);
            init(context);
        }

        private void init(Context context) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.app_launch_progress, (ViewGroup) null);
            ProgressBar progressBar = (ProgressBar) inflate.findViewById(R.id.scan_links_progressbar);
            this.mProgressBar = progressBar;
            progressBar.setProgress(0);
            this.mProgressBar.setMax(100);
            setView(inflate);
        }

        /* access modifiers changed from: package-private */
        public ProgressBar getProgressBar() {
            return this.mProgressBar;
        }
    }
}
