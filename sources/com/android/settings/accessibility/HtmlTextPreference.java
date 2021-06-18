package com.android.settings.accessibility;

import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;

public final class HtmlTextPreference extends StaticTextPreference {
    private int mFlag;
    private Html.ImageGetter mImageGetter;
    private Html.TagHandler mTagHandler;

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.itemView.findViewById(16908304);
        if (textView != null && !TextUtils.isEmpty(getSummary())) {
            textView.setText(Html.fromHtml(getSummary().toString(), this.mFlag, this.mImageGetter, this.mTagHandler));
        }
    }
}
