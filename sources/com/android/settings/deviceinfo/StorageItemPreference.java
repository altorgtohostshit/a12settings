package com.android.settings.deviceinfo;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.utils.FileSizeFormatter;

public class StorageItemPreference extends Preference {
    private ProgressBar mProgressBar;
    private int mProgressPercent;
    private long mStorageSize;

    public StorageItemPreference(Context context) {
        this(context, (AttributeSet) null);
    }

    public StorageItemPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mProgressPercent = -1;
        setLayoutResource(R.layout.storage_item);
        setSummary((int) R.string.memory_calculating_size);
    }

    public void setStorageSize(long j, long j2) {
        this.mStorageSize = j;
        setSummary((CharSequence) FileSizeFormatter.formatFileSize(getContext(), j, getGigabyteSuffix(getContext().getResources()), 1000000000));
        if (j2 == 0) {
            this.mProgressPercent = 0;
        } else {
            this.mProgressPercent = (int) ((j * 100) / j2);
        }
        updateProgressBar();
    }

    public long getStorageSize() {
        return this.mStorageSize;
    }

    /* access modifiers changed from: protected */
    public void updateProgressBar() {
        ProgressBar progressBar = this.mProgressBar;
        if (progressBar != null && this.mProgressPercent != -1) {
            progressBar.setMax(100);
            this.mProgressBar.setProgress(this.mProgressPercent);
        }
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        this.mProgressBar = (ProgressBar) preferenceViewHolder.findViewById(16908301);
        updateProgressBar();
        super.onBindViewHolder(preferenceViewHolder);
    }

    private static int getGigabyteSuffix(Resources resources) {
        return resources.getIdentifier("gigabyteShort", "string", "android");
    }
}
