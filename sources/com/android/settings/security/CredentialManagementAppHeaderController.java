package com.android.settings.security;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.security.KeyChain;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.widget.LayoutPreference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CredentialManagementAppHeaderController extends BasePreferenceController {
    private static final String TAG = "CredentialManagementApp";
    private String mCredentialManagerPackageName;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final PackageManager mPackageManager;

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

    public CredentialManagementAppHeaderController(Context context, String str) {
        super(context, str);
        this.mPackageManager = context.getPackageManager();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mExecutor.execute(new C1247x6bf2149f(this, preferenceScreen));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$1(PreferenceScreen preferenceScreen) {
        try {
            this.mCredentialManagerPackageName = KeyChain.bind(this.mContext).getService().getCredentialManagementAppPackageName();
        } catch (RemoteException | InterruptedException unused) {
            Log.e(TAG, "Unable to display credential management app header");
        }
        this.mHandler.post(new C1248x6bf214a0(this, preferenceScreen));
    }

    /* access modifiers changed from: private */
    /* renamed from: displayHeader */
    public void lambda$displayPreference$0(PreferenceScreen preferenceScreen) {
        LayoutPreference layoutPreference = (LayoutPreference) preferenceScreen.findPreference(getPreferenceKey());
        ImageView imageView = (ImageView) layoutPreference.findViewById(R.id.entity_header_icon);
        TextView textView = (TextView) layoutPreference.findViewById(R.id.entity_header_title);
        ((TextView) layoutPreference.findViewById(R.id.entity_header_summary)).setVisibility(8);
        ((TextView) layoutPreference.findViewById(R.id.entity_header_second_summary)).setVisibility(8);
        try {
            ApplicationInfo applicationInfo = this.mPackageManager.getApplicationInfo(this.mCredentialManagerPackageName, 0);
            imageView.setImageDrawable(this.mPackageManager.getApplicationIcon(applicationInfo));
            textView.setText(applicationInfo.loadLabel(this.mPackageManager));
        } catch (PackageManager.NameNotFoundException unused) {
            imageView.setImageDrawable((Drawable) null);
            textView.setText(this.mCredentialManagerPackageName);
        }
    }
}
