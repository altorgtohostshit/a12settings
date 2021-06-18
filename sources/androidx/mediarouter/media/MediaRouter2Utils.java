package androidx.mediarouter.media;

import android.annotation.SuppressLint;
import android.media.MediaRoute2Info;
import android.media.RouteDiscoveryPreference;
import android.net.Uri;
import android.os.Bundle;
import androidx.mediarouter.media.MediaRouteDescriptor;
import java.util.ArrayList;
import java.util.List;

@SuppressLint({"NewApi"})
class MediaRouter2Utils {
    public static MediaRouteDescriptor toMediaRouteDescriptor(MediaRoute2Info mediaRoute2Info) {
        if (mediaRoute2Info == null) {
            return null;
        }
        MediaRouteDescriptor.Builder canDisconnect = new MediaRouteDescriptor.Builder(mediaRoute2Info.getId(), mediaRoute2Info.getName().toString()).setConnectionState(mediaRoute2Info.getConnectionState()).setVolumeHandling(mediaRoute2Info.getVolumeHandling()).setVolumeMax(mediaRoute2Info.getVolumeMax()).setVolume(mediaRoute2Info.getVolume()).setExtras(mediaRoute2Info.getExtras()).setEnabled(true).setCanDisconnect(false);
        CharSequence description = mediaRoute2Info.getDescription();
        if (description != null) {
            canDisconnect.setDescription(description.toString());
        }
        Uri iconUri = mediaRoute2Info.getIconUri();
        if (iconUri != null) {
            canDisconnect.setIconUri(iconUri);
        }
        Bundle extras = mediaRoute2Info.getExtras();
        if (extras == null || !extras.containsKey("androidx.mediarouter.media.KEY_EXTRAS") || !extras.containsKey("androidx.mediarouter.media.KEY_DEVICE_TYPE") || !extras.containsKey("androidx.mediarouter.media.KEY_CONTROL_FILTERS")) {
            return null;
        }
        canDisconnect.setExtras(extras.getBundle("androidx.mediarouter.media.KEY_EXTRAS"));
        canDisconnect.setDeviceType(extras.getInt("androidx.mediarouter.media.KEY_DEVICE_TYPE", 0));
        canDisconnect.setPlaybackType(extras.getInt("androidx.mediarouter.media.KEY_PLAYBACK_TYPE", 1));
        ArrayList parcelableArrayList = extras.getParcelableArrayList("androidx.mediarouter.media.KEY_CONTROL_FILTERS");
        if (parcelableArrayList != null) {
            canDisconnect.addControlFilters(parcelableArrayList);
        }
        return canDisconnect.build();
    }

    static List<String> getRouteIds(List<MediaRoute2Info> list) {
        if (list == null) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        for (MediaRoute2Info next : list) {
            if (next != null) {
                arrayList.add(next.getId());
            }
        }
        return arrayList;
    }

    static RouteDiscoveryPreference toDiscoveryPreference(MediaRouteDiscoveryRequest mediaRouteDiscoveryRequest) {
        if (mediaRouteDiscoveryRequest == null || !mediaRouteDiscoveryRequest.isValid()) {
            return new RouteDiscoveryPreference.Builder(new ArrayList(), false).build();
        }
        boolean isActiveScan = mediaRouteDiscoveryRequest.isActiveScan();
        ArrayList arrayList = new ArrayList();
        for (String routeFeature : mediaRouteDiscoveryRequest.getSelector().getControlCategories()) {
            arrayList.add(toRouteFeature(routeFeature));
        }
        return new RouteDiscoveryPreference.Builder(arrayList, isActiveScan).build();
    }

    static String toRouteFeature(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -2065577523:
                if (str.equals("android.media.intent.category.REMOTE_PLAYBACK")) {
                    c = 0;
                    break;
                }
                break;
            case 956939050:
                if (str.equals("android.media.intent.category.LIVE_AUDIO")) {
                    c = 1;
                    break;
                }
                break;
            case 975975375:
                if (str.equals("android.media.intent.category.LIVE_VIDEO")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return "android.media.route.feature.REMOTE_PLAYBACK";
            case 1:
                return "android.media.route.feature.LIVE_AUDIO";
            case 2:
                return "android.media.route.feature.LIVE_VIDEO";
            default:
                return str;
        }
    }
}
