package com.android.settings.vpn2;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ProxyInfo;
import android.os.Bundle;
import android.os.SystemProperties;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.android.internal.net.VpnProfile;
import com.android.net.module.util.ProxyUtils;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.utils.AndroidKeystoreAliasLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

class ConfigDialog extends AlertDialog implements TextWatcher, View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {
    private List<String> mAllowedTypes;
    private TextView mAlwaysOnInvalidReason;
    private CheckBox mAlwaysOnVpn;
    private TextView mDnsServers;
    private boolean mEditing;
    private boolean mExists;
    private Spinner mIpsecCaCert;
    private TextView mIpsecIdentifier;
    private TextView mIpsecSecret;
    private Spinner mIpsecServerCert;
    private Spinner mIpsecUserCert;
    private TextView mL2tpSecret;
    private final DialogInterface.OnClickListener mListener;
    private CheckBox mMppe;
    private TextView mName;
    private TextView mPassword;
    private final VpnProfile mProfile;
    private TextView mProxyHost;
    private TextView mProxyPort;
    private Spinner mProxySettings;
    private TextView mRoutes;
    private CheckBox mSaveLogin;
    private TextView mSearchDomains;
    private TextView mServer;
    private CheckBox mShowOptions;
    private List<String> mTotalTypes;
    private Spinner mType;
    private TextView mUsername;
    private View mView;

