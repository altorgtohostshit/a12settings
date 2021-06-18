package com.android.settings.security;

import android.app.admin.DevicePolicyEventLogger;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.security.KeyChain;
import android.util.Log;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.widget.ActionButtonsPreference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CredentialManagementAppButtonsController extends BasePreferenceController {
    private static final String TAG = "CredentialManagementApp";
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private Fragment mFragment;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean mHasCredentialManagerPackage;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 1;
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

    public CredentialManagementAppButtonsController(Context context, String str) {
        super(context, str);
    }

    public void setParentFragment(Fragment fragment) {
        this.mFragment = fragment;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mExecutor.execute(new C1245x68006a67(this, preferenceScreen));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$1(PreferenceScreen preferenceScreen) {
        try {
            this.mHasCredentialManagerPackage = KeyChain.bind(this.mContext).getService().hasCredentialManagementApp();
        } catch (RemoteException | InterruptedException unused) {
            Log.e(TAG, "Unable to display credential management app buttons");
        }
        this.mHandler.post(new C1246x68006a68(this, preferenceScreen));
    }

    /* access modifiers changed from: private */
    /* renamed from: displayButtons */
    public void lambda$displayPreference$0(PreferenceScreen preferenceScreen) {
        if (this.mHasCredentialManagerPackage) {
            ((ActionButtonsPreference) preferenceScreen.findPreference(getPreferenceKey())).setButton1Text(R.string.remove_credential_management_app).setButton1Icon(R.drawable.ic_undo_24).setButton1OnClickListener(new C1243x68006a65(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayButtons$2(View view) {
        removeCredentialManagementApp();
    }

    private void removeCredentialManagementApp() {
        this.mExecutor.execute(new C1244x68006a66(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$removeCredentialManagementApp$3() {
        try {
            KeyChain.bind(this.mContext).getService().removeCredentialManagementApp();
            DevicePolicyEventLogger.createEvent(187).write();
            this.mFragment.getActivity().finish();
        } catch (RemoteException | InterruptedException unused) {
            Log.e(TAG, "Unable to remove the credential management app");
        }
    }
}
