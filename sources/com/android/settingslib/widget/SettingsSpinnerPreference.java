package com.android.settingslib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settingslib.widget.settingsspinner.SettingsSpinner;
import com.android.settingslib.widget.settingsspinner.SettingsSpinnerAdapter;

public class SettingsSpinnerPreference extends Preference {
    private SettingsSpinnerAdapter mAdapter;
    private boolean mIsClickable;
    /* access modifiers changed from: private */
    public AdapterView.OnItemSelectedListener mListener;
    private final AdapterView.OnItemSelectedListener mOnSelectedListener;
    /* access modifiers changed from: private */
    public int mPosition;

    public SettingsSpinnerPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mIsClickable = true;
        this.mOnSelectedListener = new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (SettingsSpinnerPreference.this.mPosition != i) {
                    int unused = SettingsSpinnerPreference.this.mPosition = i;
                    if (SettingsSpinnerPreference.this.mListener != null) {
                        SettingsSpinnerPreference.this.mListener.onItemSelected(adapterView, view, i, j);
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                if (SettingsSpinnerPreference.this.mListener != null) {
                    SettingsSpinnerPreference.this.mListener.onNothingSelected(adapterView);
                }
            }
        };
        setLayoutResource(R$layout.settings_spinner_preference);
        setSelectable(false);
    }

    public SettingsSpinnerPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIsClickable = true;
        this.mOnSelectedListener = new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (SettingsSpinnerPreference.this.mPosition != i) {
                    int unused = SettingsSpinnerPreference.this.mPosition = i;
                    if (SettingsSpinnerPreference.this.mListener != null) {
                        SettingsSpinnerPreference.this.mListener.onItemSelected(adapterView, view, i, j);
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                if (SettingsSpinnerPreference.this.mListener != null) {
                    SettingsSpinnerPreference.this.mListener.onNothingSelected(adapterView);
                }
            }
        };
        setLayoutResource(R$layout.settings_spinner_preference);
        setSelectable(false);
    }

    public SettingsSpinnerPreference(Context context) {
        this(context, (AttributeSet) null);
    }

    public <T extends SettingsSpinnerAdapter> void setAdapter(T t) {
        this.mAdapter = t;
        notifyChanged();
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        this.mListener = onItemSelectedListener;
    }

    public Object getSelectedItem() {
        SettingsSpinnerAdapter settingsSpinnerAdapter = this.mAdapter;
        if (settingsSpinnerAdapter == null) {
            return null;
        }
        return settingsSpinnerAdapter.getItem(this.mPosition);
    }

    public void setSelection(int i) {
        if (this.mPosition != i) {
            this.mPosition = i;
            notifyChanged();
        }
    }

    public void setClickable(boolean z) {
        if (this.mIsClickable != z) {
            this.mIsClickable = z;
            notifyChanged();
        }
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        SettingsSpinner settingsSpinner = (SettingsSpinner) preferenceViewHolder.findViewById(R$id.spinner);
        settingsSpinner.setEnabled(this.mIsClickable);
        settingsSpinner.setClickable(this.mIsClickable);
        settingsSpinner.setAdapter(this.mAdapter);
        settingsSpinner.setSelection(this.mPosition);
        settingsSpinner.setOnItemSelectedListener(this.mOnSelectedListener);
    }
}
