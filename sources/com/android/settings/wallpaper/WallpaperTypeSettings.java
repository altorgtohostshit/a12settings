package com.android.settings.wallpaper;

import androidx.constraintlayout.widget.R$styleable;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;

public class WallpaperTypeSettings extends DashboardFragment {
    public int getHelpResource() {
        return R.string.help_uri_wallpaper;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "WallpaperTypeSettings";
    }

    public int getMetricsCategory() {
        return R$styleable.Constraint_layout_goneMarginRight;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.wallpaper_settings;
    }
}
