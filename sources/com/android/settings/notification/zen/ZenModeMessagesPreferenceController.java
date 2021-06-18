package com.android.settings.notification.zen;

import android.content.Context;
import androidx.preference.Preference;
import com.android.settings.notification.zen.ZenModeSettings;
import com.android.settingslib.core.lifecycle.Lifecycle;

public class ZenModeMessagesPreferenceController extends AbstractZenModePreferenceController {
    private final String KEY;
    private final ZenModeSettings.SummaryBuilder mSummaryBuilder;

    public boolean isAvailable() {
        return true;
    }

    public ZenModeMessagesPreferenceController(Context context, Lifecycle lifecycle, String str) {
        super(context, str, lifecycle);
        this.KEY = str;
        this.mSummaryBuilder = new ZenModeSettings.SummaryBuilder(context);
    }

    public String getPreferenceKey() {
        return this.KEY;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        int zenMode = getZenMode();
        if (zenMode == 2 || zenMode == 3) {
            preference.setEnabled(false);
            preference.setSummary(this.mBackend.getAlarmsTotalSilencePeopleSummary(4));
            return;
        }
        preference.setEnabled(true);
        preference.setSummary((CharSequence) this.mSummaryBuilder.getMessagesSettingSummary(getPolicy()));
    }
}
