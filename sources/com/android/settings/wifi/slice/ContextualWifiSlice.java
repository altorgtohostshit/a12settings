package com.android.settings.wifi.slice;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.text.TextUtils;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.builders.ListBuilder;
import com.android.settings.R;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.CustomSliceRegistry;
import com.android.settingslib.Utils;

public class ContextualWifiSlice extends WifiSlice {
    static final int COLLAPSED_ROW_COUNT = 0;
    static long sActiveUiSession = -1000;
    static boolean sApRowCollapsed;
    private final ConnectivityManager mConnectivityManager = ((ConnectivityManager) this.mContext.getSystemService(ConnectivityManager.class));

    public ContextualWifiSlice(Context context) {
        super(context);
    }

    public Uri getUri() {
        return CustomSliceRegistry.CONTEXTUAL_WIFI_SLICE_URI;
    }

    public Slice getSlice() {
        long uiSessionToken = FeatureFactory.getFactory(this.mContext).getSlicesFeatureProvider().getUiSessionToken();
        if (uiSessionToken != sActiveUiSession) {
            sActiveUiSession = uiSessionToken;
            sApRowCollapsed = hasWorkingNetwork();
        } else if (!this.mWifiManager.isWifiEnabled()) {
            sApRowCollapsed = false;
        }
        return super.getSlice();
    }

    static int getApRowCount() {
        return sApRowCollapsed ? 0 : 3;
    }

    /* access modifiers changed from: protected */
    public boolean isApRowCollapsed() {
        return sApRowCollapsed;
    }

    /* access modifiers changed from: protected */
    public ListBuilder.RowBuilder getHeaderRow(boolean z, WifiSliceItem wifiSliceItem) {
        ListBuilder.RowBuilder headerRow = super.getHeaderRow(z, wifiSliceItem);
        headerRow.setTitleItem(getHeaderIcon(z, wifiSliceItem), 0);
        if (sApRowCollapsed) {
            headerRow.setSubtitle(getHeaderSubtitle(wifiSliceItem));
        }
        return headerRow;
    }

    private IconCompat getHeaderIcon(boolean z, WifiSliceItem wifiSliceItem) {
        Drawable drawable;
        int i;
        if (!z) {
            drawable = this.mContext.getDrawable(R.drawable.ic_wifi_off);
            Context context = this.mContext;
            i = Utils.getDisabled(context, Utils.getColorAttrDefaultColor(context, 16843817));
        } else {
            drawable = this.mContext.getDrawable(Utils.getWifiIconResource(2));
            if (wifiSliceItem == null || wifiSliceItem.getConnectedState() != 2) {
                i = Utils.getColorAttrDefaultColor(this.mContext, 16843817);
            } else {
                i = Utils.getColorAccentDefaultColor(this.mContext);
            }
        }
        drawable.setTint(i);
        return com.android.settings.Utils.createIconWithDrawable(drawable);
    }

    private CharSequence getHeaderSubtitle(WifiSliceItem wifiSliceItem) {
        if (wifiSliceItem == null || wifiSliceItem.getConnectedState() == 0) {
            return this.mContext.getText(R.string.disconnected);
        }
        if (wifiSliceItem.getConnectedState() == 1) {
            return this.mContext.getString(R.string.wifi_connecting_to_message, new Object[]{wifiSliceItem.getTitle()});
        }
        return this.mContext.getString(R.string.wifi_connected_to_message, new Object[]{wifiSliceItem.getTitle()});
    }

    private boolean hasWorkingNetwork() {
        return !TextUtils.equals(getActiveSSID(), "<unknown ssid>") && hasInternetAccess();
    }

    private String getActiveSSID() {
        if (this.mWifiManager.getWifiState() != 3) {
            return "<unknown ssid>";
        }
        return WifiInfo.sanitizeSsid(this.mWifiManager.getConnectionInfo().getSSID());
    }

    private boolean hasInternetAccess() {
        NetworkCapabilities networkCapabilities = this.mConnectivityManager.getNetworkCapabilities(this.mWifiManager.getCurrentNetwork());
        return networkCapabilities != null && !networkCapabilities.hasCapability(17) && !networkCapabilities.hasCapability(24) && networkCapabilities.hasCapability(16);
    }

    public Class getBackgroundWorkerClass() {
        return ContextualWifiScanWorker.class;
    }
}
