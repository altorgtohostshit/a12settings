package com.android.settings.applications.intentpicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settingslib.widget.TwoTargetPreference;

public class VerifiedLinksPreference extends TwoTargetPreference {
    private Context mContext;
    private View.OnClickListener mOnWidgetClickListener;
    private boolean mShowCheckBox;

    public VerifiedLinksPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context);
    }

    public VerifiedLinksPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public VerifiedLinksPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public VerifiedLinksPreference(Context context) {
        this(context, (AttributeSet) null);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mOnWidgetClickListener = null;
        this.mShowCheckBox = true;
        setLayoutResource(R.layout.preference_checkable_two_target);
        setWidgetLayoutResource(R.layout.verified_links_widget);
    }

    public void setWidgetFrameClickListener(View.OnClickListener onClickListener) {
        this.mOnWidgetClickListener = onClickListener;
    }

    public void setCheckBoxVisible(boolean z) {
        this.mShowCheckBox = z;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View findViewById = preferenceViewHolder.findViewById(16908312);
        int i = 0;
        preferenceViewHolder.findViewById(R.id.two_target_divider).setVisibility(0);
        findViewById.setVisibility(0);
        View.OnClickListener onClickListener = this.mOnWidgetClickListener;
        if (onClickListener != null) {
            findViewById.setOnClickListener(onClickListener);
        }
        View view = (View) preferenceViewHolder.findViewById(R.id.checkbox_container).getParent();
        view.setEnabled(false);
        view.setClickable(false);
        CheckBox checkBox = (CheckBox) preferenceViewHolder.findViewById(16908289);
        if (checkBox != null) {
            checkBox.setChecked(true);
            if (!this.mShowCheckBox) {
                i = 4;
            }
            checkBox.setVisibility(i);
        }
    }
}
