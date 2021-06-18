package com.android.settings.development;

import android.content.Context;
import android.content.Intent;
import android.debug.PairDevice;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.widget.ActionButtonsPreference;

public class AdbDeviceDetailsActionController extends AbstractPreferenceController {
    static final String KEY_BUTTONS_PREF = "buttons";
    private ActionButtonsPreference mButtonsPref;
    private final Fragment mFragment;
    private PairDevice mPairedDevice;

    public String getPreferenceKey() {
        return KEY_BUTTONS_PREF;
    }

    public boolean isAvailable() {
        return true;
    }

    public AdbDeviceDetailsActionController(PairDevice pairDevice, Context context, Fragment fragment) {
        super(context);
        this.mPairedDevice = pairDevice;
        this.mFragment = fragment;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mButtonsPref = ((ActionButtonsPreference) preferenceScreen.findPreference(getPreferenceKey())).setButton1Visible(false).setButton2Icon(R.drawable.ic_settings_delete).setButton2Text(R.string.adb_device_forget).setButton2OnClickListener(new AdbDeviceDetailsActionController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$0(View view) {
        forgetDevice();
    }

    private void forgetDevice() {
        Intent intent = new Intent();
        intent.putExtra("request_type", 0);
        intent.putExtra("paired_device", this.mPairedDevice);
        this.mFragment.getActivity().setResult(-1, intent);
        this.mFragment.getActivity().finish();
    }
}
