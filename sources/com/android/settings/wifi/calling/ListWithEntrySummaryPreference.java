package com.android.settings.wifi.calling;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import com.android.settings.CustomListPreference;
import com.android.settings.R;
import com.android.settings.R$styleable;

public class ListWithEntrySummaryPreference extends CustomListPreference {
    private final Context mContext;
    private CharSequence[] mSummaries;

    public ListWithEntrySummaryPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ListWithEntrySummaryPreference, 0, 0);
        this.mSummaries = obtainStyledAttributes.getTextArray(0);
        obtainStyledAttributes.recycle();
    }

    public void setEntrySummaries(CharSequence[] charSequenceArr) {
        this.mSummaries = charSequenceArr;
    }

    /* access modifiers changed from: private */
    public CharSequence getEntrySummary(int i) {
        CharSequence[] charSequenceArr = this.mSummaries;
        if (charSequenceArr != null) {
            return charSequenceArr[i];
        }
        Log.w("ListWithEntrySummaryPreference", "getEntrySummary : mSummaries is null");
        return "";
    }

    /* access modifiers changed from: protected */
    public void onPrepareDialogBuilder(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener) {
        builder.setSingleChoiceItems((ListAdapter) new SelectorAdapter(this.mContext, R.xml.single_choice_list_item_2, this), findIndexOfValue(getValue()), onClickListener);
        super.onPrepareDialogBuilder(builder, onClickListener);
    }

    private static class SelectorAdapter extends ArrayAdapter<CharSequence> {
        private final Context mContext;
        private ListWithEntrySummaryPreference mSelector;

        public SelectorAdapter(Context context, int i, ListWithEntrySummaryPreference listWithEntrySummaryPreference) {
            super(context, i, listWithEntrySummaryPreference.getEntryValues());
            this.mContext = context;
            this.mSelector = listWithEntrySummaryPreference;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            View inflate = LayoutInflater.from(this.mContext).inflate(R.xml.single_choice_list_item_2, viewGroup, false);
            ((TextView) inflate.findViewById(R.id.title)).setText(this.mSelector.getEntries()[i]);
            ((TextView) inflate.findViewById(R.id.summary)).setText(this.mSelector.getEntrySummary(i));
            RadioButton radioButton = (RadioButton) inflate.findViewById(R.id.radio);
            ListWithEntrySummaryPreference listWithEntrySummaryPreference = this.mSelector;
            if (i == listWithEntrySummaryPreference.findIndexOfValue(listWithEntrySummaryPreference.getValue())) {
                radioButton.setChecked(true);
            }
            return inflate;
        }
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        CharSequence[] unused = savedState.mEntries = getEntries();
        CharSequence[] unused2 = savedState.mEntryValues = getEntryValues();
        CharSequence[] unused3 = savedState.mSummaries = this.mSummaries;
        return savedState;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable == null || !parcelable.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setEntries(savedState.mEntries);
        setEntryValues(savedState.mEntryValues);
        this.mSummaries = savedState.mSummaries;
    }

    private static class SavedState extends Preference.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        /* access modifiers changed from: private */
        public CharSequence[] mEntries;
        /* access modifiers changed from: private */
        public CharSequence[] mEntryValues;
        /* access modifiers changed from: private */
        public CharSequence[] mSummaries;

        public SavedState(Parcel parcel) {
            super(parcel);
            this.mEntries = parcel.readCharSequenceArray();
            this.mEntryValues = parcel.readCharSequenceArray();
            this.mSummaries = parcel.readCharSequenceArray();
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeCharSequenceArray(this.mEntries);
            parcel.writeCharSequenceArray(this.mEntryValues);
            parcel.writeCharSequenceArray(this.mSummaries);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }
}
