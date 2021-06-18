package com.google.android.settings.accessibility;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.SliderPreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.SeekBarPreference;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public abstract class VibrationIntensitySliderPreferenceController extends SliderPreferenceController implements LifecycleObserver, OnStart, OnStop {
    final String mEnabledKey;
    protected int mPosition;
    protected Preference mPreference;
    private final SettingObserver mPrimaryVibrationSettingsContentObserver;
    protected HapticsRingReceiverHelper mReceiver;
    private final String mSettingKey;
    private final SettingObserver mSettingsContentObserver;
    protected HapticsSharedPreferences mSharedPrefs;
    private boolean mShouldPlayPreview;
    private final boolean mSupportRampingRinger;
    protected final Vibrator mVibrator;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    /* access modifiers changed from: protected */
    public abstract int getDefaultIntensity();

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getMin() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public int getPreviewVibrationAudioAttributesUsage() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public abstract String getVibrationEnabledSetting();

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

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public VibrationIntensitySliderPreferenceController(Context context, String str, String str2, String str3, boolean z) {
        super(context, str);
        this.mVibrator = (Vibrator) this.mContext.getSystemService(Vibrator.class);
        this.mSettingKey = str2;
        this.mEnabledKey = str3;
        this.mSupportRampingRinger = z;
        this.mSettingsContentObserver = new SettingObserver(str2, str3) {
            public void onChange(boolean z, Uri uri) {
                VibrationIntensitySliderPreferenceController vibrationIntensitySliderPreferenceController = VibrationIntensitySliderPreferenceController.this;
                vibrationIntensitySliderPreferenceController.updateState(vibrationIntensitySliderPreferenceController.mPreference);
                VibrationIntensitySliderPreferenceController.this.playVibrationPreview();
            }
        };
        this.mPrimaryVibrationSettingsContentObserver = new SettingObserver("vibrate_on") {
            public void onChange(boolean z, Uri uri) {
                VibrationIntensitySliderPreferenceController.this.toggleEnable();
            }
        };
        this.mReceiver = new HapticsRingReceiverHelper(this.mContext) {
            public void onChange() {
                if (HapticsUtils.isVibrationPrimarySwitchOn(VibrationIntensitySliderPreferenceController.this.mContext) && VibrationIntensitySliderPreferenceController.this.mReceiver.getRingerMode() == 2) {
                    VibrationIntensitySliderPreferenceController.this.setSliderPositionFromPrimarySwitchTriggerReason();
                }
                VibrationIntensitySliderPreferenceController.this.updatePreferenceVisibility();
            }
        };
        this.mSharedPrefs = new HapticsSharedPreferences(this.mContext);
    }

    public VibrationIntensitySliderPreferenceController(Context context, String str, String str2, String str3) {
        this(context, str, str2, str3, false);
    }

    /* access modifiers changed from: protected */
    public Preference getPreference() {
        return this.mPreference;
    }

    private void setPosition(int i) {
        if (i >= getMax()) {
            i = 3;
        }
        this.mPosition = i;
    }

    private void onStartSetSliderPosition() {
        int i;
        if (HapticsUtils.isVibrationPrimarySwitchOn(this.mContext)) {
            setSliderPositionFromPrimarySwitchTriggerReason();
            return;
        }
        if (hasVibrationEnabledSettingAndIsDisabled()) {
            i = 0;
        } else {
            i = restorePreference();
        }
        setFakeSliderPosition(i);
    }

    /* access modifiers changed from: protected */
    public void registerSettingsObservers() {
        this.mSettingsContentObserver.register(this.mContext.getContentResolver());
        this.mPrimaryVibrationSettingsContentObserver.register(this.mContext.getContentResolver());
    }

    public void onStart() {
        registerSettingsObservers();
        this.mReceiver.register(true);
        setPosition(getSettingValue());
        onStartSetSliderPosition();
        updatePreferenceVisibility();
    }

    /* access modifiers changed from: protected */
    public void unregisterSettingsObservers() {
        this.mSettingsContentObserver.unregister(this.mContext.getContentResolver());
        this.mPrimaryVibrationSettingsContentObserver.unregister(this.mContext.getContentResolver());
    }

    public void onStop() {
        unregisterSettingsObservers();
        this.mReceiver.register(false);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
        SeekBarPreference seekBarPreference = (SeekBarPreference) preferenceScreen.findPreference(getPreferenceKey());
        seekBarPreference.setContinuousUpdates(true);
        seekBarPreference.setMax(getMax());
        seekBarPreference.setMin(getMin());
    }

    public int getSliderPosition() {
        return this.mPosition;
    }

    /* access modifiers changed from: protected */
    public boolean updateSetting(int i) {
        if (i != 0 || !hasVibrationEnabledSetting()) {
            return Settings.System.putInt(this.mContext.getContentResolver(), this.mSettingKey, i);
        }
        return false;
    }

    public boolean setSliderPosition(int i) {
        if (i >= getMax()) {
            i = 3;
        }
        boolean z = true;
        if (this.mPosition == i) {
            updateState(this.mPreference);
            return true;
        }
        updateVibrationEnabledSettings(i);
        boolean updateSetting = updateSetting(i);
        boolean z2 = i != 0;
        if (z2) {
            savePreference();
        }
        if (!z2 || this.mPosition == i || !updateSetting) {
            z = false;
        }
        this.mShouldPlayPreview = z;
        this.mPosition = i;
        updateState(this.mPreference);
        return updateSetting;
    }

    public int getMax() {
        return this.mContext.getResources().getInteger(R.integer.haptics_level_max);
    }

    public void updateState(Preference preference) {
        if (preference != null) {
            preference.setEnabled(HapticsUtils.isVibrationPrimarySwitchOn(this.mContext));
            super.updateState(preference);
        }
    }

    private boolean hasVibrationEnabledSetting() {
        return !TextUtils.isEmpty(getVibrationEnabledSetting());
    }

    private boolean isVibrationEnabled() {
        if (Settings.System.getInt(this.mContext.getContentResolver(), getVibrationEnabledSetting(), 1) == 1) {
            return true;
        }
        return false;
    }

    private boolean hasVibrationEnabledSettingAndIsDisabled() {
        return hasVibrationEnabledSetting() && !isVibrationEnabled();
    }

    private void updateVibrationEnabledSettings(int i) {
        int i2 = 1;
        int i3 = i != 0 ? 1 : 0;
        if (hasVibrationEnabledSetting()) {
            String vibrationEnabledSetting = getVibrationEnabledSetting();
            if (!TextUtils.equals(vibrationEnabledSetting, "apply_ramping_ringer") && Settings.System.getInt(this.mContext.getContentResolver(), vibrationEnabledSetting, 1) != 1) {
                i2 = 0;
            }
            if (i3 == i2) {
                return;
            }
            if (TextUtils.equals(vibrationEnabledSetting, "apply_ramping_ringer")) {
                Settings.Global.putInt(this.mContext.getContentResolver(), vibrationEnabledSetting, 0);
            } else {
                Settings.System.putInt(this.mContext.getContentResolver(), vibrationEnabledSetting, i3);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setFakeSliderPosition(int i) {
        this.mPosition = i;
        updateState(this.mPreference);
    }

    /* access modifiers changed from: protected */
    public int getSettingValue() {
        return Settings.System.getInt(this.mContext.getContentResolver(), this.mSettingKey, getDefaultIntensity());
    }

    /* access modifiers changed from: protected */
    public void setSliderPositionFromPrimarySwitchTriggerReason() {
        int primarySwitchOffTriggerReason = this.mSharedPrefs.getPrimarySwitchOffTriggerReason();
        if (primarySwitchOffTriggerReason != 0) {
            if (primarySwitchOffTriggerReason != 1) {
                if (primarySwitchOffTriggerReason == 2 || primarySwitchOffTriggerReason == 3) {
                    this.mPosition = 0;
                    setSliderPosition(getDefaultIntensity());
                }
            } else if (hasVibrationEnabledSettingAndIsDisabled()) {
                Settings.System.putInt(this.mContext.getContentResolver(), this.mSettingKey, restorePreference());
                setFakeSliderPosition(0);
            } else {
                this.mPosition = 0;
                setSliderPosition(restorePreference());
            }
        } else if (hasVibrationEnabledSettingAndIsDisabled()) {
            setFakeSliderPosition(0);
        } else {
            setSliderPosition(getSettingValue());
        }
    }

    /* access modifiers changed from: protected */
    public void toggleEnable() {
        boolean isVibrationPrimarySwitchOn = HapticsUtils.isVibrationPrimarySwitchOn(this.mContext);
        if (isVibrationPrimarySwitchOn) {
            setSliderPositionFromPrimarySwitchTriggerReason();
        } else {
            int savePreference = savePreference();
            Settings.System.putInt(this.mContext.getContentResolver(), this.mSettingKey, 0);
            if (hasVibrationEnabledSettingAndIsDisabled()) {
                savePreference = 0;
            }
            setFakeSliderPosition(savePreference);
        }
        this.mPreference.setEnabled(isVibrationPrimarySwitchOn);
    }

    /* access modifiers changed from: protected */
    public int savePreference() {
        SharedPreferences sharedPreferences = this.mSharedPrefs.getSharedPreferences();
        int settingValue = getSettingValue();
        sharedPreferences.edit().putInt(this.mSettingKey, settingValue).apply();
        return settingValue;
    }

    /* access modifiers changed from: protected */
    public int restorePreference() {
        return this.mSharedPrefs.getSharedPreferences().getInt(this.mSettingKey, getDefaultIntensity());
    }

    /* access modifiers changed from: protected */
    public void updatePreferenceVisibility() {
        this.mPreference.setVisible(!this.mReceiver.isRingerModeSilent());
    }

    /* access modifiers changed from: protected */
    public void playVibrationPreview() {
        if (this.mShouldPlayPreview) {
            this.mShouldPlayPreview = false;
            VibrationEffect vibrationEffect = VibrationEffect.get(0);
            AudioAttributes.Builder builder = new AudioAttributes.Builder();
            builder.setUsage(getPreviewVibrationAudioAttributesUsage());
            ((Vibrator) this.mContext.getSystemService(Vibrator.class)).vibrate(vibrationEffect, builder.build());
        }
    }

    protected static class SettingObserver extends ContentObserver {
        public Uri enabledKeyUri;
        private boolean hasEnabledKey = false;
        public final Uri settingKeyUri;

        public SettingObserver(String str) {
            super(new Handler(Looper.getMainLooper()));
            this.settingKeyUri = Settings.System.getUriFor(str);
        }

        public SettingObserver(String str, String str2) {
            super(new Handler(Looper.getMainLooper()));
            this.settingKeyUri = Settings.System.getUriFor(str);
            if (!TextUtils.isEmpty(str2)) {
                this.enabledKeyUri = Settings.System.getUriFor(str2);
                this.hasEnabledKey = true;
            }
        }

        public void register(ContentResolver contentResolver) {
            contentResolver.registerContentObserver(this.settingKeyUri, false, this);
            if (this.hasEnabledKey) {
                contentResolver.registerContentObserver(this.enabledKeyUri, false, this);
            }
        }

        public void unregister(ContentResolver contentResolver) {
            contentResolver.unregisterContentObserver(this);
        }
    }
}
