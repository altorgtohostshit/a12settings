package com.android.settings.notification;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.utils.ThreadUtils;

public abstract class RingtonePreferenceControllerBase extends AbstractPreferenceController implements PreferenceControllerMixin {
    public abstract int getRingtoneType();

    public boolean handlePreferenceTreeClick(Preference preference) {
        return false;
    }

    public boolean isAvailable() {
        return true;
    }

    public RingtonePreferenceControllerBase(Context context) {
        super(context);
    }

    public void updateState(Preference preference) {
        ThreadUtils.postOnBackgroundThread((Runnable) new RingtonePreferenceControllerBase$$ExternalSyntheticLambda1(this, preference));
    }

    /* access modifiers changed from: private */
    /* renamed from: updateSummary */
    public void lambda$updateState$0(Preference preference) {
        try {
            String title = Ringtone.getTitle(this.mContext, RingtoneManager.getActualDefaultRingtoneUri(this.mContext, getRingtoneType()), false, true);
            if (title != null) {
                ThreadUtils.postOnMainThread(new RingtonePreferenceControllerBase$$ExternalSyntheticLambda0(preference, title));
            }
        } catch (IllegalArgumentException e) {
            Log.w("PrefControllerMixin", "Error getting ringtone summary.", e);
        }
    }
}
