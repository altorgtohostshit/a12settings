package vendor.google.wireless_charger.V1_2;

public final class rtxReasonType {
    public static final String toString(byte b) {
        if (b == 0) {
            return "NONE";
        }
        if (b == 1) {
            return "BATTLOW";
        }
        if (b == 2) {
            return "OVERHEAT";
        }
        if (b == 3) {
            return "TXCONFLICT";
        }
        if (b == 4) {
            return "OCP";
        }
        if (b == 15) {
            return "UNKNOWN";
        }
        return "0x" + Integer.toHexString(Byte.toUnsignedInt(b));
    }
}
