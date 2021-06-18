package com.android.settings.homepage.contextualcards.slices;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;
import com.android.settings.R;
import com.android.settings.SubSettings;
import com.android.settings.display.AdaptiveSleepPreferenceController;
import com.android.settings.display.ScreenTimeoutPreferenceController;
import com.android.settings.display.ScreenTimeoutSettings;
import com.android.settings.slices.CustomSliceRegistry;
import com.android.settings.slices.CustomSliceable;
import com.android.settings.slices.SliceBuilderUtils;
import java.util.concurrent.TimeUnit;

public class ContextualAdaptiveSleepSlice implements CustomSliceable {
    static final long DEFERRED_TIME_DAYS = TimeUnit.DAYS.toMillis(14);
    static final String PREF_KEY_SETUP_TIME = "adaptive_sleep_setup_time";
    private Context mContext;

    public ContextualAdaptiveSleepSlice(Context context) {
        this.mContext = context;
    }

    public Slice getSlice() {
        if (this.mContext.getSharedPreferences("adaptive_sleep_slice", 0).getLong(PREF_KEY_SETUP_TIME, DEFERRED_TIME_DAYS) == DEFERRED_TIME_DAYS) {
            this.mContext.getSharedPreferences("adaptive_sleep_slice", 0).edit().putLong(PREF_KEY_SETUP_TIME, System.currentTimeMillis()).apply();
            return null;
        } else if (!isSettingsAvailable() || isUserInteracted() || isRecentlySetup() || isTurnedOn()) {
            return null;
        } else {
            IconCompat createWithResource = IconCompat.createWithResource(this.mContext, R.drawable.ic_settings_adaptive_sleep);
            CharSequence text = this.mContext.getText(R.string.adaptive_sleep_contextual_slice_title);
            CharSequence text2 = this.mContext.getText(R.string.adaptive_sleep_contextual_slice_summary);
            return new ListBuilder(this.mContext, CustomSliceRegistry.CONTEXTUAL_ADAPTIVE_SLEEP_URI, -1).setAccentColor(-1).addRow(new ListBuilder.RowBuilder().setTitleItem(createWithResource, 0).setTitle(text).setSubtitle(text2).setPrimaryAction(SliceAction.createDeeplink(getPrimaryAction(), createWithResource, 0, text))).build();
        }
    }

    public Uri getUri() {
        return CustomSliceRegistry.CONTEXTUAL_ADAPTIVE_SLEEP_URI;
    }

    public Intent getIntent() {
        CharSequence text = this.mContext.getText(R.string.adaptive_sleep_title);
        return SliceBuilderUtils.buildSearchResultPageIntent(this.mContext, ScreenTimeoutSettings.class.getName(), ScreenTimeoutPreferenceController.PREF_NAME, text.toString(), 1401).setClassName(this.mContext.getPackageName(), SubSettings.class.getName()).setData(new Uri.Builder().appendPath(ScreenTimeoutPreferenceController.PREF_NAME).build());
    }

    private PendingIntent getPrimaryAction() {
        return PendingIntent.getActivity(this.mContext, 0, getIntent(), 67108864);
    }

    private boolean isTurnedOn() {
        if (Settings.Secure.getInt(this.mContext.getContentResolver(), "adaptive_sleep", 0) != 0) {
            return true;
        }
        return false;
    }

    private boolean isUserInteracted() {
        return this.mContext.getSharedPreferences("adaptive_sleep_slice", 0).getBoolean("adaptive_sleep_interacted", false);
    }

    private boolean isRecentlySetup() {
        if (this.mContext.getSharedPreferences("adaptive_sleep_slice", 0).getLong(PREF_KEY_SETUP_TIME, DEFERRED_TIME_DAYS) > System.currentTimeMillis() - DEFERRED_TIME_DAYS) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean isSettingsAvailable() {
        return AdaptiveSleepPreferenceController.isControllerAvailable(this.mContext) == 1;
    }
}
