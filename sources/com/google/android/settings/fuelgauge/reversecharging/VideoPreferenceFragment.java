package com.google.android.settings.fuelgauge.reversecharging;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.preference.PreferenceFragmentCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;

public class VideoPreferenceFragment extends PreferenceFragmentCompat {
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.reverse_charging_preference);
    }

    public RecyclerView onCreateRecyclerView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        RecyclerView onCreateRecyclerView = super.onCreateRecyclerView(layoutInflater, viewGroup, bundle);
        onCreateRecyclerView.setImportantForAccessibility(2);
        return onCreateRecyclerView;
    }
}
