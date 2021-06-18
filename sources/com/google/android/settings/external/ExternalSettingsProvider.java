package com.google.android.settings.external;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Binder;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceData;
import com.android.settings.slices.SlicesDatabaseAccessor;
import com.android.settingslib.core.instrumentation.SharedPreferencesLogger;
import com.google.android.settings.support.PsdBundle;
import com.google.android.settings.support.PsdValuesLoader;

public class ExternalSettingsProvider extends ContentProvider {
    private final int CODE_SETTINGS_MANAGER = 1;
    private final int CODE_SETTINGS_SIGNALS = 2;
    private final String TAG = "ExternalSettingProvider";
    private SlicesDatabaseAccessor mDatabaseAccessor;
    private UriMatcher mMatcher;

    public boolean onCreate() {
        this.mDatabaseAccessor = new SlicesDatabaseAccessor(getContext());
        return true;
    }

    public final Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        String verifyPermission = verifyPermission();
        int match = this.mMatcher.match(uri);
        if (match == 1) {
            return querySettings(getContext(), verifyPermission, uri);
        }
        if (match == 2) {
            return collectDeviceSignals(verifyPermission, uri);
        }
        throw new IllegalArgumentException("Unknown Uri: " + uri);
    }

    public final void attachInfo(Context context, ProviderInfo providerInfo) {
        UriMatcher uriMatcher = new UriMatcher(-1);
        this.mMatcher = uriMatcher;
        uriMatcher.addURI("com.google.android.settings.external", "settings_manager/*", 1);
        this.mMatcher.addURI("com.google.android.settings.external", "signals", 2);
        if (!providerInfo.exported) {
            throw new SecurityException("Provider must be exported");
        } else if (providerInfo.grantUriPermissions) {
            super.attachInfo(context, providerInfo);
        } else {
            throw new SecurityException("Provider must grantUriPermissions");
        }
    }

    private Cursor querySettings(Context context, String str, Uri uri) {
        SliceData sliceData = null;
        if (!isSettingsAccessApiEnabled()) {
            Log.i("ExternalSettingProvider", "Settings API disabled by gservices flag");
            return null;
        }
        String lastPathSegment = uri.getLastPathSegment();
        String newSettingValueQueryParameter = ExternalSettingsManager.getNewSettingValueQueryParameter(uri);
        try {
            sliceData = this.mDatabaseAccessor.getSliceDataFromKey(lastPathSegment);
        } catch (IllegalStateException unused) {
            Log.w("ExternalSettingProvider", "Can't find slice key, defaulting to null");
        }
        if (TextUtils.isEmpty(newSettingValueQueryParameter)) {
            return ExternalSettingsManager.getAccessCursorForSpecialSetting(context, str, lastPathSegment, sliceData);
        }
        return ExternalSettingsManager.getUpdateCursorForSpecialSetting(context, str, lastPathSegment, newSettingValueQueryParameter, sliceData);
    }

    private Cursor collectDeviceSignals(String str, Uri uri) {
        if (!isSignalsApiEnabled()) {
            Log.i("ExternalSettingProvider", "Signals API disabled by gservices flag");
            return null;
        }
        MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.DEVICE_SIGNALS_COLUMNS);
        PsdBundle psdBundle = getPsdBundle(uri);
        String[] keys = psdBundle.getKeys();
        String[] values = psdBundle.getValues();
        int length = keys.length;
        for (int i = 0; i < length; i++) {
            matrixCursor.newRow().add("signal_key", keys[i]).add("signal_value", values[i]);
        }
        Context context = getContext();
        FeatureFactory.getFactory(context).getMetricsFeatureProvider().action(0, 853, 0, SharedPreferencesLogger.buildPrefKey(str, "/signal"), 0);
        return matrixCursor;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public PsdBundle getPsdBundle(Uri uri) {
        return PsdValuesLoader.makePsdBundle(getContext(), 2);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public String verifyPermission() throws SecurityException {
        return SignatureVerifier.verifyCallerIsAllowlisted(getContext(), Binder.getCallingUid());
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean isSignalsApiEnabled() {
        try {
            return Settings.Global.getInt(getContext().getContentResolver(), "settings_use_psd_api", 0) != 0;
        } catch (Exception e) {
            Log.w("ExternalSettingProvider", "Error reading psd api enabled flag", e);
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean isSettingsAccessApiEnabled() {
        try {
            return Settings.Global.getInt(getContext().getContentResolver(), "settings_use_external_provider_api", 0) != 0;
        } catch (Exception e) {
            Log.w("ExternalSettingProvider", "Error reading settings access api enabled flag", e);
            return false;
        }
    }

    public final int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        throw new UnsupportedOperationException("Update not supported");
    }

    public final String getType(Uri uri) {
        throw new UnsupportedOperationException("MIME types not supported");
    }

    public final Uri insert(Uri uri, ContentValues contentValues) {
        throw new UnsupportedOperationException("Insert not supported");
    }

    public final int delete(Uri uri, String str, String[] strArr) {
        throw new UnsupportedOperationException("Delete not supported");
    }
}
