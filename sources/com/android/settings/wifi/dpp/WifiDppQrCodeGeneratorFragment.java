package com.android.settings.wifi.dpp;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.chooser.TargetInfo;
import com.android.settings.R;
import com.android.settings.wifi.dpp.WifiNetworkConfig;
import com.android.settings.wifi.qrcode.QrCodeGenerator;
import com.google.zxing.WriterException;

public class WifiDppQrCodeGeneratorFragment extends WifiDppQrCodeBaseFragment {
    private String mQrCode;
    private ImageView mQrCodeView;

    public int getMetricsCategory() {
        return 1595;
    }

    /* access modifiers changed from: protected */
    public boolean isFooterAvailable() {
        return false;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        WifiNetworkConfig wifiNetworkConfigFromHostActivity = getWifiNetworkConfigFromHostActivity();
        if (getActivity() == null) {
            return;
        }
        if (wifiNetworkConfigFromHostActivity.isHotspot()) {
            getActivity().setTitle(R.string.wifi_dpp_share_hotspot);
        } else {
            getActivity().setTitle(R.string.wifi_dpp_share_wifi);
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        MenuItem findItem = menu.findItem(1);
        if (findItem != null) {
            findItem.setShowAsAction(0);
        }
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.wifi_dpp_qrcode_generator_fragment, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mQrCodeView = (ImageView) view.findViewById(R.id.qrcode_view);
        WifiNetworkConfig wifiNetworkConfigFromHostActivity = getWifiNetworkConfigFromHostActivity();
        if (wifiNetworkConfigFromHostActivity.isHotspot()) {
            setHeaderTitle(R.string.wifi_dpp_share_hotspot, new Object[0]);
        } else {
            setHeaderTitle(R.string.wifi_dpp_share_wifi, new Object[0]);
        }
        String preSharedKey = wifiNetworkConfigFromHostActivity.getPreSharedKey();
        TextView textView = (TextView) view.findViewById(R.id.password);
        if (TextUtils.isEmpty(preSharedKey)) {
            this.mSummary.setText(getString(R.string.wifi_dpp_scan_open_network_qr_code_with_another_device, wifiNetworkConfigFromHostActivity.getSsid()));
            textView.setVisibility(8);
        } else {
            this.mSummary.setText(getString(R.string.wifi_dpp_scan_qr_code_with_another_device, wifiNetworkConfigFromHostActivity.getSsid()));
            if (wifiNetworkConfigFromHostActivity.isHotspot()) {
                textView.setText(getString(R.string.wifi_dpp_hotspot_password, preSharedKey));
            } else {
                textView.setText(getString(R.string.wifi_dpp_wifi_password, preSharedKey));
            }
        }
        Intent component = new Intent().setComponent(getNearbySharingComponent());
        addActionButton((ViewGroup) view.findViewById(R.id.wifi_dpp_layout), createNearbyButton(component, new WifiDppQrCodeGeneratorFragment$$ExternalSyntheticLambda0(this, component, wifiNetworkConfigFromHostActivity)));
        this.mQrCode = wifiNetworkConfigFromHostActivity.getQrCode();
        setQrCode();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$0(Intent intent, WifiNetworkConfig wifiNetworkConfig, View view) {
        intent.setAction("android.intent.action.SEND");
        intent.addFlags(268435456);
        intent.addFlags(32768);
        Bundle bundle = new Bundle();
        String removeFirstAndLastDoubleQuotes = WifiDppUtils.removeFirstAndLastDoubleQuotes(wifiNetworkConfig.getSsid());
        String preSharedKey = wifiNetworkConfig.getPreSharedKey();
        String security = wifiNetworkConfig.getSecurity();
        boolean hiddenSsid = wifiNetworkConfig.getHiddenSsid();
        bundle.putString("android.intent.extra.SSID", removeFirstAndLastDoubleQuotes);
        bundle.putString("android.intent.extra.PASSWORD", preSharedKey);
        bundle.putString("android.intent.extra.SECURITY_TYPE", security);
        bundle.putBoolean("android.intent.extra.HIDDEN_SSID", hiddenSsid);
        intent.putExtra("android.intent.extra.WIFI_CREDENTIALS_BUNDLE", bundle);
        startActivity(intent);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public ComponentName getNearbySharingComponent() {
        String string = Settings.Secure.getString(getContext().getContentResolver(), "nearby_sharing_component");
        if (TextUtils.isEmpty(string)) {
            string = getString(17039899);
        }
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return ComponentName.unflattenFromString(string);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: android.graphics.drawable.Drawable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: android.graphics.drawable.Drawable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: android.graphics.drawable.Drawable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: android.graphics.drawable.Drawable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v5, resolved type: android.graphics.drawable.Drawable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v7, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v9, resolved type: java.lang.String} */
    /* JADX WARNING: type inference failed for: r1v6, types: [android.graphics.drawable.Drawable] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.android.internal.app.chooser.TargetInfo getNearbySharingTarget(android.content.Intent r10) {
        /*
            r9 = this;
            android.content.ComponentName r0 = r9.getNearbySharingComponent()
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            android.content.Intent r7 = new android.content.Intent
            r7.<init>(r10)
            r7.setComponent(r0)
            android.content.Context r9 = r9.getContext()
            android.content.pm.PackageManager r9 = r9.getPackageManager()
            r2 = 128(0x80, float:1.794E-43)
            android.content.pm.ResolveInfo r4 = r9.resolveActivity(r7, r2)
            if (r4 == 0) goto L_0x0066
            android.content.pm.ActivityInfo r2 = r4.activityInfo
            if (r2 != 0) goto L_0x0025
            goto L_0x0066
        L_0x0025:
            android.os.Bundle r2 = r2.metaData
            if (r2 == 0) goto L_0x0046
            android.content.res.Resources r0 = r9.getResourcesForActivity(r0)     // Catch:{ NameNotFoundException | NotFoundException -> 0x0042 }
            java.lang.String r3 = "android.service.chooser.chip_label"
            int r3 = r2.getInt(r3)     // Catch:{ NameNotFoundException | NotFoundException -> 0x0042 }
            java.lang.String r3 = r0.getString(r3)     // Catch:{ NameNotFoundException | NotFoundException -> 0x0042 }
            java.lang.String r5 = "android.service.chooser.chip_icon"
            int r2 = r2.getInt(r5)     // Catch:{ NameNotFoundException | NotFoundException -> 0x0043 }
            android.graphics.drawable.Drawable r1 = r0.getDrawable(r2)     // Catch:{ NameNotFoundException | NotFoundException -> 0x0043 }
            goto L_0x0043
        L_0x0042:
            r3 = r1
        L_0x0043:
            r0 = r1
            r1 = r3
            goto L_0x0047
        L_0x0046:
            r0 = r1
        L_0x0047:
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 == 0) goto L_0x0051
            java.lang.CharSequence r1 = r4.loadLabel(r9)
        L_0x0051:
            r5 = r1
            if (r0 != 0) goto L_0x0058
            android.graphics.drawable.Drawable r0 = r4.loadIcon(r9)
        L_0x0058:
            com.android.internal.app.chooser.DisplayResolveInfo r9 = new com.android.internal.app.chooser.DisplayResolveInfo
            r8 = 0
            java.lang.String r6 = ""
            r2 = r9
            r3 = r10
            r2.<init>(r3, r4, r5, r6, r7, r8)
            r9.setDisplayIcon(r0)
            return r9
        L_0x0066:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "Device-specified nearby sharing component ("
            r9.append(r10)
            r9.append(r0)
            java.lang.String r10 = ") not available"
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            java.lang.String r10 = "WifiDppQrCodeGeneratorFragment"
            android.util.Log.e(r10, r9)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.wifi.dpp.WifiDppQrCodeGeneratorFragment.getNearbySharingTarget(android.content.Intent):com.android.internal.app.chooser.TargetInfo");
    }

    private Button createActionButton(Drawable drawable, CharSequence charSequence, View.OnClickListener onClickListener) {
        Button button = (Button) LayoutInflater.from(getContext()).inflate(17367124, (ViewGroup) null);
        if (drawable != null) {
            int dimensionPixelSize = getResources().getDimensionPixelSize(17105040);
            drawable.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
            button.setCompoundDrawablesRelative(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
        }
        button.setText(charSequence);
        button.setOnClickListener(onClickListener);
        return button;
    }

    private void addActionButton(ViewGroup viewGroup, Button button) {
        if (button != null) {
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(-2, -2);
            int dimensionPixelSize = getResources().getDimensionPixelSize(17105482) / 2;
            marginLayoutParams.setMarginsRelative(dimensionPixelSize, 0, dimensionPixelSize, 0);
            viewGroup.addView(button, marginLayoutParams);
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public Button createNearbyButton(Intent intent, View.OnClickListener onClickListener) {
        TargetInfo nearbySharingTarget = getNearbySharingTarget(intent);
        if (nearbySharingTarget == null) {
            return null;
        }
        Button createActionButton = createActionButton(nearbySharingTarget.getDisplayIcon(getContext()), nearbySharingTarget.getDisplayLabel(), onClickListener);
        createActionButton.setAllCaps(false);
        return createActionButton;
    }

    private void setQrCode() {
        try {
            this.mQrCodeView.setImageBitmap(QrCodeGenerator.encodeQrCode(this.mQrCode, getContext().getResources().getDimensionPixelSize(R.dimen.qrcode_size)));
        } catch (WriterException e) {
            Log.e("WifiDppQrCodeGeneratorFragment", "Error generating QR code bitmap " + e);
        }
    }

    private WifiNetworkConfig getWifiNetworkConfigFromHostActivity() {
        WifiNetworkConfig wifiNetworkConfig = ((WifiNetworkConfig.Retriever) getActivity()).getWifiNetworkConfig();
        if (WifiNetworkConfig.isValidConfig(wifiNetworkConfig)) {
            return wifiNetworkConfig;
        }
        throw new IllegalStateException("Invalid Wi-Fi network for configuring");
    }
}
