package com.google.android.systemui.reversecharging;

import android.frameworks.stats.IStats;
import android.frameworks.stats.VendorAtom;
import android.frameworks.stats.VendorAtomValue;
import android.hardware.google.pixel.vendor.PixelAtoms$ReverseDomainNames;
import android.os.ServiceManager;
import android.util.Log;
import java.util.Optional;

public class ReverseChargingMetrics {
    protected static final boolean DEBUG = Log.isLoggable("ReverseChargingMetrics", 3);
    private static final PixelAtoms$ReverseDomainNames RDN = ((PixelAtoms$ReverseDomainNames) PixelAtoms$ReverseDomainNames.newBuilder().build());

    private static VendorAtom createVendorAtom(int i) {
        VendorAtom vendorAtom = new VendorAtom();
        vendorAtom.reverseDomainName = RDN.getPixel();
        vendorAtom.values = new VendorAtomValue[i];
        return vendorAtom;
    }

    private static Optional<IStats> tryConnectingToStatsService() {
        String str = IStats.DESCRIPTOR + "/default";
        if (ServiceManager.isDeclared(str)) {
            return Optional.ofNullable(IStats.Stub.asInterface(ServiceManager.waitForDeclaredService(str)));
        }
        Log.e("ReverseChargingMetrics", "IStats is not registered");
        return Optional.empty();
    }

    private static void reportVendorAtom(VendorAtom vendorAtom) {
        try {
            Optional<IStats> tryConnectingToStatsService = tryConnectingToStatsService();
            if (tryConnectingToStatsService.isPresent()) {
                tryConnectingToStatsService.get().reportVendorAtom(vendorAtom);
                if (DEBUG) {
                    Log.i("ReverseChargingMetrics", "Report vendor atom OK, " + vendorAtom);
                }
            }
        } catch (Exception e) {
            Log.e("ReverseChargingMetrics", "Failed to log atom to IStats service, " + e);
        }
    }

    public static void logStopEvent(int i, int i2, long j) {
        VendorAtom createVendorAtom = createVendorAtom(3);
        createVendorAtom.atomId = 100038;
        createVendorAtom.values[0] = VendorAtomValue.intValue(i);
        createVendorAtom.values[1] = VendorAtomValue.intValue(i2);
        createVendorAtom.values[2] = VendorAtomValue.longValue(j);
        reportVendorAtom(createVendorAtom);
    }

    public static void logLowBatteryThresholdChange(int i) {
        VendorAtom createVendorAtom = createVendorAtom(2);
        createVendorAtom.atomId = 100039;
        createVendorAtom.values[0] = VendorAtomValue.intValue(1);
        createVendorAtom.values[1] = VendorAtomValue.intValue(i);
        reportVendorAtom(createVendorAtom);
    }
}
