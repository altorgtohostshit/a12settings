package com.android.settings.applications.specialaccess.notificationaccess;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.service.notification.NotificationListenerFilter;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.notification.NotificationBackend;
import com.android.settings.slices.SliceBackgroundWorker;

public class PreUpgradePreferenceController extends BasePreferenceController {
    private ComponentName mCn;
    private NotificationListenerFilter mNlf;
    private NotificationBackend mNm;
    private int mTargetSdk;
    private int mUserId;

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

    public PreUpgradePreferenceController(Context context, String str) {
        super(context, str);
    }

    public PreUpgradePreferenceController setCn(ComponentName componentName) {
        this.mCn = componentName;
        return this;
    }

    public PreUpgradePreferenceController setUserId(int i) {
        this.mUserId = i;
        return this;
    }

    public PreUpgradePreferenceController setNm(NotificationBackend notificationBackend) {
        this.mNm = notificationBackend;
        return this;
    }

    public PreUpgradePreferenceController setTargetSdk(int i) {
        this.mTargetSdk = i;
        return this;
    }

    public int getAvailabilityStatus() {
        if (this.mNm.isNotificationListenerAccessGranted(this.mCn)) {
            NotificationListenerFilter listenerFilter = this.mNm.getListenerFilter(this.mCn, this.mUserId);
            this.mNlf = listenerFilter;
            if (this.mTargetSdk <= 10000 && listenerFilter.areAllTypesAllowed() && this.mNlf.getDisallowedPackages().isEmpty()) {
                return 0;
            }
        }
        return 2;
    }
}
