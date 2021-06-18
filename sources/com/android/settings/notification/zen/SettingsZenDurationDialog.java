package com.android.settings.notification.zen;

import android.app.Dialog;
import android.os.Bundle;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settingslib.notification.ZenDurationDialog;

public class SettingsZenDurationDialog extends InstrumentedDialogFragment {
    public int getMetricsCategory() {
        return 1341;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return new ZenDurationDialog(getContext()).createDialog();
    }
}
