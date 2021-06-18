package android.frameworks.stats;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

public class VendorAtom implements Parcelable {
    public static final Parcelable.Creator<VendorAtom> CREATOR = new Parcelable.Creator<VendorAtom>() {
        public VendorAtom createFromParcel(Parcel parcel) {
            VendorAtom vendorAtom = new VendorAtom();
            vendorAtom.readFromParcel(parcel);
            return vendorAtom;
        }

        public VendorAtom[] newArray(int i) {
            return new VendorAtom[i];
        }
    };
    public int atomId = 0;
    public String reverseDomainName;
    public VendorAtomValue[] values;

    public final int getStability() {
        return 1;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeString(this.reverseDomainName);
        parcel.writeInt(this.atomId);
        parcel.writeTypedArray(this.values, 0);
        int dataPosition2 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition2 - dataPosition);
        parcel.setDataPosition(dataPosition2);
    }

    public final void readFromParcel(Parcel parcel) {
        int dataPosition = parcel.dataPosition();
        int readInt = parcel.readInt();
        if (readInt >= 0) {
            try {
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.reverseDomainName = parcel.readString();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.atomId = parcel.readInt();
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.values = (VendorAtomValue[]) parcel.createTypedArray(VendorAtomValue.CREATOR);
                            if (dataPosition <= Integer.MAX_VALUE - readInt) {
                                parcel.setDataPosition(dataPosition + readInt);
                                return;
                            }
                            throw new BadParcelableException("Overflow in the size of parcelable");
                        } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                            throw new BadParcelableException("Overflow in the size of parcelable");
                        }
                    } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                        throw new BadParcelableException("Overflow in the size of parcelable");
                    }
                } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
            } catch (Throwable th) {
                if (dataPosition > Integer.MAX_VALUE - readInt) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
                parcel.setDataPosition(dataPosition + readInt);
                throw th;
            }
        } else if (dataPosition > Integer.MAX_VALUE - readInt) {
            throw new BadParcelableException("Overflow in the size of parcelable");
        }
        parcel.setDataPosition(dataPosition + readInt);
    }

    public int describeContents() {
        return describeContents(this.values) | 0;
    }

    private int describeContents(Object obj) {
        if (obj == null) {
            return 0;
        }
        Class<?> cls = obj.getClass();
        if (cls.isArray() && cls.getComponentType() == Object.class) {
            int i = 0;
            for (Object describeContents : (Object[]) obj) {
                i |= describeContents(describeContents);
            }
            return i;
        } else if (obj instanceof Parcelable) {
            return ((Parcelable) obj).describeContents();
        } else {
            return 0;
        }
    }
}
