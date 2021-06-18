package com.android.wsuinterface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IWsuService extends IInterface {
    void getNetworkGroupSubscriptions(IGetNetworkGroupSubscriptionsCallback iGetNetworkGroupSubscriptionsCallback) throws RemoteException;

    void registerSubscriptionProvisionStatusListener(ISubscriptionProvisionStatusListener iSubscriptionProvisionStatusListener) throws RemoteException;

    void unregisterSubscriptionProvisionStatusListener(ISubscriptionProvisionStatusListener iSubscriptionProvisionStatusListener) throws RemoteException;

    public static abstract class Stub extends Binder implements IWsuService {
        public static IWsuService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.android.wsuinterface.IWsuService");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IWsuService)) {
                return new Proxy(iBinder);
            }
            return (IWsuService) queryLocalInterface;
        }

        private static class Proxy implements IWsuService {
            public static IWsuService sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void getNetworkGroupSubscriptions(IGetNetworkGroupSubscriptionsCallback iGetNetworkGroupSubscriptionsCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.android.wsuinterface.IWsuService");
                    obtain.writeStrongBinder(iGetNetworkGroupSubscriptionsCallback != null ? iGetNetworkGroupSubscriptionsCallback.asBinder() : null);
                    if (this.mRemote.transact(1, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().getNetworkGroupSubscriptions(iGetNetworkGroupSubscriptionsCallback);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void registerSubscriptionProvisionStatusListener(ISubscriptionProvisionStatusListener iSubscriptionProvisionStatusListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.android.wsuinterface.IWsuService");
                    obtain.writeStrongBinder(iSubscriptionProvisionStatusListener != null ? iSubscriptionProvisionStatusListener.asBinder() : null);
                    if (this.mRemote.transact(2, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().registerSubscriptionProvisionStatusListener(iSubscriptionProvisionStatusListener);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void unregisterSubscriptionProvisionStatusListener(ISubscriptionProvisionStatusListener iSubscriptionProvisionStatusListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.android.wsuinterface.IWsuService");
                    obtain.writeStrongBinder(iSubscriptionProvisionStatusListener != null ? iSubscriptionProvisionStatusListener.asBinder() : null);
                    if (this.mRemote.transact(3, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().unregisterSubscriptionProvisionStatusListener(iSubscriptionProvisionStatusListener);
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static IWsuService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
