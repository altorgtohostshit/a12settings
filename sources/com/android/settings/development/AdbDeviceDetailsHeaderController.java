package com.android.settings.development;

import android.app.Activity;
import android.content.Context;
import android.debug.PairDevice;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.widget.EntityHeaderController;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.widget.LayoutPreference;

public class AdbDeviceDetailsHeaderController extends AbstractPreferenceController implements PreferenceControllerMixin, LifecycleObserver {
    static final String KEY_HEADER = "adb_device_header";
    private EntityHeaderController mEntityHeaderController;
    private final Fragment mFragment;
    private PairDevice mPairedDevice;

    public String getPreferenceKey() {
        return KEY_HEADER;
    }

    public boolean isAvailable() {
        return true;
    }

    public AdbDeviceDetailsHeaderController(PairDevice pairDevice, Context context, Fragment fragment) {
        super(context);
        this.mPairedDevice = pairDevice;
        this.mFragment = fragment;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        setupEntityHeader(preferenceScreen);
    }

    private void setupEntityHeader(PreferenceScreen preferenceScreen) {
        EntityHeaderController newInstance = EntityHeaderController.newInstance(this.mFragment.getActivity(), this.mFragment, ((LayoutPreference) preferenceScreen.findPreference(KEY_HEADER)).findViewById(R.id.entity_header));
        this.mEntityHeaderController = newInstance;
        newInstance.setIcon(this.mContext.getDrawable(17302328)).setLabel((CharSequence) this.mPairedDevice.getDeviceName()).done((Activity) this.mFragment.getActivity(), true);
    }
}
