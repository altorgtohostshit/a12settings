package com.android.settings.accessibility;

import android.content.Context;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import com.android.settings.R;
import com.android.settings.widget.RadioButtonPickerFragment;
import com.android.settingslib.widget.CandidateInfo;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public abstract class VibrationPreferenceFragment extends RadioButtonPickerFragment {
    static final String KEY_INTENSITY_HIGH = "intensity_high";
    static final String KEY_INTENSITY_LOW = "intensity_low";
    static final String KEY_INTENSITY_MEDIUM = "intensity_medium";
    static final String KEY_INTENSITY_OFF = "intensity_off";
    static final String KEY_INTENSITY_ON = "intensity_on";
    private final Map<String, VibrationIntensityCandidateInfo> mCandidates = new ArrayMap();
    private final SettingsObserver mSettingsObserver = new SettingsObserver();

    /* access modifiers changed from: protected */
    public abstract int getDefaultVibrationIntensity();

    /* access modifiers changed from: protected */
    public int getPreviewVibrationAudioAttributesUsage() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public abstract String getVibrationEnabledSetting();

    /* access modifiers changed from: protected */
    public abstract String getVibrationIntensitySetting();

    /* access modifiers changed from: protected */
    public void onVibrationIntensitySelected(int i) {
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mSettingsObserver.register();
        if (this.mCandidates.isEmpty()) {
            loadCandidates(context);
        }
    }

    private void loadCandidates(Context context) {
        if (context.getResources().getBoolean(R.bool.config_vibration_supports_multiple_intensities)) {
            this.mCandidates.put(KEY_INTENSITY_OFF, new VibrationIntensityCandidateInfo(KEY_INTENSITY_OFF, R.string.accessibility_vibration_intensity_off, 0));
            this.mCandidates.put(KEY_INTENSITY_LOW, new VibrationIntensityCandidateInfo(KEY_INTENSITY_LOW, R.string.accessibility_vibration_intensity_low, 1));
            this.mCandidates.put(KEY_INTENSITY_MEDIUM, new VibrationIntensityCandidateInfo(KEY_INTENSITY_MEDIUM, R.string.accessibility_vibration_intensity_medium, 2));
            this.mCandidates.put(KEY_INTENSITY_HIGH, new VibrationIntensityCandidateInfo(KEY_INTENSITY_HIGH, R.string.accessibility_vibration_intensity_high, 3));
            return;
        }
        this.mCandidates.put(KEY_INTENSITY_OFF, new VibrationIntensityCandidateInfo(KEY_INTENSITY_OFF, R.string.switch_off_text, 0));
        this.mCandidates.put(KEY_INTENSITY_ON, new VibrationIntensityCandidateInfo(KEY_INTENSITY_ON, R.string.switch_on_text, getDefaultVibrationIntensity()));
    }

    private boolean hasVibrationEnabledSetting() {
        return !TextUtils.isEmpty(getVibrationEnabledSetting());
    }

    private void updateSettings(VibrationIntensityCandidateInfo vibrationIntensityCandidateInfo) {
        int i = 1;
        int i2 = vibrationIntensityCandidateInfo.getIntensity() != 0 ? 1 : 0;
        if (hasVibrationEnabledSetting()) {
            String vibrationEnabledSetting = getVibrationEnabledSetting();
            if (!TextUtils.equals(vibrationEnabledSetting, "apply_ramping_ringer") && Settings.System.getInt(getContext().getContentResolver(), vibrationEnabledSetting, 1) != 1) {
                i = 0;
            }
            if (i2 != i) {
                if (vibrationEnabledSetting.equals("apply_ramping_ringer")) {
                    Settings.Global.putInt(getContext().getContentResolver(), vibrationEnabledSetting, 0);
                } else {
                    Settings.System.putInt(getContext().getContentResolver(), vibrationEnabledSetting, i2);
                }
                int i3 = Settings.System.getInt(getContext().getContentResolver(), getVibrationIntensitySetting(), 0);
                if (i2 != 0 && i3 == vibrationIntensityCandidateInfo.getIntensity()) {
                    playVibrationPreview();
                }
            }
        }
        if (i2 != 0 || !hasVibrationEnabledSetting()) {
            Settings.System.putInt(getContext().getContentResolver(), getVibrationIntensitySetting(), vibrationIntensityCandidateInfo.getIntensity());
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mSettingsObserver.unregister();
    }

    /* access modifiers changed from: protected */
    public void playVibrationPreview() {
        VibrationEffect vibrationEffect = VibrationEffect.get(0);
        AudioAttributes.Builder builder = new AudioAttributes.Builder();
        builder.setUsage(getPreviewVibrationAudioAttributesUsage());
        ((Vibrator) getContext().getSystemService(Vibrator.class)).vibrate(vibrationEffect, builder.build());
    }

    /* access modifiers changed from: protected */
    public List<? extends CandidateInfo> getCandidates() {
        ArrayList arrayList = new ArrayList(this.mCandidates.values());
        arrayList.sort(Comparator.comparing(VibrationPreferenceFragment$$ExternalSyntheticLambda0.INSTANCE).reversed());
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public String getDefaultKey() {
        boolean z;
        int i = Settings.System.getInt(getContext().getContentResolver(), getVibrationIntensitySetting(), getDefaultVibrationIntensity());
        String vibrationEnabledSetting = getVibrationEnabledSetting();
        if (!TextUtils.equals(vibrationEnabledSetting, "apply_ramping_ringer") && Settings.System.getInt(getContext().getContentResolver(), vibrationEnabledSetting, 1) != 1) {
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            i = 0;
        }
        for (VibrationIntensityCandidateInfo next : this.mCandidates.values()) {
            boolean z2 = next.getIntensity() == i;
            boolean z3 = next.getKey().equals(KEY_INTENSITY_ON) && i != 0;
            if (!z2) {
                if (z3) {
                }
            }
            return next.getKey();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public boolean setDefaultKey(String str) {
        VibrationIntensityCandidateInfo vibrationIntensityCandidateInfo = this.mCandidates.get(str);
        if (vibrationIntensityCandidateInfo == null) {
            Log.e("VibrationPreferenceFragment", "Tried to set unknown intensity (key=" + str + ")!");
            return false;
        }
        updateSettings(vibrationIntensityCandidateInfo);
        onVibrationIntensitySelected(vibrationIntensityCandidateInfo.getIntensity());
        return true;
    }

    class VibrationIntensityCandidateInfo extends CandidateInfo {
        private int mIntensity;
        private String mKey;
        private int mLabelId;

        public Drawable loadIcon() {
            return null;
        }

        public VibrationIntensityCandidateInfo(String str, int i, int i2) {
            super(true);
            this.mKey = str;
            this.mLabelId = i;
            this.mIntensity = i2;
        }

        public CharSequence loadLabel() {
            return VibrationPreferenceFragment.this.getContext().getString(this.mLabelId);
        }

        public String getKey() {
            return this.mKey;
        }

        public int getIntensity() {
            return this.mIntensity;
        }
    }

    private class SettingsObserver extends ContentObserver {
        public SettingsObserver() {
            super(new Handler());
        }

        public void register() {
            VibrationPreferenceFragment.this.getContext().getContentResolver().registerContentObserver(Settings.System.getUriFor(VibrationPreferenceFragment.this.getVibrationIntensitySetting()), false, this);
        }

        public void unregister() {
            VibrationPreferenceFragment.this.getContext().getContentResolver().unregisterContentObserver(this);
        }

        public void onChange(boolean z, Uri uri) {
            VibrationPreferenceFragment.this.updateCandidates();
            VibrationPreferenceFragment.this.playVibrationPreview();
        }
    }
}
