package com.android.wsuinterface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IGetNetworkGroupSubscriptionsCallback extends IInterface {
    void onAvailable(List<NetworkGroupSubscription> list) throws RemoteException;

    public static abstract class Stub extends Binder implements IGetNetworkGroupSubscriptionsCallback {
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.wsuinterface.IGetNetworkGroupSubscriptionsCallback");
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString("com.android.wsuinterface.IGetNetworkGroupSubscriptionsCallback");
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel.enforceInterface("com.android.wsuinterface.IGetNetworkGroupSubscriptionsCallback");
                onAvailable(parcel.createTypedArrayList(NetworkGroupSubscription.CREATOR));
                return true;
            }
        }
    }
}
