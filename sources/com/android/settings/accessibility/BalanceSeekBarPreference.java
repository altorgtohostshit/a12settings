package com.android.settings.accessibility;

import android.content.Context;
import android.provider.Settings;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.widget.SeekBarPreference;

public class BalanceSeekBarPreference extends SeekBarPreference {
    private final Context mContext;
    private ImageView mIconView;
    private BalanceSeekBar mSeekBar;

    public BalanceSeekBarPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, TypedArrayUtils.getAttr(context, R.attr.preferenceStyle, 16842894));
        this.mContext = context;
        setLayoutResource(R.layout.preference_balance_slider);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.mSeekBar = (BalanceSeekBar) preferenceViewHolder.findViewById(16909414);
        this.mIconView = (ImageView) preferenceViewHolder.findViewById(16908294);
        init();
    }

    private void init() {
        if (this.mSeekBar != null) {
            float floatForUser = Settings.System.getFloatForUser(this.mContext.getContentResolver(), "master_balance", 0.0f, -2);
            this.mSeekBar.setMax(200);
            this.mSeekBar.setProgress(((int) (floatForUser * 100.0f)) + 100);
            this.mSeekBar.setEnabled(isEnabled());
        }
    }
}
