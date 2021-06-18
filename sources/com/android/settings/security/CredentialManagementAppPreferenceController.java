package com.android.settings.security;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.security.IKeyChainService;
import android.security.KeyChain;
import android.util.Log;
import androidx.preference.Preference;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CredentialManagementAppPreferenceController extends BasePreferenceController {
    private static final String TAG = "CredentialManagementApp";
    private String mCredentialManagerPackageName;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean mHasCredentialManagerPackage;
    private final PackageManager mPackageManager;

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

    public CredentialManagementAppPreferenceController(Context context, String str) {
        super(context, str);
        this.mPackageManager = context.getPackageManager();
    }

    public void updateState(Preference preference) {
        this.mExecutor.execute(new C1252xc202846e(this, preference));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateState$1(Preference preference) {
        try {
            IKeyChainService service = KeyChain.bind(this.mContext).getService();
            this.mHasCredentialManagerPackage = service.hasCredentialManagementApp();
            this.mCredentialManagerPackageName = service.getCredentialManagementAppPackageName();
        } catch (RemoteException | InterruptedException unused) {
            Log.e(TAG, "Unable to display credential management app preference");
        }
        this.mHandler.post(new C1251xc202846d(this, preference));
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    /* renamed from: displayPreference */
    public void lambda$updateState$0(Preference preference) {
        if (this.mHasCredentialManagerPackage) {
            preference.setEnabled(true);
            try {
                preference.setSummary(this.mPackageManager.getApplicationInfo(this.mCredentialManagerPackageName, 0).loadLabel(this.mPackageManager));
            } catch (PackageManager.NameNotFoundException unused) {
                preference.setSummary((CharSequence) this.mCredentialManagerPackageName);
            }
        } else {
            preference.setEnabled(false);
            preference.setSummary((int) R.string.no_certificate_management_app);
        }
    }
}
