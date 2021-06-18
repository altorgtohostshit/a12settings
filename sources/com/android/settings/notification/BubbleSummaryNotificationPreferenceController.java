package com.android.settings.notification;

import android.app.ActivityManager;
import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class BubbleSummaryNotificationPreferenceController extends BasePreferenceController {

    /* renamed from: ON */
    static final int f89ON = 1;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

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

    public BubbleSummaryNotificationPreferenceController(Context context, String str) {
        super(context, str);
    }

    public CharSequence getSummary() {
        return this.mContext.getString(areBubblesEnabled() ? R.string.notifications_bubble_setting_on_summary : R.string.switch_off_text);
    }

    public int getAvailabilityStatus() {
        return ((ActivityManager) this.mContext.getSystemService(ActivityManager.class)).isLowRamDevice() ? 3 : 0;
    }

    private boolean areBubblesEnabled() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "notification_bubbles", 1) == 1;
    }
}
