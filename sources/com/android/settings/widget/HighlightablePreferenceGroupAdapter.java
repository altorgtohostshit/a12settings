package com.android.settings.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceGroupAdapter;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.google.android.material.appbar.AppBarLayout;

public class HighlightablePreferenceGroupAdapter extends PreferenceGroupAdapter {
    static final long DELAY_COLLAPSE_DURATION_MILLIS = 300;
    static final long DELAY_HIGHLIGHT_DURATION_MILLIS = 600;
    boolean mFadeInAnimated;
    final int mHighlightColor;
    private final String mHighlightKey;
    private int mHighlightPosition = -1;
    private boolean mHighlightRequested;
    /* access modifiers changed from: private */
    public final int mNormalBackgroundRes;

    public static void adjustInitialExpandedChildCount(SettingsPreferenceFragment settingsPreferenceFragment) {
        PreferenceScreen preferenceScreen;
        if (settingsPreferenceFragment != null && (preferenceScreen = settingsPreferenceFragment.getPreferenceScreen()) != null) {
            Bundle arguments = settingsPreferenceFragment.getArguments();
            if (arguments == null || TextUtils.isEmpty(arguments.getString(":settings:fragment_args_key"))) {
                int initialExpandedChildCount = settingsPreferenceFragment.getInitialExpandedChildCount();
                if (initialExpandedChildCount > 0) {
                    preferenceScreen.setInitialExpandedChildrenCount(initialExpandedChildCount);
                    return;
                }
                return;
            }
            preferenceScreen.setInitialExpandedChildrenCount(Integer.MAX_VALUE);
        }
    }

    public HighlightablePreferenceGroupAdapter(PreferenceGroup preferenceGroup, String str, boolean z) {
        super(preferenceGroup);
        this.mHighlightKey = str;
        this.mHighlightRequested = z;
        Context context = preferenceGroup.getContext();
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(16843534, typedValue, true);
        this.mNormalBackgroundRes = typedValue.resourceId;
        this.mHighlightColor = context.getColor(R.color.preference_highligh_color);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder, int i) {
        super.onBindViewHolder(preferenceViewHolder, i);
        updateBackground(preferenceViewHolder, i);
    }

    /* access modifiers changed from: package-private */
    public void updateBackground(PreferenceViewHolder preferenceViewHolder, int i) {
        String str;
        View view = preferenceViewHolder.itemView;
        if (i == this.mHighlightPosition && (str = this.mHighlightKey) != null && TextUtils.equals(str, getItem(i).getKey())) {
            addHighlightBackground(view, !this.mFadeInAnimated);
        } else if (Boolean.TRUE.equals(view.getTag(R.id.preference_highlighted))) {
            removeHighlightBackground(view, false);
        }
    }

    public void requestHighlight(View view, RecyclerView recyclerView, AppBarLayout appBarLayout) {
        int preferenceAdapterPosition;
        if (!this.mHighlightRequested && recyclerView != null && !TextUtils.isEmpty(this.mHighlightKey) && (preferenceAdapterPosition = getPreferenceAdapterPosition(this.mHighlightKey)) >= 0) {
            if (appBarLayout != null) {
                view.postDelayed(new HighlightablePreferenceGroupAdapter$$ExternalSyntheticLambda4(appBarLayout), DELAY_COLLAPSE_DURATION_MILLIS);
            }
            view.postDelayed(new HighlightablePreferenceGroupAdapter$$ExternalSyntheticLambda3(this, recyclerView, preferenceAdapterPosition), DELAY_HIGHLIGHT_DURATION_MILLIS);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$requestHighlight$1(RecyclerView recyclerView, int i) {
        this.mHighlightRequested = true;
        recyclerView.setItemAnimator((RecyclerView.ItemAnimator) null);
        recyclerView.smoothScrollToPosition(i);
        this.mHighlightPosition = i;
        notifyItemChanged(i);
    }

    public boolean isHighlightRequested() {
        return this.mHighlightRequested;
    }

    /* access modifiers changed from: package-private */
    public void requestRemoveHighlightDelayed(View view) {
        view.postDelayed(new HighlightablePreferenceGroupAdapter$$ExternalSyntheticLambda2(this, view), 15000);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$requestRemoveHighlightDelayed$2(View view) {
        this.mHighlightPosition = -1;
        removeHighlightBackground(view, true);
    }

    private void addHighlightBackground(View view, boolean z) {
        view.setTag(R.id.preference_highlighted, Boolean.TRUE);
        if (!z) {
            view.setBackgroundColor(this.mHighlightColor);
            Log.d("HighlightableAdapter", "AddHighlight: Not animation requested - setting highlight background");
            requestRemoveHighlightDelayed(view);
            return;
        }
        this.mFadeInAnimated = true;
        int i = this.mNormalBackgroundRes;
        int i2 = this.mHighlightColor;
        ValueAnimator ofObject = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
        ofObject.setDuration(200);
        ofObject.addUpdateListener(new HighlightablePreferenceGroupAdapter$$ExternalSyntheticLambda0(view));
        ofObject.setRepeatMode(2);
        ofObject.setRepeatCount(4);
        ofObject.start();
        Log.d("HighlightableAdapter", "AddHighlight: starting fade in animation");
        requestRemoveHighlightDelayed(view);
    }

    private void removeHighlightBackground(final View view, boolean z) {
        if (!z) {
            view.setTag(R.id.preference_highlighted, Boolean.FALSE);
            view.setBackgroundResource(this.mNormalBackgroundRes);
            Log.d("HighlightableAdapter", "RemoveHighlight: No animation requested - setting normal background");
        } else if (!Boolean.TRUE.equals(view.getTag(R.id.preference_highlighted))) {
            Log.d("HighlightableAdapter", "RemoveHighlight: Not highlighted - skipping");
        } else {
            int i = this.mHighlightColor;
            int i2 = this.mNormalBackgroundRes;
            view.setTag(R.id.preference_highlighted, Boolean.FALSE);
            ValueAnimator ofObject = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
            ofObject.setDuration(500);
            ofObject.addUpdateListener(new HighlightablePreferenceGroupAdapter$$ExternalSyntheticLambda1(view));
            ofObject.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    view.setBackgroundResource(HighlightablePreferenceGroupAdapter.this.mNormalBackgroundRes);
                }
            });
            ofObject.start();
            Log.d("HighlightableAdapter", "Starting fade out animation");
        }
    }
}
