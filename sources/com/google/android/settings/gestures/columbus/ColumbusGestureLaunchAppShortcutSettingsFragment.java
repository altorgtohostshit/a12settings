package com.google.android.settings.gestures.columbus;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.widget.LayoutPreference;
import java.util.List;

public class ColumbusGestureLaunchAppShortcutSettingsFragment extends DashboardFragment {
    private ApplicationInfo mApplicationInfo;
    private Context mContext;
    private int mIconSizePx;
    private MetricsFeatureProvider mMetricsFeatureProvider;
    private RecyclerView mRecyclerView;
    private List<ShortcutInfo> mShortcutInfos;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "ColumbusAppShortcutSettings";
    }

    public int getMetricsCategory() {
        return 1872;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.columbus_launch_app_shortcut_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
        this.mContext = context;
        this.mIconSizePx = context.getResources().getDimensionPixelSize(R.dimen.columbus_app_icon_size);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mApplicationInfo = (ApplicationInfo) getIntent().getParcelableExtra("columbus_launch_app_info");
        this.mShortcutInfos = getIntent().getParcelableArrayListExtra("columbus_app_shortcuts");
        this.mRecyclerView = (RecyclerView) ((LayoutPreference) findPreference("columbus_launch_app_shortcut")).findViewById(R.id.app_shortcuts);
    }

    public void onResume() {
        int i;
        int i2;
        super.onResume();
        String string = Settings.Secure.getString(this.mContext.getContentResolver(), "columbus_launch_app_shortcut");
        if (string == null || !string.equals(this.mApplicationInfo.packageName)) {
            int i3 = 0;
            while (true) {
                if (i3 >= this.mShortcutInfos.size()) {
                    i2 = -1;
                    break;
                } else if (this.mShortcutInfos.get(i3).getId().equals(string)) {
                    i2 = i3 + 1;
                    break;
                } else {
                    i3++;
                }
            }
            i = i2;
        } else {
            i = 0;
        }
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
        Drawable applicationIcon = this.mContext.getPackageManager().getApplicationIcon(this.mApplicationInfo);
        int i4 = this.mIconSizePx;
        applicationIcon.setBounds(0, 0, i4, i4);
        this.mRecyclerView.setAdapter(new ColumbusAppShortcutRecyclerViewAdapter(this.mContext, this.mShortcutInfos, this.mApplicationInfo.packageName, applicationIcon, i));
    }
}
