package android.hidl.base.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.Objects;

public final class DebugInfo {
    public int arch = 0;
    public int pid = 0;
    public long ptr = 0;

    public static final class Architecture {
        public static final String toString(int i) {
            if (i == 0) {
                return "UNKNOWN";
            }
            if (i == 1) {
                return "IS_64BIT";
            }
            if (i == 2) {
                return "IS_32BIT";
            }
            return "0x" + Integer.toHexString(i);
        }
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != DebugInfo.class) {
            return false;
        }
        DebugInfo debugInfo = (DebugInfo) obj;
        return this.pid == debugInfo.pid && this.ptr == debugInfo.ptr && this.arch == debugInfo.arch;
    }

    public final int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.pid))), Integer.valueOf(HidlSupport.deepHashCode(Long.valueOf(this.ptr))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.arch)))});
    }

    public final String toString() {
        return "{" + ".pid = " + this.pid + ", .ptr = " + this.ptr + ", .arch = " + Architecture.toString(this.arch) + "}";
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(24);
        writeEmbeddedToBlob(hwBlob, 0);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt32(0 + j, this.pid);
        hwBlob.putInt64(8 + j, this.ptr);
        hwBlob.putInt32(j + 16, this.arch);
    }
}
