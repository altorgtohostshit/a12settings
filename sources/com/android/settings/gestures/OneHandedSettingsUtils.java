package com.android.settings.gestures;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;

public class OneHandedSettingsUtils {
    private static int sCurrentUserId;
    /* access modifiers changed from: private */
    public final Context mContext;
    private final SettingsObserver mSettingsObserver = new SettingsObserver(new Handler(Looper.getMainLooper()));

    public interface TogglesCallback {
        void onChange(Uri uri);
    }

    public enum OneHandedTimeout {
        NEVER(0),
        SHORT(4),
        MEDIUM(8),
        LONG(12);
        
        private final int mValue;

        private OneHandedTimeout(int i) {
            this.mValue = i;
        }

        public int getValue() {
            return this.mValue;
        }
    }

    OneHandedSettingsUtils(Context context) {
        this.mContext = context;
        sCurrentUserId = UserHandle.myUserId();
    }

    public static boolean isSupportOneHandedMode() {
        return SystemProperties.getBoolean("ro.support_one_handed_mode", false);
    }

    public static boolean isOneHandedModeEnabled(Context context) {
        return Settings.Secure.getIntForUser(context.getContentResolver(), "one_handed_mode_enabled", 0, sCurrentUserId) == 1;
    }

    public static void setOneHandedModeEnabled(Context context, boolean z) {
        Settings.Secure.putIntForUser(context.getContentResolver(), "one_handed_mode_enabled", z ? 1 : 0, sCurrentUserId);
    }

    public static boolean isTapsAppToExitEnabled(Context context) {
        return Settings.Secure.getIntForUser(context.getContentResolver(), "taps_app_to_exit", 0, sCurrentUserId) == 1;
    }

    public static boolean setTapsAppToExitEnabled(Context context, boolean z) {
        return Settings.Secure.putIntForUser(context.getContentResolver(), "taps_app_to_exit", z ? 1 : 0, sCurrentUserId);
    }

    public static int getTimeoutValue(Context context) {
        return Settings.Secure.getIntForUser(context.getContentResolver(), "one_handed_mode_timeout", OneHandedTimeout.MEDIUM.getValue(), sCurrentUserId);
    }

    public static void setUserId(int i) {
        sCurrentUserId = i;
    }

    public static void setTimeoutValue(Context context, int i) {
        Settings.Secure.putIntForUser(context.getContentResolver(), "one_handed_mode_timeout", i, sCurrentUserId);
    }

    public static boolean isSwipeDownNotificationEnabled(Context context) {
        return Settings.Secure.getIntForUser(context.getContentResolver(), "swipe_bottom_to_notification_enabled", 0, sCurrentUserId) == 1;
    }

    public static void setSwipeDownNotificationEnabled(Context context, boolean z) {
        Settings.Secure.putIntForUser(context.getContentResolver(), "swipe_bottom_to_notification_enabled", z ? 1 : 0, sCurrentUserId);
    }

    public void registerToggleAwareObserver(TogglesCallback togglesCallback) {
        this.mSettingsObserver.observe();
        this.mSettingsObserver.setCallback(togglesCallback);
    }

    public void unregisterToggleAwareObserver() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mSettingsObserver);
    }

    private final class SettingsObserver extends ContentObserver {
        private TogglesCallback mCallback;
        private final Uri mOneHandedEnabledAware = Settings.Secure.getUriFor("one_handed_mode_enabled");

        SettingsObserver(Handler handler) {
            super(handler);
        }

        /* access modifiers changed from: private */
        public void setCallback(TogglesCallback togglesCallback) {
            this.mCallback = togglesCallback;
        }

        public void observe() {
            OneHandedSettingsUtils.this.mContext.getContentResolver().registerContentObserver(this.mOneHandedEnabledAware, true, this);
        }

        public void onChange(boolean z, Uri uri) {
            TogglesCallback togglesCallback = this.mCallback;
            if (togglesCallback != null) {
                togglesCallback.onChange(uri);
            }
        }
    }
}
