package com.android.settings.network.apn;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Telephony;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.internal.util.ArrayUtils;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settings.network.ProxySubscriptionManager;
import com.android.settingslib.utils.ThreadUtils;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApnEditor extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener, View.OnKeyListener {
    static final int APN_INDEX = 2;
    public static final String[] APN_TYPES = {"default", "mms", "supl", "dun", "hipri", "fota", "ims", "cbs", "ia", "emergency", "mcx", "xcap"};
    static final int CARRIER_ENABLED_INDEX = 17;
    static final int MCC_INDEX = 9;
    static final int MNC_INDEX = 10;
    static final int NAME_INDEX = 1;
    static final int PROTOCOL_INDEX = 16;
    static final int ROAMING_PROTOCOL_INDEX = 20;
    private static final String TAG = ApnEditor.class.getSimpleName();
    static final int TYPE_INDEX = 15;
    static String sNotSet;
    private static final String[] sProjection = {"_id", "name", "apn", "proxy", "port", "user", "server", "password", "mmsc", "mcc", "mnc", "numeric", "mmsproxy", "mmsport", "authtype", "type", "protocol", "carrier_enabled", "bearer", "bearer_bitmask", "roaming_protocol", "mvno_type", "mvno_match_data", "edited", "user_editable"};
    EditTextPreference mApn;
    ApnData mApnData;
    EditTextPreference mApnType;
    ListPreference mAuthType;
    private int mBearerInitialVal = 0;
    MultiSelectListPreference mBearerMulti;
    SwitchPreference mCarrierEnabled;
    private Uri mCarrierUri;
    private String mCurMcc;
    private String mCurMnc;
    String mDefaultApnProtocol;
    String mDefaultApnRoamingProtocol;
    String[] mDefaultApnTypes;
    EditTextPreference mMcc;
    EditTextPreference mMmsPort;
    EditTextPreference mMmsProxy;
    EditTextPreference mMmsc;
    EditTextPreference mMnc;
    EditTextPreference mMvnoMatchData;
    private String mMvnoMatchDataStr;
    ListPreference mMvnoType;
    private String mMvnoTypeStr;
    EditTextPreference mName;
    private boolean mNewApn;
    EditTextPreference mPassword;
    EditTextPreference mPort;
    ListPreference mProtocol;
    EditTextPreference mProxy;
    ProxySubscriptionManager mProxySubscriptionMgr;
    private boolean mReadOnlyApn;
    private String[] mReadOnlyApnFields;
    String[] mReadOnlyApnTypes;
    ListPreference mRoamingProtocol;
    EditTextPreference mServer;
    private int mSubId;
    EditTextPreference mUser;

    private static boolean bitmaskHasTech(int i, int i2) {
        if (i == 0) {
            return true;
        }
        if (i2 >= 1) {
            return (i & (1 << (i2 - 1))) != 0;
        }
        return false;
    }

    private static int getBitmaskForTech(int i) {
        if (i >= 1) {
            return 1 << (i - 1);
        }
        return 0;
    }

    public int getMetricsCategory() {
        return 13;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setLifecycleForAllControllers();
        Intent intent = getIntent();
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            finish();
            return;
        }
        this.mSubId = intent.getIntExtra("sub_id", -1);
        initApnEditorUi();
        getCarrierCustomizedConfig();
        Uri uri = null;
        if (action.equals("android.intent.action.EDIT")) {
            uri = intent.getData();
            if (!uri.isPathPrefixMatch(Telephony.Carriers.CONTENT_URI)) {
                String str = TAG;
                Log.e(str, "Edit request not for carrier table. Uri: " + uri);
                finish();
                return;
            }
        } else if (action.equals("android.intent.action.INSERT")) {
            Uri data = intent.getData();
            this.mCarrierUri = data;
            if (!data.isPathPrefixMatch(Telephony.Carriers.CONTENT_URI)) {
                String str2 = TAG;
                Log.e(str2, "Insert request not for carrier table. Uri: " + this.mCarrierUri);
                finish();
                return;
            }
            this.mNewApn = true;
            this.mMvnoTypeStr = intent.getStringExtra("mvno_type");
            this.mMvnoMatchDataStr = intent.getStringExtra("mvno_match_data");
        } else {
            finish();
            return;
        }
        if (uri != null) {
            this.mApnData = getApnDataFromUri(uri);
        } else {
            this.mApnData = new ApnData(sProjection.length);
        }
        boolean z = this.mApnData.getInteger(23, 1).intValue() == 1;
        String str3 = TAG;
        Log.d(str3, "onCreate: EDITED " + z);
        if (!z && (this.mApnData.getInteger(24, 1).intValue() == 0 || apnTypesMatch(this.mReadOnlyApnTypes, this.mApnData.getString(15)))) {
            Log.d(str3, "onCreate: apnTypesMatch; read-only APN");
            this.mReadOnlyApn = true;
            disableAllFields();
        } else if (!ArrayUtils.isEmpty(this.mReadOnlyApnFields)) {
            disableFields(this.mReadOnlyApnFields);
        }
        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
            getPreferenceScreen().getPreference(i).setOnPreferenceChangeListener(this);
        }
    }

    private void setLifecycleForAllControllers() {
        if (this.mProxySubscriptionMgr == null) {
            this.mProxySubscriptionMgr = ProxySubscriptionManager.getInstance(getContext());
        }
        this.mProxySubscriptionMgr.setLifecycle(getLifecycle());
    }

    public void onViewStateRestored(Bundle bundle) {
        super.onViewStateRestored(bundle);
        fillUI(bundle == null);
        setCarrierCustomizedConfigToUi();
    }

    static String formatInteger(String str) {
        try {
            int parseInt = Integer.parseInt(str);
            return String.format(getCorrectDigitsFormat(str), new Object[]{Integer.valueOf(parseInt)});
        } catch (NumberFormatException unused) {
            return str;
        }
    }

    static String getCorrectDigitsFormat(String str) {
        return str.length() == 2 ? "%02d" : "%03d";
    }

    static boolean hasAllApns(String[] strArr) {
        if (ArrayUtils.isEmpty(strArr)) {
            return false;
        }
        List asList = Arrays.asList(strArr);
        if (asList.contains("*")) {
            Log.d(TAG, "hasAllApns: true because apnList.contains(APN_TYPE_ALL)");
            return true;
        }
        for (String contains : APN_TYPES) {
            if (!asList.contains(contains)) {
                return false;
            }
        }
        Log.d(TAG, "hasAllApns: true");
        return true;
    }

    private boolean apnTypesMatch(String[] strArr, String str) {
        if (ArrayUtils.isEmpty(strArr)) {
            return false;
        }
        if (hasAllApns(strArr) || TextUtils.isEmpty(str)) {
            return true;
        }
        List asList = Arrays.asList(strArr);
        for (String str2 : str.split(",")) {
            if (asList.contains(str2.trim())) {
                Log.d(TAG, "apnTypesMatch: true because match found for " + str2.trim());
                return true;
            }
        }
        Log.d(TAG, "apnTypesMatch: false");
        return false;
    }

    private Preference getPreferenceFromFieldName(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -2135515857:
                if (str.equals("mvno_type")) {
                    c = 0;
                    break;
                }
                break;
            case -1954254981:
                if (str.equals("mmsproxy")) {
                    c = 1;
                    break;
                }
                break;
            case -1640523526:
                if (str.equals("carrier_enabled")) {
                    c = 2;
                    break;
                }
                break;
            case -1393032351:
                if (str.equals("bearer")) {
                    c = 3;
                    break;
                }
                break;
            case -1230508389:
                if (str.equals("bearer_bitmask")) {
                    c = 4;
                    break;
                }
                break;
            case -1039601666:
                if (str.equals("roaming_protocol")) {
                    c = 5;
                    break;
                }
                break;
            case -989163880:
                if (str.equals("protocol")) {
                    c = 6;
                    break;
                }
                break;
            case -905826493:
                if (str.equals("server")) {
                    c = 7;
                    break;
                }
                break;
            case -520149991:
                if (str.equals("mvno_match_data")) {
                    c = 8;
                    break;
                }
                break;
            case 96799:
                if (str.equals("apn")) {
                    c = 9;
                    break;
                }
                break;
            case 107917:
                if (str.equals("mcc")) {
                    c = 10;
                    break;
                }
                break;
            case 108258:
                if (str.equals("mnc")) {
                    c = 11;
                    break;
                }
                break;
            case 3355632:
                if (str.equals("mmsc")) {
                    c = 12;
                    break;
                }
                break;
            case 3373707:
                if (str.equals("name")) {
                    c = 13;
                    break;
                }
                break;
            case 3446913:
                if (str.equals("port")) {
                    c = 14;
                    break;
                }
                break;
            case 3575610:
                if (str.equals("type")) {
                    c = 15;
                    break;
                }
                break;
            case 3599307:
                if (str.equals("user")) {
                    c = 16;
                    break;
                }
                break;
            case 106941038:
                if (str.equals("proxy")) {
                    c = 17;
                    break;
                }
                break;
            case 1183882708:
                if (str.equals("mmsport")) {
                    c = 18;
                    break;
                }
                break;
            case 1216985755:
                if (str.equals("password")) {
                    c = 19;
                    break;
                }
                break;
            case 1433229538:
                if (str.equals("authtype")) {
                    c = 20;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return this.mMvnoType;
            case 1:
                return this.mMmsProxy;
            case 2:
                return this.mCarrierEnabled;
            case 3:
            case 4:
                return this.mBearerMulti;
            case 5:
                return this.mRoamingProtocol;
            case 6:
                return this.mProtocol;
            case 7:
                return this.mServer;
            case 8:
                return this.mMvnoMatchData;
            case 9:
                return this.mApn;
            case 10:
                return this.mMcc;
            case 11:
                return this.mMnc;
            case 12:
                return this.mMmsc;
            case 13:
                return this.mName;
            case 14:
                return this.mPort;
            case 15:
                return this.mApnType;
            case 16:
                return this.mUser;
            case 17:
                return this.mProxy;
            case 18:
                return this.mMmsPort;
            case 19:
                return this.mPassword;
            case 20:
                return this.mAuthType;
            default:
                return null;
        }
    }

    private void disableFields(String[] strArr) {
        for (String preferenceFromFieldName : strArr) {
            Preference preferenceFromFieldName2 = getPreferenceFromFieldName(preferenceFromFieldName);
            if (preferenceFromFieldName2 != null) {
                preferenceFromFieldName2.setEnabled(false);
            }
        }
    }

    private void disableAllFields() {
        this.mName.setEnabled(false);
        this.mApn.setEnabled(false);
        this.mProxy.setEnabled(false);
        this.mPort.setEnabled(false);
        this.mUser.setEnabled(false);
        this.mServer.setEnabled(false);
        this.mPassword.setEnabled(false);
        this.mMmsProxy.setEnabled(false);
        this.mMmsPort.setEnabled(false);
        this.mMmsc.setEnabled(false);
        this.mMcc.setEnabled(false);
        this.mMnc.setEnabled(false);
        this.mApnType.setEnabled(false);
        this.mAuthType.setEnabled(false);
        this.mProtocol.setEnabled(false);
        this.mRoamingProtocol.setEnabled(false);
        this.mCarrierEnabled.setEnabled(false);
        this.mBearerMulti.setEnabled(false);
        this.mMvnoType.setEnabled(false);
        this.mMvnoMatchData.setEnabled(false);
    }

    /* access modifiers changed from: package-private */
    public void fillUI(boolean z) {
        String str;
        String str2;
        String str3;
        if (z) {
            this.mName.setText(this.mApnData.getString(1));
            this.mApn.setText(this.mApnData.getString(2));
            this.mProxy.setText(this.mApnData.getString(3));
            this.mPort.setText(this.mApnData.getString(4));
            this.mUser.setText(this.mApnData.getString(5));
            this.mServer.setText(this.mApnData.getString(6));
            this.mPassword.setText(this.mApnData.getString(7));
            this.mMmsProxy.setText(this.mApnData.getString(12));
            this.mMmsPort.setText(this.mApnData.getString(13));
            this.mMmsc.setText(this.mApnData.getString(8));
            this.mMcc.setText(this.mApnData.getString(9));
            this.mMnc.setText(this.mApnData.getString(10));
            this.mApnType.setText(this.mApnData.getString(15));
            if (this.mNewApn) {
                SubscriptionInfo accessibleSubscriptionInfo = this.mProxySubscriptionMgr.getAccessibleSubscriptionInfo(this.mSubId);
                if (accessibleSubscriptionInfo == null) {
                    str2 = null;
                } else {
                    str2 = accessibleSubscriptionInfo.getMccString();
                }
                if (accessibleSubscriptionInfo == null) {
                    str3 = null;
                } else {
                    str3 = accessibleSubscriptionInfo.getMncString();
                }
                if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str2)) {
                    this.mMcc.setText(str2);
                    this.mMnc.setText(str3);
                    this.mCurMnc = str3;
                    this.mCurMcc = str2;
                }
            }
            int intValue = this.mApnData.getInteger(14, -1).intValue();
            if (intValue != -1) {
                this.mAuthType.setValueIndex(intValue);
            } else {
                this.mAuthType.setValue((String) null);
            }
            this.mProtocol.setValue(this.mApnData.getString(16));
            this.mRoamingProtocol.setValue(this.mApnData.getString(20));
            this.mCarrierEnabled.setChecked(this.mApnData.getInteger(17, 1).intValue() == 1);
            this.mBearerInitialVal = this.mApnData.getInteger(18, 0).intValue();
            HashSet hashSet = new HashSet();
            int intValue2 = this.mApnData.getInteger(19, 0).intValue();
            if (intValue2 != 0) {
                int i = 1;
                while (intValue2 != 0) {
                    if ((intValue2 & 1) == 1) {
                        hashSet.add("" + i);
                    }
                    intValue2 >>= 1;
                    i++;
                }
            } else if (this.mBearerInitialVal == 0) {
                hashSet.add("0");
            }
            if (this.mBearerInitialVal != 0) {
                if (!hashSet.contains("" + this.mBearerInitialVal)) {
                    hashSet.add("" + this.mBearerInitialVal);
                }
            }
            this.mBearerMulti.setValues(hashSet);
            this.mMvnoType.setValue(this.mApnData.getString(21));
            this.mMvnoMatchData.setEnabled(false);
            this.mMvnoMatchData.setText(this.mApnData.getString(22));
            if (!(!this.mNewApn || (str = this.mMvnoTypeStr) == null || this.mMvnoMatchDataStr == null)) {
                this.mMvnoType.setValue(str);
                this.mMvnoMatchData.setText(this.mMvnoMatchDataStr);
            }
        }
        EditTextPreference editTextPreference = this.mName;
        editTextPreference.setSummary((CharSequence) checkNull(editTextPreference.getText()));
        EditTextPreference editTextPreference2 = this.mApn;
        editTextPreference2.setSummary((CharSequence) checkNull(editTextPreference2.getText()));
        EditTextPreference editTextPreference3 = this.mProxy;
        editTextPreference3.setSummary((CharSequence) checkNull(editTextPreference3.getText()));
        EditTextPreference editTextPreference4 = this.mPort;
        editTextPreference4.setSummary((CharSequence) checkNull(editTextPreference4.getText()));
        EditTextPreference editTextPreference5 = this.mUser;
        editTextPreference5.setSummary((CharSequence) checkNull(editTextPreference5.getText()));
        EditTextPreference editTextPreference6 = this.mServer;
        editTextPreference6.setSummary((CharSequence) checkNull(editTextPreference6.getText()));
        EditTextPreference editTextPreference7 = this.mPassword;
        editTextPreference7.setSummary((CharSequence) starify(editTextPreference7.getText()));
        EditTextPreference editTextPreference8 = this.mMmsProxy;
        editTextPreference8.setSummary((CharSequence) checkNull(editTextPreference8.getText()));
        EditTextPreference editTextPreference9 = this.mMmsPort;
        editTextPreference9.setSummary((CharSequence) checkNull(editTextPreference9.getText()));
        EditTextPreference editTextPreference10 = this.mMmsc;
        editTextPreference10.setSummary((CharSequence) checkNull(editTextPreference10.getText()));
        EditTextPreference editTextPreference11 = this.mMcc;
        editTextPreference11.setSummary((CharSequence) formatInteger(checkNull(editTextPreference11.getText())));
        EditTextPreference editTextPreference12 = this.mMnc;
        editTextPreference12.setSummary((CharSequence) formatInteger(checkNull(editTextPreference12.getText())));
        EditTextPreference editTextPreference13 = this.mApnType;
        editTextPreference13.setSummary((CharSequence) checkNull(editTextPreference13.getText()));
        String value = this.mAuthType.getValue();
        if (value != null) {
            int parseInt = Integer.parseInt(value);
            this.mAuthType.setValueIndex(parseInt);
            this.mAuthType.setSummary(getResources().getStringArray(R.array.apn_auth_entries)[parseInt]);
        } else {
            this.mAuthType.setSummary(sNotSet);
        }
        ListPreference listPreference = this.mProtocol;
        listPreference.setSummary(checkNull(protocolDescription(listPreference.getValue(), this.mProtocol)));
        ListPreference listPreference2 = this.mRoamingProtocol;
        listPreference2.setSummary(checkNull(protocolDescription(listPreference2.getValue(), this.mRoamingProtocol)));
        MultiSelectListPreference multiSelectListPreference = this.mBearerMulti;
        multiSelectListPreference.setSummary((CharSequence) checkNull(bearerMultiDescription(multiSelectListPreference.getValues())));
        ListPreference listPreference3 = this.mMvnoType;
        listPreference3.setSummary(checkNull(mvnoDescription(listPreference3.getValue())));
        EditTextPreference editTextPreference14 = this.mMvnoMatchData;
        editTextPreference14.setSummary((CharSequence) checkNullforMvnoValue(editTextPreference14.getText()));
        if (getResources().getBoolean(R.bool.config_allow_edit_carrier_enabled)) {
            this.mCarrierEnabled.setEnabled(true);
        } else {
            this.mCarrierEnabled.setEnabled(false);
        }
    }

    private String protocolDescription(String str, ListPreference listPreference) {
        String upperCase = checkNull(str).toUpperCase();
        if (upperCase.equals("IPV4")) {
            upperCase = "IP";
        }
        int findIndexOfValue = listPreference.findIndexOfValue(upperCase);
        if (findIndexOfValue == -1) {
            return null;
        }
        try {
            return getResources().getStringArray(R.array.apn_protocol_entries)[findIndexOfValue];
        } catch (ArrayIndexOutOfBoundsException unused) {
            return null;
        }
    }

    private String bearerMultiDescription(Set<String> set) {
        String[] stringArray = getResources().getStringArray(R.array.bearer_entries);
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (String findIndexOfValue : set) {
            int findIndexOfValue2 = this.mBearerMulti.findIndexOfValue(findIndexOfValue);
            if (z) {
                try {
                    sb.append(stringArray[findIndexOfValue2]);
                    z = false;
                } catch (ArrayIndexOutOfBoundsException unused) {
                }
            } else {
                sb.append(", " + stringArray[findIndexOfValue2]);
            }
        }
        String sb2 = sb.toString();
        if (!TextUtils.isEmpty(sb2)) {
            return sb2;
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0022, code lost:
        r4 = r8.mReadOnlyApnFields;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String mvnoDescription(java.lang.String r9) {
        /*
            r8 = this;
            androidx.preference.ListPreference r0 = r8.mMvnoType
            int r0 = r0.findIndexOfValue(r9)
            androidx.preference.ListPreference r1 = r8.mMvnoType
            java.lang.String r1 = r1.getValue()
            r2 = 0
            r3 = -1
            if (r0 != r3) goto L_0x0011
            return r2
        L_0x0011:
            android.content.res.Resources r3 = r8.getResources()
            r4 = 2130772121(0x7f010099, float:1.7147351E38)
            java.lang.String[] r3 = r3.getStringArray(r4)
            boolean r4 = r8.mReadOnlyApn
            r5 = 0
            r6 = 1
            if (r4 != 0) goto L_0x0035
            java.lang.String[] r4 = r8.mReadOnlyApnFields
            if (r4 == 0) goto L_0x0033
            java.util.List r4 = java.util.Arrays.asList(r4)
            java.lang.String r7 = "mvno_match_data"
            boolean r4 = r4.contains(r7)
            if (r4 == 0) goto L_0x0033
            goto L_0x0035
        L_0x0033:
            r4 = r5
            goto L_0x0036
        L_0x0035:
            r4 = r6
        L_0x0036:
            androidx.preference.EditTextPreference r7 = r8.mMvnoMatchData
            if (r4 != 0) goto L_0x003d
            if (r0 == 0) goto L_0x003d
            r5 = r6
        L_0x003d:
            r7.setEnabled(r5)
            if (r9 == 0) goto L_0x00e5
            boolean r9 = r9.equals(r1)
            if (r9 != 0) goto L_0x00e5
            r9 = r3[r0]
            java.lang.String r1 = "SPN"
            boolean r9 = r9.equals(r1)
            if (r9 == 0) goto L_0x0072
            android.content.Context r9 = r8.getContext()
            java.lang.Class<android.telephony.TelephonyManager> r1 = android.telephony.TelephonyManager.class
            java.lang.Object r9 = r9.getSystemService(r1)
            android.telephony.TelephonyManager r9 = (android.telephony.TelephonyManager) r9
            int r1 = r8.mSubId
            android.telephony.TelephonyManager r1 = r9.createForSubscriptionId(r1)
            if (r1 == 0) goto L_0x0067
            r9 = r1
        L_0x0067:
            androidx.preference.EditTextPreference r8 = r8.mMvnoMatchData
            java.lang.String r9 = r9.getSimOperatorName()
            r8.setText(r9)
            goto L_0x00e5
        L_0x0072:
            r9 = r3[r0]
            java.lang.String r1 = "IMSI"
            boolean r9 = r9.equals(r1)
            java.lang.String r1 = ""
            if (r9 == 0) goto L_0x00b7
            com.android.settings.network.ProxySubscriptionManager r9 = r8.mProxySubscriptionMgr
            int r4 = r8.mSubId
            android.telephony.SubscriptionInfo r9 = r9.getAccessibleSubscriptionInfo(r4)
            if (r9 != 0) goto L_0x008a
            r4 = r1
            goto L_0x0092
        L_0x008a:
            java.lang.String r4 = r9.getMccString()
            java.lang.String r4 = java.util.Objects.toString(r4, r1)
        L_0x0092:
            if (r9 != 0) goto L_0x0095
            goto L_0x009d
        L_0x0095:
            java.lang.String r9 = r9.getMncString()
            java.lang.String r1 = java.util.Objects.toString(r9, r1)
        L_0x009d:
            androidx.preference.EditTextPreference r8 = r8.mMvnoMatchData
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            r9.append(r4)
            r9.append(r1)
            java.lang.String r1 = "x"
            r9.append(r1)
            java.lang.String r9 = r9.toString()
            r8.setText(r9)
            goto L_0x00e5
        L_0x00b7:
            r9 = r3[r0]
            java.lang.String r4 = "GID"
            boolean r9 = r9.equals(r4)
            if (r9 == 0) goto L_0x00e0
            android.content.Context r9 = r8.getContext()
            java.lang.Class<android.telephony.TelephonyManager> r1 = android.telephony.TelephonyManager.class
            java.lang.Object r9 = r9.getSystemService(r1)
            android.telephony.TelephonyManager r9 = (android.telephony.TelephonyManager) r9
            int r1 = r8.mSubId
            android.telephony.TelephonyManager r1 = r9.createForSubscriptionId(r1)
            if (r1 == 0) goto L_0x00d6
            r9 = r1
        L_0x00d6:
            androidx.preference.EditTextPreference r8 = r8.mMvnoMatchData
            java.lang.String r9 = r9.getGroupIdLevel1()
            r8.setText(r9)
            goto L_0x00e5
        L_0x00e0:
            androidx.preference.EditTextPreference r8 = r8.mMvnoMatchData
            r8.setText(r1)
        L_0x00e5:
            r8 = r3[r0]     // Catch:{ ArrayIndexOutOfBoundsException -> 0x00e8 }
            return r8
        L_0x00e8:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.network.apn.ApnEditor.mvnoDescription(java.lang.String):java.lang.String");
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        String key = preference.getKey();
        if ("auth_type".equals(key)) {
            try {
                int parseInt = Integer.parseInt((String) obj);
                this.mAuthType.setValueIndex(parseInt);
                this.mAuthType.setSummary(getResources().getStringArray(R.array.apn_auth_entries)[parseInt]);
                return true;
            } catch (NumberFormatException unused) {
                return false;
            }
        } else if ("apn_type".equals(key)) {
            String str = (String) obj;
            if (TextUtils.isEmpty(str) && !ArrayUtils.isEmpty(this.mDefaultApnTypes)) {
                str = getEditableApnType(this.mDefaultApnTypes);
            }
            if (TextUtils.isEmpty(str)) {
                return true;
            }
            this.mApnType.setSummary((CharSequence) str);
            return true;
        } else if ("apn_protocol".equals(key)) {
            String str2 = (String) obj;
            String protocolDescription = protocolDescription(str2, this.mProtocol);
            if (protocolDescription == null) {
                return false;
            }
            this.mProtocol.setSummary(protocolDescription);
            this.mProtocol.setValue(str2);
            return true;
        } else if ("apn_roaming_protocol".equals(key)) {
            String str3 = (String) obj;
            String protocolDescription2 = protocolDescription(str3, this.mRoamingProtocol);
            if (protocolDescription2 == null) {
                return false;
            }
            this.mRoamingProtocol.setSummary(protocolDescription2);
            this.mRoamingProtocol.setValue(str3);
            return true;
        } else if ("bearer_multi".equals(key)) {
            Set set = (Set) obj;
            String bearerMultiDescription = bearerMultiDescription(set);
            if (bearerMultiDescription == null) {
                return false;
            }
            this.mBearerMulti.setValues(set);
            this.mBearerMulti.setSummary((CharSequence) bearerMultiDescription);
            return true;
        } else if ("mvno_type".equals(key)) {
            String str4 = (String) obj;
            String mvnoDescription = mvnoDescription(str4);
            if (mvnoDescription == null) {
                return false;
            }
            this.mMvnoType.setValue(str4);
            this.mMvnoType.setSummary(mvnoDescription);
            EditTextPreference editTextPreference = this.mMvnoMatchData;
            editTextPreference.setSummary((CharSequence) checkNullforMvnoValue(editTextPreference.getText()));
            return true;
        } else if ("apn_password".equals(key)) {
            this.mPassword.setSummary((CharSequence) starify(obj != null ? String.valueOf(obj) : ""));
            return true;
        } else if ("carrier_enabled".equals(key)) {
            return true;
        } else {
            preference.setSummary((CharSequence) checkNull(obj != null ? String.valueOf(obj) : null));
            return true;
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        if (!this.mNewApn && !this.mReadOnlyApn) {
            menu.add(0, 1, 0, R.string.menu_delete).setIcon(R.drawable.ic_delete);
        }
        if (!this.mReadOnlyApn) {
            menu.add(0, 2, 0, R.string.menu_save).setIcon(17301582);
        }
        menu.add(0, 3, 0, R.string.menu_cancel).setIcon(17301560);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 1) {
            deleteApn();
            finish();
            return true;
        } else if (itemId == 2) {
            if (validateAndSaveApnData()) {
                finish();
            }
            return true;
        } else if (itemId != 3) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            finish();
            return true;
        }
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        view.setOnKeyListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() != 0 || i != 4) {
            return false;
        }
        if (!validateAndSaveApnData()) {
            return true;
        }
        finish();
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean setStringValueAndCheckIfDiff(ContentValues contentValues, String str, String str2, boolean z, int i) {
        String string = this.mApnData.getString(i);
        boolean z2 = z || ((!TextUtils.isEmpty(str2) || !TextUtils.isEmpty(string)) && (str2 == null || !str2.equals(string)));
        if (z2 && str2 != null) {
            contentValues.put(str, str2);
        }
        return z2;
    }

    /* access modifiers changed from: package-private */
    public boolean setIntValueAndCheckIfDiff(ContentValues contentValues, String str, int i, boolean z, int i2) {
        boolean z2 = z || i != this.mApnData.getInteger(i2).intValue();
        if (z2) {
            contentValues.put(str, Integer.valueOf(i));
        }
        return z2;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x01af, code lost:
        r0 = r13.mBearerInitialVal;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean validateAndSaveApnData() {
        /*
            r13 = this;
            boolean r0 = r13.mReadOnlyApn
            r6 = 1
            java.lang.Integer r7 = java.lang.Integer.valueOf(r6)
            if (r0 == 0) goto L_0x000a
            return r6
        L_0x000a:
            androidx.preference.EditTextPreference r0 = r13.mName
            java.lang.String r0 = r0.getText()
            java.lang.String r3 = r13.checkNotSet(r0)
            androidx.preference.EditTextPreference r0 = r13.mApn
            java.lang.String r0 = r0.getText()
            java.lang.String r8 = r13.checkNotSet(r0)
            androidx.preference.EditTextPreference r0 = r13.mMcc
            java.lang.String r0 = r0.getText()
            java.lang.String r9 = r13.checkNotSet(r0)
            androidx.preference.EditTextPreference r0 = r13.mMnc
            java.lang.String r0 = r0.getText()
            java.lang.String r10 = r13.checkNotSet(r0)
            java.lang.String r0 = r13.validateApnData()
            r11 = 0
            if (r0 == 0) goto L_0x003d
            r13.showError()
            return r11
        L_0x003d:
            android.content.ContentValues r12 = new android.content.ContentValues
            r12.<init>()
            boolean r4 = r13.mNewApn
            r5 = 1
            java.lang.String r2 = "name"
            r0 = r13
            r1 = r12
            boolean r4 = r0.setStringValueAndCheckIfDiff(r1, r2, r3, r4, r5)
            r5 = 2
            java.lang.String r2 = "apn"
            r3 = r8
            boolean r4 = r0.setStringValueAndCheckIfDiff(r1, r2, r3, r4, r5)
            androidx.preference.EditTextPreference r0 = r13.mProxy
            java.lang.String r0 = r0.getText()
            java.lang.String r3 = r13.checkNotSet(r0)
            r5 = 3
            java.lang.String r2 = "proxy"
            r0 = r13
            boolean r4 = r0.setStringValueAndCheckIfDiff(r1, r2, r3, r4, r5)
            androidx.preference.EditTextPreference r0 = r13.mPort
            java.lang.String r0 = r0.getText()
            java.lang.String r3 = r13.checkNotSet(r0)
            r5 = 4
            java.lang.String r2 = "port"
            r0 = r13
            boolean r4 = r0.setStringValueAndCheckIfDiff(r1, r2, r3, r4, r5)
            androidx.preference.EditTextPreference r0 = r13.mMmsProxy
            java.lang.String r0 = r0.getText()
            java.lang.String r3 = r13.checkNotSet(r0)
            r5 = 12
            java.lang.String r2 = "mmsproxy"
            r0 = r13
            boolean r4 = r0.setStringValueAndCheckIfDiff(r1, r2, r3, r4, r5)
            androidx.preference.EditTextPreference r0 = r13.mMmsPort
            java.lang.String r0 = r0.getText()
            java.lang.String r3 = r13.checkNotSet(r0)
            r5 = 13
            java.lang.String r2 = "mmsport"
            r0 = r13
            boolean r4 = r0.setStringValueAndCheckIfDiff(r1, r2, r3, r4, r5)
            androidx.preference.EditTextPreference r0 = r13.mUser
            java.lang.String r0 = r0.getText()
            java.lang.String r3 = r13.checkNotSet(r0)
            r5 = 5
            java.lang.String r2 = "user"
            r0 = r13
            boolean r4 = r0.setStringValueAndCheckIfDiff(r1, r2, r3, r4, r5)
            androidx.preference.EditTextPreference r0 = r13.mServer
            java.lang.String r0 = r0.getText()
            java.lang.String r3 = r13.checkNotSet(r0)
            r5 = 6
            java.lang.String r2 = "server"
            r0 = r13
            boolean r4 = r0.setStringValueAndCheckIfDiff(r1, r2, r3, r4, r5)
            androidx.preference.EditTextPreference r0 = r13.mPassword
            java.lang.String r0 = r0.getText()
            java.lang.String r3 = r13.checkNotSet(r0)
            r5 = 7
            java.lang.String r2 = "password"
            r0 = r13
            boolean r4 = r0.setStringValueAndCheckIfDiff(r1, r2, r3, r4, r5)
            androidx.preference.EditTextPreference r0 = r13.mMmsc
            java.lang.String r0 = r0.getText()
            java.lang.String r3 = r13.checkNotSet(r0)
            r5 = 8
            java.lang.String r2 = "mmsc"
            r0 = r13
            boolean r4 = r0.setStringValueAndCheckIfDiff(r1, r2, r3, r4, r5)
            androidx.preference.ListPreference r0 = r13.mAuthType
            java.lang.String r0 = r0.getValue()
            if (r0 == 0) goto L_0x00ff
            int r3 = java.lang.Integer.parseInt(r0)
            r5 = 14
            java.lang.String r2 = "authtype"
            r0 = r13
            r1 = r12
            boolean r0 = r0.setIntValueAndCheckIfDiff(r1, r2, r3, r4, r5)
            r4 = r0
        L_0x00ff:
            androidx.preference.ListPreference r0 = r13.mProtocol
            java.lang.String r0 = r0.getValue()
            java.lang.String r3 = r13.checkNotSet(r0)
            r5 = 16
            java.lang.String r2 = "protocol"
            r0 = r13
            r1 = r12
            boolean r4 = r0.setStringValueAndCheckIfDiff(r1, r2, r3, r4, r5)
            androidx.preference.ListPreference r0 = r13.mRoamingProtocol
            java.lang.String r0 = r0.getValue()
            java.lang.String r3 = r13.checkNotSet(r0)
            r5 = 20
            java.lang.String r2 = "roaming_protocol"
            r0 = r13
            boolean r4 = r0.setStringValueAndCheckIfDiff(r1, r2, r3, r4, r5)
            java.lang.String r0 = r13.getUserEnteredApnType()
            java.lang.String r3 = r13.checkNotSet(r0)
            r5 = 15
            java.lang.String r2 = "type"
            r0 = r13
            boolean r4 = r0.setStringValueAndCheckIfDiff(r1, r2, r3, r4, r5)
            r5 = 9
            java.lang.String r2 = "mcc"
            r3 = r9
            boolean r4 = r0.setStringValueAndCheckIfDiff(r1, r2, r3, r4, r5)
            r5 = 10
            java.lang.String r2 = "mnc"
            r3 = r10
            boolean r4 = r0.setStringValueAndCheckIfDiff(r1, r2, r3, r4, r5)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r9)
            r0.append(r10)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "numeric"
            r12.put(r1, r0)
            java.lang.String r0 = r13.mCurMnc
            if (r0 == 0) goto L_0x0178
            java.lang.String r1 = r13.mCurMcc
            if (r1 == 0) goto L_0x0178
            boolean r0 = r0.equals(r10)
            if (r0 == 0) goto L_0x0178
            java.lang.String r0 = r13.mCurMcc
            boolean r0 = r0.equals(r9)
            if (r0 == 0) goto L_0x0178
            java.lang.String r0 = "current"
            r12.put(r0, r7)
        L_0x0178:
            androidx.preference.MultiSelectListPreference r0 = r13.mBearerMulti
            java.util.Set r0 = r0.getValues()
            java.util.Iterator r0 = r0.iterator()
            r1 = r11
        L_0x0183:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x01a1
            java.lang.Object r2 = r0.next()
            java.lang.String r2 = (java.lang.String) r2
            int r3 = java.lang.Integer.parseInt(r2)
            if (r3 != 0) goto L_0x0197
            r8 = r11
            goto L_0x01a2
        L_0x0197:
            int r2 = java.lang.Integer.parseInt(r2)
            int r2 = getBitmaskForTech(r2)
            r1 = r1 | r2
            goto L_0x0183
        L_0x01a1:
            r8 = r1
        L_0x01a2:
            r5 = 19
            java.lang.String r2 = "bearer_bitmask"
            r0 = r13
            r1 = r12
            r3 = r8
            boolean r4 = r0.setIntValueAndCheckIfDiff(r1, r2, r3, r4, r5)
            if (r8 == 0) goto L_0x01be
            int r0 = r13.mBearerInitialVal
            if (r0 != 0) goto L_0x01b4
            goto L_0x01be
        L_0x01b4:
            boolean r0 = bitmaskHasTech(r8, r0)
            if (r0 == 0) goto L_0x01be
            int r0 = r13.mBearerInitialVal
            r3 = r0
            goto L_0x01bf
        L_0x01be:
            r3 = r11
        L_0x01bf:
            r5 = 18
            java.lang.String r2 = "bearer"
            r0 = r13
            r1 = r12
            boolean r4 = r0.setIntValueAndCheckIfDiff(r1, r2, r3, r4, r5)
            androidx.preference.ListPreference r0 = r13.mMvnoType
            java.lang.String r0 = r0.getValue()
            java.lang.String r3 = r13.checkNotSet(r0)
            r5 = 21
            java.lang.String r2 = "mvno_type"
            r0 = r13
            boolean r4 = r0.setStringValueAndCheckIfDiff(r1, r2, r3, r4, r5)
            androidx.preference.EditTextPreference r0 = r13.mMvnoMatchData
            java.lang.String r0 = r0.getText()
            java.lang.String r3 = r13.checkNotSet(r0)
            r5 = 22
            java.lang.String r2 = "mvno_match_data"
            r0 = r13
            boolean r4 = r0.setStringValueAndCheckIfDiff(r1, r2, r3, r4, r5)
            androidx.preference.SwitchPreference r0 = r13.mCarrierEnabled
            boolean r3 = r0.isChecked()
            r5 = 17
            java.lang.String r2 = "carrier_enabled"
            r0 = r13
            r1 = r12
            boolean r0 = r0.setIntValueAndCheckIfDiff(r1, r2, r3, r4, r5)
            java.lang.String r1 = "edited"
            r12.put(r1, r7)
            if (r0 == 0) goto L_0x021a
            com.android.settings.network.apn.ApnEditor$ApnData r0 = r13.mApnData
            android.net.Uri r0 = r0.getUri()
            if (r0 != 0) goto L_0x0211
            android.net.Uri r0 = r13.mCarrierUri
            goto L_0x0217
        L_0x0211:
            com.android.settings.network.apn.ApnEditor$ApnData r0 = r13.mApnData
            android.net.Uri r0 = r0.getUri()
        L_0x0217:
            r13.updateApnDataToDatabase(r0, r12)
        L_0x021a:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.network.apn.ApnEditor.validateAndSaveApnData():boolean");
    }

    private void updateApnDataToDatabase(Uri uri, ContentValues contentValues) {
        ThreadUtils.postOnBackgroundThread((Runnable) new ApnEditor$$ExternalSyntheticLambda0(this, uri, contentValues));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateApnDataToDatabase$0(Uri uri, ContentValues contentValues) {
        if (!uri.equals(this.mCarrierUri)) {
            getContentResolver().update(uri, contentValues, (String) null, (String[]) null);
        } else if (getContentResolver().insert(this.mCarrierUri, contentValues) == null) {
            String str = TAG;
            Log.e(str, "Can't add a new apn to database " + this.mCarrierUri);
        }
    }

    /* access modifiers changed from: package-private */
    public String validateApnData() {
        String str;
        String checkNotSet = checkNotSet(this.mName.getText());
        String checkNotSet2 = checkNotSet(this.mApn.getText());
        String checkNotSet3 = checkNotSet(this.mMcc.getText());
        String checkNotSet4 = checkNotSet(this.mMnc.getText());
        if (TextUtils.isEmpty(checkNotSet)) {
            str = getResources().getString(R.string.error_name_empty);
        } else if (TextUtils.isEmpty(checkNotSet2)) {
            str = getResources().getString(R.string.error_apn_empty);
        } else if (checkNotSet3 == null || checkNotSet3.length() != 3) {
            str = getResources().getString(R.string.error_mcc_not3);
        } else if (checkNotSet4 == null || (checkNotSet4.length() & 65534) != 2) {
            str = getResources().getString(R.string.error_mnc_not23);
        } else {
            str = null;
        }
        if (str != null || ArrayUtils.isEmpty(this.mReadOnlyApnTypes) || !apnTypesMatch(this.mReadOnlyApnTypes, getUserEnteredApnType())) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (String str2 : this.mReadOnlyApnTypes) {
            sb.append(str2);
            sb.append(", ");
            Log.d(TAG, "validateApnData: appending type: " + str2);
        }
        if (sb.length() >= 2) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return String.format(getResources().getString(R.string.error_adding_apn_type), new Object[]{sb});
    }

    /* access modifiers changed from: package-private */
    public void showError() {
        ErrorDialog.showError(this);
    }

    private void deleteApn() {
        if (this.mApnData.getUri() != null) {
            getContentResolver().delete(this.mApnData.getUri(), (String) null, (String[]) null);
            this.mApnData = new ApnData(sProjection.length);
        }
    }

    private String starify(String str) {
        if (str == null || str.length() == 0) {
            return sNotSet;
        }
        int length = str.length();
        char[] cArr = new char[length];
        for (int i = 0; i < length; i++) {
            cArr[i] = '*';
        }
        return new String(cArr);
    }

    private String checkNull(String str) {
        return TextUtils.isEmpty(str) ? sNotSet : str;
    }

    private String checkNullforMvnoValue(String str) {
        return TextUtils.isEmpty(str) ? getResources().getString(R.string.apn_not_set_for_mvno) : str;
    }

    private String checkNotSet(String str) {
        if (sNotSet.equals(str)) {
            return null;
        }
        return str;
    }

    /* access modifiers changed from: package-private */
    public String getUserEnteredApnType() {
        String text = this.mApnType.getText();
        if (text != null) {
            text = text.trim();
        }
        if (TextUtils.isEmpty(text) || "*".equals(text)) {
            text = getEditableApnType(APN_TYPES);
        }
        String str = TAG;
        Log.d(str, "getUserEnteredApnType: changed apn type to editable apn types: " + text);
        return text;
    }

    private String getEditableApnType(String[] strArr) {
        StringBuilder sb = new StringBuilder();
        List asList = Arrays.asList(this.mReadOnlyApnTypes);
        boolean z = true;
        for (String str : strArr) {
            if (!asList.contains(str) && !str.equals("ia") && !str.equals("emergency") && !str.equals("mcx")) {
                if (z) {
                    z = false;
                } else {
                    sb.append(",");
                }
                sb.append(str);
            }
        }
        return sb.toString();
    }

    private void initApnEditorUi() {
        addPreferencesFromResource(R.xml.apn_editor);
        sNotSet = getResources().getString(R.string.apn_not_set);
        this.mName = (EditTextPreference) findPreference("apn_name");
        this.mApn = (EditTextPreference) findPreference("apn_apn");
        this.mProxy = (EditTextPreference) findPreference("apn_http_proxy");
        this.mPort = (EditTextPreference) findPreference("apn_http_port");
        this.mUser = (EditTextPreference) findPreference("apn_user");
        this.mServer = (EditTextPreference) findPreference("apn_server");
        this.mPassword = (EditTextPreference) findPreference("apn_password");
        this.mMmsProxy = (EditTextPreference) findPreference("apn_mms_proxy");
        this.mMmsPort = (EditTextPreference) findPreference("apn_mms_port");
        this.mMmsc = (EditTextPreference) findPreference("apn_mmsc");
        this.mMcc = (EditTextPreference) findPreference("apn_mcc");
        this.mMnc = (EditTextPreference) findPreference("apn_mnc");
        this.mApnType = (EditTextPreference) findPreference("apn_type");
        this.mAuthType = (ListPreference) findPreference("auth_type");
        this.mProtocol = (ListPreference) findPreference("apn_protocol");
        this.mRoamingProtocol = (ListPreference) findPreference("apn_roaming_protocol");
        this.mCarrierEnabled = (SwitchPreference) findPreference("carrier_enabled");
        this.mBearerMulti = (MultiSelectListPreference) findPreference("bearer_multi");
        this.mMvnoType = (ListPreference) findPreference("mvno_type");
        this.mMvnoMatchData = (EditTextPreference) findPreference("mvno_match_data");
    }

    private void getCarrierCustomizedConfig() {
        PersistableBundle configForSubId;
        this.mReadOnlyApn = false;
        this.mReadOnlyApnTypes = null;
        this.mReadOnlyApnFields = null;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) getSystemService("carrier_config");
        if (carrierConfigManager != null && (configForSubId = carrierConfigManager.getConfigForSubId(this.mSubId)) != null) {
            String[] stringArray = configForSubId.getStringArray("read_only_apn_types_string_array");
            this.mReadOnlyApnTypes = stringArray;
            if (!ArrayUtils.isEmpty(stringArray)) {
                String str = TAG;
                Log.d(str, "onCreate: read only APN type: " + Arrays.toString(this.mReadOnlyApnTypes));
            }
            this.mReadOnlyApnFields = configForSubId.getStringArray("read_only_apn_fields_string_array");
            String[] stringArray2 = configForSubId.getStringArray("apn_settings_default_apn_types_string_array");
            this.mDefaultApnTypes = stringArray2;
            if (!ArrayUtils.isEmpty(stringArray2)) {
                String str2 = TAG;
                Log.d(str2, "onCreate: default apn types: " + Arrays.toString(this.mDefaultApnTypes));
            }
            String string = configForSubId.getString("apn.settings_default_protocol_string");
            this.mDefaultApnProtocol = string;
            if (!TextUtils.isEmpty(string)) {
                String str3 = TAG;
                Log.d(str3, "onCreate: default apn protocol: " + this.mDefaultApnProtocol);
            }
            String string2 = configForSubId.getString("apn.settings_default_roaming_protocol_string");
            this.mDefaultApnRoamingProtocol = string2;
            if (!TextUtils.isEmpty(string2)) {
                String str4 = TAG;
                Log.d(str4, "onCreate: default apn roaming protocol: " + this.mDefaultApnRoamingProtocol);
            }
        }
    }

    private void setCarrierCustomizedConfigToUi() {
        if (TextUtils.isEmpty(this.mApnType.getText()) && !ArrayUtils.isEmpty(this.mDefaultApnTypes)) {
            String editableApnType = getEditableApnType(this.mDefaultApnTypes);
            this.mApnType.setText(editableApnType);
            this.mApnType.setSummary((CharSequence) editableApnType);
        }
        String protocolDescription = protocolDescription(this.mDefaultApnProtocol, this.mProtocol);
        if (TextUtils.isEmpty(this.mProtocol.getValue()) && !TextUtils.isEmpty(protocolDescription)) {
            this.mProtocol.setValue(this.mDefaultApnProtocol);
            this.mProtocol.setSummary(protocolDescription);
        }
        String protocolDescription2 = protocolDescription(this.mDefaultApnRoamingProtocol, this.mRoamingProtocol);
        if (TextUtils.isEmpty(this.mRoamingProtocol.getValue()) && !TextUtils.isEmpty(protocolDescription2)) {
            this.mRoamingProtocol.setValue(this.mDefaultApnRoamingProtocol);
            this.mRoamingProtocol.setSummary(protocolDescription2);
        }
    }

    public static class ErrorDialog extends InstrumentedDialogFragment {
        public int getMetricsCategory() {
            return 530;
        }

        public static void showError(ApnEditor apnEditor) {
            ErrorDialog errorDialog = new ErrorDialog();
            errorDialog.setTargetFragment(apnEditor, 0);
            errorDialog.show(apnEditor.getFragmentManager(), "error");
        }

        public Dialog onCreateDialog(Bundle bundle) {
            return new AlertDialog.Builder(getContext()).setTitle((int) R.string.error_title).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).setMessage((CharSequence) ((ApnEditor) getTargetFragment()).validateApnData()).create();
        }
    }

    /* access modifiers changed from: package-private */
    public ApnData getApnDataFromUri(Uri uri) {
        ApnData apnData;
        Cursor query = getContentResolver().query(uri, sProjection, (String) null, (String[]) null, (String) null);
        if (query != null) {
            try {
                query.moveToFirst();
                apnData = new ApnData(uri, query);
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
        } else {
            apnData = null;
        }
        if (query != null) {
            query.close();
        }
        if (apnData == null) {
            String str = TAG;
            Log.d(str, "Can't get apnData from Uri " + uri);
        }
        return apnData;
        throw th;
    }

    static class ApnData {
        Object[] mData;
        Uri mUri;

        ApnData(int i) {
            this.mData = new Object[i];
        }

        ApnData(Uri uri, Cursor cursor) {
            this.mUri = uri;
            this.mData = new Object[cursor.getColumnCount()];
            for (int i = 0; i < this.mData.length; i++) {
                int type = cursor.getType(i);
                if (type == 1) {
                    this.mData[i] = Integer.valueOf(cursor.getInt(i));
                } else if (type == 2) {
                    this.mData[i] = Float.valueOf(cursor.getFloat(i));
                } else if (type == 3) {
                    this.mData[i] = cursor.getString(i);
                } else if (type != 4) {
                    this.mData[i] = null;
                } else {
                    this.mData[i] = cursor.getBlob(i);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public Uri getUri() {
            return this.mUri;
        }

        /* access modifiers changed from: package-private */
        public Integer getInteger(int i) {
            return (Integer) this.mData[i];
        }

        /* access modifiers changed from: package-private */
        public Integer getInteger(int i, Integer num) {
            Integer integer = getInteger(i);
            return integer == null ? num : integer;
        }

        /* access modifiers changed from: package-private */
        public String getString(int i) {
            return (String) this.mData[i];
        }
    }
}
