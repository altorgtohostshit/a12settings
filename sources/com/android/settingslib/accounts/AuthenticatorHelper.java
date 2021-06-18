package com.android.settingslib.accounts;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorDescription;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SyncAdapterType;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.UserHandle;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class AuthenticatorHelper extends BroadcastReceiver {
    private final Map<String, Drawable> mAccTypeIconCache = new HashMap();
    private final HashMap<String, ArrayList<String>> mAccountTypeToAuthorities = new HashMap<>();
    private final Context mContext;
    private final ArrayList<String> mEnabledAccountTypes = new ArrayList<>();
    private final OnAccountsUpdateListener mListener;
    private boolean mListeningToAccountUpdates;
    private final Map<String, AuthenticatorDescription> mTypeToAuthDescription = new HashMap();
    private final UserHandle mUserHandle;

    public interface OnAccountsUpdateListener {
        void onAccountsUpdate(UserHandle userHandle);
    }

    public AuthenticatorHelper(Context context, UserHandle userHandle, OnAccountsUpdateListener onAccountsUpdateListener) {
        this.mContext = context;
        this.mUserHandle = userHandle;
        this.mListener = onAccountsUpdateListener;
        onAccountsUpdated((Account[]) null);
    }

    public String[] getEnabledAccountTypes() {
        ArrayList<String> arrayList = this.mEnabledAccountTypes;
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public void preloadDrawableForType(final Context context, final String str) {
        new AsyncTask<Void, Void, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                AuthenticatorHelper.this.getDrawableForType(context, str);
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[]) null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001d, code lost:
        if (r5.mTypeToAuthDescription.containsKey(r7) == false) goto L_0x004f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        r0 = r5.mTypeToAuthDescription.get(r7);
        r1 = r5.mContext.getPackageManager().getUserBadgedIcon(r6.createPackageContextAsUser(r0.packageName, 0, r5.mUserHandle).getDrawable(r0.iconId), r5.mUserHandle);
        r0 = r5.mAccTypeIconCache;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0044, code lost:
        monitor-enter(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r5.mAccTypeIconCache.put(r7, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004a, code lost:
        monitor-exit(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0016, code lost:
        r1 = null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.drawable.Drawable getDrawableForType(android.content.Context r6, java.lang.String r7) {
        /*
            r5 = this;
            java.util.Map<java.lang.String, android.graphics.drawable.Drawable> r0 = r5.mAccTypeIconCache
            monitor-enter(r0)
            java.util.Map<java.lang.String, android.graphics.drawable.Drawable> r1 = r5.mAccTypeIconCache     // Catch:{ all -> 0x0062 }
            boolean r1 = r1.containsKey(r7)     // Catch:{ all -> 0x0062 }
            if (r1 == 0) goto L_0x0015
            java.util.Map<java.lang.String, android.graphics.drawable.Drawable> r5 = r5.mAccTypeIconCache     // Catch:{ all -> 0x0062 }
            java.lang.Object r5 = r5.get(r7)     // Catch:{ all -> 0x0062 }
            android.graphics.drawable.Drawable r5 = (android.graphics.drawable.Drawable) r5     // Catch:{ all -> 0x0062 }
            monitor-exit(r0)     // Catch:{ all -> 0x0062 }
            return r5
        L_0x0015:
            monitor-exit(r0)     // Catch:{ all -> 0x0062 }
            java.util.Map<java.lang.String, android.accounts.AuthenticatorDescription> r0 = r5.mTypeToAuthDescription
            boolean r0 = r0.containsKey(r7)
            r1 = 0
            if (r0 == 0) goto L_0x004f
            java.util.Map<java.lang.String, android.accounts.AuthenticatorDescription> r0 = r5.mTypeToAuthDescription     // Catch:{ NameNotFoundException | NotFoundException -> 0x004f }
            java.lang.Object r0 = r0.get(r7)     // Catch:{ NameNotFoundException | NotFoundException -> 0x004f }
            android.accounts.AuthenticatorDescription r0 = (android.accounts.AuthenticatorDescription) r0     // Catch:{ NameNotFoundException | NotFoundException -> 0x004f }
            java.lang.String r2 = r0.packageName     // Catch:{ NameNotFoundException | NotFoundException -> 0x004f }
            r3 = 0
            android.os.UserHandle r4 = r5.mUserHandle     // Catch:{ NameNotFoundException | NotFoundException -> 0x004f }
            android.content.Context r2 = r6.createPackageContextAsUser(r2, r3, r4)     // Catch:{ NameNotFoundException | NotFoundException -> 0x004f }
            android.content.Context r3 = r5.mContext     // Catch:{ NameNotFoundException | NotFoundException -> 0x004f }
            android.content.pm.PackageManager r3 = r3.getPackageManager()     // Catch:{ NameNotFoundException | NotFoundException -> 0x004f }
            int r0 = r0.iconId     // Catch:{ NameNotFoundException | NotFoundException -> 0x004f }
            android.graphics.drawable.Drawable r0 = r2.getDrawable(r0)     // Catch:{ NameNotFoundException | NotFoundException -> 0x004f }
            android.os.UserHandle r2 = r5.mUserHandle     // Catch:{ NameNotFoundException | NotFoundException -> 0x004f }
            android.graphics.drawable.Drawable r1 = r3.getUserBadgedIcon(r0, r2)     // Catch:{ NameNotFoundException | NotFoundException -> 0x004f }
            java.util.Map<java.lang.String, android.graphics.drawable.Drawable> r0 = r5.mAccTypeIconCache     // Catch:{ NameNotFoundException | NotFoundException -> 0x004f }
            monitor-enter(r0)     // Catch:{ NameNotFoundException | NotFoundException -> 0x004f }
            java.util.Map<java.lang.String, android.graphics.drawable.Drawable> r2 = r5.mAccTypeIconCache     // Catch:{ all -> 0x004c }
            r2.put(r7, r1)     // Catch:{ all -> 0x004c }
            monitor-exit(r0)     // Catch:{ all -> 0x004c }
            goto L_0x004f
        L_0x004c:
            r7 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x004c }
            throw r7     // Catch:{ NameNotFoundException | NotFoundException -> 0x004f }
        L_0x004f:
            if (r1 != 0) goto L_0x0059
            android.content.pm.PackageManager r6 = r6.getPackageManager()
            android.graphics.drawable.Drawable r1 = r6.getDefaultActivityIcon()
        L_0x0059:
            android.content.Context r6 = r5.mContext
            android.os.UserHandle r5 = r5.mUserHandle
            android.graphics.drawable.Drawable r5 = com.android.settingslib.Utils.getBadgedIcon(r6, r1, r5)
            return r5
        L_0x0062:
            r5 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0062 }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.accounts.AuthenticatorHelper.getDrawableForType(android.content.Context, java.lang.String):android.graphics.drawable.Drawable");
    }

    public CharSequence getLabelForType(Context context, String str) {
        if (this.mTypeToAuthDescription.containsKey(str)) {
            try {
                AuthenticatorDescription authenticatorDescription = this.mTypeToAuthDescription.get(str);
                return context.createPackageContextAsUser(authenticatorDescription.packageName, 0, this.mUserHandle).getResources().getText(authenticatorDescription.labelId);
            } catch (PackageManager.NameNotFoundException unused) {
                Log.w("AuthenticatorHelper", "No label name for account type " + str);
            } catch (Resources.NotFoundException unused2) {
                Log.w("AuthenticatorHelper", "No label icon for account type " + str);
            }
        }
        return null;
    }

    public String getPackageForType(String str) {
        if (this.mTypeToAuthDescription.containsKey(str)) {
            return this.mTypeToAuthDescription.get(str).packageName;
        }
        return null;
    }

    public int getLabelIdForType(String str) {
        if (this.mTypeToAuthDescription.containsKey(str)) {
            return this.mTypeToAuthDescription.get(str).labelId;
        }
        return -1;
    }

    public void updateAuthDescriptions(Context context) {
        AuthenticatorDescription[] authenticatorTypesAsUser = AccountManager.get(context).getAuthenticatorTypesAsUser(this.mUserHandle.getIdentifier());
        for (int i = 0; i < authenticatorTypesAsUser.length; i++) {
            this.mTypeToAuthDescription.put(authenticatorTypesAsUser[i].type, authenticatorTypesAsUser[i]);
        }
    }

    public boolean containsAccountType(String str) {
        return this.mTypeToAuthDescription.containsKey(str);
    }

    public AuthenticatorDescription getAccountTypeDescription(String str) {
        return this.mTypeToAuthDescription.get(str);
    }

    /* access modifiers changed from: package-private */
    public void onAccountsUpdated(Account[] accountArr) {
        updateAuthDescriptions(this.mContext);
        if (accountArr == null) {
            accountArr = AccountManager.get(this.mContext).getAccountsAsUser(this.mUserHandle.getIdentifier());
        }
        this.mEnabledAccountTypes.clear();
        this.mAccTypeIconCache.clear();
        for (Account account : accountArr) {
            if (!this.mEnabledAccountTypes.contains(account.type)) {
                this.mEnabledAccountTypes.add(account.type);
            }
        }
        buildAccountTypeToAuthoritiesMap();
        if (this.mListeningToAccountUpdates) {
            this.mListener.onAccountsUpdate(this.mUserHandle);
        }
    }

    public void onReceive(Context context, Intent intent) {
        onAccountsUpdated(AccountManager.get(this.mContext).getAccountsAsUser(this.mUserHandle.getIdentifier()));
    }

    public void listenToAccountUpdates() {
        if (!this.mListeningToAccountUpdates) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.accounts.LOGIN_ACCOUNTS_CHANGED");
            intentFilter.addAction("android.intent.action.DEVICE_STORAGE_OK");
            this.mContext.registerReceiverAsUser(this, this.mUserHandle, intentFilter, (String) null, (Handler) null);
            this.mListeningToAccountUpdates = true;
        }
    }

    public void stopListeningToAccountUpdates() {
        if (this.mListeningToAccountUpdates) {
            this.mContext.unregisterReceiver(this);
            this.mListeningToAccountUpdates = false;
        }
    }

    public ArrayList<String> getAuthoritiesForAccountType(String str) {
        return this.mAccountTypeToAuthorities.get(str);
    }

    private void buildAccountTypeToAuthoritiesMap() {
        this.mAccountTypeToAuthorities.clear();
        for (SyncAdapterType syncAdapterType : ContentResolver.getSyncAdapterTypesAsUser(this.mUserHandle.getIdentifier())) {
            ArrayList arrayList = this.mAccountTypeToAuthorities.get(syncAdapterType.accountType);
            if (arrayList == null) {
                arrayList = new ArrayList();
                this.mAccountTypeToAuthorities.put(syncAdapterType.accountType, arrayList);
            }
            if (Log.isLoggable("AuthenticatorHelper", 2)) {
                Log.v("AuthenticatorHelper", "Added authority " + syncAdapterType.authority + " to accountType " + syncAdapterType.accountType);
            }
            arrayList.add(syncAdapterType.authority);
        }
    }
}
