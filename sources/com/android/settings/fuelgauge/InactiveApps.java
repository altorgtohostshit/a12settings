package com.android.settings.fuelgauge;

import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import java.util.Arrays;

public class InactiveApps extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {
    private static final CharSequence[] FULL_SETTABLE_BUCKETS_NAMES;
    private static final CharSequence[] FULL_SETTABLE_BUCKETS_VALUES;
    private static final CharSequence[] REDUCED_SETTABLE_BUCKETS_NAMES;
    private static final CharSequence[] REDUCED_SETTABLE_BUCKETS_VALUES;
    private UsageStatsManager mUsageStats;

    static String bucketToName(int i) {
        return i != 5 ? i != 10 ? i != 20 ? i != 30 ? i != 40 ? i != 45 ? i != 50 ? "" : "NEVER" : "RESTRICTED" : "RARE" : "FREQUENT" : "WORKING_SET" : "ACTIVE" : "EXEMPTED";
    }

    public int getMetricsCategory() {
        return 238;
    }

    static {
        CharSequence[] charSequenceArr = {"ACTIVE", "WORKING_SET", "FREQUENT", "RARE", "RESTRICTED"};
        FULL_SETTABLE_BUCKETS_NAMES = charSequenceArr;
        REDUCED_SETTABLE_BUCKETS_NAMES = (CharSequence[]) Arrays.copyOfRange(charSequenceArr, 0, 4);
        CharSequence[] charSequenceArr2 = {Integer.toString(10), Integer.toString(20), Integer.toString(30), Integer.toString(40), Integer.toString(45)};
        FULL_SETTABLE_BUCKETS_VALUES = charSequenceArr2;
        REDUCED_SETTABLE_BUCKETS_VALUES = (CharSequence[]) Arrays.copyOfRange(charSequenceArr2, 0, 4);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mUsageStats = (UsageStatsManager) getActivity().getSystemService(UsageStatsManager.class);
        addPreferencesFromResource(R.xml.placeholder_preference_screen);
        getActivity().setTitle(R.string.inactive_apps_title);
    }

    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        preferenceScreen.removeAll();
        preferenceScreen.setOrderingAsAdded(false);
        FragmentActivity activity = getActivity();
        PackageManager packageManager = activity.getPackageManager();
        String packageName = activity.getPackageName();
        boolean z = true;
        if (Settings.Global.getInt(getContentResolver(), "enable_restricted_bucket", 1) != 1) {
            z = false;
        }
        CharSequence[] charSequenceArr = z ? FULL_SETTABLE_BUCKETS_NAMES : REDUCED_SETTABLE_BUCKETS_NAMES;
        CharSequence[] charSequenceArr2 = z ? FULL_SETTABLE_BUCKETS_VALUES : REDUCED_SETTABLE_BUCKETS_VALUES;
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        for (ResolveInfo next : packageManager.queryIntentActivities(intent, 0)) {
            String str = next.activityInfo.applicationInfo.packageName;
            ListPreference listPreference = new ListPreference(getPrefContext());
            listPreference.setTitle(next.loadLabel(packageManager));
            listPreference.setIcon(next.loadIcon(packageManager));
            listPreference.setKey(str);
            listPreference.setEntries(charSequenceArr);
            listPreference.setEntryValues(charSequenceArr2);
            updateSummary(listPreference);
            if (TextUtils.equals(str, packageName)) {
                listPreference.setEnabled(false);
            }
            listPreference.setOnPreferenceChangeListener(this);
            preferenceScreen.addPreference(listPreference);
        }
    }

    private void updateSummary(ListPreference listPreference) {
        Resources resources = getActivity().getResources();
        int appStandbyBucket = this.mUsageStats.getAppStandbyBucket(listPreference.getKey());
        boolean z = true;
        listPreference.setSummary(resources.getString(R.string.standby_bucket_summary, new Object[]{bucketToName(appStandbyBucket)}));
        if (appStandbyBucket < 10 || appStandbyBucket > 45) {
            z = false;
        }
        if (z) {
            listPreference.setValue(Integer.toString(appStandbyBucket));
        }
        listPreference.setEnabled(z);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        this.mUsageStats.setAppStandbyBucket(preference.getKey(), Integer.parseInt((String) obj));
        updateSummary((ListPreference) preference);
        return false;
    }
}
