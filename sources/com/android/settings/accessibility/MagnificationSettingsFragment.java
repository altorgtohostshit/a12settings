package com.android.settings.accessibility;

import android.app.Dialog;
import android.content.Context;
import com.android.settings.DialogCreatable;
import com.android.settings.R;
import com.android.settings.accessibility.MagnificationModePreferenceController;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class MagnificationSettingsFragment extends DashboardFragment implements MagnificationModePreferenceController.DialogHelper {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.accessibility_magnification_service_settings);
    private DialogCreatable mDialogDelegate;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "MagnificationSettingsFragment";
    }

    public int getMetricsCategory() {
        return 1815;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.accessibility_magnification_service_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((MagnificationModePreferenceController) use(MagnificationModePreferenceController.class)).setDialogHelper(this);
    }

    public void showDialog(int i) {
        super.showDialog(i);
    }

    public void setDialogDelegate(DialogCreatable dialogCreatable) {
        this.mDialogDelegate = dialogCreatable;
    }

    public int getDialogMetricsCategory(int i) {
        DialogCreatable dialogCreatable = this.mDialogDelegate;
        if (dialogCreatable != null) {
            return dialogCreatable.getDialogMetricsCategory(i);
        }
        return 0;
    }

    public Dialog onCreateDialog(int i) {
        Dialog onCreateDialog;
        DialogCreatable dialogCreatable = this.mDialogDelegate;
        if (dialogCreatable != null && (onCreateDialog = dialogCreatable.onCreateDialog(i)) != null) {
            return onCreateDialog;
        }
        throw new IllegalArgumentException("Unsupported dialogId " + i);
    }
}
