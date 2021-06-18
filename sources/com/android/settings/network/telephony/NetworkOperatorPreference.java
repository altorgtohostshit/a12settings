package com.android.settings.network.telephony;

import android.content.Context;
import android.telephony.CellIdentity;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoTdscdma;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrength;
import androidx.preference.Preference;
import com.android.internal.telephony.OperatorInfo;
import com.android.settings.R;
import java.util.List;
import java.util.Objects;

public class NetworkOperatorPreference extends Preference {
    private CellIdentity mCellId;
    private CellInfo mCellInfo;
    private List<String> mForbiddenPlmns;
    private int mLevel;
    private boolean mShow4GForLTE;
    private boolean mUseNewApi;

    public NetworkOperatorPreference(Context context, CellInfo cellInfo, List<String> list, boolean z) {
        this(context, list, z);
        updateCell(cellInfo);
    }

    public NetworkOperatorPreference(Context context, CellIdentity cellIdentity, List<String> list, boolean z) {
        this(context, list, z);
        updateCell((CellInfo) null, cellIdentity);
    }

    private NetworkOperatorPreference(Context context, List<String> list, boolean z) {
        super(context);
        this.mLevel = -1;
        this.mForbiddenPlmns = list;
        this.mShow4GForLTE = z;
        this.mUseNewApi = context.getResources().getBoolean(17891539);
    }

    public void updateCell(CellInfo cellInfo) {
        updateCell(cellInfo, CellInfoUtil.getCellIdentity(cellInfo));
    }

    private void updateCell(CellInfo cellInfo, CellIdentity cellIdentity) {
        this.mCellInfo = cellInfo;
        this.mCellId = cellIdentity;
        refresh();
    }

    public boolean isSameCell(CellInfo cellInfo) {
        if (cellInfo == null) {
            return false;
        }
        return this.mCellId.equals(CellInfoUtil.getCellIdentity(cellInfo));
    }

    public void refresh() {
        String operatorName = getOperatorName();
        List<String> list = this.mForbiddenPlmns;
        if (list != null && list.contains(getOperatorNumeric())) {
            operatorName = operatorName + " " + getContext().getResources().getString(R.string.forbidden_network);
        }
        setTitle((CharSequence) Objects.toString(operatorName, ""));
        CellInfo cellInfo = this.mCellInfo;
        if (cellInfo != null) {
            CellSignalStrength cellSignalStrength = getCellSignalStrength(cellInfo);
            int level = cellSignalStrength != null ? cellSignalStrength.getLevel() : -1;
            this.mLevel = level;
            updateIcon(level);
        }
    }

