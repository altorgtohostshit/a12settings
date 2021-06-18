package android.frameworks.stats;

import android.os.Parcel;
import android.os.Parcelable;

public final class VendorAtomValue implements Parcelable {
    public static final Parcelable.Creator<VendorAtomValue> CREATOR = new Parcelable.Creator<VendorAtomValue>() {
        public VendorAtomValue createFromParcel(Parcel parcel) {
            return new VendorAtomValue(parcel);
        }

        public VendorAtomValue[] newArray(int i) {
            return new VendorAtomValue[i];
        }
    };
    private int _tag;
    private Object _value;

    public final int getStability() {
        return 1;
    }

    public VendorAtomValue() {
        this._tag = 0;
        this._value = 0;
    }

    private VendorAtomValue(Parcel parcel) {
        readFromParcel(parcel);
    }

    private VendorAtomValue(int i, Object obj) {
        this._tag = i;
        this._value = obj;
    }

    public int getTag() {
        return this._tag;
    }

    public static VendorAtomValue intValue(int i) {
        return new VendorAtomValue(0, (Object) Integer.valueOf(i));
    }

    public int getIntValue() {
        _assertTag(0);
        return ((Integer) this._value).intValue();
    }

    public static VendorAtomValue longValue(long j) {
        return new VendorAtomValue(1, (Object) Long.valueOf(j));
    }

    public long getLongValue() {
        _assertTag(1);
        return ((Long) this._value).longValue();
    }

    public float getFloatValue() {
        _assertTag(2);
        return ((Float) this._value).floatValue();
    }

    public String getStringValue() {
        _assertTag(3);
        return (String) this._value;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this._tag);
        int i2 = this._tag;
        if (i2 == 0) {
            parcel.writeInt(getIntValue());
        } else if (i2 == 1) {
            parcel.writeLong(getLongValue());
        } else if (i2 == 2) {
            parcel.writeFloat(getFloatValue());
        } else if (i2 == 3) {
            parcel.writeString(getStringValue());
        }
    }

    public void readFromParcel(Parcel parcel) {
        int readInt = parcel.readInt();
        if (readInt == 0) {
            _set(readInt, Integer.valueOf(parcel.readInt()));
        } else if (readInt == 1) {
            _set(readInt, Long.valueOf(parcel.readLong()));
        } else if (readInt == 2) {
            _set(readInt, Float.valueOf(parcel.readFloat()));
        } else if (readInt == 3) {
            _set(readInt, parcel.readString());
        } else {
            throw new IllegalArgumentException("union: unknown tag: " + readInt);
        }
    }

    public int describeContents() {
        getTag();
        return 0;
    }

    private void _assertTag(int i) {
        if (getTag() != i) {
            throw new IllegalStateException("bad access: " + _tagString(i) + ", " + _tagString(getTag()) + " is available.");
        }
    }

    private String _tagString(int i) {
        if (i == 0) {
            return "intValue";
        }
        if (i == 1) {
            return "longValue";
        }
        if (i == 2) {
            return "floatValue";
        }
        if (i == 3) {
            return "stringValue";
        }
        throw new IllegalStateException("unknown field: " + i);
    }

    private void _set(int i, Object obj) {
        this._tag = i;
        this._value = obj;
    }
}
