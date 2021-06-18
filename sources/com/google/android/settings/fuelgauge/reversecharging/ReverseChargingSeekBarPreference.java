package com.google.android.settings.fuelgauge.reversecharging;

import android.content.Context;
import android.provider.Settings;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R;
import com.android.settings.widget.SeekBarPreference;
import com.android.settingslib.Utils;

public class ReverseChargingSeekBarPreference extends SeekBarPreference {
    private Context mContext;
    @VisibleForTesting
    TextView mPercentage;

    public ReverseChargingSeekBarPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        setLayoutResource(R.layout.preference_reverse_charging_slider);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.mPercentage = (TextView) preferenceViewHolder.findViewById(R.id.percentage_number);
        ((ImageView) preferenceViewHolder.findViewById(16908294)).setImageResource(R.drawable.ic_reverse_charging);
        this.mPercentage.setText(getUsageAmount());
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public CharSequence getUsageAmount() {
        return Utils.formatPercentage(Settings.Global.getInt(this.mContext.getContentResolver(), "advanced_battery_usage_amount", 2) * 5);
    }

    public void setPercentageValue(String str) {
        this.mPercentage.setText(str);
    }
}
