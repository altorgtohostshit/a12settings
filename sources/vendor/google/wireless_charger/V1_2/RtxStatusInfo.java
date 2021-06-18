package vendor.google.wireless_charger.V1_2;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.Objects;

public final class RtxStatusInfo {
    public int acctype = 0;
    public boolean chg_s = false;
    public int iout = 0;
    public int level = 0;
    public byte mode = 0;
    public byte reason = 0;
    public int vout = 0;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != RtxStatusInfo.class) {
            return false;
        }
        RtxStatusInfo rtxStatusInfo = (RtxStatusInfo) obj;
        return this.mode == rtxStatusInfo.mode && this.acctype == rtxStatusInfo.acctype && this.chg_s == rtxStatusInfo.chg_s && this.vout == rtxStatusInfo.vout && this.iout == rtxStatusInfo.iout && this.level == rtxStatusInfo.level && this.reason == rtxStatusInfo.reason;
    }

    public final int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.mode))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.acctype))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.chg_s))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.vout))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.iout))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.level))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.reason)))});
    }

    public final String toString() {
        return "{" + ".mode = " + rtxModeType.toString(this.mode) + ", .acctype = " + this.acctype + ", .chg_s = " + this.chg_s + ", .vout = " + this.vout + ", .iout = " + this.iout + ", .level = " + this.level + ", .reason = " + rtxReasonType.toString(this.reason) + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(28), 0);
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.mode = hwBlob.getInt8(0 + j);
        this.acctype = hwBlob.getInt32(4 + j);
        this.chg_s = hwBlob.getBool(8 + j);
        this.vout = hwBlob.getInt32(12 + j);
        this.iout = hwBlob.getInt32(16 + j);
        this.level = hwBlob.getInt32(20 + j);
        this.reason = hwBlob.getInt8(j + 24);
    }
}
