package com.android.settingslib.widget.settingsspinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.android.settingslib.widget.R$layout;

public class SettingsSpinnerAdapter<T> extends ArrayAdapter<T> {
    private static final int DEFAULT_RESOURCE = R$layout.settings_spinner_view;
    private static final int DFAULT_DROPDOWN_RESOURCE = R$layout.settings_spinner_dropdown_view;
    private final LayoutInflater mDefaultInflater;

    public SettingsSpinnerAdapter(Context context) {
        super(context, DEFAULT_RESOURCE);
        setDropDownViewResource(DFAULT_DROPDOWN_RESOURCE);
        this.mDefaultInflater = LayoutInflater.from(context);
    }

    public View getDefaultView(int i, View view, ViewGroup viewGroup) {
        return this.mDefaultInflater.inflate(DEFAULT_RESOURCE, viewGroup, false);
    }

    public View getDefaultDropDownView(int i, View view, ViewGroup viewGroup) {
        return this.mDefaultInflater.inflate(DFAULT_DROPDOWN_RESOURCE, viewGroup, false);
    }
}
