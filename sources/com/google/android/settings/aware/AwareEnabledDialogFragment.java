package com.google.android.settings.aware;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class AwareEnabledDialogFragment extends InstrumentedDialogFragment {
    public int getMetricsCategory() {
        return 1701;
    }

    public static void show(Fragment fragment, Boolean bool) {
        AwareEnabledDialogFragment awareEnabledDialogFragment = new AwareEnabledDialogFragment();
        Bundle bundle = new Bundle(1);
        bundle.putBoolean("extra_aware_allowed", bool.booleanValue());
        awareEnabledDialogFragment.setArguments(bundle);
        awareEnabledDialogFragment.setTargetFragment(fragment, 0);
        awareEnabledDialogFragment.show(fragment.getFragmentManager(), "AwareSettingsDialog");
    }

    public Dialog onCreateDialog(Bundle bundle) {
        int i;
        int i2;
        if (getArguments().getBoolean("extra_aware_allowed")) {
            i = R.string.aware_settings_enabled_info_dialog_title;
            i2 = R.string.aware_settings_enabled_info_dialog_content;
        } else {
            i = R.string.aware_settings_disabled_info_dialog_title;
            i2 = R.string.aware_settings_disabled_info_dialog_content;
        }
        return new AlertDialog.Builder(getContext()).setTitle(i).setMessage(i2).setPositiveButton((int) R.string.nfc_how_it_works_got_it, (DialogInterface.OnClickListener) null).create();
    }
}
