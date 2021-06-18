package com.android.settings.network;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.telephony.SubscriptionManager;
import android.util.Log;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;
import com.android.settings.R;
import com.android.settings.SubSettings;
import com.android.settings.network.telephony.MobileNetworkUtils;
import com.android.settings.network.telephony.NetworkProviderWorker;
import com.android.settings.slices.CustomSliceRegistry;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.slices.SliceBuilderUtils;
import com.android.settings.wifi.slice.WifiSlice;
import com.android.settings.wifi.slice.WifiSliceItem;
import com.android.settingslib.Utils;
import com.android.settingslib.wifi.WifiUtils;

public class ProviderModelSlice extends WifiSlice {
    private final ProviderModelSliceHelper mHelper = getHelper();

    /* access modifiers changed from: protected */
    public boolean isApRowCollapsed() {
        return false;
    }

    public ProviderModelSlice(Context context) {
        super(context);
    }

    public Uri getUri() {
        return CustomSliceRegistry.PROVIDER_MODEL_SLICE_URI;
    }

    private static void log(String str) {
        Log.d("ProviderModelSlice", str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00f6 A[LOOP:0: B:32:0x00f0->B:34:0x00f6, LOOP_END] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public androidx.slice.Slice getSlice() {
        /*
            r10 = this;
            int r0 = r10.getInternetType()
            com.android.settings.network.ProviderModelSliceHelper r1 = r10.mHelper
            android.net.Uri r2 = r10.getUri()
            androidx.slice.builders.ListBuilder r1 = r1.createListBuilder(r2)
            com.android.settings.network.ProviderModelSliceHelper r2 = r10.mHelper
            boolean r2 = r2.isAirplaneModeEnabled()
            r3 = 4
            if (r2 == 0) goto L_0x002b
            android.net.wifi.WifiManager r2 = r10.mWifiManager
            boolean r2 = r2.isWifiEnabled()
            if (r2 != 0) goto L_0x002b
            if (r0 == r3) goto L_0x002b
            java.lang.String r10 = "Airplane mode is enabled."
            log(r10)
            androidx.slice.Slice r10 = r1.build()
            return r10
        L_0x002b:
            r2 = 0
            com.android.settings.network.telephony.NetworkProviderWorker r4 = r10.getWorker()
            r5 = 0
            if (r4 == 0) goto L_0x003c
            java.util.List r2 = r4.getResults()
            int r6 = r4.getApRowCount()
            goto L_0x0042
        L_0x003c:
            java.lang.String r6 = "network provider worker is null."
            log(r6)
            r6 = r5
        L_0x0042:
            com.android.settings.network.ProviderModelSliceHelper r7 = r10.mHelper
            boolean r7 = r7.hasCarrier()
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = "hasCarrier: "
            r8.append(r9)
            r8.append(r7)
            java.lang.String r8 = r8.toString()
            log(r8)
            com.android.settings.network.ProviderModelSliceHelper r8 = r10.mHelper
            com.android.settings.wifi.slice.WifiSliceItem r8 = r8.getConnectedWifiItem(r2)
            if (r0 != r3) goto L_0x0073
            java.lang.String r0 = "get Ethernet item which is connected"
            log(r0)
            androidx.slice.builders.ListBuilder$RowBuilder r0 = r10.createEthernetRow()
            r1.addRow(r0)
        L_0x0070:
            int r6 = r6 + -1
            goto L_0x0086
        L_0x0073:
            if (r8 == 0) goto L_0x0086
            r3 = 2
            if (r0 != r3) goto L_0x0086
            java.lang.String r0 = "get Wi-Fi item which is connected to internet"
            log(r0)
            androidx.slice.builders.ListBuilder$RowBuilder r0 = r10.getWifiSliceItemRow(r8)
            r1.addRow(r0)
            r5 = 1
            goto L_0x0070
        L_0x0086:
            if (r7 == 0) goto L_0x00a1
            com.android.settings.network.ProviderModelSliceHelper r0 = r10.mHelper
            r0.updateTelephony()
            com.android.settings.network.ProviderModelSliceHelper r0 = r10.mHelper
            if (r4 == 0) goto L_0x0096
            java.lang.String r3 = r4.getNetworkTypeDescription()
            goto L_0x0098
        L_0x0096:
            java.lang.String r3 = ""
        L_0x0098:
            androidx.slice.builders.ListBuilder$RowBuilder r0 = r0.createCarrierRow(r3)
            r1.addRow(r0)
            int r6 = r6 + -1
        L_0x00a1:
            if (r8 == 0) goto L_0x00b3
            if (r5 != 0) goto L_0x00b3
            java.lang.String r0 = "get Wi-Fi item which is connected"
            log(r0)
            androidx.slice.builders.ListBuilder$RowBuilder r0 = r10.getWifiSliceItemRow(r8)
            r1.addRow(r0)
            int r6 = r6 + -1
        L_0x00b3:
            if (r2 == 0) goto L_0x0104
            int r0 = r2.size()
            if (r0 <= 0) goto L_0x0104
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = "get Wi-Fi items which are not connected. Wi-Fi items : "
            r0.append(r3)
            int r3 = r2.size()
            r0.append(r3)
            java.lang.String r0 = r0.toString()
            log(r0)
            java.util.stream.Stream r0 = r2.stream()
            com.android.settings.network.ProviderModelSlice$$ExternalSyntheticLambda0 r2 = com.android.settings.network.ProviderModelSlice$$ExternalSyntheticLambda0.INSTANCE
            java.util.stream.Stream r0 = r0.filter(r2)
            long r2 = (long) r6
            java.util.stream.Stream r0 = r0.limit(r2)
            java.util.stream.Collector r2 = java.util.stream.Collectors.toList()
            java.lang.Object r0 = r0.collect(r2)
            java.util.List r0 = (java.util.List) r0
            java.util.Iterator r0 = r0.iterator()
        L_0x00f0:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x0104
            java.lang.Object r2 = r0.next()
            com.android.settings.wifi.slice.WifiSliceItem r2 = (com.android.settings.wifi.slice.WifiSliceItem) r2
            androidx.slice.builders.ListBuilder$RowBuilder r2 = r10.getWifiSliceItemRow(r2)
            r1.addRow(r2)
            goto L_0x00f0
        L_0x0104:
            androidx.slice.Slice r10 = r1.build()
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.network.ProviderModelSlice.getSlice():androidx.slice.Slice");
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getSlice$0(WifiSliceItem wifiSliceItem) {
        return wifiSliceItem.getConnectedState() != 2;
    }

    public void onNotifyChange(Intent intent) {
        if (this.mHelper.getSubscriptionManager() != null) {
            int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
            log("defaultSubId:" + defaultDataSubscriptionId);
            if (defaultSubscriptionIsUsable(defaultDataSubscriptionId)) {
                boolean hasExtra = intent.hasExtra("android.app.slice.extra.TOGGLE_STATE");
                boolean booleanExtra = intent.getBooleanExtra("android.app.slice.extra.TOGGLE_STATE", this.mHelper.isMobileDataEnabled());
                if (hasExtra) {
                    MobileNetworkUtils.setMobileDataEnabled(this.mContext, defaultDataSubscriptionId, booleanExtra, false);
                }
                if (!hasExtra) {
                    booleanExtra = MobileNetworkUtils.isMobileDataEnabled(this.mContext);
                }
                doCarrierNetworkAction(hasExtra, booleanExtra, defaultDataSubscriptionId);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void doCarrierNetworkAction(boolean z, boolean z2, int i) {
        NetworkProviderWorker worker = getWorker();
        if (worker != null) {
            if (z) {
                worker.setCarrierNetworkEnabledIfNeeded(z2, i);
            } else if (z2) {
                worker.connectCarrierNetwork();
            }
        }
    }

    public Intent getIntent() {
        return SliceBuilderUtils.buildSearchResultPageIntent(this.mContext, NetworkProviderSettings.class.getName(), "", this.mContext.getText(R.string.provider_internet_settings).toString(), 1401).setClassName(this.mContext.getPackageName(), SubSettings.class.getName()).setData(getUri());
    }

    public Class getBackgroundWorkerClass() {
        return NetworkProviderWorker.class;
    }

    /* access modifiers changed from: package-private */
    public ProviderModelSliceHelper getHelper() {
        return new ProviderModelSliceHelper(this.mContext, this);
    }

    /* access modifiers changed from: package-private */
    public NetworkProviderWorker getWorker() {
        return (NetworkProviderWorker) SliceBackgroundWorker.getInstance(getUri());
    }

    private int getInternetType() {
        NetworkProviderWorker worker = getWorker();
        if (worker == null) {
            return 1;
        }
        return worker.getInternetType();
    }

    /* access modifiers changed from: package-private */
    public ListBuilder.RowBuilder createEthernetRow() {
        ListBuilder.RowBuilder rowBuilder = new ListBuilder.RowBuilder();
        Drawable drawable = this.mContext.getDrawable(R.drawable.ic_settings_ethernet);
        if (drawable != null) {
            drawable.setTintList(Utils.getColorAttr(this.mContext, 16843829));
            rowBuilder.setTitleItem(com.android.settings.Utils.createIconWithDrawable(drawable), 0);
        }
        return rowBuilder.setTitle(this.mContext.getText(R.string.ethernet)).setSubtitle(this.mContext.getText(R.string.to_switch_networks_disconnect_ethernet));
    }

    /* access modifiers changed from: protected */
    public ListBuilder.RowBuilder getWifiSliceItemRow(WifiSliceItem wifiSliceItem) {
        IconCompat iconCompat;
        String title = wifiSliceItem.getTitle();
        IconCompat wifiSliceItemLevelIcon = getWifiSliceItemLevelIcon(wifiSliceItem);
        ListBuilder.RowBuilder contentDescription = new ListBuilder.RowBuilder().setTitleItem(wifiSliceItemLevelIcon, 0).setTitle(title).setSubtitle(wifiSliceItem.getSummary()).setContentDescription(wifiSliceItem.getContentDescription());
        if (wifiSliceItem.hasInternetAccess()) {
            contentDescription.setPrimaryAction(SliceAction.create(getBroadcastIntent(this.mContext), wifiSliceItemLevelIcon, 0, "Connect_To_Carrier"));
            iconCompat = IconCompat.createWithResource(this.mContext, R.drawable.ic_settings_close);
        } else {
            contentDescription.setPrimaryAction(getWifiEntryAction(wifiSliceItem, wifiSliceItemLevelIcon, title));
            iconCompat = getEndIcon(wifiSliceItem);
        }
        if (iconCompat != null) {
            contentDescription.addEndItem(iconCompat, 0);
        }
        return contentDescription;
    }

    /* access modifiers changed from: protected */
    public IconCompat getWifiSliceItemLevelIcon(WifiSliceItem wifiSliceItem) {
        if (wifiSliceItem.getConnectedState() != 2 || getInternetType() == 2) {
            return super.getWifiSliceItemLevelIcon(wifiSliceItem);
        }
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(this.mContext, 16843817);
        Drawable drawable = this.mContext.getDrawable(WifiUtils.getInternetIconResource(wifiSliceItem.getLevel(), wifiSliceItem.shouldShowXLevelIcon()));
        drawable.setTint(colorAttrDefaultColor);
        return com.android.settings.Utils.createIconWithDrawable(drawable);
    }

    /* access modifiers changed from: protected */
    public boolean defaultSubscriptionIsUsable(int i) {
        return SubscriptionManager.isUsableSubscriptionId(i);
    }
}
