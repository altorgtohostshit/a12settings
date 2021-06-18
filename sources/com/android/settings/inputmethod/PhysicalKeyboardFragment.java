package com.android.settings.inputmethod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.hardware.input.InputDeviceIdentifier;
import android.hardware.input.InputManager;
import android.hardware.input.KeyboardLayout;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.InputDevice;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.internal.util.Preconditions;
import com.android.settings.R;
import com.android.settings.Settings;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.inputmethod.KeyboardLayoutDialogFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.utils.ThreadUtils;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class PhysicalKeyboardFragment extends SettingsPreferenceFragment implements InputManager.InputDeviceListener, KeyboardLayoutDialogFragment.OnSetupKeyboardLayoutsListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider() {
        public List<SearchIndexableResource> getXmlResourcesToIndex(Context context, boolean z) {
            SearchIndexableResource searchIndexableResource = new SearchIndexableResource(context);
            searchIndexableResource.xmlResId = R.xml.physical_keyboard_settings;
            return Arrays.asList(new SearchIndexableResource[]{searchIndexableResource});
        }
    };
    private final ContentObserver mContentObserver = new ContentObserver(new Handler(true)) {
        public void onChange(boolean z) {
            PhysicalKeyboardFragment.this.updateShowVirtualKeyboardSwitch();
        }
    };
    private InputManager mIm;
    private Intent mIntentWaitingForResult;
    private PreferenceCategory mKeyboardAssistanceCategory;
    private final ArrayList<HardKeyboardDeviceInfo> mLastHardKeyboards = new ArrayList<>();
    private SwitchPreference mShowVirtualKeyboardSwitch;
    private final Preference.OnPreferenceChangeListener mShowVirtualKeyboardSwitchPreferenceChangeListener = new PhysicalKeyboardFragment$$ExternalSyntheticLambda0(this);

    public int getMetricsCategory() {
        return 346;
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.physical_keyboard_settings);
        this.mIm = (InputManager) Preconditions.checkNotNull((InputManager) ((Activity) Preconditions.checkNotNull(getActivity())).getSystemService(InputManager.class));
        PreferenceCategory preferenceCategory = (PreferenceCategory) Preconditions.checkNotNull((PreferenceCategory) findPreference("keyboard_assistance_category"));
        this.mKeyboardAssistanceCategory = preferenceCategory;
        this.mShowVirtualKeyboardSwitch = (SwitchPreference) Preconditions.checkNotNull((SwitchPreference) preferenceCategory.findPreference("show_virtual_keyboard_switch"));
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        if (!"keyboard_shortcuts_helper".equals(preference.getKey())) {
            return super.onPreferenceTreeClick(preference);
        }
        writePreferenceClickMetric(preference);
        toggleKeyboardShortcutsMenu();
        return true;
    }

    public void onResume() {
        super.onResume();
        this.mLastHardKeyboards.clear();
        scheduleUpdateHardKeyboards();
        this.mIm.registerInputDeviceListener(this, (Handler) null);
        this.mShowVirtualKeyboardSwitch.setOnPreferenceChangeListener(this.mShowVirtualKeyboardSwitchPreferenceChangeListener);
        registerShowVirtualKeyboardSettingsObserver();
    }

    public void onPause() {
        super.onPause();
        this.mLastHardKeyboards.clear();
        this.mIm.unregisterInputDeviceListener(this);
        this.mShowVirtualKeyboardSwitch.setOnPreferenceChangeListener((Preference.OnPreferenceChangeListener) null);
        unregisterShowVirtualKeyboardSettingsObserver();
    }

    public void onInputDeviceAdded(int i) {
        scheduleUpdateHardKeyboards();
    }

    public void onInputDeviceRemoved(int i) {
        scheduleUpdateHardKeyboards();
    }

    public void onInputDeviceChanged(int i) {
        scheduleUpdateHardKeyboards();
    }

    private void scheduleUpdateHardKeyboards() {
        ThreadUtils.postOnBackgroundThread((Runnable) new PhysicalKeyboardFragment$$ExternalSyntheticLambda2(this, getContext()));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleUpdateHardKeyboards$1(Context context) {
        ThreadUtils.postOnMainThread(new PhysicalKeyboardFragment$$ExternalSyntheticLambda3(this, getHardKeyboards(context)));
    }

    /* access modifiers changed from: private */
    /* renamed from: updateHardKeyboards */
    public void lambda$scheduleUpdateHardKeyboards$0(List<HardKeyboardDeviceInfo> list) {
        if (!Objects.equals(this.mLastHardKeyboards, list)) {
            this.mLastHardKeyboards.clear();
            this.mLastHardKeyboards.addAll(list);
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            preferenceScreen.removeAll();
            PreferenceCategory preferenceCategory = new PreferenceCategory(getPrefContext());
            preferenceCategory.setTitle((int) R.string.builtin_keyboard_settings_title);
            preferenceCategory.setOrder(0);
            preferenceScreen.addPreference(preferenceCategory);
            for (HardKeyboardDeviceInfo next : list) {
                Preference preference = new Preference(getPrefContext());
                preference.setTitle((CharSequence) next.mDeviceName);
                preference.setSummary((CharSequence) next.mLayoutLabel);
                preference.setOnPreferenceClickListener(new PhysicalKeyboardFragment$$ExternalSyntheticLambda1(this, next));
                preferenceCategory.addPreference(preference);
            }
            this.mKeyboardAssistanceCategory.setOrder(1);
            preferenceScreen.addPreference(this.mKeyboardAssistanceCategory);
            updateShowVirtualKeyboardSwitch();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateHardKeyboards$2(HardKeyboardDeviceInfo hardKeyboardDeviceInfo, Preference preference) {
        showKeyboardLayoutDialog(hardKeyboardDeviceInfo.mDeviceIdentifier);
        return true;
    }

    private void showKeyboardLayoutDialog(InputDeviceIdentifier inputDeviceIdentifier) {
        KeyboardLayoutDialogFragment keyboardLayoutDialogFragment = new KeyboardLayoutDialogFragment(inputDeviceIdentifier);
        keyboardLayoutDialogFragment.setTargetFragment(this, 0);
        keyboardLayoutDialogFragment.show(getActivity().getSupportFragmentManager(), "keyboardLayout");
    }

    private void registerShowVirtualKeyboardSettingsObserver() {
        unregisterShowVirtualKeyboardSettingsObserver();
        getActivity().getContentResolver().registerContentObserver(Settings.Secure.getUriFor("show_ime_with_hard_keyboard"), false, this.mContentObserver, UserHandle.myUserId());
        updateShowVirtualKeyboardSwitch();
    }

    private void unregisterShowVirtualKeyboardSettingsObserver() {
        getActivity().getContentResolver().unregisterContentObserver(this.mContentObserver);
    }

    /* access modifiers changed from: private */
    public void updateShowVirtualKeyboardSwitch() {
        SwitchPreference switchPreference = this.mShowVirtualKeyboardSwitch;
        boolean z = false;
        if (Settings.Secure.getInt(getContentResolver(), "show_ime_with_hard_keyboard", 0) != 0) {
            z = true;
        }
        switchPreference.setChecked(z);
    }

    private void toggleKeyboardShortcutsMenu() {
        getActivity().requestShowKeyboardShortcuts();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$3(Preference preference, Object obj) {
        Settings.Secure.putInt(getContentResolver(), "show_ime_with_hard_keyboard", ((Boolean) obj).booleanValue() ? 1 : 0);
        return true;
    }

    public void onSetupKeyboardLayouts(InputDeviceIdentifier inputDeviceIdentifier) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setClass(getActivity(), Settings.KeyboardLayoutPickerActivity.class);
        intent.putExtra("input_device_identifier", inputDeviceIdentifier);
        this.mIntentWaitingForResult = intent;
        startActivityForResult(intent, 0);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        Intent intent2 = this.mIntentWaitingForResult;
        if (intent2 != null) {
            this.mIntentWaitingForResult = null;
            showKeyboardLayoutDialog(intent2.getParcelableExtra("input_device_identifier"));
        }
    }

    private static String getLayoutLabel(InputDevice inputDevice, Context context, InputManager inputManager) {
        String currentKeyboardLayoutForInputDevice = inputManager.getCurrentKeyboardLayoutForInputDevice(inputDevice.getIdentifier());
        if (currentKeyboardLayoutForInputDevice == null) {
            return context.getString(R.string.keyboard_layout_default_label);
        }
        KeyboardLayout keyboardLayout = inputManager.getKeyboardLayout(currentKeyboardLayoutForInputDevice);
        if (keyboardLayout == null) {
            return context.getString(R.string.keyboard_layout_default_label);
        }
        return TextUtils.emptyIfNull(keyboardLayout.getLabel());
    }

    static List<HardKeyboardDeviceInfo> getHardKeyboards(Context context) {
        ArrayList arrayList = new ArrayList();
        InputManager inputManager = (InputManager) context.getSystemService(InputManager.class);
        if (inputManager == null) {
            return new ArrayList();
        }
        for (int device : InputDevice.getDeviceIds()) {
            InputDevice device2 = InputDevice.getDevice(device);
            if (device2 != null && !device2.isVirtual() && device2.isFullKeyboard()) {
                arrayList.add(new HardKeyboardDeviceInfo(device2.getName(), device2.getIdentifier(), getLayoutLabel(device2, context, inputManager)));
            }
        }
        arrayList.sort(new PhysicalKeyboardFragment$$ExternalSyntheticLambda4(Collator.getInstance()));
        return arrayList;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ int lambda$getHardKeyboards$4(Collator collator, HardKeyboardDeviceInfo hardKeyboardDeviceInfo, HardKeyboardDeviceInfo hardKeyboardDeviceInfo2) {
        int compare = collator.compare(hardKeyboardDeviceInfo.mDeviceName, hardKeyboardDeviceInfo2.mDeviceName);
        if (compare != 0) {
            return compare;
        }
        int compareTo = hardKeyboardDeviceInfo.mDeviceIdentifier.getDescriptor().compareTo(hardKeyboardDeviceInfo2.mDeviceIdentifier.getDescriptor());
        if (compareTo != 0) {
            return compareTo;
        }
        return collator.compare(hardKeyboardDeviceInfo.mLayoutLabel, hardKeyboardDeviceInfo2.mLayoutLabel);
    }

    public static final class HardKeyboardDeviceInfo {
        public final InputDeviceIdentifier mDeviceIdentifier;
        public final String mDeviceName;
        public final String mLayoutLabel;

        public HardKeyboardDeviceInfo(String str, InputDeviceIdentifier inputDeviceIdentifier, String str2) {
            this.mDeviceName = TextUtils.emptyIfNull(str);
            this.mDeviceIdentifier = inputDeviceIdentifier;
            this.mLayoutLabel = str2;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || !(obj instanceof HardKeyboardDeviceInfo)) {
                return false;
            }
            HardKeyboardDeviceInfo hardKeyboardDeviceInfo = (HardKeyboardDeviceInfo) obj;
            return TextUtils.equals(this.mDeviceName, hardKeyboardDeviceInfo.mDeviceName) && Objects.equals(this.mDeviceIdentifier, hardKeyboardDeviceInfo.mDeviceIdentifier) && TextUtils.equals(this.mLayoutLabel, hardKeyboardDeviceInfo.mLayoutLabel);
        }
    }
}
