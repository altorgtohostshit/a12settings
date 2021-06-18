package com.android.settings.accessibility;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.utils.AnnotationSpan;
import com.android.settingslib.HelpUtils;
import com.android.settingslib.widget.FooterPreference;

public final class AccessibilityFooterPreference extends FooterPreference {
    private CharSequence mIconContentDescription;
    private boolean mLinkEnabled;

    public AccessibilityFooterPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AccessibilityFooterPreference(Context context) {
        super(context);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.itemView.findViewById(16908310);
        if (this.mLinkEnabled) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            textView.setMovementMethod((MovementMethod) null);
        }
        LinearLayout linearLayout = (LinearLayout) preferenceViewHolder.itemView.findViewById(R.id.icon_frame);
        if (!TextUtils.isEmpty(this.mIconContentDescription)) {
            linearLayout.setContentDescription(this.mIconContentDescription);
            textView.setFocusable(false);
            return;
        }
        linearLayout.setContentDescription((CharSequence) null);
        textView.setFocusable(true);
    }

    public void setIconContentDescription(CharSequence charSequence) {
        if (!TextUtils.equals(charSequence, this.mIconContentDescription)) {
            this.mIconContentDescription = charSequence;
            notifyChanged();
        }
    }

    public void setLinkEnabled(boolean z) {
        if (this.mLinkEnabled != z) {
            this.mLinkEnabled = z;
            notifyChanged();
        }
    }

    public void appendHelpLink(int i) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append(getTitle()).append("\n\n").append(getLearnMoreLink(getContext(), i));
        setTitle((CharSequence) spannableStringBuilder);
    }

    private CharSequence getLearnMoreLink(Context context, int i) {
        AnnotationSpan.LinkInfo linkInfo = new AnnotationSpan.LinkInfo(context, "link", HelpUtils.getHelpIntent(context, context.getString(i), context.getClass().getName()));
        return AnnotationSpan.linkify(context.getText(R.string.footer_learn_more), linkInfo);
    }
}
