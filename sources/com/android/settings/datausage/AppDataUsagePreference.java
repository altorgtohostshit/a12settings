package com.android.settings.datausage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ProgressBar;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settingslib.AppItem;
import com.android.settingslib.net.UidDetail;
import com.android.settingslib.net.UidDetailProvider;
import com.android.settingslib.utils.ThreadUtils;
import com.android.settingslib.widget.AppPreference;
import java.text.NumberFormat;

public class AppDataUsagePreference extends AppPreference {
    private UidDetail mDetail;
    private final AppItem mItem;
    private final int mPercent;

    public AppDataUsagePreference(Context context, AppItem appItem, int i, UidDetailProvider uidDetailProvider) {
        super(context);
        this.mItem = appItem;
        this.mPercent = i;
        if (!appItem.restricted || appItem.total > 0) {
            setSummary(DataUsageUtils.formatDataUsage(context, appItem.total));
        } else {
            setSummary((int) R.string.data_usage_app_restricted);
        }
        UidDetail uidDetail = uidDetailProvider.getUidDetail(appItem.key, false);
        this.mDetail = uidDetail;
        if (uidDetail != null) {
            lambda$new$0();
        } else {
            ThreadUtils.postOnBackgroundThread((Runnable) new AppDataUsagePreference$$ExternalSyntheticLambda1(this, uidDetailProvider));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(UidDetailProvider uidDetailProvider) {
        this.mDetail = uidDetailProvider.getUidDetail(this.mItem.key, true);
        ThreadUtils.postOnMainThread(new AppDataUsagePreference$$ExternalSyntheticLambda0(this));
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ProgressBar progressBar = (ProgressBar) preferenceViewHolder.findViewById(16908301);
        AppItem appItem = this.mItem;
        if (!appItem.restricted || appItem.total > 0) {
            progressBar.setVisibility(0);
        } else {
            progressBar.setVisibility(8);
        }
        progressBar.setProgress(this.mPercent);
        progressBar.setContentDescription(NumberFormat.getPercentInstance().format(((double) this.mPercent) / 100.0d));
    }

    /* access modifiers changed from: private */
    /* renamed from: setAppInfo */
    public void lambda$new$0() {
        UidDetail uidDetail = this.mDetail;
        if (uidDetail != null) {
            setIcon(uidDetail.icon);
            setTitle(this.mDetail.label);
            return;
        }
        setIcon((Drawable) null);
        setTitle((CharSequence) null);
    }

    public AppItem getItem() {
        return this.mItem;
    }
}
