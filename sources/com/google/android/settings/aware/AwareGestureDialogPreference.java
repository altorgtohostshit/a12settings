package com.google.android.settings.aware;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;

abstract class AwareGestureDialogPreference extends AwareDialogPreferenceBase implements DialogInterface.OnClickListener {
    /* access modifiers changed from: package-private */
    public abstract String getDestination();

    /* access modifiers changed from: package-private */
    public abstract int getDialogDisabledMessage();

    /* access modifiers changed from: package-private */
    public abstract int getGestureDialogMessage();

    /* access modifiers changed from: package-private */
    public abstract int getGestureDialogTitle();

    public AwareGestureDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        new SubSettingLauncher(getContext()).setDestination(AwareSettings.class.getName()).setSourceMetricsCategory(getSourceMetricsCategory()).launch();
    }

    /* access modifiers changed from: protected */
    public boolean isAvailable() {
        return this.mHelper.isGestureConfigurable();
    }

    /* access modifiers changed from: protected */
    public void performEnabledClick() {
        super.performEnabledClick();
        new SubSettingLauncher(getContext()).setDestination(getDestination()).setSourceMetricsCategory(getSourceMetricsCategory()).launch();
    }

    /* access modifiers changed from: protected */
    public void onPrepareDialogBuilder(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener) {
        super.onPrepareDialogBuilder(builder, onClickListener);
        if (!this.mHelper.isSupported()) {
            builder.setTitle(getGestureDialogTitle()).setMessage(getDialogDisabledMessage()).setPositiveButton((int) R.string.gesture_aware_confirmation_action_button, (DialogInterface.OnClickListener) null).setNegativeButton((CharSequence) "", (DialogInterface.OnClickListener) null);
        } else {
            builder.setTitle(getGestureDialogTitle()).setMessage(getGestureDialogMessage()).setPositiveButton((int) R.string.aware_disabled_preference_action, (DialogInterface.OnClickListener) this).setNegativeButton((int) R.string.aware_disabled_preference_neutral, (DialogInterface.OnClickListener) null);
        }
    }
}
