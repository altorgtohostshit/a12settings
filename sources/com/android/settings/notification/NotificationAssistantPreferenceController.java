package com.android.settings.notification;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import androidx.fragment.app.Fragment;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;

public class NotificationAssistantPreferenceController extends TogglePreferenceController {
    private static final int AVAILABLE = 1;
    private static final String TAG = "NASPreferenceController";
    private Fragment mFragment;
    protected NotificationBackend mNotificationBackend;
    private int mUserId = UserHandle.myUserId();
    private final UserManager mUserManager;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public int getAvailabilityStatus() {
        return 1;
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

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public NotificationAssistantPreferenceController(Context context, NotificationBackend notificationBackend, Fragment fragment, String str) {
        super(context, str);
        this.mNotificationBackend = notificationBackend;
        this.mFragment = fragment;
        this.mUserManager = UserManager.get(context);
    }

    public boolean isChecked() {
        ComponentName allowedNotificationAssistant = this.mNotificationBackend.getAllowedNotificationAssistant();
        return allowedNotificationAssistant != null && allowedNotificationAssistant.equals(this.mNotificationBackend.getDefaultNotificationAssistant());
    }

    public boolean setChecked(boolean z) {
        ComponentName defaultNotificationAssistant = z ? this.mNotificationBackend.getDefaultNotificationAssistant() : null;
        if (!z) {
            setNotificationAssistantGranted((ComponentName) null);
            return true;
        } else if (this.mFragment != null) {
            showDialog(defaultNotificationAssistant);
            return false;
        } else {
            throw new IllegalStateException("No fragment to start activity");
        }
    }

    /* access modifiers changed from: protected */
    public void setNotificationAssistantGranted(ComponentName componentName) {
        boolean z = false;
        if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "nas_settings_updated", 0, this.mUserId) == 0) {
            NotificationBackend notificationBackend = this.mNotificationBackend;
            int i = this.mUserId;
            if (componentName != null) {
                z = true;
            }
            notificationBackend.setNASMigrationDoneAndResetDefault(i, z);
        }
        this.mNotificationBackend.setNotificationAssistantGranted(componentName);
    }

    /* access modifiers changed from: protected */
    public void showDialog(ComponentName componentName) {
        NotificationAssistantDialogFragment.newInstance(this.mFragment, componentName).show(this.mFragment.getFragmentManager(), TAG);
    }
}
