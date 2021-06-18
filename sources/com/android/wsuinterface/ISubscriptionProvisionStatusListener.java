package com.android.wsuinterface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISubscriptionProvisionStatusListener extends IInterface {
    void onStatusChanged(String str, int i) throws RemoteException;

    public static abstract class Stub extends Binder implements ISubscriptionProvisionStatusListener {
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.wsuinterface.ISubscriptionProvisionStatusListener");
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString("com.android.wsuinterface.ISubscriptionProvisionStatusListener");
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel.enforceInterface("com.android.wsuinterface.ISubscriptionProvisionStatusListener");
                onStatusChanged(parcel.readString(), parcel.readInt());
                return true;
            }
        }
    }
}
