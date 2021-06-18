package com.android.settings.datausage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.datausage.CycleAdapter;
import com.android.settingslib.widget.settingsspinner.SettingsSpinner;

public class SpinnerPreference extends Preference implements CycleAdapter.SpinnerInterface {
    /* access modifiers changed from: private */
    public CycleAdapter mAdapter;
    /* access modifiers changed from: private */
    public Object mCurrentObject;
    /* access modifiers changed from: private */
    public AdapterView.OnItemSelectedListener mListener;
    private final AdapterView.OnItemSelectedListener mOnSelectedListener = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            if (SpinnerPreference.this.mPosition != i) {
                int unused = SpinnerPreference.this.mPosition = i;
                SpinnerPreference spinnerPreference = SpinnerPreference.this;
                Object unused2 = spinnerPreference.mCurrentObject = spinnerPreference.mAdapter.getItem(i);
                SpinnerPreference.this.mListener.onItemSelected(adapterView, view, i, j);
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            SpinnerPreference.this.mListener.onNothingSelected(adapterView);
        }
    };
    /* access modifiers changed from: private */
    public int mPosition;

    public SpinnerPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R.layout.data_usage_cycles);
    }

    public void setAdapter(CycleAdapter cycleAdapter) {
        this.mAdapter = cycleAdapter;
        notifyChanged();
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        this.mListener = onItemSelectedListener;
    }

    public Object getSelectedItem() {
        return this.mCurrentObject;
    }

    public void setSelection(int i) {
        this.mPosition = i;
        this.mCurrentObject = this.mAdapter.getItem(i);
        notifyChanged();
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        SettingsSpinner settingsSpinner = (SettingsSpinner) preferenceViewHolder.findViewById(R.id.cycles_spinner);
        settingsSpinner.setAdapter(this.mAdapter);
        settingsSpinner.setSelection(this.mPosition);
        settingsSpinner.setOnItemSelectedListener(this.mOnSelectedListener);
    }

    /* access modifiers changed from: protected */
    public void performClick(View view) {
        view.findViewById(R.id.cycles_spinner).performClick();
    }
}
