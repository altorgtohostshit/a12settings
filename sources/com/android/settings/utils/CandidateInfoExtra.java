package com.android.settings.utils;

import android.graphics.drawable.Drawable;
import com.android.settingslib.widget.CandidateInfo;

public class CandidateInfoExtra extends CandidateInfo {
    private final String mKey;
    private final CharSequence mLabel;
    private final CharSequence mSummary;

    public Drawable loadIcon() {
        return null;
    }

    public CandidateInfoExtra(CharSequence charSequence, CharSequence charSequence2, String str, boolean z) {
        super(z);
        this.mLabel = charSequence;
        this.mSummary = charSequence2;
        this.mKey = str;
    }

    public CharSequence loadLabel() {
        return this.mLabel;
    }

    public CharSequence loadSummary() {
        return this.mSummary;
    }

    public String getKey() {
        return this.mKey;
    }
}
