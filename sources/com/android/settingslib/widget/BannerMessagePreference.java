package com.android.settingslib.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

public class BannerMessagePreference extends Preference {
    private ButtonInfo mNegativeButtonInfo;
    private ButtonInfo mPositiveButtonInfo;

    public BannerMessagePreference(Context context) {
        super(context);
        init();
    }

    public BannerMessagePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public BannerMessagePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public BannerMessagePreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init();
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.setDividerAllowedAbove(true);
        preferenceViewHolder.setDividerAllowedBelow(true);
        Button unused = this.mPositiveButtonInfo.mButton = (Button) preferenceViewHolder.findViewById(R$id.banner_positive_btn);
        Button unused2 = this.mNegativeButtonInfo.mButton = (Button) preferenceViewHolder.findViewById(R$id.banner_negative_btn);
        this.mPositiveButtonInfo.setUpButton();
        this.mNegativeButtonInfo.setUpButton();
        ((TextView) preferenceViewHolder.findViewById(R$id.banner_title)).setText(getTitle());
        ((TextView) preferenceViewHolder.findViewById(R$id.banner_summary)).setText(getSummary());
    }

    private void init() {
        this.mPositiveButtonInfo = new ButtonInfo();
        this.mNegativeButtonInfo = new ButtonInfo();
        setSelectable(false);
        setLayoutResource(R$layout.banner_message);
    }

    public BannerMessagePreference setPositiveButtonOnClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != this.mPositiveButtonInfo.mListener) {
            View.OnClickListener unused = this.mPositiveButtonInfo.mListener = onClickListener;
            notifyChanged();
        }
        return this;
    }

    public BannerMessagePreference setPositiveButtonText(int i) {
        String string = getContext().getString(i);
        if (!TextUtils.equals(string, this.mPositiveButtonInfo.mText)) {
            CharSequence unused = this.mPositiveButtonInfo.mText = string;
            notifyChanged();
        }
        return this;
    }

    static class ButtonInfo {
        /* access modifiers changed from: private */
        public Button mButton;
        private boolean mIsVisible = true;
        /* access modifiers changed from: private */
        public View.OnClickListener mListener;
        /* access modifiers changed from: private */
        public CharSequence mText;

        ButtonInfo() {
        }

        /* access modifiers changed from: package-private */
        public void setUpButton() {
            this.mButton.setText(this.mText);
            this.mButton.setOnClickListener(this.mListener);
            if (shouldBeVisible()) {
                this.mButton.setVisibility(0);
            } else {
                this.mButton.setVisibility(8);
            }
        }

        private boolean shouldBeVisible() {
            return this.mIsVisible && !TextUtils.isEmpty(this.mText);
        }
    }
}
