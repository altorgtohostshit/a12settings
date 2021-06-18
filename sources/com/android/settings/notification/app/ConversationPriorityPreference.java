package com.android.settings.notification.app;

import android.content.Context;
import android.content.res.ColorStateList;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settingslib.Utils;

public class ConversationPriorityPreference extends Preference {
    private View mAlertButton;
    private Context mContext;
    private int mImportance;
    private boolean mIsConfigurable = true;
    private int mOriginalImportance;
    private View mPriorityButton;
    private boolean mPriorityConversation;
    private View mSilenceButton;

    public ConversationPriorityPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context);
    }

    public ConversationPriorityPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    public ConversationPriorityPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public ConversationPriorityPreference(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        setLayoutResource(R.layout.notif_priority_conversation_preference);
    }

    public void setImportance(int i) {
        this.mImportance = i;
    }

    public void setConfigurable(boolean z) {
        this.mIsConfigurable = z;
    }

    public void setPriorityConversation(boolean z) {
        this.mPriorityConversation = z;
    }

    public void setOriginalImportance(int i) {
        this.mOriginalImportance = i;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.itemView.setClickable(false);
        this.mSilenceButton = preferenceViewHolder.findViewById(R.id.silence);
        this.mAlertButton = preferenceViewHolder.findViewById(R.id.alert);
        this.mPriorityButton = preferenceViewHolder.findViewById(R.id.priority_group);
        if (!this.mIsConfigurable) {
            this.mSilenceButton.setEnabled(false);
            this.mAlertButton.setEnabled(false);
            this.mPriorityButton.setEnabled(false);
        }
        updateToggles((ViewGroup) preferenceViewHolder.itemView, this.mImportance, this.mPriorityConversation, false);
        this.mSilenceButton.setOnClickListener(new ConversationPriorityPreference$$ExternalSyntheticLambda1(this, preferenceViewHolder));
        this.mAlertButton.setOnClickListener(new ConversationPriorityPreference$$ExternalSyntheticLambda2(this, preferenceViewHolder));
        this.mPriorityButton.setOnClickListener(new ConversationPriorityPreference$$ExternalSyntheticLambda0(this, preferenceViewHolder));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(PreferenceViewHolder preferenceViewHolder, View view) {
        callChangeListener(new Pair(2, Boolean.FALSE));
        updateToggles((ViewGroup) preferenceViewHolder.itemView, 2, false, true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$1(PreferenceViewHolder preferenceViewHolder, View view) {
        int max = Math.max(this.mOriginalImportance, 3);
        callChangeListener(new Pair(Integer.valueOf(max), Boolean.FALSE));
        updateToggles((ViewGroup) preferenceViewHolder.itemView, max, false, true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$2(PreferenceViewHolder preferenceViewHolder, View view) {
        int max = Math.max(this.mOriginalImportance, 3);
        callChangeListener(new Pair(Integer.valueOf(max), Boolean.TRUE));
        updateToggles((ViewGroup) preferenceViewHolder.itemView, max, true, true);
    }

    private ColorStateList getAccentTint() {
        return Utils.getColorAccent(getContext());
    }

    private ColorStateList getRegularTint() {
        return Utils.getColorAttr(getContext(), 16842806);
    }

    /* access modifiers changed from: package-private */
    public void updateToggles(ViewGroup viewGroup, int i, boolean z, boolean z2) {
        if (z2) {
            AutoTransition autoTransition = new AutoTransition();
            autoTransition.setDuration(100);
            TransitionManager.beginDelayedTransition(viewGroup, autoTransition);
        }
        if (i <= 2 && i > -1000) {
            setSelected(this.mPriorityButton, false);
            setSelected(this.mAlertButton, false);
            setSelected(this.mSilenceButton, true);
        } else if (z) {
            setSelected(this.mPriorityButton, true);
            setSelected(this.mAlertButton, false);
            setSelected(this.mSilenceButton, false);
        } else {
            setSelected(this.mPriorityButton, false);
            setSelected(this.mAlertButton, true);
            setSelected(this.mSilenceButton, false);
        }
    }

    /* access modifiers changed from: package-private */
    public void setSelected(View view, boolean z) {
        ColorStateList accentTint = getAccentTint();
        ColorStateList regularTint = getRegularTint();
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        TextView textView = (TextView) view.findViewById(R.id.label);
        TextView textView2 = (TextView) view.findViewById(R.id.summary);
        imageView.setImageTintList(z ? accentTint : regularTint);
        if (!z) {
            accentTint = regularTint;
        }
        textView.setTextColor(accentTint);
        textView2.setVisibility(z ? 0 : 8);
        view.setBackground(this.mContext.getDrawable(z ? R.drawable.button_border_selected : R.drawable.button_border_unselected));
        view.post(new ConversationPriorityPreference$$ExternalSyntheticLambda3(view, z));
    }
}
