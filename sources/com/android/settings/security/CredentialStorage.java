package com.android.settings.security;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.security.IKeyChainService;
import android.security.KeyChain;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.R;
import com.android.settings.password.ChooseLockSettingsHelper;
import com.android.settings.vpn2.VpnUtils;
import com.android.settingslib.core.lifecycle.HideNonSystemOverlayMixin;

public final class CredentialStorage extends FragmentActivity {
    private Bundle mInstallBundle;
    /* access modifiers changed from: private */
    public LockPatternUtils mUtils;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mUtils = new LockPatternUtils(this);
        getLifecycle().addObserver(new HideNonSystemOverlayMixin(this));
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String action = intent.getAction();
        if (((UserManager) getSystemService("user")).hasUserRestriction("no_config_credentials")) {
            finish();
        } else if ("com.android.credentials.RESET".equals(action)) {
            new ResetDialog();
        } else {
            if ("com.android.credentials.INSTALL".equals(action) && checkCallerIsCertInstallerOrSelfInProfile()) {
                this.mInstallBundle = intent.getExtras();
            }
            handleInstall();
        }
    }

    private void handleInstall() {
        if (!isFinishing() && installIfAvailable()) {
            finish();
        }
    }

    private boolean installIfAvailable() {
        Bundle bundle = this.mInstallBundle;
        if (bundle == null || bundle.isEmpty()) {
            return true;
        }
        Bundle bundle2 = this.mInstallBundle;
        this.mInstallBundle = null;
        int i = bundle2.getInt("install_as_uid", -1);
        if (i == -1 || UserHandle.isSameUser(i, Process.myUid())) {
            String string = bundle2.getString("user_key_pair_name", (String) null);
            if (TextUtils.isEmpty(string)) {
                Log.e("CredentialStorage", "Cannot install key without an alias");
                return true;
            }
            new InstallKeyInKeyChain(string, bundle2.getByteArray("user_private_key_data"), bundle2.getByteArray("user_certificate_data"), bundle2.getByteArray("ca_certificates_data"), i).execute(new Void[0]);
            return false;
        }
        int userId = UserHandle.getUserId(i);
        if (i != 1010) {
            Log.e("CredentialStorage", "Failed to install credentials as uid " + i + ": cross-user installs may only target wifi uids");
            return true;
        }
        startActivityAsUser(new Intent("com.android.credentials.INSTALL").setFlags(33554432).putExtras(bundle2), new UserHandle(userId));
        return true;
    }

    private class ResetDialog implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener {
        private boolean mResetConfirmed;

        private ResetDialog() {
            AlertDialog create = new AlertDialog.Builder(CredentialStorage.this).setTitle(17039380).setMessage((int) R.string.credentials_reset_hint).setPositiveButton(17039370, (DialogInterface.OnClickListener) this).setNegativeButton(17039360, (DialogInterface.OnClickListener) this).create();
            create.setOnDismissListener(this);
            create.show();
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            this.mResetConfirmed = i == -1;
        }

        public void onDismiss(DialogInterface dialogInterface) {
            if (!this.mResetConfirmed) {
                CredentialStorage.this.finish();
                return;
            }
            this.mResetConfirmed = false;
            if (!CredentialStorage.this.mUtils.isSecure(UserHandle.myUserId())) {
                new ResetKeyStoreAndKeyChain().execute(new Void[0]);
            } else if (!CredentialStorage.this.confirmKeyGuard(1)) {
                Log.w("CredentialStorage", "Failed to launch credential confirmation for a secure user.");
                CredentialStorage.this.finish();
            }
        }
    }

    private class ResetKeyStoreAndKeyChain extends AsyncTask<Void, Void, Boolean> {
        private ResetKeyStoreAndKeyChain() {
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Can't wrap try/catch for region: R(5:9|10|11|12|13) */
        /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
            r2 = java.lang.Boolean.FALSE;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
            r1.close();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x002a, code lost:
            return r2;
         */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0025 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Boolean doInBackground(java.lang.Void... r2) {
            /*
                r1 = this;
                com.android.settings.security.CredentialStorage r2 = com.android.settings.security.CredentialStorage.this
                com.android.internal.widget.LockPatternUtils r2 = r2.mUtils
                int r0 = android.os.UserHandle.myUserId()
                r2.resetKeyStore(r0)
                com.android.settings.security.CredentialStorage r1 = com.android.settings.security.CredentialStorage.this     // Catch:{ InterruptedException -> 0x002f }
                android.security.KeyChain$KeyChainConnection r1 = android.security.KeyChain.bind(r1)     // Catch:{ InterruptedException -> 0x002f }
                android.security.IKeyChainService r2 = r1.getService()     // Catch:{ RemoteException -> 0x0025 }
                boolean r2 = r2.reset()     // Catch:{ RemoteException -> 0x0025 }
                java.lang.Boolean r2 = java.lang.Boolean.valueOf(r2)     // Catch:{ RemoteException -> 0x0025 }
                r1.close()     // Catch:{ InterruptedException -> 0x002f }
                return r2
            L_0x0023:
                r2 = move-exception
                goto L_0x002b
            L_0x0025:
                java.lang.Boolean r2 = java.lang.Boolean.FALSE     // Catch:{ all -> 0x0023 }
                r1.close()     // Catch:{ InterruptedException -> 0x002f }
                return r2
            L_0x002b:
                r1.close()     // Catch:{ InterruptedException -> 0x002f }
                throw r2     // Catch:{ InterruptedException -> 0x002f }
            L_0x002f:
                java.lang.Thread r1 = java.lang.Thread.currentThread()
                r1.interrupt()
                java.lang.Boolean r1 = java.lang.Boolean.FALSE
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settings.security.CredentialStorage.ResetKeyStoreAndKeyChain.doInBackground(java.lang.Void[]):java.lang.Boolean");
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Boolean bool) {
            if (bool.booleanValue()) {
                Toast.makeText(CredentialStorage.this, R.string.credentials_erased, 0).show();
                CredentialStorage.this.clearLegacyVpnIfEstablished();
            } else {
                Toast.makeText(CredentialStorage.this, R.string.credentials_not_erased, 0).show();
            }
            CredentialStorage.this.finish();
        }
    }

    /* access modifiers changed from: private */
    public void clearLegacyVpnIfEstablished() {
        if (VpnUtils.disconnectLegacyVpn(getApplicationContext())) {
            Toast.makeText(this, R.string.vpn_disconnected, 0).show();
        }
    }

    private class InstallKeyInKeyChain extends AsyncTask<Void, Void, Boolean> {
        final String mAlias;
        private final byte[] mCaListData;
        private final byte[] mCertData;
        private final byte[] mKeyData;
        private final int mUid;

        InstallKeyInKeyChain(String str, byte[] bArr, byte[] bArr2, byte[] bArr3, int i) {
            this.mAlias = str;
            this.mKeyData = bArr;
            this.mCertData = bArr2;
            this.mCaListData = bArr3;
            this.mUid = i;
        }

        /* access modifiers changed from: protected */
        public Boolean doInBackground(Void... voidArr) {
            KeyChain.KeyChainConnection bind;
            try {
                bind = KeyChain.bind(CredentialStorage.this);
                IKeyChainService service = bind.getService();
                if (!service.installKeyPair(this.mKeyData, this.mCertData, this.mCaListData, this.mAlias, this.mUid)) {
                    Log.w("CredentialStorage", String.format("Failed installing key %s", new Object[]{this.mAlias}));
                    Boolean bool = Boolean.FALSE;
                    bind.close();
                    return bool;
                }
                int i = this.mUid;
                if (i == 1000 || i == -1) {
                    service.setUserSelectable(this.mAlias, true);
                }
                Boolean bool2 = Boolean.TRUE;
                bind.close();
                return bool2;
            } catch (RemoteException e) {
                Log.w("CredentialStorage", String.format("Failed to install key %s to uid %d", new Object[]{this.mAlias, Integer.valueOf(this.mUid)}), e);
                return Boolean.FALSE;
            } catch (InterruptedException e2) {
                Log.w("CredentialStorage", String.format("Interrupted while installing key %s", new Object[]{this.mAlias}), e2);
                Thread.currentThread().interrupt();
                return Boolean.FALSE;
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
            throw th;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Boolean bool) {
            CredentialStorage.this.onKeyInstalled(this.mAlias, this.mUid, bool.booleanValue());
        }
    }

    /* access modifiers changed from: private */
    public void onKeyInstalled(String str, int i, boolean z) {
        if (!z) {
            Log.w("CredentialStorage", String.format("Error installing alias %s for uid %d", new Object[]{str, Integer.valueOf(i)}));
            finish();
            return;
        }
        Log.i("CredentialStorage", String.format("Successfully installed alias %s to uid %d.", new Object[]{str, Integer.valueOf(i)}));
        sendBroadcast(new Intent("android.security.action.KEYCHAIN_CHANGED"));
        setResult(-1);
        finish();
    }

    private boolean checkCallerIsCertInstallerOrSelfInProfile() {
        if (!TextUtils.equals("com.android.certinstaller", getCallingPackage())) {
            try {
                int launchedFromUid = ActivityManager.getService().getLaunchedFromUid(getActivityToken());
                if (launchedFromUid == -1) {
                    Log.e("CredentialStorage", "com.android.credentials.INSTALL must be started with startActivityForResult");
                    return false;
                } else if (!UserHandle.isSameApp(launchedFromUid, Process.myUid())) {
                    return false;
                } else {
                    UserInfo profileParent = ((UserManager) getSystemService("user")).getProfileParent(UserHandle.getUserId(launchedFromUid));
                    if (profileParent == null || profileParent.id != UserHandle.myUserId()) {
                        return false;
                    }
                    return true;
                }
            } catch (RemoteException unused) {
                return false;
            }
        } else if (getPackageManager().checkSignatures(getCallingPackage(), getPackageName()) == 0) {
            return true;
        } else {
            return false;
        }
    }

    /* access modifiers changed from: private */
    public boolean confirmKeyGuard(int i) {
        return new ChooseLockSettingsHelper.Builder(this).setRequestCode(i).setTitle(getResources().getText(R.string.credentials_title)).show();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != 1) {
            return;
        }
        if (i2 == -1) {
            new ResetKeyStoreAndKeyChain().execute(new Void[0]);
        } else {
            finish();
        }
    }
}