    private boolean requiresUsernamePassword(int i) {
        return (i == 7 || i == 8) ? false : true;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    ConfigDialog(Context context, DialogInterface.OnClickListener onClickListener, VpnProfile vpnProfile, boolean z, boolean z2) {
        super(context);
        this.mListener = onClickListener;
        this.mProfile = vpnProfile;
        this.mEditing = z;
        this.mExists = z2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        List<String> list;
        View inflate = getLayoutInflater().inflate(R.layout.vpn_dialog, (ViewGroup) null);
        this.mView = inflate;
        setView(inflate);
        Context context = getContext();
        this.mName = (TextView) this.mView.findViewById(R.id.name);
        this.mType = (Spinner) this.mView.findViewById(R.id.type);
        this.mServer = (TextView) this.mView.findViewById(R.id.server);
        this.mUsername = (TextView) this.mView.findViewById(R.id.username);
        this.mPassword = (TextView) this.mView.findViewById(R.id.password);
        this.mSearchDomains = (TextView) this.mView.findViewById(R.id.search_domains);
        this.mDnsServers = (TextView) this.mView.findViewById(R.id.dns_servers);
        this.mRoutes = (TextView) this.mView.findViewById(R.id.routes);
        this.mProxySettings = (Spinner) this.mView.findViewById(R.id.vpn_proxy_settings);
        this.mProxyHost = (TextView) this.mView.findViewById(R.id.vpn_proxy_host);
        this.mProxyPort = (TextView) this.mView.findViewById(R.id.vpn_proxy_port);
        this.mMppe = (CheckBox) this.mView.findViewById(R.id.mppe);
        this.mL2tpSecret = (TextView) this.mView.findViewById(R.id.l2tp_secret);
        this.mIpsecIdentifier = (TextView) this.mView.findViewById(R.id.ipsec_identifier);
        this.mIpsecSecret = (TextView) this.mView.findViewById(R.id.ipsec_secret);
        this.mIpsecUserCert = (Spinner) this.mView.findViewById(R.id.ipsec_user_cert);
        this.mIpsecCaCert = (Spinner) this.mView.findViewById(R.id.ipsec_ca_cert);
        this.mIpsecServerCert = (Spinner) this.mView.findViewById(R.id.ipsec_server_cert);
        this.mSaveLogin = (CheckBox) this.mView.findViewById(R.id.save_login);
        this.mShowOptions = (CheckBox) this.mView.findViewById(R.id.show_options);
        this.mAlwaysOnVpn = (CheckBox) this.mView.findViewById(R.id.always_on_vpn);
        this.mAlwaysOnInvalidReason = (TextView) this.mView.findViewById(R.id.always_on_invalid_reason);
        this.mName.setText(this.mProfile.name);
        setTypesByFeature(this.mType);
        List<String> list2 = this.mAllowedTypes;
        if (list2 == null || (list = this.mTotalTypes) == null) {
            Log.w("ConfigDialog", "Allowed or Total vpn types not initialized when setting initial selection");
        } else {
            this.mType.setSelection(list2.indexOf(list.get(this.mProfile.type)));
        }
        this.mServer.setText(this.mProfile.server);
        VpnProfile vpnProfile = this.mProfile;
        if (vpnProfile.saveLogin) {
            this.mUsername.setText(vpnProfile.username);
            this.mPassword.setText(this.mProfile.password);
        }
        this.mSearchDomains.setText(this.mProfile.searchDomains);
        this.mDnsServers.setText(this.mProfile.dnsServers);
        this.mRoutes.setText(this.mProfile.routes);
        ProxyInfo proxyInfo = this.mProfile.proxy;
        if (proxyInfo != null) {
            this.mProxyHost.setText(proxyInfo.getHost());
            int port = this.mProfile.proxy.getPort();
            this.mProxyPort.setText(port == 0 ? "" : Integer.toString(port));
        }
        this.mMppe.setChecked(this.mProfile.mppe);
        this.mL2tpSecret.setText(this.mProfile.l2tpSecret);
        this.mL2tpSecret.setTextAppearance(16974257);
        this.mIpsecIdentifier.setText(this.mProfile.ipsecIdentifier);
        this.mIpsecSecret.setText(this.mProfile.ipsecSecret);
        AndroidKeystoreAliasLoader androidKeystoreAliasLoader = new AndroidKeystoreAliasLoader((Integer) null);
        loadCertificates(this.mIpsecUserCert, androidKeystoreAliasLoader.getKeyCertAliases(), 0, this.mProfile.ipsecUserCert);
        loadCertificates(this.mIpsecCaCert, androidKeystoreAliasLoader.getCaCertAliases(), R.string.vpn_no_ca_cert, this.mProfile.ipsecCaCert);
        loadCertificates(this.mIpsecServerCert, androidKeystoreAliasLoader.getKeyCertAliases(), R.string.vpn_no_server_cert, this.mProfile.ipsecServerCert);
        this.mSaveLogin.setChecked(this.mProfile.saveLogin);
        this.mAlwaysOnVpn.setChecked(this.mProfile.key.equals(VpnUtils.getLockdownVpn()));
        this.mPassword.setTextAppearance(16974257);
        if (SystemProperties.getBoolean("persist.radio.imsregrequired", false)) {
            this.mAlwaysOnVpn.setVisibility(8);
        }
        this.mName.addTextChangedListener(this);
        this.mType.setOnItemSelectedListener(this);
        this.mServer.addTextChangedListener(this);
        this.mUsername.addTextChangedListener(this);
        this.mPassword.addTextChangedListener(this);
        this.mDnsServers.addTextChangedListener(this);
        this.mRoutes.addTextChangedListener(this);
        this.mProxySettings.setOnItemSelectedListener(this);
        this.mProxyHost.addTextChangedListener(this);
        this.mProxyPort.addTextChangedListener(this);
        this.mIpsecIdentifier.addTextChangedListener(this);
        this.mIpsecSecret.addTextChangedListener(this);
        this.mIpsecUserCert.setOnItemSelectedListener(this);
        this.mShowOptions.setOnClickListener(this);
        this.mAlwaysOnVpn.setOnCheckedChangeListener(this);
        boolean z = this.mEditing || !validate(true);
        this.mEditing = z;
        if (z) {
            setTitle((int) R.string.vpn_edit);
            this.mView.findViewById(R.id.editor).setVisibility(0);
            changeType(this.mProfile.type);
            this.mSaveLogin.setVisibility(8);
            configureAdvancedOptionsVisibility();
            if (this.mExists) {
                setButton(-3, context.getString(R.string.vpn_forget), this.mListener);
                if (VpnProfile.isLegacyType(this.mProfile.type)) {
                    ((TextView) this.mView.findViewById(R.id.dialog_alert_subtitle)).setVisibility(0);
                }
            }
            setButton(-1, context.getString(R.string.vpn_save), this.mListener);
        } else {
            setTitle(context.getString(R.string.vpn_connect_to, new Object[]{this.mProfile.name}));
            setUsernamePasswordVisibility(this.mProfile.type);
            setButton(-1, context.getString(R.string.vpn_connect), this.mListener);
        }
        setButton(-2, context.getString(R.string.vpn_cancel), this.mListener);
        super.onCreate(bundle);
        updateUiControls();
        getWindow().setSoftInputMode(20);
    }

    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        configureAdvancedOptionsVisibility();
    }