    public void setIcon(int i) {
        updateIcon(i);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0036, code lost:
        r2 = (android.telephony.CellIdentityNr) r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getOperatorNumeric() {
        /*
            r2 = this;
            android.telephony.CellIdentity r2 = r2.mCellId
            r0 = 0
            if (r2 != 0) goto L_0x0006
            return r0
        L_0x0006:
            boolean r1 = r2 instanceof android.telephony.CellIdentityGsm
            if (r1 == 0) goto L_0x0011
            android.telephony.CellIdentityGsm r2 = (android.telephony.CellIdentityGsm) r2
            java.lang.String r2 = r2.getMobileNetworkOperator()
            return r2
        L_0x0011:
            boolean r1 = r2 instanceof android.telephony.CellIdentityWcdma
            if (r1 == 0) goto L_0x001c
            android.telephony.CellIdentityWcdma r2 = (android.telephony.CellIdentityWcdma) r2
            java.lang.String r2 = r2.getMobileNetworkOperator()
            return r2
        L_0x001c:
            boolean r1 = r2 instanceof android.telephony.CellIdentityTdscdma
            if (r1 == 0) goto L_0x0027
            android.telephony.CellIdentityTdscdma r2 = (android.telephony.CellIdentityTdscdma) r2
            java.lang.String r2 = r2.getMobileNetworkOperator()
            return r2
        L_0x0027:
            boolean r1 = r2 instanceof android.telephony.CellIdentityLte
            if (r1 == 0) goto L_0x0032
            android.telephony.CellIdentityLte r2 = (android.telephony.CellIdentityLte) r2
            java.lang.String r2 = r2.getMobileNetworkOperator()
            return r2
        L_0x0032:
            boolean r1 = r2 instanceof android.telephony.CellIdentityNr
            if (r1 == 0) goto L_0x0048
            android.telephony.CellIdentityNr r2 = (android.telephony.CellIdentityNr) r2
            java.lang.String r1 = r2.getMccString()
            if (r1 != 0) goto L_0x003f
            return r0
        L_0x003f:
            java.lang.String r2 = r2.getMncString()
            java.lang.String r2 = r1.concat(r2)
            return r2
        L_0x0048:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.network.telephony.NetworkOperatorPreference.getOperatorNumeric():java.lang.String");
    }

    public String getOperatorName() {
        return CellInfoUtil.getNetworkTitle(this.mCellId, getOperatorNumeric());
    }

    public OperatorInfo getOperatorInfo() {
        return new OperatorInfo(Objects.toString(this.mCellId.getOperatorAlphaLong(), ""), Objects.toString(this.mCellId.getOperatorAlphaShort(), ""), getOperatorNumeric(), getAccessNetworkTypeFromCellInfo(this.mCellInfo));
    }

    private int getIconIdForCell(CellInfo cellInfo) {
        if (cellInfo instanceof CellInfoGsm) {
            return R.drawable.signal_strength_g;
        }
        if (cellInfo instanceof CellInfoCdma) {
            return R.drawable.signal_strength_1x;
        }
        if ((cellInfo instanceof CellInfoWcdma) || (cellInfo instanceof CellInfoTdscdma)) {
            return R.drawable.signal_strength_3g;
        }
        if (cellInfo instanceof CellInfoLte) {
            return this.mShow4GForLTE ? R.drawable.ic_signal_strength_4g : R.drawable.signal_strength_lte;
        }
        if (cellInfo instanceof CellInfoNr) {
            return R.drawable.signal_strength_5g;
        }
        return 0;
    }

    private CellSignalStrength getCellSignalStrength(CellInfo cellInfo) {
        if (cellInfo instanceof CellInfoGsm) {
            return ((CellInfoGsm) cellInfo).getCellSignalStrength();
        }
        if (cellInfo instanceof CellInfoCdma) {
            return ((CellInfoCdma) cellInfo).getCellSignalStrength();
        }
        if (cellInfo instanceof CellInfoWcdma) {
            return ((CellInfoWcdma) cellInfo).getCellSignalStrength();
        }
        if (cellInfo instanceof CellInfoTdscdma) {
            return ((CellInfoTdscdma) cellInfo).getCellSignalStrength();
        }
        if (cellInfo instanceof CellInfoLte) {
            return ((CellInfoLte) cellInfo).getCellSignalStrength();
        }
        if (cellInfo instanceof CellInfoNr) {
            return ((CellInfoNr) cellInfo).getCellSignalStrength();
        }
        return null;
    }

    private int getAccessNetworkTypeFromCellInfo(CellInfo cellInfo) {
        if (cellInfo instanceof CellInfoGsm) {
            return 1;
        }
        if (cellInfo instanceof CellInfoCdma) {
            return 4;
        }
        if ((cellInfo instanceof CellInfoWcdma) || (cellInfo instanceof CellInfoTdscdma)) {
            return 2;
        }
        if (cellInfo instanceof CellInfoLte) {
            return 3;
        }
        return cellInfo instanceof CellInfoNr ? 6 : 0;
    }

    private void updateIcon(int i) {
        if (this.mUseNewApi && i >= 0 && i < 5) {
            setIcon(MobileNetworkUtils.getSignalStrengthIcon(getContext(), i, 5, getIconIdForCell(this.mCellInfo), false));
        }
    }
}
