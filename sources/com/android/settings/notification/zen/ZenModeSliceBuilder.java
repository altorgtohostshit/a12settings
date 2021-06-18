package com.android.settings.notification.zen;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;
import com.android.settings.R;
import com.android.settings.SubSettings;
import com.android.settings.slices.CustomSliceRegistry;
import com.android.settings.slices.SliceBroadcastReceiver;
import com.android.settings.slices.SliceBuilderUtils;
import com.android.settingslib.Utils;

public class ZenModeSliceBuilder {
    public static final IntentFilter INTENT_FILTER;

    static {
        IntentFilter intentFilter = new IntentFilter();
        INTENT_FILTER = intentFilter;
        intentFilter.addAction("android.app.action.NOTIFICATION_POLICY_CHANGED");
        intentFilter.addAction("android.app.action.INTERRUPTION_FILTER_CHANGED");
        intentFilter.addAction("android.app.action.INTERRUPTION_FILTER_CHANGED_INTERNAL");
    }

    public static Slice getSlice(Context context) {
        boolean isZenModeEnabled = isZenModeEnabled(context);
        CharSequence text = context.getText(R.string.zen_mode_settings_title);
        CharSequence text2 = context.getText(R.string.zen_mode_slice_subtitle);
        int colorAccentDefaultColor = Utils.getColorAccentDefaultColor(context);
        PendingIntent broadcastIntent = getBroadcastIntent(context);
        ListBuilder.RowBuilder primaryAction = new ListBuilder.RowBuilder().setTitle(text).addEndItem(SliceAction.createToggle(broadcastIntent, (CharSequence) null, isZenModeEnabled)).setPrimaryAction(SliceAction.createDeeplink(getPrimaryAction(context), (IconCompat) null, 0, text));
        if (!com.android.settings.Utils.isSettingsIntelligence(context)) {
            primaryAction.setSubtitle(text2);
        }
        return new ListBuilder(context, CustomSliceRegistry.ZEN_MODE_SLICE_URI, -1).setAccentColor(colorAccentDefaultColor).addRow(primaryAction).build();
    }

    public static void handleUriChange(Context context, Intent intent) {
        NotificationManager.from(context).setZenMode(intent.getBooleanExtra("android.app.slice.extra.TOGGLE_STATE", false) ? 1 : 0, (Uri) null, "ZenModeSliceBuilder");
    }

    public static Intent getIntent(Context context) {
        return SliceBuilderUtils.buildSearchResultPageIntent(context, ZenModeSettings.class.getName(), "zen_mode_toggle", context.getText(R.string.zen_mode_settings_title).toString(), 76).setClassName(context.getPackageName(), SubSettings.class.getName()).setData(new Uri.Builder().appendPath("zen_mode_toggle").build());
    }

    private static boolean isZenModeEnabled(Context context) {
        int zenMode = ((NotificationManager) context.getSystemService(NotificationManager.class)).getZenMode();
        return zenMode == 1 || zenMode == 2 || zenMode == 3;
    }

    private static PendingIntent getPrimaryAction(Context context) {
        return PendingIntent.getActivity(context, 0, getIntent(context), 67108864);
    }

    private static PendingIntent getBroadcastIntent(Context context) {
        return PendingIntent.getBroadcast(context, 0, new Intent("com.android.settings.notification.ZEN_MODE_CHANGED").setClass(context, SliceBroadcastReceiver.class), 335544320);
    }
}