    public void afterTextChanged(Editable editable) {
        updateUiControls();
    }

    public void onClick(View view) {
        if (view == this.mShowOptions) {
            configureAdvancedOptionsVisibility();
        }
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
        if (adapterView == this.mType) {
            changeType(convertAllowedIndexToProfileType(i));
        } else if (adapterView == this.mProxySettings) {
            updateProxyFieldsVisibility(i);
        }
        updateUiControls();
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        if (compoundButton == this.mAlwaysOnVpn) {
            updateUiControls();
        }
    }

    public boolean isVpnAlwaysOn() {
        return this.mAlwaysOnVpn.isChecked();
    }

    private void updateUiControls() {
        VpnProfile profile = getProfile();
        if (profile.isValidLockdownProfile()) {
            this.mAlwaysOnVpn.setEnabled(true);
            this.mAlwaysOnInvalidReason.setVisibility(8);
        } else {
            this.mAlwaysOnVpn.setChecked(false);
            this.mAlwaysOnVpn.setEnabled(false);
            if (!profile.isTypeValidForLockdown()) {
                this.mAlwaysOnInvalidReason.setText(R.string.vpn_always_on_invalid_reason_type);
            } else if (VpnProfile.isLegacyType(profile.type) && !profile.isServerAddressNumeric()) {
                this.mAlwaysOnInvalidReason.setText(R.string.vpn_always_on_invalid_reason_server);
            } else if (VpnProfile.isLegacyType(profile.type) && !profile.hasDns()) {
                this.mAlwaysOnInvalidReason.setText(R.string.vpn_always_on_invalid_reason_no_dns);
            } else if (!VpnProfile.isLegacyType(profile.type) || profile.areDnsAddressesNumeric()) {
                this.mAlwaysOnInvalidReason.setText(R.string.vpn_always_on_invalid_reason_other);
            } else {
                this.mAlwaysOnInvalidReason.setText(R.string.vpn_always_on_invalid_reason_dns);
            }
            this.mAlwaysOnInvalidReason.setVisibility(0);
        }
        ProxyInfo proxyInfo = this.mProfile.proxy;
        if (proxyInfo != null && (!proxyInfo.getHost().isEmpty() || this.mProfile.proxy.getPort() != 0)) {
            this.mProxySettings.setSelection(1);
            updateProxyFieldsVisibility(1);
        }
        if (this.mAlwaysOnVpn.isChecked()) {
            this.mSaveLogin.setChecked(true);
            this.mSaveLogin.setEnabled(false);
        } else {
            this.mSaveLogin.setChecked(this.mProfile.saveLogin);
            this.mSaveLogin.setEnabled(true);
        }
        getButton(-1).setEnabled(validate(this.mEditing));
    }

    private void updateProxyFieldsVisibility(int i) {
        this.mView.findViewById(R.id.vpn_proxy_fields).setVisibility(i == 1 ? 0 : 8);
    }

    private boolean isAdvancedOptionsEnabled() {
        return this.mSearchDomains.getText().length() > 0 || this.mDnsServers.getText().length() > 0 || this.mRoutes.getText().length() > 0 || this.mProxyHost.getText().length() > 0 || this.mProxyPort.getText().length() > 0;
    }

