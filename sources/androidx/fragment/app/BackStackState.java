package androidx.fragment.app;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

@SuppressLint({"BanParcelableUsage"})
class BackStackState implements Parcelable {
    public static final Parcelable.Creator<BackStackState> CREATOR = new Parcelable.Creator<BackStackState>() {
        public BackStackState createFromParcel(Parcel parcel) {
            return new BackStackState(parcel);
        }

        public BackStackState[] newArray(int i) {
            return new BackStackState[i];
        }
    };
    final ArrayList<FragmentState> mFragments;
    final ArrayList<BackStackRecordState> mTransactions;

    public int describeContents() {
        return 0;
    }

    BackStackState(Parcel parcel) {
        this.mFragments = parcel.createTypedArrayList(FragmentState.CREATOR);
        this.mTransactions = parcel.createTypedArrayList(BackStackRecordState.CREATOR);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(this.mFragments);
        parcel.writeTypedList(this.mTransactions);
    }
}
