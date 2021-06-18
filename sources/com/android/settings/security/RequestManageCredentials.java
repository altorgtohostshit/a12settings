package com.android.settings.security;

import android.app.Activity;
import android.app.admin.DevicePolicyEventLogger;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.pm.UserInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserManager;
import android.security.AppUriAuthenticationPolicy;
import android.security.KeyChain;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.Map;

public class RequestManageCredentials extends Activity {
    private AppUriAuthenticationPolicy mAuthenticationPolicy;
    private LinearLayout mButtonPanel;
    private String mCredentialManagerPackage;
    /* access modifiers changed from: private */
    public boolean mDisplayingButtonPanel = false;
    /* access modifiers changed from: private */
    public ExtendedFloatingActionButton mExtendedFab;
    private KeyChain.KeyChainConnection mKeyChainConnection;
    private HandlerThread mKeyChainTread;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!"android.security.MANAGE_CREDENTIALS".equals(getIntent().getAction())) {
            Log.e("ManageCredentials", "Unable to start activity because intent action is not android.security.MANAGE_CREDENTIALS");
            logRequestFailure();
            finishWithResultCancelled();
        } else if (isManagedDevice()) {
            Log.e("ManageCredentials", "Credential management on managed devices should be done by the Device Policy Controller, not a credential management app");
            logRequestFailure();
            finishWithResultCancelled();
        } else {
            String launchedFromPackage = getLaunchedFromPackage();
            this.mCredentialManagerPackage = launchedFromPackage;
            if (TextUtils.isEmpty(launchedFromPackage)) {
                Log.e("ManageCredentials", "Unknown credential manager app");
                logRequestFailure();
                finishWithResultCancelled();
                return;
            }
            DevicePolicyEventLogger.createEvent(178).setStrings(new String[]{this.mCredentialManagerPackage}).write();
            setContentView(R.layout.request_manage_credentials);
            HandlerThread handlerThread = new HandlerThread("KeyChainConnection");
            this.mKeyChainTread = handlerThread;
            handlerThread.start();
            this.mKeyChainConnection = getKeyChainConnection(this, this.mKeyChainTread);
            AppUriAuthenticationPolicy parcelableExtra = getIntent().getParcelableExtra("android.security.extra.AUTHENTICATION_POLICY");
            if (!isValidAuthenticationPolicy(parcelableExtra)) {
                Log.e("ManageCredentials", "Invalid authentication policy");
                logRequestFailure();
                finishWithResultCancelled();
                return;
            }
            this.mAuthenticationPolicy = parcelableExtra;
            DevicePolicyEventLogger.createEvent(179).setStrings(new String[]{getNumberOfAuthenticationPolicyApps(this.mAuthenticationPolicy), getNumberOfAuthenticationPolicyUris(this.mAuthenticationPolicy)}).write();
            loadRecyclerView();
            loadButtons();
            loadExtendedFloatingActionButton();
            addOnScrollListener();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        KeyChain.KeyChainConnection keyChainConnection = this.mKeyChainConnection;
        if (keyChainConnection != null) {
            keyChainConnection.close();
            this.mKeyChainConnection = null;
            this.mKeyChainTread.quitSafely();
        }
    }

    private boolean isValidAuthenticationPolicy(AppUriAuthenticationPolicy appUriAuthenticationPolicy) {
        if (appUriAuthenticationPolicy != null && !appUriAuthenticationPolicy.getAppAndUriMappings().isEmpty()) {
            try {
                for (String requestPrivateKey : appUriAuthenticationPolicy.getAliases()) {
                    if (this.mKeyChainConnection.getService().requestPrivateKey(requestPrivateKey) != null) {
                        return false;
                    }
                }
                return true;
            } catch (RemoteException e) {
                Log.e("ManageCredentials", "Invalid authentication policy", e);
            }
        }
        return false;
    }

    private boolean isManagedDevice() {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(DevicePolicyManager.class);
        return (devicePolicyManager.getDeviceOwnerUser() == null && devicePolicyManager.getProfileOwner() == null && !hasManagedProfile()) ? false : true;
    }

    private boolean hasManagedProfile() {
        for (UserInfo isManagedProfile : ((UserManager) getSystemService(UserManager.class)).getProfiles(getUserId())) {
            if (isManagedProfile.isManagedProfile()) {
                return true;
            }
        }
        return false;
    }

    private void loadRecyclerView() {
        this.mLayoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.apps_list);
        this.mRecyclerView = recyclerView;
        recyclerView.setLayoutManager(this.mLayoutManager);
        this.mRecyclerView.setAdapter(new CredentialManagementAppAdapter(this, this.mCredentialManagerPackage, this.mAuthenticationPolicy.getAppAndUriMappings(), true, false));
    }

    private void loadButtons() {
        this.mButtonPanel = (LinearLayout) findViewById(R.id.button_panel);
        ((Button) findViewById(R.id.dont_allow_button)).setOnClickListener(new RequestManageCredentials$$ExternalSyntheticLambda0(this));
        ((Button) findViewById(R.id.allow_button)).setOnClickListener(new RequestManageCredentials$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$loadButtons$0(View view) {
        DevicePolicyEventLogger.createEvent(181).write();
        finishWithResultCancelled();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$loadButtons$1(View view) {
        setOrUpdateCredentialManagementAppAndFinish();
    }

    private void loadExtendedFloatingActionButton() {
        ExtendedFloatingActionButton extendedFloatingActionButton = (ExtendedFloatingActionButton) findViewById(R.id.extended_fab);
        this.mExtendedFab = extendedFloatingActionButton;
        extendedFloatingActionButton.setOnClickListener(new RequestManageCredentials$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$loadExtendedFloatingActionButton$2(View view) {
        this.mRecyclerView.scrollToPosition(this.mAuthenticationPolicy.getAppAndUriMappings().size());
        this.mExtendedFab.hide();
        showButtonPanel();
    }

    private void setOrUpdateCredentialManagementAppAndFinish() {
        try {
            this.mKeyChainConnection.getService().setCredentialManagementApp(this.mCredentialManagerPackage, this.mAuthenticationPolicy);
            DevicePolicyEventLogger.createEvent(180).write();
            setResult(-1);
        } catch (RemoteException e) {
            Log.e("ManageCredentials", "Unable to set credential manager app", e);
            logRequestFailure();
        }
        finish();
    }

    /* access modifiers changed from: package-private */
    public KeyChain.KeyChainConnection getKeyChainConnection(Context context, HandlerThread handlerThread) {
        try {
            return KeyChain.bindAsUser(context, new Handler(handlerThread.getLooper()), Process.myUserHandle());
        } catch (InterruptedException e) {
            throw new RuntimeException("Faile to bind to KeyChain", e);
        }
    }

    private void addOnScrollListener() {
        this.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                if (!RequestManageCredentials.this.mDisplayingButtonPanel) {
                    if (i2 > 0 && RequestManageCredentials.this.mExtendedFab.getVisibility() == 0) {
                        RequestManageCredentials.this.mExtendedFab.setExtended(false);
                    }
                    if (RequestManageCredentials.this.isRecyclerScrollable()) {
                        RequestManageCredentials.this.mExtendedFab.show();
                        RequestManageCredentials.this.hideButtonPanel();
                        return;
                    }
                    RequestManageCredentials.this.mExtendedFab.hide();
                    RequestManageCredentials.this.showButtonPanel();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void showButtonPanel() {
        this.mRecyclerView.setPadding(0, 0, 0, (int) ((getResources().getDisplayMetrics().density * 60.0f) + 0.5f));
        this.mButtonPanel.setVisibility(0);
        this.mDisplayingButtonPanel = true;
    }

    /* access modifiers changed from: private */
    public void hideButtonPanel() {
        this.mRecyclerView.setPadding(0, 0, 0, 0);
        this.mButtonPanel.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public boolean isRecyclerScrollable() {
        if (this.mLayoutManager == null || this.mRecyclerView.getAdapter() == null || this.mLayoutManager.findLastCompletelyVisibleItemPosition() >= this.mRecyclerView.getAdapter().getItemCount() - 1) {
            return false;
        }
        return true;
    }

    private void finishWithResultCancelled() {
        setResult(0);
        finish();
    }

    private void logRequestFailure() {
        DevicePolicyEventLogger.createEvent(182).write();
    }

    private String getNumberOfAuthenticationPolicyUris(AppUriAuthenticationPolicy appUriAuthenticationPolicy) {
        int i = 0;
        for (Map.Entry value : appUriAuthenticationPolicy.getAppAndUriMappings().entrySet()) {
            i += ((Map) value.getValue()).size();
        }
        return String.valueOf(i);
    }

    private String getNumberOfAuthenticationPolicyApps(AppUriAuthenticationPolicy appUriAuthenticationPolicy) {
        return String.valueOf(appUriAuthenticationPolicy.getAppAndUriMappings().size());
    }
}
