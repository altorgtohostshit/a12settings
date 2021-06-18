package com.android.settings.accessibility;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;

public class ShortcutPreference extends Preference {
    private boolean mChecked = false;
    private OnClickCallback mClickCallback = null;
    private boolean mSettingsEditable = true;

    public interface OnClickCallback {
        void onSettingsClicked(ShortcutPreference shortcutPreference);

        void onToggleClicked(ShortcutPreference shortcutPreference);
    }

    ShortcutPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R.layout.accessibility_shortcut_secondary_action);
        setWidgetLayoutResource(R.layout.preference_widget_primary_switch);
        setIconSpaceReserved(false);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(16843534, typedValue, true);
        LinearLayout linearLayout = (LinearLayout) preferenceViewHolder.itemView.findViewById(R.id.main_frame);
        int i = 0;
        if (linearLayout != null) {
            linearLayout.setOnClickListener(new ShortcutPreference$$ExternalSyntheticLambda2(this));
            linearLayout.setClickable(this.mSettingsEditable);
            linearLayout.setFocusable(this.mSettingsEditable);
            linearLayout.setBackgroundResource(this.mSettingsEditable ? typedValue.resourceId : 0);
        }
        Switch switchR = (Switch) preferenceViewHolder.itemView.findViewById(R.id.switchWidget);
        if (switchR != null) {
            switchR.setOnTouchListener(ShortcutPreference$$ExternalSyntheticLambda3.INSTANCE);
            switchR.setContentDescription(getContext().getText(R.string.accessibility_shortcut_settings));
            switchR.setChecked(this.mChecked);
            switchR.setOnClickListener(new ShortcutPreference$$ExternalSyntheticLambda1(this));
            switchR.setClickable(this.mSettingsEditable);
            switchR.setFocusable(this.mSettingsEditable);
            switchR.setBackgroundResource(this.mSettingsEditable ? typedValue.resourceId : 0);
        }
        View findViewById = preferenceViewHolder.itemView.findViewById(R.id.divider);
        if (findViewById != null) {
            if (!this.mSettingsEditable) {
                i = 8;
            }
            findViewById.setVisibility(i);
        }
        preferenceViewHolder.itemView.setOnClickListener(new ShortcutPreference$$ExternalSyntheticLambda0(this));
        preferenceViewHolder.itemView.setClickable(!this.mSettingsEditable);
        preferenceViewHolder.itemView.setFocusable(!this.mSettingsEditable);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(View view) {
        callOnSettingsClicked();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onBindViewHolder$1(View view, MotionEvent motionEvent) {
        return motionEvent.getActionMasked() == 2;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$2(View view) {
        callOnToggleClicked();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$3(View view) {
        callOnToggleClicked();
    }

    public void setChecked(boolean z) {
        if (this.mChecked != z) {
            this.mChecked = z;
            notifyChanged();
        }
    }

    public boolean isChecked() {
        return this.mChecked;
    }

    public void setSettingsEditable(boolean z) {
        if (this.mSettingsEditable != z) {
            this.mSettingsEditable = z;
            notifyChanged();
        }
    }

    public boolean isSettingsEditable() {
        return this.mSettingsEditable;
    }

    public void setOnClickCallback(OnClickCallback onClickCallback) {
        this.mClickCallback = onClickCallback;
    }

    private void callOnSettingsClicked() {
        OnClickCallback onClickCallback = this.mClickCallback;
        if (onClickCallback != null) {
            onClickCallback.onSettingsClicked(this);
        }
    }

    private void callOnToggleClicked() {
        setChecked(!this.mChecked);
        OnClickCallback onClickCallback = this.mClickCallback;
        if (onClickCallback != null) {
            onClickCallback.onToggleClicked(this);
        }
    }
}
