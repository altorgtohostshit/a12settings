package com.android.settings.slices;

import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.util.ArrayMap;
import android.util.Log;
import com.android.settingslib.SliceBroadcastRelay;
import java.util.Map;

public class VolumeSliceHelper {
    static IntentFilter sIntentFilter;
    static Map<Uri, Integer> sRegisteredUri = new ArrayMap();

    static void registerIntentToUri(Context context, IntentFilter intentFilter, Uri uri, int i) {
        Log.d("VolumeSliceHelper", "Registering uri for broadcast relay: " + uri);
        synchronized (sRegisteredUri) {
            if (sRegisteredUri.isEmpty()) {
                SliceBroadcastRelay.registerReceiver(context, CustomSliceRegistry.VOLUME_SLICES_URI, VolumeSliceRelayReceiver.class, intentFilter);
                sIntentFilter = intentFilter;
            }
            sRegisteredUri.put(uri, Integer.valueOf(i));
        }
    }

    static boolean unregisterUri(Context context, Uri uri) {
        if (!sRegisteredUri.containsKey(uri)) {
            return false;
        }
        Log.d("VolumeSliceHelper", "Unregistering uri broadcast relay: " + uri);
        synchronized (sRegisteredUri) {
            sRegisteredUri.remove(uri);
            if (sRegisteredUri.isEmpty()) {
                sIntentFilter = null;
                SliceBroadcastRelay.unregisterReceivers(context, CustomSliceRegistry.VOLUME_SLICES_URI);
            }
        }
        return true;
    }

    static void onReceive(Context context, Intent intent) {
        String stringExtra;
        String action = intent.getAction();
        IntentFilter intentFilter = sIntentFilter;
        if (intentFilter != null && action != null && intentFilter.hasAction(action) && (stringExtra = intent.getStringExtra("uri")) != null) {
            if (!CustomSliceRegistry.VOLUME_SLICES_URI.equals(ContentProvider.getUriWithoutUserId(Uri.parse(stringExtra)))) {
                Log.w("VolumeSliceHelper", "Invalid uri: " + stringExtra);
            } else if ("android.media.VOLUME_CHANGED_ACTION".equals(action)) {
                handleVolumeChanged(context, intent);
            } else if ("android.media.STREAM_MUTE_CHANGED_ACTION".equals(action) || "android.media.STREAM_DEVICES_CHANGED_ACTION".equals(action)) {
                handleStreamChanged(context, intent);
            } else {
                notifyAllStreamsChanged(context);
            }
        }
    }

    private static void handleVolumeChanged(Context context, Intent intent) {
        if (intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", -1) != intent.getIntExtra("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE", -1)) {
            handleStreamChanged(context, intent);
        }
    }

    private static void handleStreamChanged(Context context, Intent intent) {
        int intExtra = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
        for (Map.Entry next : sRegisteredUri.entrySet()) {
            if (((Integer) next.getValue()).intValue() == intExtra) {
                context.getContentResolver().notifyChange((Uri) next.getKey(), (ContentObserver) null);
                return;
            }
        }
    }

    private static void notifyAllStreamsChanged(Context context) {
        sRegisteredUri.forEach(new VolumeSliceHelper$$ExternalSyntheticLambda0(context));
    }
}
