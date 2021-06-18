package android.frameworks.stats;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IStats extends IInterface {
    public static final String DESCRIPTOR = "android$frameworks$stats$IStats".replace('$', '.');

    void reportVendorAtom(VendorAtom vendorAtom) throws RemoteException;

    public static abstract class Stub extends Binder implements IStats {
        public static IStats asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IStats.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IStats)) {
                return new Proxy(iBinder);
            }
            return (IStats) queryLocalInterface;
        }

        private static class Proxy implements IStats {
            public static IStats sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void reportVendorAtom(VendorAtom vendorAtom) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStats.DESCRIPTOR);
                    if (vendorAtom != null) {
                        obtain.writeInt(1);
                        vendorAtom.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(1, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().reportVendorAtom(vendorAtom);
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static IStats getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
