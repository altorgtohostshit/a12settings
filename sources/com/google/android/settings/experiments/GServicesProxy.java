package com.google.android.settings.experiments;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GServicesProxy {
    private static final Uri PROXY_AUTHORITY = new Uri.Builder().scheme("content").authority("com.google.android.settings.intelligence.provider.serviceflags").build();

    public static boolean getBoolean(ContentResolver contentResolver, String str, boolean z) {
        Bundle buildRequest = buildRequest(str);
        buildRequest.putBoolean("default", z);
        Bundle result = getResult(contentResolver, "getBooleanForKey", buildRequest);
        if (result == null) {
            return z;
        }
        return result.getBoolean("value", z);
    }

    public static String getString(ContentResolver contentResolver, String str, String str2) {
        Bundle buildRequest = buildRequest(str);
        Bundle result = getResult(contentResolver, "getStringForKey", buildRequest);
        buildRequest.putString("default", str2);
        if (result == null) {
            return str2;
        }
        return result.getString("value", str2);
    }

    public static long getLong(ContentResolver contentResolver, String str, long j) {
        Bundle buildRequest = buildRequest(str);
        buildRequest.putLong("default", j);
        Bundle result = getResult(contentResolver, "getLongForKey", buildRequest);
        if (result == null) {
            return j;
        }
        return result.getLong("value", j);
    }

    private static Bundle buildRequest(String str) {
        Bundle bundle = new Bundle();
        bundle.putString("key", str);
        return bundle;
    }

    private static Bundle getResult(ContentResolver contentResolver, String str, Bundle bundle) {
        try {
            FutureTask futureTask = new FutureTask(new GServicesProxy$$ExternalSyntheticLambda0(contentResolver, str, bundle));
            Executors.newSingleThreadExecutor().submit(futureTask);
            return (Bundle) futureTask.get(100, TimeUnit.MILLISECONDS);
        } catch (TimeoutException unused) {
            Log.w("GServicesProxy", "Timeout to query service flag provider for method " + str);
            return null;
        } catch (Exception e) {
            Log.e("GServicesProxy", "Failed to query service flag provider for method " + str, e);
            return null;
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Bundle lambda$getResult$0(ContentResolver contentResolver, String str, Bundle bundle) throws Exception {
        ContentProviderClient acquireUnstableContentProviderClient = contentResolver.acquireUnstableContentProviderClient(PROXY_AUTHORITY);
        try {
            Bundle call = acquireUnstableContentProviderClient.call(str, (String) null, bundle);
            acquireUnstableContentProviderClient.close();
            return call;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }
}
