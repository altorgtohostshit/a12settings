package com.android.settings.applications.defaultapps;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settings.fuelgauge.BatteryUtils;
import com.android.settings.widget.RadioButtonPickerFragment;
import com.android.settingslib.applications.DefaultAppInfo;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.widget.CandidateInfo;
import com.android.settingslib.widget.RadioButtonPreference;

public abstract class DefaultAppPickerFragment extends RadioButtonPickerFragment {
    protected BatteryUtils mBatteryUtils;
    protected PackageManager mPm;

    /* access modifiers changed from: protected */
    public CharSequence getConfirmationMessage(CandidateInfo candidateInfo) {
        return null;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mPm = context.getPackageManager();
        this.mBatteryUtils = BatteryUtils.getInstance(context);
    }

    public void onRadioButtonClicked(RadioButtonPreference radioButtonPreference) {
        String key = radioButtonPreference.getKey();
        CharSequence confirmationMessage = getConfirmationMessage(getCandidate(key));
        FragmentActivity activity = getActivity();
        if (TextUtils.isEmpty(confirmationMessage)) {
            super.onRadioButtonClicked(radioButtonPreference);
        } else if (activity != null) {
            newConfirmationDialogFragment(key, confirmationMessage).show(activity.getSupportFragmentManager(), "DefaultAppConfirm");
        }
    }

    /* access modifiers changed from: protected */
    public void onRadioButtonConfirmed(String str) {
        MetricsFeatureProvider metricsFeatureProvider = this.mMetricsFeatureProvider;
        metricsFeatureProvider.action(metricsFeatureProvider.getAttribution(getActivity()), 1000, getMetricsCategory(), str, 0);
        super.onRadioButtonConfirmed(str);
    }

    public void bindPreferenceExtra(RadioButtonPreference radioButtonPreference, String str, CandidateInfo candidateInfo, String str2, String str3) {
        if (candidateInfo instanceof DefaultAppInfo) {
            if (TextUtils.equals(str3, str)) {
                radioButtonPreference.setSummary((int) R.string.system_app);
                return;
            }
            DefaultAppInfo defaultAppInfo = (DefaultAppInfo) candidateInfo;
            if (!TextUtils.isEmpty(defaultAppInfo.summary)) {
                radioButtonPreference.setSummary((CharSequence) defaultAppInfo.summary);
            }
        }
    }

    /* access modifiers changed from: protected */
    public ConfirmationDialogFragment newConfirmationDialogFragment(String str, CharSequence charSequence) {
        ConfirmationDialogFragment confirmationDialogFragment = new ConfirmationDialogFragment();
        confirmationDialogFragment.init(this, str, charSequence);
        return confirmationDialogFragment;
    }

    public static class ConfirmationDialogFragment extends InstrumentedDialogFragment implements DialogInterface.OnClickListener {
        private DialogInterface.OnClickListener mCancelListener;

        public int getMetricsCategory() {
            return 791;
        }

        public void init(DefaultAppPickerFragment defaultAppPickerFragment, String str, CharSequence charSequence) {
            Bundle bundle = new Bundle();
            bundle.putString("extra_key", str);
            bundle.putCharSequence("extra_message", charSequence);
            setArguments(bundle);
            setTargetFragment(defaultAppPickerFragment, 0);
        }

        public void setCancelListener(DialogInterface.OnClickListener onClickListener) {
            this.mCancelListener = onClickListener;
        }

        public Dialog onCreateDialog(Bundle bundle) {
            return new AlertDialog.Builder(getActivity()).setMessage(getArguments().getCharSequence("extra_message")).setPositiveButton(17039370, (DialogInterface.OnClickListener) this).setNegativeButton(17039360, this.mCancelListener).create();
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            Fragment targetFragment = getTargetFragment();
            if (targetFragment instanceof DefaultAppPickerFragment) {
                ((DefaultAppPickerFragment) targetFragment).onRadioButtonConfirmed(getArguments().getString("extra_key"));
            }
        }
    }
}
