package com.android.settings.accessibility;

import android.view.View;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.SwitchPreference;

public final class DividerSwitchPreference extends SwitchPreference {
    private Boolean mDividerAllowBelow;
    private Boolean mDividerAllowedAbove;
    private int mSwitchVisibility;

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.setDividerAllowedAbove(this.mDividerAllowedAbove.booleanValue());
        preferenceViewHolder.setDividerAllowedBelow(this.mDividerAllowBelow.booleanValue());
        View findViewById = preferenceViewHolder.itemView.findViewById(16908312);
        if (findViewById != null) {
            findViewById.setVisibility(this.mSwitchVisibility);
        }
    }
}
