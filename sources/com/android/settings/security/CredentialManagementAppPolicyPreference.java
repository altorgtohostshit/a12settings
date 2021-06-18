package com.android.settings.security;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.security.AppUriAuthenticationPolicy;
import android.security.IKeyChainService;
import android.security.KeyChain;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CredentialManagementAppPolicyPreference extends Preference {
    private final Context mContext;
    private String mCredentialManagerPackageName;
    private AppUriAuthenticationPolicy mCredentialManagerPolicy;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean mHasCredentialManagerPackage;

    public CredentialManagementAppPolicyPreference(Context context) {
        super(context);
        setLayoutResource(R.layout.credential_management_app_policy);
        this.mContext = context;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.mExecutor.execute(new C1250x82e16144(this, preferenceViewHolder));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$1(PreferenceViewHolder preferenceViewHolder) {
        try {
            IKeyChainService service = KeyChain.bind(this.mContext).getService();
            this.mHasCredentialManagerPackage = service.hasCredentialManagementApp();
            this.mCredentialManagerPackageName = service.getCredentialManagementAppPackageName();
            this.mCredentialManagerPolicy = service.getCredentialManagementAppPolicy();
        } catch (RemoteException | InterruptedException unused) {
            Log.e("CredentialManagementApp", "Unable to display credential management app policy");
        }
        this.mHandler.post(new C1249x82e16143(this, preferenceViewHolder));
    }

    /* access modifiers changed from: private */
    /* renamed from: displayPolicy */
    public void lambda$onBindViewHolder$0(PreferenceViewHolder preferenceViewHolder) {
        if (this.mHasCredentialManagerPackage) {
            RecyclerView recyclerView = (RecyclerView) preferenceViewHolder.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
            recyclerView.setAdapter(new CredentialManagementAppAdapter(this.mContext, this.mCredentialManagerPackageName, this.mCredentialManagerPolicy.getAppAndUriMappings(), false, true));
        }
    }
}
