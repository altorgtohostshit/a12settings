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
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public abstract class VibrationTogglePreferenceController extends TogglePreferenceController implements LifecycleObserver, OnStart, OnStop {
    protected String mFakePositionSetting;
    /* access modifiers changed from: private */
    public Preference mPreference;
    private final SettingObserver mPrimaryVibrationSettingsContentObserver;
    protected HapticsRingReceiverHelper mReceiver;
    private final String mSettingKey;
    private final SettingObserver mSettingsContentObserver;
    protected HapticsSharedPreferences mSharedPrefs;
    private boolean mShouldPlayPreview;
    protected final Vibrator mVibrator = ((Vibrator) this.mContext.getSystemService(Vibrator.class));

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

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public VibrationTogglePreferenceController(Context context, String str, String str2) {
        super(context, str);
        this.mSettingKey = str2;
        this.mSharedPrefs = new HapticsSharedPreferences(this.mContext);
        this.mFakePositionSetting = str2 + "_FAKE";
        this.mSettingsContentObserver = new SettingObserver(str2, getVibrationEnabledSetting()) {
            public void onChange(boolean z, Uri uri) {
                VibrationTogglePreferenceController vibrationTogglePreferenceController = VibrationTogglePreferenceController.this;
                vibrationTogglePreferenceController.updateState(vibrationTogglePreferenceController.mPreference);
                VibrationTogglePreferenceController.this.playVibrationPreview();
            }
        };
        this.mPrimaryVibrationSettingsContentObserver = new SettingObserver("vibrate_on") {
            public void onChange(boolean z, Uri uri) {
                VibrationTogglePreferenceController.this.toggleEnable();
            }
        };
        this.mReceiver = new HapticsRingReceiverHelper(this.mContext) {
            public void onChange() {
                if (HapticsUtils.isVibrationPrimarySwitchOn(VibrationTogglePreferenceController.this.mContext) && VibrationTogglePreferenceController.this.mReceiver.getRingerMode() == 2) {
                    VibrationTogglePreferenceController.this.setIntensityFromPrimarySwitchTriggerReason();
                }
            }
        };
        if (HapticsUtils.isVibrationPrimarySwitchOn(this.mContext)) {
            setIntensityFromPrimarySwitchTriggerReason();
        }
    }

    /* access modifiers changed from: protected */
    public Preference getPreference() {
        return this.mPreference;
    }

    public int getAvailabilityStatus() {
        return (!HapticsUtils.isVibrationPrimarySwitchOn(this.mContext) || this.mReceiver.isRingerModeSilent()) ? 5 : 0;
    }

    public boolean isChecked() {
        if (!HapticsUtils.isVibrationPrimarySwitchOn(this.mContext)) {
            int primarySwitchOffTriggerReason = this.mSharedPrefs.getPrimarySwitchOffTriggerReason();
            if ((primarySwitchOffTriggerReason == 0 || primarySwitchOffTriggerReason == 1) && restoreFakePreference() >= 1) {
                return true;
            }
            return false;
        } else if (hasVibrationEnabledSetting()) {
            return isVibrationEnabled();
        } else {
            if (getSettingValue() >= 1) {
                return true;
            }
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public boolean updateSetting(int i) {
        if (!hasVibrationEnabledSettingAndIsDisabled()) {
            return Settings.System.putInt(this.mContext.getContentResolver(), this.mSettingKey, i);
        }
        return false;
    }

    public boolean setChecked(boolean z) {
        boolean z2 = false;
        if (!HapticsUtils.isVibrationPrimarySwitchOn(this.mContext)) {
            return false;
        }
        boolean updateVibrationEnabledSettings = updateVibrationEnabledSettings(z ? 1 : 0) | updateSetting(z ? getDefaultIntensity() : 0);
        if (z && updateVibrationEnabledSettings) {
            z2 = true;
        }
        this.mShouldPlayPreview = z2;
        savePreference();
        return updateVibrationEnabledSettings;
    }

    public void onStart() {
        this.mSettingsContentObserver.register(this.mContext.getContentResolver());
        this.mPrimaryVibrationSettingsContentObserver.register(this.mContext.getContentResolver());
        this.mReceiver.register(true);
    }

    public void onStop() {
        this.mSettingsContentObserver.unregister(this.mContext.getContentResolver());
        this.mPrimaryVibrationSettingsContentObserver.unregister(this.mContext.getContentResolver());
        if (HapticsUtils.isVibrationPrimarySwitchOn(this.mContext)) {
            savePreference();
        }
        this.mReceiver.register(false);
    }

    public void updateState(Preference preference) {
        if (preference != null) {
            boolean z = false;
            if (Settings.System.getInt(this.mContext.getContentResolver(), "vibrate_on", 0) == 1) {
                z = true;
            }
            preference.setEnabled(z);
            super.updateState(preference);
        }
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    private boolean hasVibrationEnabledSetting() {
        return !TextUtils.isEmpty(getVibrationEnabledSetting());
    }

    /* access modifiers changed from: protected */
    public boolean isVibrationEnabled() {
        if (Settings.System.getInt(this.mContext.getContentResolver(), getVibrationEnabledSetting(), 1) == 1) {
            return true;
        }
        return false;
    }

    private boolean hasVibrationEnabledSettingAndIsDisabled() {
        return hasVibrationEnabledSetting() && !isVibrationEnabled();
    }

    private boolean updateVibrationEnabledSettings(int i) {
        int i2 = 1;
        int i3 = i != 0 ? 1 : 0;
        if (hasVibrationEnabledSetting()) {
            String vibrationEnabledSetting = getVibrationEnabledSetting();
            if (!TextUtils.equals(vibrationEnabledSetting, "apply_ramping_ringer") && Settings.System.getInt(this.mContext.getContentResolver(), vibrationEnabledSetting, 1) != 1) {
                i2 = 0;
            }
            if (i3 != i2) {
                if (vibrationEnabledSetting.equals("apply_ramping_ringer")) {
                    return Settings.Global.putInt(this.mContext.getContentResolver(), vibrationEnabledSetting, 0);
                }
                return Settings.System.putInt(this.mContext.getContentResolver(), vibrationEnabledSetting, i3);
            }
        }
        return false;
    }

    private int getSettingValue() {
        return Settings.System.getInt(this.mContext.getContentResolver(), this.mSettingKey, 1);
    }

    /* access modifiers changed from: protected */
    public void setSettingValue() {
        setSettingValue(restorePreference());
    }

    private void setSettingValue(int i) {
        Settings.System.putInt(this.mContext.getContentResolver(), this.mSettingKey, i);
    }

    /* access modifiers changed from: protected */
    public void setIntensityFromPrimarySwitchTriggerReason() {
        int primarySwitchOffTriggerReason = this.mSharedPrefs.getPrimarySwitchOffTriggerReason();
        if (primarySwitchOffTriggerReason == 0) {
            setSettingValue();
        } else if (primarySwitchOffTriggerReason == 1) {
            setSettingValue(restorePreference());
        } else if (primarySwitchOffTriggerReason == 2 || primarySwitchOffTriggerReason == 3) {
            updateVibrationEnabledSettings(getDefaultIntensity());
            Settings.System.putInt(this.mContext.getContentResolver(), this.mSettingKey, getDefaultIntensity());
        }
        updateState(getPreference());
    }

    /* access modifiers changed from: protected */
    public void toggleEnable() {
        boolean isVibrationPrimarySwitchOn = HapticsUtils.isVibrationPrimarySwitchOn(this.mContext);
        if (isVibrationPrimarySwitchOn) {
            this.mSettingsContentObserver.register(this.mContext.getContentResolver());
            this.mShouldPlayPreview = true;
            setIntensityFromPrimarySwitchTriggerReason();
        } else {
            savePreference();
            saveFakePreference();
            this.mSettingsContentObserver.unregister(this.mContext.getContentResolver());
            Settings.System.putInt(this.mContext.getContentResolver(), this.mSettingKey, 0);
        }
        this.mPreference.setEnabled(isVibrationPrimarySwitchOn);
    }

    private int savePreference() {
        SharedPreferences sharedPreferences = this.mSharedPrefs.getSharedPreferences();
        int settingValue = getSettingValue();
        sharedPreferences.edit().putInt(this.mSettingKey, settingValue).apply();
        return settingValue;
    }

    private int restorePreference() {
        return this.mSharedPrefs.getSharedPreferences().getInt(this.mSettingKey, 1);
    }

    /* access modifiers changed from: protected */
    public void saveFakePreference() {
        this.mSharedPrefs.getSharedPreferences().edit().putInt(this.mFakePositionSetting, getSettingValue()).apply();
    }

    private int restoreFakePreference() {
        return this.mSharedPrefs.getSharedPreferences().getInt(this.mFakePositionSetting, getSettingValue());
    }

    /* access modifiers changed from: private */
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
