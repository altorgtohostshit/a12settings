package com.android.settings.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settingslib.RestrictedPreference;

public class AddPreference extends RestrictedPreference implements View.OnClickListener {
    private View mAddWidget;
    private OnAddClickListener mListener;
    private View mWidgetFrame;

    public interface OnAddClickListener {
        void onAddClick(AddPreference addPreference);
    }

    /* access modifiers changed from: package-private */
    public int getAddWidgetResId() {
        return R.id.add_preference_widget;
    }

    /* access modifiers changed from: protected */
    public int getSecondTargetResId() {
        return R.layout.preference_widget_add;
    }

    public AddPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setOnAddClickListener(OnAddClickListener onAddClickListener) {
        this.mListener = onAddClickListener;
        View view = this.mWidgetFrame;
        if (view != null) {
            view.setVisibility(shouldHideSecondTarget() ? 8 : 0);
        }
    }

    public void setAddWidgetEnabled(boolean z) {
        View view = this.mAddWidget;
        if (view != null) {
            view.setEnabled(z);
        }
    }

    /* access modifiers changed from: protected */
    public boolean shouldHideSecondTarget() {
        return this.mListener == null;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.mWidgetFrame = preferenceViewHolder.findViewById(16908312);
        View findViewById = preferenceViewHolder.findViewById(getAddWidgetResId());
        this.mAddWidget = findViewById;
        findViewById.setEnabled(true);
        this.mAddWidget.setOnClickListener(this);
    }

    public void onClick(View view) {
        OnAddClickListener onAddClickListener;
        if (view.getId() == getAddWidgetResId() && (onAddClickListener = this.mListener) != null) {
            onAddClickListener.onAddClick(this);
        }
    }
}
