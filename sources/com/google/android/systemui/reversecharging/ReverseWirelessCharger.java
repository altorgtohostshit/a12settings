package com.google.android.systemui.reversecharging;

import android.content.Context;
import android.os.IHwBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import vendor.google.wireless_charger.V1_2.IWirelessCharger;
import vendor.google.wireless_charger.V1_2.IWirelessChargerRtxStatusCallback;
import vendor.google.wireless_charger.V1_2.RtxStatusInfo;

public class ReverseWirelessCharger extends IWirelessChargerRtxStatusCallback.Stub implements IHwBinder.DeathRecipient {
    private static final boolean DEBUG = Log.isLoggable("ReverseWirelessCharger", 3);
    private Context mContext;
    private final ArrayList<?> mIsDockPresentCallbacks = new ArrayList<>();
    private final LocalIsDockPresentCallback mLocalIsDockPresentCallback;
    private final LocalRtxInformationCallback mLocalRtxInformationCallback;
    private final Object mLock = new Object();
    private final ArrayList<RtxInformationCallback> mRtxInformationCallbacks = new ArrayList<>();
    private final ArrayList<RtxStatusCallback> mRtxStatusCallbacks = new ArrayList<>();
    private IWirelessCharger mWirelessCharger;

    public interface RtxInformationCallback {
    }

    public interface RtxStatusCallback {
        void onRtxStatusChanged(RtxStatusInfo rtxStatusInfo);
    }

    public ReverseWirelessCharger(Context context) {
        this.mContext = context;
        this.mLocalIsDockPresentCallback = new LocalIsDockPresentCallback();
        this.mLocalRtxInformationCallback = new LocalRtxInformationCallback();
    }

    public void serviceDied(long j) {
        Log.i("ReverseWirelessCharger", "serviceDied");
        this.mWirelessCharger = null;
    }

    private void initHALInterface() {
        if (this.mWirelessCharger == null) {
            try {
                IWirelessCharger service = IWirelessCharger.getService();
                this.mWirelessCharger = service;
                service.linkToDeath(this, 0);
                this.mWirelessCharger.registerRtxCallback(this);
            } catch (Exception e) {
                Log.i("ReverseWirelessCharger", "no wireless charger hal found: " + e.getMessage(), e);
                this.mWirelessCharger = null;
            }
        }
    }

    public boolean isRtxSupported() {
        initHALInterface();
        IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                return iWirelessCharger.isRtxSupported();
            } catch (Exception e) {
                Log.i("ReverseWirelessCharger", "isRtxSupported fail: ", e);
            }
        }
        return false;
    }

    public void addRtxInformationCallback(RtxInformationCallback rtxInformationCallback) {
        synchronized (this.mLock) {
            this.mRtxInformationCallbacks.add(rtxInformationCallback);
        }
    }

    public void setRtxMode(boolean z) {
        initHALInterface();
        IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                iWirelessCharger.setRtxMode(z);
            } catch (Exception e) {
                Log.i("ReverseWirelessCharger", "setRtxMode fail: ", e);
            }
        }
    }

    public boolean isRtxModeOn() {
        initHALInterface();
        IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                return iWirelessCharger.isRtxModeOn();
            } catch (Exception e) {
                Log.i("ReverseWirelessCharger", "isRtxModeOn fail: ", e);
            }
        }
        return false;
    }

    public void addRtxStatusCallback(RtxStatusCallback rtxStatusCallback) {
        synchronized (this.mLock) {
            this.mRtxStatusCallbacks.add(rtxStatusCallback);
        }
    }

    private void dispatchRtxStatusCallbacks(RtxStatusInfo rtxStatusInfo) {
        ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList(this.mRtxStatusCallbacks);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((RtxStatusCallback) it.next()).onRtxStatusChanged(rtxStatusInfo);
        }
    }

    public void rtxStatusInfoChanged(RtxStatusInfo rtxStatusInfo) throws RemoteException {
        dispatchRtxStatusCallbacks(rtxStatusInfo);
    }

    class LocalIsDockPresentCallback {
        LocalIsDockPresentCallback() {
        }
    }

    class LocalRtxInformationCallback {
        LocalRtxInformationCallback() {
        }
    }
}
