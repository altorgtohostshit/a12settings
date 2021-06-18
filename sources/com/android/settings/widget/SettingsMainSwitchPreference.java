package com.android.settings.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R$styleable;
import androidx.preference.TwoStatePreference;
import com.android.settings.R;
import com.android.settings.widget.SettingsMainSwitchBar;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.widget.OnMainSwitchChangeListener;
import java.util.ArrayList;
import java.util.List;

public class SettingsMainSwitchPreference extends TwoStatePreference {
    private final List<SettingsMainSwitchBar.OnBeforeCheckedChangeListener> mBeforeCheckedChangeListeners = new ArrayList();
    private RestrictedLockUtils.EnforcedAdmin mEnforcedAdmin;
    private boolean mIsVisible;
    private SettingsMainSwitchBar mMainSwitchBar;
    private final List<OnMainSwitchChangeListener> mSwitchChangeListeners = new ArrayList();
    private CharSequence mTitle;

    public SettingsMainSwitchPreference(Context context) {
        super(context);
        init(context, (AttributeSet) null);
    }

    public SettingsMainSwitchPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public SettingsMainSwitchPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    public SettingsMainSwitchPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context, attributeSet);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.setDividerAllowedAbove(true);
        preferenceViewHolder.setDividerAllowedBelow(false);
        SettingsMainSwitchBar settingsMainSwitchBar = (SettingsMainSwitchBar) preferenceViewHolder.findViewById(R.id.main_switch_bar);
        this.mMainSwitchBar = settingsMainSwitchBar;
        settingsMainSwitchBar.show();
        updateStatus(isChecked());
        registerListenerToSwitchBar();
        if (!this.mIsVisible) {
            this.mMainSwitchBar.hide();
        }
    }

    private void init(Context context, AttributeSet attributeSet) {
        setLayoutResource(R.layout.preference_widget_main_switch);
        this.mIsVisible = true;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.Preference, 0, 0);
            CharSequence text = TypedArrayUtils.getText(obtainStyledAttributes, 39, 4);
            if (!TextUtils.isEmpty(text)) {
                setTitle(text.toString());
            }
            obtainStyledAttributes.recycle();
        }
    }

    public void setChecked(boolean z) {
        super.setChecked(z);
        SettingsMainSwitchBar settingsMainSwitchBar = this.mMainSwitchBar;
        if (settingsMainSwitchBar != null) {
            settingsMainSwitchBar.setChecked(z);
        }
    }

    public void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        SettingsMainSwitchBar settingsMainSwitchBar = this.mMainSwitchBar;
        if (settingsMainSwitchBar != null) {
            settingsMainSwitchBar.setTitle(charSequence);
        }
    }

    public void updateStatus(boolean z) {
        setChecked(z);
        SettingsMainSwitchBar settingsMainSwitchBar = this.mMainSwitchBar;
        if (settingsMainSwitchBar != null) {
            settingsMainSwitchBar.setChecked(z);
            this.mMainSwitchBar.setTitle(this.mTitle);
            this.mMainSwitchBar.setDisabledByAdmin(this.mEnforcedAdmin);
            this.mMainSwitchBar.show();
        }
    }

    public void show() {
        this.mIsVisible = true;
        SettingsMainSwitchBar settingsMainSwitchBar = this.mMainSwitchBar;
        if (settingsMainSwitchBar != null) {
            settingsMainSwitchBar.show();
        }
    }

    public void hide() {
        this.mIsVisible = false;
        SettingsMainSwitchBar settingsMainSwitchBar = this.mMainSwitchBar;
        if (settingsMainSwitchBar != null) {
            settingsMainSwitchBar.hide();
        }
    }

    public void setCheckedInternal(boolean z) {
        super.setChecked(z);
        SettingsMainSwitchBar settingsMainSwitchBar = this.mMainSwitchBar;
        if (settingsMainSwitchBar != null) {
            settingsMainSwitchBar.setCheckedInternal(z);
        }
    }

    public void setSwitchBarEnabled(boolean z) {
        setEnabled(z);
        SettingsMainSwitchBar settingsMainSwitchBar = this.mMainSwitchBar;
        if (settingsMainSwitchBar != null) {
            settingsMainSwitchBar.setEnabled(z);
        }
    }

    public void setOnBeforeCheckedChangeListener(SettingsMainSwitchBar.OnBeforeCheckedChangeListener onBeforeCheckedChangeListener) {
        SettingsMainSwitchBar settingsMainSwitchBar = this.mMainSwitchBar;
        if (settingsMainSwitchBar == null) {
            this.mBeforeCheckedChangeListeners.add(onBeforeCheckedChangeListener);
        } else {
            settingsMainSwitchBar.setOnBeforeCheckedChangeListener(onBeforeCheckedChangeListener);
        }
    }

    public void addOnSwitchChangeListener(OnMainSwitchChangeListener onMainSwitchChangeListener) {
        SettingsMainSwitchBar settingsMainSwitchBar = this.mMainSwitchBar;
        if (settingsMainSwitchBar == null) {
            this.mSwitchChangeListeners.add(onMainSwitchChangeListener);
        } else {
            settingsMainSwitchBar.addOnSwitchChangeListener(onMainSwitchChangeListener);
        }
    }

    public void setDisabledByAdmin(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        this.mEnforcedAdmin = enforcedAdmin;
        SettingsMainSwitchBar settingsMainSwitchBar = this.mMainSwitchBar;
        if (settingsMainSwitchBar != null) {
            settingsMainSwitchBar.setDisabledByAdmin(enforcedAdmin);
        }
    }

    private void registerListenerToSwitchBar() {
        for (SettingsMainSwitchBar.OnBeforeCheckedChangeListener onBeforeCheckedChangeListener : this.mBeforeCheckedChangeListeners) {
            this.mMainSwitchBar.setOnBeforeCheckedChangeListener(onBeforeCheckedChangeListener);
        }
        for (OnMainSwitchChangeListener addOnSwitchChangeListener : this.mSwitchChangeListeners) {
            this.mMainSwitchBar.addOnSwitchChangeListener(addOnSwitchChangeListener);
        }
        this.mBeforeCheckedChangeListeners.clear();
        this.mSwitchChangeListeners.clear();
    }
}
