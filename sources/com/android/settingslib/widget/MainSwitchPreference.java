package com.android.settingslib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R$styleable;
import androidx.preference.TwoStatePreference;
import java.util.ArrayList;
import java.util.List;

public class MainSwitchPreference extends TwoStatePreference {
    private MainSwitchBar mMainSwitchBar;
    private final List<OnMainSwitchChangeListener> mSwitchChangeListeners = new ArrayList();
    private CharSequence mTitle;

    public MainSwitchPreference(Context context) {
        super(context);
        init(context, (AttributeSet) null);
    }

    public MainSwitchPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public MainSwitchPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    public MainSwitchPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context, attributeSet);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.setDividerAllowedAbove(false);
        preferenceViewHolder.setDividerAllowedBelow(false);
        this.mMainSwitchBar = (MainSwitchBar) preferenceViewHolder.findViewById(R$id.settingslib_main_switch_bar);
        updateStatus(isChecked());
        registerListenerToSwitchBar();
    }

    private void init(Context context, AttributeSet attributeSet) {
        setLayoutResource(R$layout.settingslib_main_switch_layout);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.Preference, 0, 0);
            setTitle(obtainStyledAttributes.getText(R$styleable.Preference_android_title));
            obtainStyledAttributes.recycle();
        }
    }

    public void setChecked(boolean z) {
        super.setChecked(z);
        MainSwitchBar mainSwitchBar = this.mMainSwitchBar;
        if (mainSwitchBar != null) {
            mainSwitchBar.setChecked(z);
        }
    }

    public void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        MainSwitchBar mainSwitchBar = this.mMainSwitchBar;
        if (mainSwitchBar != null) {
            mainSwitchBar.setTitle(charSequence);
        }
    }

    public void updateStatus(boolean z) {
        setChecked(z);
        MainSwitchBar mainSwitchBar = this.mMainSwitchBar;
        if (mainSwitchBar != null) {
            mainSwitchBar.setChecked(z);
            this.mMainSwitchBar.setTitle(this.mTitle);
            this.mMainSwitchBar.show();
        }
    }

    public void addOnSwitchChangeListener(OnMainSwitchChangeListener onMainSwitchChangeListener) {
        MainSwitchBar mainSwitchBar = this.mMainSwitchBar;
        if (mainSwitchBar == null) {
            this.mSwitchChangeListeners.add(onMainSwitchChangeListener);
        } else {
            mainSwitchBar.addOnSwitchChangeListener(onMainSwitchChangeListener);
        }
    }

    private void registerListenerToSwitchBar() {
        for (OnMainSwitchChangeListener addOnSwitchChangeListener : this.mSwitchChangeListeners) {
            this.mMainSwitchBar.addOnSwitchChangeListener(addOnSwitchChangeListener);
        }
        this.mSwitchChangeListeners.clear();
    }
}
