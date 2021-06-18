package com.android.settings.deviceinfo.firmwareversion;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

public class MainlineModuleVersionPreferenceController extends BasePreferenceController {
    static final Intent MODULE_UPDATE_INTENT = new Intent("android.settings.MODULE_UPDATE_SETTINGS");
    static final Intent MODULE_UPDATE_V2_INTENT = new Intent("android.settings.MODULE_UPDATE_VERSIONS");
    private static final String TAG = "MainlineModuleControl";
    private static final List<String> VERSION_NAME_DATE_PATTERNS = Arrays.asList(new String[]{"yyyy-MM-dd", "yyyy-MM"});
    private String mModuleVersion;
    private final PackageManager mPackageManager = this.mContext.getPackageManager();

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
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

    public MainlineModuleVersionPreferenceController(Context context, String str) {
        super(context, str);
        initModules();
    }

    public int getAvailabilityStatus() {
        return !TextUtils.isEmpty(this.mModuleVersion) ? 0 : 3;
    }

    private void initModules() {
        String string = this.mContext.getString(17039897);
        if (!TextUtils.isEmpty(string)) {
            try {
                this.mModuleVersion = this.mPackageManager.getPackageInfo(string, 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Failed to get mainline version.", e);
                this.mModuleVersion = null;
            }
        }
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        PackageManager packageManager = this.mPackageManager;
        Intent intent = MODULE_UPDATE_V2_INTENT;
        if (packageManager.resolveActivity(intent, 0) != null) {
            preference.setIntent(intent);
            preference.setSelectable(true);
            return;
        }
        PackageManager packageManager2 = this.mPackageManager;
        Intent intent2 = MODULE_UPDATE_INTENT;
        if (packageManager2.resolveActivity(intent2, 0) != null) {
            preference.setIntent(intent2);
            preference.setSelectable(true);
            return;
        }
        Log.d(TAG, "The ResolveInfo of the update intent is null.");
        preference.setIntent((Intent) null);
        preference.setSelectable(false);
    }

    public CharSequence getSummary() {
        if (TextUtils.isEmpty(this.mModuleVersion)) {
            return this.mModuleVersion;
        }
        Optional<Date> parseDateFromVersionName = parseDateFromVersionName(this.mModuleVersion);
        if (parseDateFromVersionName.isPresent()) {
            return DateFormat.getLongDateFormat(this.mContext).format(parseDateFromVersionName.get());
        }
        Log.w("Could not parse mainline versionName (%s) as date.", this.mModuleVersion);
        return this.mModuleVersion;
    }

    private Optional<Date> parseDateFromVersionName(String str) {
        for (String simpleDateFormat : VERSION_NAME_DATE_PATTERNS) {
            try {
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(simpleDateFormat, Locale.getDefault());
                simpleDateFormat2.setTimeZone(TimeZone.getDefault());
                return Optional.of(simpleDateFormat2.parse(str));
            } catch (ParseException unused) {
            }
        }
        return Optional.empty();
    }
}
