package com.android.settings.panel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.constraintlayout.widget.R$styleable;
import com.android.settings.R;
import com.android.settings.SubSettings;
import com.android.settings.slices.CustomSliceRegistry;
import com.android.settings.slices.SliceBuilderUtils;
import com.android.settings.wifi.WifiSettings;
import java.util.ArrayList;
import java.util.List;

public class WifiPanel implements PanelContent {
    private final Context mContext;

    public int getMetricsCategory() {
        return 1687;
    }

    public static WifiPanel create(Context context) {
        return new WifiPanel(context);
    }

    private WifiPanel(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public CharSequence getTitle() {
        return this.mContext.getText(R.string.wifi_settings);
    }

    public List<Uri> getSlices() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(CustomSliceRegistry.WIFI_SLICE_URI);
        return arrayList;
    }

    public Intent getSeeMoreIntent() {
        Intent buildSearchResultPageIntent = SliceBuilderUtils.buildSearchResultPageIntent(this.mContext, WifiSettings.class.getName(), (String) null, this.mContext.getText(R.string.wifi_settings).toString(), R$styleable.Constraint_layout_goneMarginTop);
        buildSearchResultPageIntent.setClassName(this.mContext.getPackageName(), SubSettings.class.getName());
        buildSearchResultPageIntent.addFlags(268435456);
        return buildSearchResultPageIntent;
    }
}
