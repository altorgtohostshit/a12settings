package com.android.settings.development;

import android.content.Context;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public class PictureColorModePreferenceController extends DeveloperOptionsPreferenceController implements LifecycleObserver, OnResume, OnPause, PreferenceControllerMixin {
    private ColorModePreference mPreference;

    public String getPreferenceKey() {
        return "picture_color_mode";
    }

    public PictureColorModePreferenceController(Context context, Lifecycle lifecycle) {
        super(context);
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    public boolean isAvailable() {
        return getColorModeDescriptionsSize() > 1 && !isWideColorGamut();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        ColorModePreference colorModePreference = (ColorModePreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = colorModePreference;
        if (colorModePreference != null) {
            colorModePreference.updateCurrentAndSupported();
        }
    }

    public void onResume() {
        ColorModePreference colorModePreference = this.mPreference;
        if (colorModePreference != null) {
            colorModePreference.startListening();
            this.mPreference.updateCurrentAndSupported();
        }
    }

    public void onPause() {
        ColorModePreference colorModePreference = this.mPreference;
        if (colorModePreference != null) {
            colorModePreference.stopListening();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isWideColorGamut() {
        return this.mContext.getResources().getConfiguration().isScreenWideColorGamut();
    }

    /* access modifiers changed from: package-private */
    public int getColorModeDescriptionsSize() {
        return ColorModePreference.getColorModeDescriptions(this.mContext).size();
    }
}
