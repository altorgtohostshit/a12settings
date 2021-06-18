package com.google.android.settings.gestures.columbus;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.widget.LayoutPreference;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ColumbusGestureLaunchSettingsFragment extends DashboardFragment {
    private Context mContext;
    private int mIconSizePx;
    private LauncherApps mLauncherApps;
    private MetricsFeatureProvider mMetricsFeatureProvider;
    private RecyclerView mRecyclerView;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "ColumbusLaunchSettings";
    }

    public int getMetricsCategory() {
        return 1871;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.columbus_launch_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
        this.mContext = context;
        this.mLauncherApps = (LauncherApps) context.getSystemService(LauncherApps.class);
        this.mIconSizePx = context.getResources().getDimensionPixelSize(R.dimen.columbus_app_icon_size);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        LayoutPreference layoutPreference = (LayoutPreference) findPreference("columbus_launch_app");
        TextView textView = (TextView) layoutPreference.findViewById(R.id.help_text);
        this.mRecyclerView = (RecyclerView) layoutPreference.findViewById(R.id.apps);
    }

    public void onResume() {
        super.onResume();
        String string = Settings.Secure.getString(this.mContext.getContentResolver(), "columbus_launch_app");
        PackageManager packageManager = this.mContext.getPackageManager();
        List list = (List) packageManager.getInstalledApplications(128).stream().filter(new ColumbusGestureLaunchSettingsFragment$$ExternalSyntheticLambda1(packageManager)).sorted(Comparator.comparing(new ColumbusGestureLaunchSettingsFragment$$ExternalSyntheticLambda0(packageManager))).collect(Collectors.toList());
        ArrayList arrayList = new ArrayList();
        int i = -1;
        for (int i2 = 0; i2 < list.size(); i2++) {
            ApplicationInfo applicationInfo = (ApplicationInfo) list.get(i2);
            arrayList.add(queryForShortcuts(applicationInfo));
            if (applicationInfo.packageName.equals(string)) {
                i = i2;
            }
        }
        this.mRecyclerView.setNestedScrollingEnabled(false);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
        this.mRecyclerView.setAdapter(new ColumbusAppRecyclerViewAdapter(this.mContext, list, arrayList, i));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onResume$0(PackageManager packageManager, ApplicationInfo applicationInfo) {
        Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage(applicationInfo.packageName);
        if (launchIntentForPackage == null) {
            return false;
        }
        return launchIntentForPackage.getCategories().contains("android.intent.category.LAUNCHER");
    }

    private List<ShortcutInfo> queryForShortcuts(ApplicationInfo applicationInfo) {
        LauncherApps.ShortcutQuery shortcutQuery = new LauncherApps.ShortcutQuery();
        shortcutQuery.setQueryFlags(9);
        shortcutQuery.setPackage(applicationInfo.packageName);
        try {
            return this.mLauncherApps.getShortcuts(shortcutQuery, UserHandle.getUserHandleForUid(0));
        } catch (IllegalStateException | SecurityException e) {
            Log.e("ColumbusLaunchSettings", "Failed to query for shortcuts", e);
            return null;
        }
    }
}
