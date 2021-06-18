package com.android.settings.inputmethod;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.input.InputDeviceIdentifier;
import android.hardware.input.InputManager;
import android.hardware.input.KeyboardLayout;
import android.os.Handler;
import android.view.InputDevice;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KeyboardLayoutPickerController extends BasePreferenceController implements InputManager.InputDeviceListener, LifecycleObserver, OnStart, OnStop {
    private final InputManager mIm;
    private int mInputDeviceId = -1;
    private InputDeviceIdentifier mInputDeviceIdentifier;
    private KeyboardLayout[] mKeyboardLayouts;
    private Fragment mParent;
    private final Map<SwitchPreference, KeyboardLayout> mPreferenceMap = new HashMap();
    private PreferenceScreen mScreen;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 0;
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

    public void onInputDeviceAdded(int i) {
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public KeyboardLayoutPickerController(Context context, String str) {
        super(context, str);
        this.mIm = (InputManager) context.getSystemService("input");
    }

    public void initialize(Fragment fragment, InputDeviceIdentifier inputDeviceIdentifier) {
        this.mParent = fragment;
        this.mInputDeviceIdentifier = inputDeviceIdentifier;
        KeyboardLayout[] keyboardLayoutsForInputDevice = this.mIm.getKeyboardLayoutsForInputDevice(inputDeviceIdentifier);
        this.mKeyboardLayouts = keyboardLayoutsForInputDevice;
        Arrays.sort(keyboardLayoutsForInputDevice);
    }

    public void onStart() {
        this.mIm.registerInputDeviceListener(this, (Handler) null);
        InputDevice inputDeviceByDescriptor = this.mIm.getInputDeviceByDescriptor(this.mInputDeviceIdentifier.getDescriptor());
        if (inputDeviceByDescriptor == null) {
            this.mParent.getActivity().finish();
            return;
        }
        this.mInputDeviceId = inputDeviceByDescriptor.getId();
        updateCheckedState();
    }

    public void onStop() {
        this.mIm.unregisterInputDeviceListener(this);
        this.mInputDeviceId = -1;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mScreen = preferenceScreen;
        createPreferenceHierarchy();
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!(preference instanceof SwitchPreference)) {
            return false;
        }
        SwitchPreference switchPreference = (SwitchPreference) preference;
        KeyboardLayout keyboardLayout = this.mPreferenceMap.get(switchPreference);
        if (keyboardLayout == null) {
            return true;
        }
        if (switchPreference.isChecked()) {
            this.mIm.addKeyboardLayoutForInputDevice(this.mInputDeviceIdentifier, keyboardLayout.getDescriptor());
            return true;
        }
        this.mIm.removeKeyboardLayoutForInputDevice(this.mInputDeviceIdentifier, keyboardLayout.getDescriptor());
        return true;
    }

    public void onInputDeviceRemoved(int i) {
        int i2 = this.mInputDeviceId;
        if (i2 >= 0 && i == i2) {
            this.mParent.getActivity().finish();
        }
    }

    public void onInputDeviceChanged(int i) {
        int i2 = this.mInputDeviceId;
        if (i2 >= 0 && i == i2) {
            updateCheckedState();
        }
    }

    private void updateCheckedState() {
        String[] enabledKeyboardLayoutsForInputDevice = this.mIm.getEnabledKeyboardLayoutsForInputDevice(this.mInputDeviceIdentifier);
        Arrays.sort(enabledKeyboardLayoutsForInputDevice);
        for (Map.Entry next : this.mPreferenceMap.entrySet()) {
            ((SwitchPreference) next.getKey()).setChecked(Arrays.binarySearch(enabledKeyboardLayoutsForInputDevice, ((KeyboardLayout) next.getValue()).getDescriptor()) >= 0);
        }
    }

    private void createPreferenceHierarchy() {
        for (KeyboardLayout keyboardLayout : this.mKeyboardLayouts) {
            SwitchPreference switchPreference = new SwitchPreference(this.mScreen.getContext());
            switchPreference.setTitle((CharSequence) keyboardLayout.getLabel());
            switchPreference.setSummary((CharSequence) keyboardLayout.getCollection());
            switchPreference.setKey(keyboardLayout.getDescriptor());
            this.mScreen.addPreference(switchPreference);
            this.mPreferenceMap.put(switchPreference, keyboardLayout);
        }
    }
}
