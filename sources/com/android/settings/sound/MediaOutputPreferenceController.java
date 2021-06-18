package com.android.settings.sound;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.media.MediaOutputUtils;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.Utils;
import com.android.settingslib.bluetooth.A2dpProfile;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.HearingAidProfile;
import java.util.List;

public class MediaOutputPreferenceController extends AudioSwitchPreferenceController {
    private MediaController mMediaController;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ void onAclConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        super.onAclConnectionStateChanged(cachedBluetoothDevice, i);
    }

    public /* bridge */ /* synthetic */ void onConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        super.onConnectionStateChanged(cachedBluetoothDevice, i);
    }

    public /* bridge */ /* synthetic */ void onDeviceBondStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        super.onDeviceBondStateChanged(cachedBluetoothDevice, i);
    }

    public /* bridge */ /* synthetic */ void onDeviceDeleted(CachedBluetoothDevice cachedBluetoothDevice) {
        super.onDeviceDeleted(cachedBluetoothDevice);
    }

    public /* bridge */ /* synthetic */ void onScanningStateChanged(boolean z) {
        super.onScanningStateChanged(z);
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public MediaOutputPreferenceController(Context context, String str) {
        super(context, str);
        this.mMediaController = MediaOutputUtils.getActiveLocalMediaController((MediaSessionManager) context.getSystemService(MediaSessionManager.class));
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        if (!Utils.isAudioModeOngoingCall(this.mContext) && this.mMediaController != null) {
            this.mPreference.setVisible(true);
        }
    }

    public void updateState(Preference preference) {
        CharSequence charSequence;
        if (preference != null && this.mMediaController != null) {
            if (Utils.isAudioModeOngoingCall(this.mContext)) {
                this.mPreference.setVisible(false);
                preference.setSummary(this.mContext.getText(R.string.media_out_summary_ongoing_call_state));
                return;
            }
            BluetoothDevice bluetoothDevice = null;
            List<BluetoothDevice> connectedA2dpDevices = getConnectedA2dpDevices();
            List<BluetoothDevice> connectedHearingAidDevices = getConnectedHearingAidDevices();
            if (this.mAudioManager.getMode() == 0 && ((connectedA2dpDevices != null && !connectedA2dpDevices.isEmpty()) || (connectedHearingAidDevices != null && !connectedHearingAidDevices.isEmpty()))) {
                bluetoothDevice = findActiveDevice();
            }
            Preference preference2 = this.mPreference;
            Context context = this.mContext;
            preference2.setTitle((CharSequence) context.getString(R.string.media_output_label_title, new Object[]{com.android.settings.Utils.getApplicationLabel(context, this.mMediaController.getPackageName())}));
            Preference preference3 = this.mPreference;
            if (bluetoothDevice == null) {
                charSequence = this.mContext.getText(R.string.media_output_default_summary);
            } else {
                charSequence = bluetoothDevice.getAlias();
            }
            preference3.setSummary(charSequence);
        }
    }

    public BluetoothDevice findActiveDevice() {
        BluetoothDevice findActiveHearingAidDevice = findActiveHearingAidDevice();
        A2dpProfile a2dpProfile = this.mProfileManager.getA2dpProfile();
        return (findActiveHearingAidDevice != null || a2dpProfile == null) ? findActiveHearingAidDevice : a2dpProfile.getActiveDevice();
    }

    /* access modifiers changed from: protected */
    public BluetoothDevice findActiveHearingAidDevice() {
        HearingAidProfile hearingAidProfile = this.mProfileManager.getHearingAidProfile();
        if (hearingAidProfile == null) {
            return null;
        }
        for (BluetoothDevice next : hearingAidProfile.getActiveDevices()) {
            if (next != null) {
                return next;
            }
        }
        return null;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return false;
        }
        this.mContext.sendBroadcast(new Intent().setAction("com.android.systemui.action.LAUNCH_MEDIA_OUTPUT_DIALOG").setPackage("com.android.systemui").putExtra("package_name", this.mMediaController.getPackageName()).putExtra("key_media_session_token", this.mMediaController.getSessionToken()));
        return true;
    }
}
