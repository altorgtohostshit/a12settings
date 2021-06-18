package com.android.settings.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.R$styleable;

public class LabeledSeekBarPreference extends SeekBarPreference {
    private Preference.OnPreferenceChangeListener mStopListener;
    private final int mTextEndId;
    private final int mTextStartId;
    private final int mTickMarkId;

    public LabeledSeekBarPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setLayoutResource(R.layout.preference_labeled_slider);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.LabeledSeekBarPreference);
        this.mTextStartId = obtainStyledAttributes.getResourceId(1, R.string.summary_placeholder);
        this.mTextEndId = obtainStyledAttributes.getResourceId(0, R.string.summary_placeholder);
        this.mTickMarkId = obtainStyledAttributes.getResourceId(2, 0);
        obtainStyledAttributes.recycle();
    }

    public LabeledSeekBarPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.getAttr(context, R.attr.seekBarPreferenceStyle, 17957079), 0);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ((TextView) preferenceViewHolder.findViewById(16908308)).setText(this.mTextStartId);
        ((TextView) preferenceViewHolder.findViewById(16908309)).setText(this.mTextEndId);
        if (this.mTickMarkId != 0) {
            ((SeekBar) preferenceViewHolder.findViewById(16909414)).setTickMark(getContext().getDrawable(this.mTickMarkId));
        }
    }

    public void setOnPreferenceChangeStopListener(Preference.OnPreferenceChangeListener onPreferenceChangeListener) {
        this.mStopListener = onPreferenceChangeListener;
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
        Preference.OnPreferenceChangeListener onPreferenceChangeListener = this.mStopListener;
        if (onPreferenceChangeListener != null) {
            onPreferenceChangeListener.onPreferenceChange(this, Integer.valueOf(seekBar.getProgress()));
        }
    }
}
