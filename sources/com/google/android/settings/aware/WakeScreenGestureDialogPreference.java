package com.google.android.settings.aware;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;

public class WakeScreenGestureDialogPreference extends AwareGestureDialogPreference {
    /* access modifiers changed from: package-private */
    public int getDialogDisabledMessage() {
        return R.string.wake_screen_aware_disabled_info_dialog_content;
    }

    public int getGestureDialogMessage() {
        return R.string.wake_screen_aware_off_dialog_content;
    }

    public int getGestureDialogTitle() {
        return R.string.wake_screen_aware_off_dialog_title;
    }

    public /* bridge */ /* synthetic */ void onClick(DialogInterface dialogInterface, int i) {
        super.onClick(dialogInterface, i);
    }

    public WakeScreenGestureDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public String getDestination() {
        return WakeScreenGestureSettings.class.getName();
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ((TextView) preferenceViewHolder.findViewById(16908310)).setSingleLine(false);
    }
}
