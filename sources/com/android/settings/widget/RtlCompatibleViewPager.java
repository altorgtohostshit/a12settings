package com.android.settings.widget;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import androidx.viewpager.widget.ViewPager;
import java.util.Locale;

public final class RtlCompatibleViewPager extends ViewPager {
    public RtlCompatibleViewPager(Context context) {
        this(context, (AttributeSet) null);
    }

    public RtlCompatibleViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public int getCurrentItem() {
        return getRtlAwareIndex(super.getCurrentItem());
    }

    public void setCurrentItem(int i) {
        super.setCurrentItem(getRtlAwareIndex(i));
    }

    public Parcelable onSaveInstanceState() {
        RtlSavedState rtlSavedState = new RtlSavedState(super.onSaveInstanceState());
        rtlSavedState.position = getCurrentItem();
        return rtlSavedState;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        RtlSavedState rtlSavedState = (RtlSavedState) parcelable;
        super.onRestoreInstanceState(rtlSavedState.getSuperState());
        setCurrentItem(rtlSavedState.position);
    }

    public int getRtlAwareIndex(int i) {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1 ? (getAdapter().getCount() - i) - 1 : i;
    }

    static class RtlSavedState extends View.BaseSavedState {
        public static final Parcelable.ClassLoaderCreator<RtlSavedState> CREATOR = new Parcelable.ClassLoaderCreator<RtlSavedState>() {
            public RtlSavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new RtlSavedState(parcel, classLoader);
            }

            public RtlSavedState createFromParcel(Parcel parcel) {
                return new RtlSavedState(parcel, (ClassLoader) null);
            }

            public RtlSavedState[] newArray(int i) {
                return new RtlSavedState[i];
            }
        };
        int position;

        public RtlSavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private RtlSavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.position = parcel.readInt();
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.position);
        }
    }
}
