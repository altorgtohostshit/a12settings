package com.android.settings.homepage.contextualcards.slices;

import android.app.PendingIntent;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;
import com.android.settings.R;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.CustomSliceRegistry;
import com.android.settings.slices.CustomSliceable;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.Utils;

public class DarkThemeSlice implements CustomSliceable {
    private static final boolean DEBUG = Build.IS_DEBUGGABLE;
    static long sActiveUiSession = -1000;
    static boolean sKeepSliceShow;
    static boolean sPreChecked = false;
    static boolean sSliceClicked = false;
    private final Context mContext;
    private final PowerManager mPowerManager;
    private final UiModeManager mUiModeManager;

    public Intent getIntent() {
        return null;
    }

    public DarkThemeSlice(Context context) {
        this.mContext = context;
        this.mUiModeManager = (UiModeManager) context.getSystemService(UiModeManager.class);
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
    }

    public Slice getSlice() {
        long uiSessionToken = FeatureFactory.getFactory(this.mContext).getSlicesFeatureProvider().getUiSessionToken();
        if (uiSessionToken != sActiveUiSession) {
            sActiveUiSession = uiSessionToken;
            sKeepSliceShow = false;
        }
        if (DEBUG) {
            Log.d("DarkThemeSlice", "sKeepSliceShow = " + sKeepSliceShow + ", sSliceClicked = " + sSliceClicked + ", isAvailable = " + isAvailable(this.mContext));
        }
        if (this.mPowerManager.isPowerSaveMode() || ((!sKeepSliceShow || !sSliceClicked) && !isAvailable(this.mContext))) {
            return new ListBuilder(this.mContext, CustomSliceRegistry.DARK_THEME_SLICE_URI, -1).setIsError(true).build();
        }
        sKeepSliceShow = true;
        PendingIntent broadcastIntent = getBroadcastIntent(this.mContext);
        int colorAccentDefaultColor = Utils.getColorAccentDefaultColor(this.mContext);
        IconCompat createWithResource = IconCompat.createWithResource(this.mContext, R.drawable.dark_theme);
        boolean isNightMode = com.android.settings.Utils.isNightMode(this.mContext);
        if (sPreChecked != isNightMode) {
            resetValue(isNightMode, false);
        }
        return new ListBuilder(this.mContext, CustomSliceRegistry.DARK_THEME_SLICE_URI, -1).setAccentColor(colorAccentDefaultColor).addRow(new ListBuilder.RowBuilder().setTitle(this.mContext.getText(R.string.dark_theme_slice_title)).setTitleItem(createWithResource, 0).setSubtitle(this.mContext.getText(R.string.dark_theme_slice_subtitle)).setPrimaryAction(SliceAction.createToggle(broadcastIntent, (CharSequence) null, isNightMode))).build();
    }

    public Uri getUri() {
        return CustomSliceRegistry.DARK_THEME_SLICE_URI;
    }

    public void onNotifyChange(Intent intent) {
        boolean booleanExtra = intent.getBooleanExtra("android.app.slice.extra.TOGGLE_STATE", false);
        if (booleanExtra) {
            resetValue(booleanExtra, true);
        }
        new Handler(Looper.getMainLooper()).postDelayed(new DarkThemeSlice$$ExternalSyntheticLambda0(this, booleanExtra), 200);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onNotifyChange$0(boolean z) {
        this.mUiModeManager.setNightModeActivated(z);
    }

    public Class getBackgroundWorkerClass() {
        return DarkThemeWorker.class;
    }

    /* access modifiers changed from: package-private */
    public boolean isAvailable(Context context) {
        if (com.android.settings.Utils.isNightMode(context) || isNightModeScheduled()) {
            return false;
        }
        int intProperty = ((BatteryManager) context.getSystemService(BatteryManager.class)).getIntProperty(4);
        Log.d("DarkThemeSlice", "battery level = " + intProperty);
        if (intProperty <= 50) {
            return true;
        }
        return false;
    }

    private void resetValue(boolean z, boolean z2) {
        sPreChecked = z;
        sSliceClicked = z2;
    }

    private boolean isNightModeScheduled() {
        int nightMode = this.mUiModeManager.getNightMode();
        if (DEBUG) {
            Log.d("DarkThemeSlice", "night mode = " + nightMode);
        }
        return nightMode == 0 || nightMode == 3;
    }

    public static class DarkThemeWorker extends SliceBackgroundWorker<Void> {
        private final ContentObserver mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            public void onChange(boolean z) {
                if (((PowerManager) DarkThemeWorker.this.mContext.getSystemService(PowerManager.class)).isPowerSaveMode()) {
                    DarkThemeWorker.this.notifySliceChange();
                }
            }
        };
        /* access modifiers changed from: private */
        public final Context mContext;

        public void close() {
        }

        public DarkThemeWorker(Context context, Uri uri) {
            super(context, uri);
            this.mContext = context;
        }

        /* access modifiers changed from: protected */
        public void onSlicePinned() {
            this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("low_power"), false, this.mContentObserver);
        }

        /* access modifiers changed from: protected */
        public void onSliceUnpinned() {
            this.mContext.getContentResolver().unregisterContentObserver(this.mContentObserver);
        }
    }
}
