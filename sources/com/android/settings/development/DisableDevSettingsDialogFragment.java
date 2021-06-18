package com.android.settings.development;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class DisableDevSettingsDialogFragment extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
    public int getMetricsCategory() {
        return 1591;
    }

    static DisableDevSettingsDialogFragment newInstance() {
        return new DisableDevSettingsDialogFragment();
    }

    public static void show(DevelopmentSettingsDashboardFragment developmentSettingsDashboardFragment) {
        DisableDevSettingsDialogFragment disableDevSettingsDialogFragment = new DisableDevSettingsDialogFragment();
        disableDevSettingsDialogFragment.setTargetFragment(developmentSettingsDashboardFragment, 0);
        disableDevSettingsDialogFragment.show(developmentSettingsDashboardFragment.getActivity().getSupportFragmentManager(), "DisableDevSettingDlg");
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(getActivity()).setMessage((int) R.string.bluetooth_disable_a2dp_hw_offload_dialog_message).setTitle((int) R.string.bluetooth_disable_a2dp_hw_offload_dialog_title).setPositiveButton((int) R.string.bluetooth_disable_a2dp_hw_offload_dialog_confirm, (DialogInterface.OnClickListener) this).setNegativeButton((int) R.string.bluetooth_disable_a2dp_hw_offload_dialog_cancel, (DialogInterface.OnClickListener) this).create();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        Fragment targetFragment = getTargetFragment();
        if (!(targetFragment instanceof DevelopmentSettingsDashboardFragment)) {
            Log.e("DisableDevSettingDlg", "getTargetFragment return unexpected type");
        }
        DevelopmentSettingsDashboardFragment developmentSettingsDashboardFragment = (DevelopmentSettingsDashboardFragment) targetFragment;
        if (i == -1) {
            developmentSettingsDashboardFragment.onDisableDevelopmentOptionsConfirmed();
            ((PowerManager) getContext().getSystemService(PowerManager.class)).reboot((String) null);
            return;
        }
        developmentSettingsDashboardFragment.onDisableDevelopmentOptionsRejected();
    }
}
