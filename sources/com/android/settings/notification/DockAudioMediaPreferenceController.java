package com.android.settings.notification;

import android.content.Context;
import android.content.res.Resources;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.core.lifecycle.Lifecycle;

public class DockAudioMediaPreferenceController extends SettingPrefController {
    public DockAudioMediaPreferenceController(Context context, SettingsPreferenceFragment settingsPreferenceFragment, Lifecycle lifecycle) {
        super(context, settingsPreferenceFragment, lifecycle);
        this.mPreference = new SettingPref(1, "dock_audio_media", "dock_audio_media_enabled", 0, 0, 1) {
            public boolean isApplicable(Context context) {
                return context.getResources().getBoolean(R.bool.has_dock_settings);
            }

            /* access modifiers changed from: protected */
            public String getCaption(Resources resources, int i) {
                if (i == 0) {
                    return resources.getString(R.string.dock_audio_media_disabled);
                }
                if (i == 1) {
                    return resources.getString(R.string.dock_audio_media_enabled);
                }
                throw new IllegalArgumentException();
            }
        };
    }
}
