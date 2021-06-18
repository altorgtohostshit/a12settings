package com.android.settings.notification.zen;

import android.content.Context;
import com.android.settings.notification.zen.ZenModeSettings;
import com.android.settingslib.core.lifecycle.Lifecycle;

public class ZenModeBlockedEffectsPreferenceController extends AbstractZenModePreferenceController {
    private final ZenModeSettings.SummaryBuilder mSummaryBuilder;

    public String getPreferenceKey() {
        return "zen_mode_block_effects_settings";
    }

    public boolean isAvailable() {
        return true;
    }

    public ZenModeBlockedEffectsPreferenceController(Context context, Lifecycle lifecycle) {
        super(context, "zen_mode_block_effects_settings", lifecycle);
        this.mSummaryBuilder = new ZenModeSettings.SummaryBuilder(context);
    }

    public CharSequence getSummary() {
        return this.mSummaryBuilder.getBlockedEffectsSummary(getPolicy());
    }
}
