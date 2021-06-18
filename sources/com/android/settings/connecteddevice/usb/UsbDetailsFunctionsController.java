package com.android.settings.connecteddevice.usb;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.net.TetheringManager;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.util.Log;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settingslib.widget.RadioButtonPreference;
import java.util.LinkedHashMap;
import java.util.Map;

public class UsbDetailsFunctionsController extends UsbDetailsController implements RadioButtonPreference.OnClickListener {
    private static final boolean DEBUG = Log.isLoggable("UsbFunctionsCtrl", 3);
    static final Map<Long, Integer> FUNCTIONS_MAP;
    private Handler mHandler;
    OnStartTetheringCallback mOnStartTetheringCallback = new OnStartTetheringCallback();
    long mPreviousFunction = this.mUsbBackend.getCurrentFunctions();
    private PreferenceCategory mProfilesContainer;
    private TetheringManager mTetheringManager;

    private boolean isAccessoryMode(long j) {
        return (j & 2) != 0;
    }

    public String getPreferenceKey() {
        return "usb_details_functions";
    }

    static {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        FUNCTIONS_MAP = linkedHashMap;
        linkedHashMap.put(4L, Integer.valueOf(R.string.usb_use_file_transfers));
        linkedHashMap.put(32L, Integer.valueOf(R.string.usb_use_tethering));
        linkedHashMap.put(8L, Integer.valueOf(R.string.usb_use_MIDI));
        linkedHashMap.put(16L, Integer.valueOf(R.string.usb_use_photo_transfers));
        linkedHashMap.put(0L, Integer.valueOf(R.string.usb_use_charging_only));
    }

    public UsbDetailsFunctionsController(Context context, UsbDetailsFragment usbDetailsFragment, UsbBackend usbBackend) {
        super(context, usbDetailsFragment, usbBackend);
        this.mTetheringManager = (TetheringManager) context.getSystemService(TetheringManager.class);
        this.mHandler = new Handler(context.getMainLooper());
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mProfilesContainer = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
    }

    private RadioButtonPreference getProfilePreference(String str, int i) {
        RadioButtonPreference radioButtonPreference = (RadioButtonPreference) this.mProfilesContainer.findPreference(str);
        if (radioButtonPreference != null) {
            return radioButtonPreference;
        }
        RadioButtonPreference radioButtonPreference2 = new RadioButtonPreference(this.mProfilesContainer.getContext());
        radioButtonPreference2.setKey(str);
        radioButtonPreference2.setTitle(i);
        radioButtonPreference2.setOnClickListener(this);
        this.mProfilesContainer.addPreference(radioButtonPreference2);
        return radioButtonPreference2;
    }

    /* access modifiers changed from: protected */
    public void refresh(boolean z, long j, int i, int i2) {
        if (DEBUG) {
            Log.d("UsbFunctionsCtrl", "refresh() connected : " + z + ", functions : " + j + ", powerRole : " + i + ", dataRole : " + i2);
        }
        if (!z || i2 != 2) {
            this.mProfilesContainer.setEnabled(false);
        } else {
            this.mProfilesContainer.setEnabled(true);
        }
        for (Long longValue : FUNCTIONS_MAP.keySet()) {
            long longValue2 = longValue.longValue();
            RadioButtonPreference profilePreference = getProfilePreference(UsbBackend.usbFunctionsToString(longValue2), FUNCTIONS_MAP.get(Long.valueOf(longValue2)).intValue());
            if (!this.mUsbBackend.areFunctionsSupported(longValue2)) {
                this.mProfilesContainer.removePreference(profilePreference);
            } else if (isAccessoryMode(j)) {
                profilePreference.setChecked(4 == longValue2);
            } else {
                profilePreference.setChecked(j == longValue2);
            }
        }
    }

    public void onRadioButtonClicked(RadioButtonPreference radioButtonPreference) {
        long usbFunctionsFromString = UsbBackend.usbFunctionsFromString(radioButtonPreference.getKey());
        long currentFunctions = this.mUsbBackend.getCurrentFunctions();
        if (DEBUG) {
            Log.d("UsbFunctionsCtrl", "onRadioButtonClicked() function : " + usbFunctionsFromString + ", toString() : " + UsbManager.usbFunctionsToString(usbFunctionsFromString) + ", previousFunction : " + currentFunctions + ", toString() : " + UsbManager.usbFunctionsToString(currentFunctions));
        }
        if (usbFunctionsFromString != currentFunctions && !Utils.isMonkeyRunning() && !isClickEventIgnored(usbFunctionsFromString, currentFunctions)) {
            this.mPreviousFunction = currentFunctions;
            RadioButtonPreference radioButtonPreference2 = (RadioButtonPreference) this.mProfilesContainer.findPreference(UsbBackend.usbFunctionsToString(currentFunctions));
            if (radioButtonPreference2 != null) {
                radioButtonPreference2.setChecked(false);
                radioButtonPreference.setChecked(true);
            }
            if (usbFunctionsFromString == 32) {
                this.mTetheringManager.startTethering(1, new HandlerExecutor(this.mHandler), this.mOnStartTetheringCallback);
            } else {
                this.mUsbBackend.setCurrentFunctions(usbFunctionsFromString);
            }
        }
    }

    private boolean isClickEventIgnored(long j, long j2) {
        return isAccessoryMode(j2) && j == 4;
    }

    public boolean isAvailable() {
        return !Utils.isMonkeyRunning();
    }

    final class OnStartTetheringCallback implements TetheringManager.StartTetheringCallback {
        OnStartTetheringCallback() {
        }

        public void onTetheringFailed(int i) {
            Log.w("UsbFunctionsCtrl", "onTetheringFailed() error : " + i);
            UsbDetailsFunctionsController usbDetailsFunctionsController = UsbDetailsFunctionsController.this;
            usbDetailsFunctionsController.mUsbBackend.setCurrentFunctions(usbDetailsFunctionsController.mPreviousFunction);
        }
    }
}
