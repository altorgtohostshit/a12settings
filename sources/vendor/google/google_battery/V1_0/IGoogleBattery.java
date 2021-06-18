package vendor.google.google_battery.V1_0;

import android.os.IHwInterface;
import android.os.RemoteException;

public interface IGoogleBattery extends IHwInterface {

    @FunctionalInterface
    public interface getChargingStageAndDeadlineCallback {
        void onValues(byte b, String str, int i);
    }

    void getChargingStageAndDeadline(getChargingStageAndDeadlineCallback getchargingstageanddeadlinecallback) throws RemoteException;

    byte setChargingDeadline(int i) throws RemoteException;
}
