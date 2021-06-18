package com.google.android.settings.core.instrumentation;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.LogWriter;
import com.android.settingslib.utils.ThreadUtils;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SearchResultTraceLogWriter implements LogWriter {
    static final int DASHBOARD_SEARCH_RESULTS = 34;
    static final String KEY_LOG_TO_DATABASE_ENABLED = "log_to_database_enabled";
    static final int OFF = 0;

    /* renamed from: ON */
    static final int f126ON = 1;

    public void action(Context context, int i, int i2) {
    }

    public void action(Context context, int i, String str) {
    }

    public void action(Context context, int i, boolean z) {
    }

    public void action(Context context, int i, Pair<Integer, Object>... pairArr) {
    }

    public void hidden(Context context, int i, int i2) {
    }

    public void visible(Context context, int i, int i2, int i3) {
        if (i == 34) {
            setLogToDatabaseEnabled(true);
        }
    }

    public void action(int i, int i2, int i3, String str, int i4) {
        ContentProviderClient acquireUnstableContentProviderClient;
        if (isLogToDatabaseEnabled()) {
            Uri build = new Uri.Builder().scheme("content").authority("com.google.android.settings.intelligence.modules.search.logging").build();
            ContentValues generateContentValues = generateContentValues(i, i2, i3, str, i4);
            try {
                acquireUnstableContentProviderClient = FeatureFactory.getAppContext().getContentResolver().acquireUnstableContentProviderClient(build);
                if (acquireUnstableContentProviderClient == null) {
                    Log.w("SRTraceLogWriter", "Client not found. Skipping logging.");
                    setLogToDatabaseEnabled(false);
                    if (acquireUnstableContentProviderClient != null) {
                        acquireUnstableContentProviderClient.close();
                        return;
                    }
                    return;
                }
                ThreadUtils.postOnBackgroundThread((Runnable) new SearchResultTraceLogWriter$$ExternalSyntheticLambda0(acquireUnstableContentProviderClient, build, generateContentValues));
                acquireUnstableContentProviderClient.close();
                return;
            } catch (Exception e) {
                setLogToDatabaseEnabled(false);
                Log.w("SRTraceLogWriter", "Unable to send logs. Skipping.", e);
                return;
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
        } else {
            return;
        }
        throw th;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$action$0(ContentProviderClient contentProviderClient, Uri uri, ContentValues contentValues) {
        try {
            contentProviderClient.insert(uri, contentValues);
        } catch (RemoteException e) {
            Log.w("SRTraceLogWriter", "Unable to send logs.", e);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isLogToDatabaseEnabled() {
        Context appContext = FeatureFactory.getAppContext();
        if (appContext != null && Settings.Global.getInt(appContext.getContentResolver(), KEY_LOG_TO_DATABASE_ENABLED, 0) == 1) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void setLogToDatabaseEnabled(boolean z) {
        Context appContext = FeatureFactory.getAppContext();
        if (appContext == null) {
            Log.w("SRTraceLogWriter", "Context not found.");
        } else {
            Settings.Global.putInt(appContext.getContentResolver(), KEY_LOG_TO_DATABASE_ENABLED, z ? 1 : 0);
        }
    }

    /* access modifiers changed from: package-private */
    public ContentValues generateContentValues(int i, int i2, int i3, String str, int i4) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("created_time", Long.valueOf(Timestamp.from(ZonedDateTime.now(ZoneId.systemDefault()).toInstant()).getTime()));
        contentValues.put("parent_page", Integer.valueOf(i));
        contentValues.put("action", Integer.valueOf(i2));
        contentValues.put("current_page", Integer.valueOf(i3));
        contentValues.put("pref_key", str);
        contentValues.put("value", Integer.valueOf(i4));
        return contentValues;
    }
}
