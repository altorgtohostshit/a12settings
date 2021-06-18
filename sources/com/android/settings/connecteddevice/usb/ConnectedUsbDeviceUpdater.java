package com.android.settings.connecteddevice.usb;

import android.content.Context;
import android.os.UserHandle;
import android.util.AttributeSet;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.connecteddevice.DevicePreferenceCallback;
import com.android.settings.connecteddevice.usb.UsbConnectionBroadcastReceiver;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedPreference;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;

public class ConnectedUsbDeviceUpdater {
    private DevicePreferenceCallback mDevicePreferenceCallback;
    private DashboardFragment mFragment;
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private UsbBackend mUsbBackend;
    UsbConnectionBroadcastReceiver.UsbConnectionListener mUsbConnectionListener;
    RestrictedPreference mUsbPreference;
    UsbConnectionBroadcastReceiver mUsbReceiver;

    public static int getSummary(long j, int i) {
        return i != 1 ? i != 2 ? R.string.usb_summary_charging_only : j == 4 ? R.string.usb_summary_file_transfers : j == 32 ? R.string.usb_summary_tether : j == 16 ? R.string.usb_summary_photo_transfers : j == 8 ? R.string.usb_summary_MIDI : R.string.usb_summary_charging_only : j == 4 ? R.string.usb_summary_file_transfers_power : j == 32 ? R.string.usb_summary_tether_power : j == 16 ? R.string.usb_summary_photo_transfers_power : j == 8 ? R.string.usb_summary_MIDI_power : R.string.usb_summary_power_only;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(boolean z, long j, int i, int i2) {
        if (z) {
            RestrictedPreference restrictedPreference = this.mUsbPreference;
            if (i2 != 2) {
                j = 0;
            }
            restrictedPreference.setSummary(getSummary(j, i));
            this.mDevicePreferenceCallback.onDeviceAdded(this.mUsbPreference);
            return;
        }
        this.mDevicePreferenceCallback.onDeviceRemoved(this.mUsbPreference);
    }

    public ConnectedUsbDeviceUpdater(Context context, DashboardFragment dashboardFragment, DevicePreferenceCallback devicePreferenceCallback) {
        this(context, dashboardFragment, devicePreferenceCallback, new UsbBackend(context));
    }

    ConnectedUsbDeviceUpdater(Context context, DashboardFragment dashboardFragment, DevicePreferenceCallback devicePreferenceCallback, UsbBackend usbBackend) {
        this.mUsbConnectionListener = new ConnectedUsbDeviceUpdater$$ExternalSyntheticLambda1(this);
        this.mFragment = dashboardFragment;
        this.mDevicePreferenceCallback = devicePreferenceCallback;
        this.mUsbBackend = usbBackend;
        this.mUsbReceiver = new UsbConnectionBroadcastReceiver(context, this.mUsbConnectionListener, this.mUsbBackend);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(this.mFragment.getContext()).getMetricsFeatureProvider();
    }

    public void registerCallback() {
        this.mUsbReceiver.register();
    }

    public void unregisterCallback() {
        this.mUsbReceiver.unregister();
    }

    public void initUsbPreference(Context context) {
        RestrictedPreference restrictedPreference = new RestrictedPreference(context, (AttributeSet) null);
        this.mUsbPreference = restrictedPreference;
        restrictedPreference.setTitle((int) R.string.usb_pref);
        this.mUsbPreference.setIcon((int) R.drawable.ic_usb);
        this.mUsbPreference.setKey("connected_usb");
        this.mUsbPreference.setDisabledByAdmin(RestrictedLockUtilsInternal.checkIfUsbDataSignalingIsDisabled(context, UserHandle.myUserId()));
        this.mUsbPreference.setOnPreferenceClickListener(new ConnectedUsbDeviceUpdater$$ExternalSyntheticLambda0(this));
        forceUpdate();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$initUsbPreference$1(Preference preference) {
        this.mMetricsFeatureProvider.logClickedPreference(preference, this.mFragment.getMetricsCategory());
        new SubSettingLauncher(this.mFragment.getContext()).setDestination(UsbDetailsFragment.class.getName()).setTitleRes(R.string.usb_preference).setSourceMetricsCategory(this.mFragment.getMetricsCategory()).launch();
        return true;
    }

    private void forceUpdate() {
        this.mUsbReceiver.register();
    }
}
