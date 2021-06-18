package com.android.settingslib.widget;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsageProgressBarPreference extends Preference {
    private CharSequence mBottomSummary;
    private ImageView mCustomImageView;
    private final Pattern mNumberPattern;
    private int mPercent;
    private CharSequence mTotalSummary;
    private CharSequence mUsageSummary;

    public UsageProgressBarPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mNumberPattern = Pattern.compile("[\\d]*[\\.,]?[\\d]+");
        this.mPercent = -1;
        setLayoutResource(R$layout.preference_usage_progress_bar);
    }

    public UsageProgressBarPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mNumberPattern = Pattern.compile("[\\d]*[\\.,]?[\\d]+");
        this.mPercent = -1;
        setLayoutResource(R$layout.preference_usage_progress_bar);
    }

    public UsageProgressBarPreference(Context context) {
        this(context, (AttributeSet) null);
    }

    public void setUsageSummary(CharSequence charSequence) {
        if (!TextUtils.equals(this.mUsageSummary, charSequence)) {
            this.mUsageSummary = charSequence;
            notifyChanged();
        }
    }

    public void setTotalSummary(CharSequence charSequence) {
        if (!TextUtils.equals(this.mTotalSummary, charSequence)) {
            this.mTotalSummary = charSequence;
            notifyChanged();
        }
    }

    public void setBottomSummary(CharSequence charSequence) {
        if (!TextUtils.equals(this.mBottomSummary, charSequence)) {
            this.mBottomSummary = charSequence;
            notifyChanged();
        }
    }

    public void setPercent(long j, long j2) {
        if (j <= j2) {
            if (j2 != 0) {
                int i = (int) ((((double) j) / ((double) j2)) * 100.0d);
                if (this.mPercent != i) {
                    this.mPercent = i;
                    notifyChanged();
                }
            } else if (this.mPercent != 0) {
                this.mPercent = 0;
                notifyChanged();
            }
        }
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ((TextView) preferenceViewHolder.findViewById(R$id.usage_summary)).setText(enlargeFontOfNumber(this.mUsageSummary));
        TextView textView = (TextView) preferenceViewHolder.findViewById(R$id.total_summary);
        CharSequence charSequence = this.mTotalSummary;
        if (charSequence != null) {
            textView.setText(charSequence);
        }
        TextView textView2 = (TextView) preferenceViewHolder.findViewById(R$id.bottom_summary);
        if (TextUtils.isEmpty(this.mBottomSummary)) {
            textView2.setVisibility(8);
        } else {
            textView2.setVisibility(0);
            textView2.setText(this.mBottomSummary);
        }
        ProgressBar progressBar = (ProgressBar) preferenceViewHolder.findViewById(16908301);
        if (this.mPercent < 0) {
            progressBar.setIndeterminate(true);
        } else {
            progressBar.setIndeterminate(false);
            progressBar.setProgress(this.mPercent);
        }
        FrameLayout frameLayout = (FrameLayout) preferenceViewHolder.findViewById(R$id.custom_content);
        if (this.mCustomImageView == null) {
            frameLayout.removeAllViews();
            frameLayout.setVisibility(8);
            return;
        }
        frameLayout.removeAllViews();
        frameLayout.addView(this.mCustomImageView);
        frameLayout.setVisibility(0);
    }

    private CharSequence enlargeFontOfNumber(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            return "";
        }
        Matcher matcher = this.mNumberPattern.matcher(charSequence);
        if (!matcher.find()) {
            return charSequence;
        }
        SpannableString spannableString = new SpannableString(charSequence);
        spannableString.setSpan(new AbsoluteSizeSpan(64, true), matcher.start(), matcher.end(), 33);
        return spannableString;
    }
}
