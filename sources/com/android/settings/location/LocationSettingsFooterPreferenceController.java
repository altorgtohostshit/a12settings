package com.android.settings.location;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.widget.FooterPreference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocationSettingsFooterPreferenceController extends LocationBasePreferenceController {
    private static final Intent INJECT_INTENT = new Intent("com.android.settings.location.DISPLAYED_FOOTER");
    private static final String TAG = "LocationFooter";
    private FooterPreference mFooterPreference;
    private String mInjectedFooterString;
    private boolean mLocationEnabled;
    private final PackageManager mPackageManager;

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

    public LocationSettingsFooterPreferenceController(Context context, String str) {
        super(context, str);
        this.mPackageManager = context.getPackageManager();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mFooterPreference = (FooterPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public void onLocationModeChanged(int i, boolean z) {
        this.mLocationEnabled = this.mLocationEnabler.isEnabled(i);
        updateFooterPreference();
    }

    public void updateState(Preference preference) {
        for (FooterData next : getFooterData()) {
            try {
                this.mInjectedFooterString = this.mPackageManager.getResourcesForApplication(next.applicationInfo).getString(next.footerStringRes);
                updateFooterPreference();
            } catch (PackageManager.NameNotFoundException unused) {
                Log.w(TAG, "Resources not found for application " + next.applicationInfo.packageName);
            }
        }
    }

    private void updateFooterPreference() {
        String string = this.mContext.getString(this.mLocationEnabled ? R.string.location_settings_footer_location_on : R.string.location_settings_footer_location_off);
        if (this.mLocationEnabled) {
            string = this.mInjectedFooterString + string;
        }
        FooterPreference footerPreference = this.mFooterPreference;
        if (footerPreference != null) {
            footerPreference.setTitle((CharSequence) Html.fromHtml(string));
        }
    }

    public int getAvailabilityStatus() {
        return !getFooterData().isEmpty() ? 0 : 3;
    }

    private List<FooterData> getFooterData() {
        PackageManager packageManager = this.mPackageManager;
        Intent intent = INJECT_INTENT;
        List<ResolveInfo> queryBroadcastReceivers = packageManager.queryBroadcastReceivers(intent, 128);
        if (queryBroadcastReceivers == null) {
            Log.e(TAG, "Unable to resolve intent " + intent);
            return Collections.emptyList();
        }
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "Found broadcast receivers: " + queryBroadcastReceivers);
        }
        ArrayList arrayList = new ArrayList(queryBroadcastReceivers.size());
        for (ResolveInfo next : queryBroadcastReceivers) {
            ActivityInfo activityInfo = next.activityInfo;
            ApplicationInfo applicationInfo = activityInfo.applicationInfo;
            if ((applicationInfo.flags & 1) == 0) {
                Log.w(TAG, "Ignoring attempt to inject footer from app not in system image: " + next);
            } else {
                Bundle bundle = activityInfo.metaData;
                if (bundle != null) {
                    int i = bundle.getInt("com.android.settings.location.FOOTER_STRING");
                    if (i == 0) {
                        Log.w(TAG, "No mapping of integer exists for com.android.settings.location.FOOTER_STRING");
                    } else {
                        arrayList.add(new FooterData(i, applicationInfo));
                    }
                } else if (Log.isLoggable(TAG, 3)) {
                    Log.d(TAG, "No METADATA in broadcast receiver " + activityInfo.name);
                }
            }
        }
        return arrayList;
    }

    private static class FooterData {
        public final ApplicationInfo applicationInfo;
        public final int footerStringRes;

        FooterData(int i, ApplicationInfo applicationInfo2) {
            this.footerStringRes = i;
            this.applicationInfo = applicationInfo2;
        }
    }
}
