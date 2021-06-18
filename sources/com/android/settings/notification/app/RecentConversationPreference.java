package com.android.settings.notification.app;

import android.content.Context;
import android.view.View;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settingslib.widget.TwoTargetPreference;

public class RecentConversationPreference extends TwoTargetPreference {
    private View mClearView;
    private OnClearClickListener mOnClearClickListener;

    public interface OnClearClickListener {
        void onClear();
    }

    /* access modifiers changed from: package-private */
    public int getClearId() {
        return R.id.clear_button;
    }

    /* access modifiers changed from: protected */
    public int getSecondTargetResId() {
        return R.layout.preference_widget_clear;
    }

    public RecentConversationPreference(Context context) {
        super(context);
    }

    public void setOnClearClickListener(OnClearClickListener onClearClickListener) {
        this.mOnClearClickListener = onClearClickListener;
    }

    /* access modifiers changed from: package-private */
    public View getClearView() {
        return this.mClearView;
    }

    /* access modifiers changed from: package-private */
    public boolean hasClearListener() {
        return this.mOnClearClickListener != null;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.findViewById(16908312).setVisibility(this.mOnClearClickListener != null ? 0 : 8);
        View findViewById = preferenceViewHolder.findViewById(getClearId());
        this.mClearView = findViewById;
        findViewById.setOnClickListener(new RecentConversationPreference$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(View view) {
        OnClearClickListener onClearClickListener = this.mOnClearClickListener;
        if (onClearClickListener != null) {
            onClearClickListener.onClear();
        }
    }
}