    private void configureAdvancedOptionsVisibility() {
        int i = 8;
        if (this.mShowOptions.isChecked() || isAdvancedOptionsEnabled()) {
            this.mView.findViewById(R.id.options).setVisibility(0);
            this.mShowOptions.setVisibility(8);
            if (VpnProfile.isLegacyType(getSelectedVpnType())) {
                i = 0;
            }
            this.mView.findViewById(R.id.network_options).setVisibility(i);
            return;
        }
        this.mView.findViewById(R.id.options).setVisibility(8);
        this.mShowOptions.setVisibility(0);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0097, code lost:
        configureAdvancedOptionsVisibility();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x009a, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0063, code lost:
        r7.mView.findViewById(com.android.settings.R.id.ipsec_user).setVisibility(0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x006c, code lost:
        r7.mView.findViewById(com.android.settings.R.id.ipsec_peer).setVisibility(0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x007f, code lost:
        r7.mView.findViewById(com.android.settings.R.id.ipsec_psk).setVisibility(0);
        r7.mView.findViewById(com.android.settings.R.id.options_ipsec_identity).setVisibility(0);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void changeType(int r8) {
        /*
            r7 = this;
            android.widget.CheckBox r0 = r7.mMppe
            r1 = 8
            r0.setVisibility(r1)
            android.view.View r0 = r7.mView
            r2 = 2131559131(0x7f0d02db, float:1.8743597E38)
            android.view.View r0 = r0.findViewById(r2)
            r0.setVisibility(r1)
            android.view.View r0 = r7.mView
            r3 = 2131559119(0x7f0d02cf, float:1.8743573E38)
            android.view.View r0 = r0.findViewById(r3)
            r0.setVisibility(r1)
            android.view.View r0 = r7.mView
            r4 = 2131559122(0x7f0d02d2, float:1.874358E38)
            android.view.View r0 = r0.findViewById(r4)
            r0.setVisibility(r1)
            android.view.View r0 = r7.mView
            r5 = 2131559118(0x7f0d02ce, float:1.8743571E38)
            android.view.View r0 = r0.findViewById(r5)
            r0.setVisibility(r1)
            android.view.View r0 = r7.mView
            r6 = 2131559384(0x7f0d03d8, float:1.874411E38)
            android.view.View r0 = r0.findViewById(r6)
            r0.setVisibility(r1)
            r7.setUsernamePasswordVisibility(r8)
            boolean r0 = com.android.internal.net.VpnProfile.isLegacyType(r8)
            r1 = 0
            if (r0 != 0) goto L_0x0056
            android.view.View r0 = r7.mView
            android.view.View r0 = r0.findViewById(r6)
            r0.setVisibility(r1)
        L_0x0056:
            switch(r8) {
                case 0: goto L_0x0092;
                case 1: goto L_0x0076;
                case 2: goto L_0x005a;
                case 3: goto L_0x007f;
                case 4: goto L_0x0063;
                case 5: goto L_0x006c;
                case 6: goto L_0x006c;
                case 7: goto L_0x007f;
                case 8: goto L_0x0063;
                default: goto L_0x0059;
            }
        L_0x0059:
            goto L_0x0097
        L_0x005a:
            android.view.View r8 = r7.mView
            android.view.View r8 = r8.findViewById(r2)
            r8.setVisibility(r1)
        L_0x0063:
            android.view.View r8 = r7.mView
            android.view.View r8 = r8.findViewById(r4)
            r8.setVisibility(r1)
        L_0x006c:
            android.view.View r8 = r7.mView
            android.view.View r8 = r8.findViewById(r5)
            r8.setVisibility(r1)
            goto L_0x0097
        L_0x0076:
            android.view.View r8 = r7.mView
            android.view.View r8 = r8.findViewById(r2)
            r8.setVisibility(r1)
        L_0x007f:
            android.view.View r8 = r7.mView
            android.view.View r8 = r8.findViewById(r3)
            r8.setVisibility(r1)
            android.view.View r8 = r7.mView
            android.view.View r8 = r8.findViewById(r6)
            r8.setVisibility(r1)
            goto L_0x0097
        L_0x0092:
            android.widget.CheckBox r8 = r7.mMppe
            r8.setVisibility(r1)
        L_0x0097:
            r7.configureAdvancedOptionsVisibility()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.vpn2.ConfigDialog.changeType(int):void");
    }

    private boolean validate(boolean z) {
        if (this.mAlwaysOnVpn.isChecked() && !getProfile().isValidLockdownProfile()) {
            return false;
        }
        int selectedVpnType = getSelectedVpnType();
        if (z || !requiresUsernamePassword(selectedVpnType)) {
            if (this.mName.getText().length() == 0 || this.mServer.getText().length() == 0) {
                return false;
            }
            if (VpnProfile.isLegacyType(this.mProfile.type) && (!validateAddresses(this.mDnsServers.getText().toString(), false) || !validateAddresses(this.mRoutes.getText().toString(), true))) {
                return false;
            }
            if ((!VpnProfile.isLegacyType(this.mProfile.type) && this.mIpsecIdentifier.getText().length() == 0) || !validateProxy()) {
                return false;
            }
            switch (selectedVpnType) {
                case 0:
                case 5:
                case 6:
                    return true;
                case 1:
                case 3:
                case 7:
                    if (this.mIpsecSecret.getText().length() != 0) {
                        return true;
                    }
                    return false;
                case 2:
                case 4:
                case 8:
                    if (this.mIpsecUserCert.getSelectedItemPosition() != 0) {
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        } else if (this.mUsername.getText().length() == 0 || this.mPassword.getText().length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v4, resolved type: byte} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean validateAddresses(java.lang.String r10, boolean r11) {
        /*
            r9 = this;
            r9 = 0
            java.lang.String r0 = " "
            java.lang.String[] r10 = r10.split(r0)     // Catch:{ Exception -> 0x0062 }
            int r0 = r10.length     // Catch:{ Exception -> 0x0062 }
            r1 = r9
        L_0x0009:
            r2 = 1
            if (r1 >= r0) goto L_0x0061
            r3 = r10[r1]     // Catch:{ Exception -> 0x0062 }
            boolean r4 = r3.isEmpty()     // Catch:{ Exception -> 0x0062 }
            if (r4 == 0) goto L_0x0015
            goto L_0x005d
        L_0x0015:
            r4 = 2
            r5 = 32
            if (r11 == 0) goto L_0x002c
            java.lang.String r6 = "/"
            java.lang.String[] r3 = r3.split(r6, r4)     // Catch:{ Exception -> 0x0062 }
            r6 = r3[r9]     // Catch:{ Exception -> 0x0062 }
            r3 = r3[r2]     // Catch:{ Exception -> 0x0062 }
            int r3 = java.lang.Integer.parseInt(r3)     // Catch:{ Exception -> 0x0062 }
            r8 = r6
            r6 = r3
            r3 = r8
            goto L_0x002d
        L_0x002c:
            r6 = r5
        L_0x002d:
            java.net.InetAddress r3 = java.net.InetAddress.parseNumericAddress(r3)     // Catch:{ Exception -> 0x0062 }
            byte[] r3 = r3.getAddress()     // Catch:{ Exception -> 0x0062 }
            r7 = 3
            byte r7 = r3[r7]     // Catch:{ Exception -> 0x0062 }
            r7 = r7 & 255(0xff, float:3.57E-43)
            byte r4 = r3[r4]     // Catch:{ Exception -> 0x0062 }
            r4 = r4 & 255(0xff, float:3.57E-43)
            int r4 = r4 << 8
            r4 = r4 | r7
            byte r2 = r3[r2]     // Catch:{ Exception -> 0x0062 }
            r2 = r2 & 255(0xff, float:3.57E-43)
            int r2 = r2 << 16
            r2 = r2 | r4
            byte r4 = r3[r9]     // Catch:{ Exception -> 0x0062 }
            r4 = r4 & 255(0xff, float:3.57E-43)
            int r4 = r4 << 24
            r2 = r2 | r4
            int r3 = r3.length     // Catch:{ Exception -> 0x0062 }
            r4 = 4
            if (r3 != r4) goto L_0x0060
            if (r6 < 0) goto L_0x0060
            if (r6 > r5) goto L_0x0060
            if (r6 >= r5) goto L_0x005d
            int r2 = r2 << r6
            if (r2 == 0) goto L_0x005d
            goto L_0x0060
        L_0x005d:
            int r1 = r1 + 1
            goto L_0x0009
        L_0x0060:
            return r9
        L_0x0061:
            return r2
        L_0x0062:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.vpn2.ConfigDialog.validateAddresses(java.lang.String, boolean):boolean");
    }

    private void setTypesByFeature(Spinner spinner) {
        String[] stringArray = getContext().getResources().getStringArray(R.array.vpn_types);
        this.mTotalTypes = new ArrayList(Arrays.asList(stringArray));
        this.mAllowedTypes = new ArrayList(Arrays.asList(stringArray));
        if (!getContext().getPackageManager().hasSystemFeature("android.software.ipsec_tunnels")) {
            ArrayList arrayList = new ArrayList(Arrays.asList(stringArray));
            arrayList.remove(8);
            arrayList.remove(7);
            arrayList.remove(6);
            stringArray = (String[]) arrayList.toArray(new String[0]);
        } else if (Utils.isProviderModelEnabled(getContext())) {
            if (!this.mExists) {
                this.mProfile.type = 6;
            }
            if (!VpnProfile.isLegacyType(this.mProfile.type)) {
                for (int size = this.mAllowedTypes.size() - 1; size >= 0; size--) {
                    if (VpnProfile.isLegacyType(size)) {
                        this.mAllowedTypes.remove(size);
                    }
                }
                stringArray = (String[]) this.mAllowedTypes.toArray(new String[0]);
            }
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), 17367048, stringArray);
        arrayAdapter.setDropDownViewResource(17367049);
        spinner.setAdapter(arrayAdapter);
    }

    private void loadCertificates(Spinner spinner, Collection<String> collection, int i, String str) {
        String str2;
        String[] strArr;
        Context context = getContext();
        if (i == 0) {
            str2 = "";
        } else {
            str2 = context.getString(i);
        }
        if (collection == null || collection.size() == 0) {
            strArr = new String[]{str2};
        } else {
            strArr = new String[(collection.size() + 1)];
            strArr[0] = str2;
            int i2 = 1;
            for (String str3 : collection) {
                strArr[i2] = str3;
                i2++;
            }
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(context, 17367048, strArr);
        arrayAdapter.setDropDownViewResource(17367049);
        spinner.setAdapter(arrayAdapter);
        for (int i3 = 1; i3 < strArr.length; i3++) {
            if (strArr[i3].equals(str)) {
                spinner.setSelection(i3);
                return;
            }
        }
    }

    private void setUsernamePasswordVisibility(int i) {
        this.mView.findViewById(R.id.userpass).setVisibility(requiresUsernamePassword(i) ? 0 : 8);
    }

    /* access modifiers changed from: package-private */
    public boolean isEditing() {
        return this.mEditing;
    }

    /* access modifiers changed from: package-private */
    public boolean hasProxy() {
        return this.mProxySettings.getSelectedItemPosition() == 1;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x00d9, code lost:
        if (r5.mIpsecUserCert.getSelectedItemPosition() == 0) goto L_0x00e5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x00db, code lost:
        r0.ipsecUserCert = (java.lang.String) r5.mIpsecUserCert.getSelectedItem();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x00eb, code lost:
        if (r5.mIpsecCaCert.getSelectedItemPosition() == 0) goto L_0x00f7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x00ed, code lost:
        r0.ipsecCaCert = (java.lang.String) r5.mIpsecCaCert.getSelectedItem();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00fd, code lost:
        if (r5.mIpsecServerCert.getSelectedItemPosition() == 0) goto L_0x0137;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x00ff, code lost:
        r0.ipsecServerCert = (java.lang.String) r5.mIpsecServerCert.getSelectedItem();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0116, code lost:
        r0.ipsecIdentifier = r5.mIpsecIdentifier.getText().toString();
        r0.ipsecSecret = r5.mIpsecSecret.getText().toString();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x013e, code lost:
        if (r0.username.isEmpty() == false) goto L_0x014b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0146, code lost:
        if (r0.password.isEmpty() != false) goto L_0x0149;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0149, code lost:
        r1 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x014b, code lost:
        r1 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0152, code lost:
        if (r5.mSaveLogin.isChecked() != false) goto L_0x015a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0156, code lost:
        if (r5.mEditing == false) goto L_0x015b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0158, code lost:
        if (r1 == false) goto L_0x015b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x015a, code lost:
        r2 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x015b, code lost:
        r0.saveLogin = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x015d, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.android.internal.net.VpnProfile getProfile() {
        /*
            r5 = this;
            com.android.internal.net.VpnProfile r0 = new com.android.internal.net.VpnProfile
            com.android.internal.net.VpnProfile r1 = r5.mProfile
            java.lang.String r1 = r1.key
            r0.<init>(r1)
            android.widget.TextView r1 = r5.mName
            java.lang.CharSequence r1 = r1.getText()
            java.lang.String r1 = r1.toString()
            r0.name = r1
            int r1 = r5.getSelectedVpnType()
            r0.type = r1
            android.widget.TextView r1 = r5.mServer
            java.lang.CharSequence r1 = r1.getText()
            java.lang.String r1 = r1.toString()
            java.lang.String r1 = r1.trim()
            r0.server = r1
            android.widget.TextView r1 = r5.mUsername
            java.lang.CharSequence r1 = r1.getText()
            java.lang.String r1 = r1.toString()
            r0.username = r1
            android.widget.TextView r1 = r5.mPassword
            java.lang.CharSequence r1 = r1.getText()
            java.lang.String r1 = r1.toString()
            r0.password = r1
            int r1 = r0.type
            boolean r1 = com.android.internal.net.VpnProfile.isLegacyType(r1)
            if (r1 == 0) goto L_0x007c
            android.widget.TextView r1 = r5.mSearchDomains
            java.lang.CharSequence r1 = r1.getText()
            java.lang.String r1 = r1.toString()
            java.lang.String r1 = r1.trim()
            r0.searchDomains = r1
            android.widget.TextView r1 = r5.mDnsServers
            java.lang.CharSequence r1 = r1.getText()
            java.lang.String r1 = r1.toString()
            java.lang.String r1 = r1.trim()
            r0.dnsServers = r1
            android.widget.TextView r1 = r5.mRoutes
            java.lang.CharSequence r1 = r1.getText()
            java.lang.String r1 = r1.toString()
            java.lang.String r1 = r1.trim()
            r0.routes = r1
            goto L_0x0088
        L_0x007c:
            android.widget.TextView r1 = r5.mIpsecIdentifier
            java.lang.CharSequence r1 = r1.getText()
            java.lang.String r1 = r1.toString()
            r0.ipsecIdentifier = r1
        L_0x0088:
            boolean r1 = r5.hasProxy()
            r2 = 0
            if (r1 == 0) goto L_0x00be
            android.widget.TextView r1 = r5.mProxyHost
            java.lang.CharSequence r1 = r1.getText()
            java.lang.String r1 = r1.toString()
            java.lang.String r1 = r1.trim()
            android.widget.TextView r3 = r5.mProxyPort
            java.lang.CharSequence r3 = r3.getText()
            java.lang.String r3 = r3.toString()
            java.lang.String r3 = r3.trim()
            boolean r4 = r3.isEmpty()
            if (r4 == 0) goto L_0x00b3
            r3 = r2
            goto L_0x00b7
        L_0x00b3:
            int r3 = java.lang.Integer.parseInt(r3)
        L_0x00b7:
            android.net.ProxyInfo r1 = android.net.ProxyInfo.buildDirectProxy(r1, r3)
            r0.proxy = r1
            goto L_0x00c1
        L_0x00be:
            r1 = 0
            r0.proxy = r1
        L_0x00c1:
            int r1 = r0.type
            switch(r1) {
                case 0: goto L_0x012f;
                case 1: goto L_0x010a;
                case 2: goto L_0x00c7;
                case 3: goto L_0x0116;
                case 4: goto L_0x00d3;
                case 5: goto L_0x00e5;
                case 6: goto L_0x00e5;
                case 7: goto L_0x0116;
                case 8: goto L_0x00d3;
                default: goto L_0x00c6;
            }
        L_0x00c6:
            goto L_0x0137
        L_0x00c7:
            android.widget.TextView r1 = r5.mL2tpSecret
            java.lang.CharSequence r1 = r1.getText()
            java.lang.String r1 = r1.toString()
            r0.l2tpSecret = r1
        L_0x00d3:
            android.widget.Spinner r1 = r5.mIpsecUserCert
            int r1 = r1.getSelectedItemPosition()
            if (r1 == 0) goto L_0x00e5
            android.widget.Spinner r1 = r5.mIpsecUserCert
            java.lang.Object r1 = r1.getSelectedItem()
            java.lang.String r1 = (java.lang.String) r1
            r0.ipsecUserCert = r1
        L_0x00e5:
            android.widget.Spinner r1 = r5.mIpsecCaCert
            int r1 = r1.getSelectedItemPosition()
            if (r1 == 0) goto L_0x00f7
            android.widget.Spinner r1 = r5.mIpsecCaCert
            java.lang.Object r1 = r1.getSelectedItem()
            java.lang.String r1 = (java.lang.String) r1
            r0.ipsecCaCert = r1
        L_0x00f7:
            android.widget.Spinner r1 = r5.mIpsecServerCert
            int r1 = r1.getSelectedItemPosition()
            if (r1 == 0) goto L_0x0137
            android.widget.Spinner r1 = r5.mIpsecServerCert
            java.lang.Object r1 = r1.getSelectedItem()
            java.lang.String r1 = (java.lang.String) r1
            r0.ipsecServerCert = r1
            goto L_0x0137
        L_0x010a:
            android.widget.TextView r1 = r5.mL2tpSecret
            java.lang.CharSequence r1 = r1.getText()
            java.lang.String r1 = r1.toString()
            r0.l2tpSecret = r1
        L_0x0116:
            android.widget.TextView r1 = r5.mIpsecIdentifier
            java.lang.CharSequence r1 = r1.getText()
            java.lang.String r1 = r1.toString()
            r0.ipsecIdentifier = r1
            android.widget.TextView r1 = r5.mIpsecSecret
            java.lang.CharSequence r1 = r1.getText()
            java.lang.String r1 = r1.toString()
            r0.ipsecSecret = r1
            goto L_0x0137
        L_0x012f:
            android.widget.CheckBox r1 = r5.mMppe
            boolean r1 = r1.isChecked()
            r0.mppe = r1
        L_0x0137:
            java.lang.String r1 = r0.username
            boolean r1 = r1.isEmpty()
            r3 = 1
            if (r1 == 0) goto L_0x014b
            java.lang.String r1 = r0.password
            boolean r1 = r1.isEmpty()
            if (r1 != 0) goto L_0x0149
            goto L_0x014b
        L_0x0149:
            r1 = r2
            goto L_0x014c
        L_0x014b:
            r1 = r3
        L_0x014c:
            android.widget.CheckBox r4 = r5.mSaveLogin
            boolean r4 = r4.isChecked()
            if (r4 != 0) goto L_0x015a
            boolean r5 = r5.mEditing
            if (r5 == 0) goto L_0x015b
            if (r1 == 0) goto L_0x015b
        L_0x015a:
            r2 = r3
        L_0x015b:
            r0.saveLogin = r2
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.vpn2.ConfigDialog.getProfile():com.android.internal.net.VpnProfile");
    }

    private boolean validateProxy() {
        if (hasProxy() && ProxyUtils.validate(this.mProxyHost.getText().toString().trim(), this.mProxyPort.getText().toString().trim(), "") != 0) {
            return false;
        }
        return true;
    }

    private int getSelectedVpnType() {
        return convertAllowedIndexToProfileType(this.mType.getSelectedItemPosition());
    }

    private int convertAllowedIndexToProfileType(int i) {
        List<String> list = this.mAllowedTypes;
        if (list == null || this.mTotalTypes == null) {
            Log.w("ConfigDialog", "Allowed or Total vpn types not initialized when converting protileType");
            return i;
        }
        return this.mTotalTypes.indexOf(list.get(i));
    }
}
