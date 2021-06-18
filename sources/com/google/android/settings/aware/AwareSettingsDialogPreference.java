package com.google.android.settings.aware;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;

public class AwareSettingsDialogPreference extends AwareDialogPreferenceBase {
    public AwareSettingsDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AwareSettingsDialogPreference(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public boolean isAvailable() {
        return this.mHelper.isAvailable();
    }

    /* access modifiers changed from: protected */
    public void performEnabledClick() {
        super.performEnabledClick();
        new SubSettingLauncher(getContext()).setDestination(AwareSettings.class.getName()).setSourceMetricsCategory(getSourceMetricsCategory()).launch();
    }

    /* access modifiers changed from: protected */
    public void onPrepareDialogBuilder(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener) {
        super.onPrepareDialogBuilder(builder, onClickListener);
        builder.setTitle((int) R.string.aware_settings_disabled_info_dialog_title).setMessage((int) R.string.aware_settings_disabled_info_dialog_content).setPositiveButton((int) R.string.nfc_how_it_works_got_it, (DialogInterface.OnClickListener) null).setNegativeButton((CharSequence) "", (DialogInterface.OnClickListener) null);
    }
}
