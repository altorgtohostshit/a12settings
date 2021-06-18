package com.android.settings.network.telephony;

import android.telephony.CellIdentity;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityTdscdma;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoTdscdma;
import android.telephony.CellInfoWcdma;
import android.text.BidiFormatter;
import android.text.TextDirectionHeuristics;
import android.text.TextUtils;
import com.android.internal.telephony.OperatorInfo;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class CellInfoUtil {
    public static String getNetworkTitle(CellIdentity cellIdentity, String str) {
        if (cellIdentity != null) {
            String objects = Objects.toString(cellIdentity.getOperatorAlphaLong(), "");
            if (TextUtils.isEmpty(objects)) {
                objects = Objects.toString(cellIdentity.getOperatorAlphaShort(), "");
            }
            if (!TextUtils.isEmpty(objects)) {
                return objects;
            }
        }
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return BidiFormatter.getInstance().unicodeWrap(str, TextDirectionHeuristics.LTR);
    }

    public static CellIdentity getCellIdentity(CellInfo cellInfo) {
        if (cellInfo == null) {
            return null;
        }
        if (cellInfo instanceof CellInfoGsm) {
            return ((CellInfoGsm) cellInfo).getCellIdentity();
        }
        if (cellInfo instanceof CellInfoCdma) {
            return ((CellInfoCdma) cellInfo).getCellIdentity();
        }
        if (cellInfo instanceof CellInfoWcdma) {
            return ((CellInfoWcdma) cellInfo).getCellIdentity();
        }
        if (cellInfo instanceof CellInfoTdscdma) {
            return ((CellInfoTdscdma) cellInfo).getCellIdentity();
        }
        if (cellInfo instanceof CellInfoLte) {
            return ((CellInfoLte) cellInfo).getCellIdentity();
        }
        if (cellInfo instanceof CellInfoNr) {
            return ((CellInfoNr) cellInfo).getCellIdentity();
        }
        return null;
    }

    public static CellInfo convertOperatorInfoToCellInfo(OperatorInfo operatorInfo) {
        String str;
        String str2;
        String operatorNumeric = operatorInfo.getOperatorNumeric();
        if (operatorNumeric == null || !operatorNumeric.matches("^[0-9]{5,6}$")) {
            str2 = null;
            str = null;
        } else {
            String substring = operatorNumeric.substring(0, 3);
            str = operatorNumeric.substring(3);
            str2 = substring;
        }
        CellIdentityGsm cellIdentityGsm = new CellIdentityGsm(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, str2, str, operatorInfo.getOperatorAlphaLong(), operatorInfo.getOperatorAlphaShort(), Collections.emptyList());
        CellInfoGsm cellInfoGsm = new CellInfoGsm();
        cellInfoGsm.setCellIdentity(cellIdentityGsm);
        return cellInfoGsm;
    }

    public static String cellInfoListToString(List<CellInfo> list) {
        return (String) list.stream().map(CellInfoUtil$$ExternalSyntheticLambda0.INSTANCE).collect(Collectors.joining(", "));
    }

    public static String cellInfoToString(CellInfo cellInfo) {
        String str;
        String mccString;
        String mncString;
        String simpleName = cellInfo.getClass().getSimpleName();
        CellIdentity cellIdentity = getCellIdentity(cellInfo);
        String str2 = null;
        if (cellIdentity != null) {
            if (cellIdentity instanceof CellIdentityGsm) {
                CellIdentityGsm cellIdentityGsm = (CellIdentityGsm) cellIdentity;
                mccString = cellIdentityGsm.getMccString();
                mncString = cellIdentityGsm.getMncString();
            } else if (cellIdentity instanceof CellIdentityWcdma) {
                CellIdentityWcdma cellIdentityWcdma = (CellIdentityWcdma) cellIdentity;
                mccString = cellIdentityWcdma.getMccString();
                mncString = cellIdentityWcdma.getMncString();
            } else if (cellIdentity instanceof CellIdentityTdscdma) {
                CellIdentityTdscdma cellIdentityTdscdma = (CellIdentityTdscdma) cellIdentity;
                mccString = cellIdentityTdscdma.getMccString();
                mncString = cellIdentityTdscdma.getMncString();
            } else if (cellIdentity instanceof CellIdentityLte) {
                CellIdentityLte cellIdentityLte = (CellIdentityLte) cellIdentity;
                mccString = cellIdentityLte.getMccString();
                mncString = cellIdentityLte.getMncString();
            } else if (cellIdentity instanceof CellIdentityNr) {
                CellIdentityNr cellIdentityNr = (CellIdentityNr) cellIdentity;
                mccString = cellIdentityNr.getMccString();
                mncString = cellIdentityNr.getMncString();
            }
            String str3 = mccString;
            str = mncString;
            str2 = str3;
            return String.format("{CellType = %s, isRegistered = %b, mcc = %s, mnc = %s, alphaL = %s, alphaS = %s}", new Object[]{simpleName, Boolean.valueOf(cellInfo.isRegistered()), str2, str, cellIdentity.getOperatorAlphaLong(), cellIdentity.getOperatorAlphaShort()});
        }
        str = null;
        return String.format("{CellType = %s, isRegistered = %b, mcc = %s, mnc = %s, alphaL = %s, alphaS = %s}", new Object[]{simpleName, Boolean.valueOf(cellInfo.isRegistered()), str2, str, cellIdentity.getOperatorAlphaLong(), cellIdentity.getOperatorAlphaShort()});
    }
}
