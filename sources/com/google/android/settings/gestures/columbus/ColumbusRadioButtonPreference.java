package com.google.android.settings.gestures.columbus;

import android.content.Context;
import com.android.settingslib.widget.RadioButtonPreference;

public class ColumbusRadioButtonPreference extends RadioButtonPreference {
    private ContextualSummaryProvider mContextualSummaryProvider;
    private int mMetric;

    public interface ContextualSummaryProvider {
        CharSequence getSummary(Context context);
    }

    public ColumbusRadioButtonPreference(Context context) {
        super(context);
    }

    /* access modifiers changed from: package-private */
    public void setMetric(int i) {
        this.mMetric = i;
    }

    /* access modifiers changed from: package-private */
    public int getMetric() {
        return this.mMetric;
    }

    /* access modifiers changed from: package-private */
    public void setContextualSummaryProvider(ContextualSummaryProvider contextualSummaryProvider) {
        this.mContextualSummaryProvider = contextualSummaryProvider;
    }

    /* access modifiers changed from: package-private */
    public void updateSummary(Context context) {
        ContextualSummaryProvider contextualSummaryProvider = this.mContextualSummaryProvider;
        if (contextualSummaryProvider == null) {
            setSummary((CharSequence) null);
        } else {
            setSummary(contextualSummaryProvider.getSummary(context));
        }
    }
}
