package com.android.settingslib.drawer;

import android.app.ActivityManager;
import android.content.Context;
import android.content.IContentProvider;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TileUtils {
    static final String SETTING_PKG = "com.android.settings";

    public static List<DashboardCategory> getCategories(Context context, Map<Pair<String, String>, Tile> map) {
        System.currentTimeMillis();
        boolean z = false;
        if (Settings.Global.getInt(context.getContentResolver(), "device_provisioned", 0) != 0) {
            z = true;
        }
        ArrayList arrayList = new ArrayList();
        for (UserHandle next : ((UserManager) context.getSystemService("user")).getUserProfiles()) {
            if (next.getIdentifier() == ActivityManager.getCurrentUser()) {
                Context context2 = context;
                UserHandle userHandle = next;
                Map<Pair<String, String>, Tile> map2 = map;
                ArrayList arrayList2 = arrayList;
                loadTilesForAction(context2, userHandle, "com.android.settings.action.SETTINGS", map2, (String) null, arrayList2, true);
                loadTilesForAction(context2, userHandle, "com.android.settings.OPERATOR_APPLICATION_SETTING", map2, "com.android.settings.category.wireless", arrayList2, false);
                loadTilesForAction(context2, userHandle, "com.android.settings.MANUFACTURER_APPLICATION_SETTING", map2, "com.android.settings.category.device", arrayList2, false);
            }
            if (z) {
                Context context3 = context;
                UserHandle userHandle2 = next;
                Map<Pair<String, String>, Tile> map3 = map;
                ArrayList arrayList3 = arrayList;
                loadTilesForAction(context3, userHandle2, "com.android.settings.action.EXTRA_SETTINGS", map3, (String) null, arrayList3, false);
                loadTilesForAction(context3, userHandle2, "com.android.settings.action.IA_SETTINGS", map3, (String) null, arrayList3, false);
            }
        }
        HashMap hashMap = new HashMap();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Tile tile = (Tile) it.next();
            String category = tile.getCategory();
            DashboardCategory dashboardCategory = (DashboardCategory) hashMap.get(category);
            if (dashboardCategory == null) {
                dashboardCategory = new DashboardCategory(category);
                hashMap.put(category, dashboardCategory);
            }
            dashboardCategory.addTile(tile);
        }
        ArrayList arrayList4 = new ArrayList(hashMap.values());
        Iterator it2 = arrayList4.iterator();
        while (it2.hasNext()) {
            ((DashboardCategory) it2.next()).sortTiles();
        }
        return arrayList4;
    }

    static void loadTilesForAction(Context context, UserHandle userHandle, String str, Map<Pair<String, String>, Tile> map, String str2, List<Tile> list, boolean z) {
        Intent intent = new Intent(str);
        if (z) {
            intent.setPackage(SETTING_PKG);
        }
        Context context2 = context;
        UserHandle userHandle2 = userHandle;
        Map<Pair<String, String>, Tile> map2 = map;
        String str3 = str2;
        List<Tile> list2 = list;
        Intent intent2 = intent;
        loadActivityTiles(context2, userHandle2, map2, str3, list2, intent2);
        loadProviderTiles(context2, userHandle2, map2, str3, list2, intent2);
    }

    private static void loadActivityTiles(Context context, UserHandle userHandle, Map<Pair<String, String>, Tile> map, String str, List<Tile> list, Intent intent) {
        for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentActivitiesAsUser(intent, 128, userHandle.getIdentifier())) {
            if (resolveInfo.system) {
                ActivityInfo activityInfo = resolveInfo.activityInfo;
                loadTile(userHandle, map, str, list, intent, activityInfo.metaData, activityInfo);
            }
        }
    }

    private static void loadProviderTiles(Context context, UserHandle userHandle, Map<Pair<String, String>, Tile> map, String str, List<Tile> list, Intent intent) {
        for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentContentProvidersAsUser(intent, 0, userHandle.getIdentifier())) {
            if (resolveInfo.system) {
                ProviderInfo providerInfo = resolveInfo.providerInfo;
                List<Bundle> switchDataFromProvider = getSwitchDataFromProvider(context, providerInfo.authority);
                if (switchDataFromProvider != null && !switchDataFromProvider.isEmpty()) {
                    for (Bundle loadTile : switchDataFromProvider) {
                        loadTile(userHandle, map, str, list, intent, loadTile, providerInfo);
                    }
                }
            }
        }
    }

    private static void loadTile(UserHandle userHandle, Map<Pair<String, String>, Tile> map, String str, List<Tile> list, Intent intent, Bundle bundle, ComponentInfo componentInfo) {
        Pair pair;
        Tile tile;
        if (userHandle.getIdentifier() == ActivityManager.getCurrentUser() || !Tile.isPrimaryProfileOnly(componentInfo.metaData)) {
            String str2 = "com.android.settings.category";
            if ((bundle == null || !bundle.containsKey(str2)) && str == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Found ");
                sb.append(componentInfo.name);
                sb.append(" for intent ");
                sb.append(intent);
                sb.append(" missing metadata ");
                if (bundle == null) {
                    str2 = "";
                }
                sb.append(str2);
                Log.w("TileUtils", sb.toString());
                return;
            }
            String string = bundle.getString(str2);
            boolean z = componentInfo instanceof ProviderInfo;
            if (z) {
                pair = new Pair(((ProviderInfo) componentInfo).authority, bundle.getString("com.android.settings.keyhint"));
            } else {
                pair = new Pair(componentInfo.packageName, componentInfo.name);
            }
            Tile tile2 = map.get(pair);
            if (tile2 == null) {
                if (z) {
                    tile = new ProviderTile((ProviderInfo) componentInfo, string, bundle);
                } else {
                    tile = new ActivityTile((ActivityInfo) componentInfo, string);
                }
                tile2 = tile;
                map.put(pair, tile2);
            } else {
                tile2.setMetaData(bundle);
            }
            if (!tile2.userHandle.contains(userHandle)) {
                tile2.userHandle.add(userHandle);
            }
            if (!list.contains(tile2)) {
                list.add(tile2);
                return;
            }
            return;
        }
        Log.w("TileUtils", "Found " + componentInfo.name + " for intent " + intent + " is primary profile only, skip loading tile for uid " + userHandle.getIdentifier());
    }

    static Bundle getSwitchDataFromProvider(Context context, String str, String str2) {
        return getBundleFromUri(context, buildUri(str, "getSwitchData", str2), new ArrayMap(), (Bundle) null);
    }

    private static List<Bundle> getSwitchDataFromProvider(Context context, String str) {
        Bundle bundleFromUri = getBundleFromUri(context, buildUri(str, "getSwitchData"), new ArrayMap(), (Bundle) null);
        if (bundleFromUri != null) {
            return bundleFromUri.getParcelableArrayList("switch_data");
        }
        return null;
    }

    public static Uri getCompleteUri(Tile tile, String str, String str2) {
        String string = tile.getMetaData().getString(str);
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        Uri parse = Uri.parse(string);
        List<String> pathSegments = parse.getPathSegments();
        if (pathSegments != null && !pathSegments.isEmpty()) {
            return parse;
        }
        String string2 = tile.getMetaData().getString("com.android.settings.keyhint");
        if (!TextUtils.isEmpty(string2)) {
            return buildUri(parse.getAuthority(), str2, string2);
        }
        Log.w("TileUtils", "Please specify the meta-data com.android.settings.keyhint in AndroidManifest.xml for " + string);
        return buildUri(parse.getAuthority(), str2);
    }

    static Uri buildUri(String str, String str2, String str3) {
        return new Uri.Builder().scheme("content").authority(str).appendPath(str2).appendPath(str3).build();
    }

    private static Uri buildUri(String str, String str2) {
        return new Uri.Builder().scheme("content").authority(str).appendPath(str2).build();
    }

    public static Pair<String, Integer> getIconFromUri(Context context, String str, Uri uri, Map<String, IContentProvider> map) {
        int i;
        Bundle bundleFromUri = getBundleFromUri(context, uri, map, (Bundle) null);
        if (bundleFromUri == null) {
            return null;
        }
        String string = bundleFromUri.getString("com.android.settings.icon_package");
        if (TextUtils.isEmpty(string) || (i = bundleFromUri.getInt("com.android.settings.icon", 0)) == 0) {
            return null;
        }
        if (string.equals(str) || string.equals(context.getPackageName())) {
            return Pair.create(string, Integer.valueOf(i));
        }
        return null;
    }

    public static String getTextFromUri(Context context, Uri uri, Map<String, IContentProvider> map, String str) {
        Bundle bundleFromUri = getBundleFromUri(context, uri, map, (Bundle) null);
        if (bundleFromUri != null) {
            return bundleFromUri.getString(str);
        }
        return null;
    }

    public static boolean getBooleanFromUri(Context context, Uri uri, Map<String, IContentProvider> map, String str) {
        Bundle bundleFromUri = getBundleFromUri(context, uri, map, (Bundle) null);
        if (bundleFromUri != null) {
            return bundleFromUri.getBoolean(str);
        }
        return false;
    }

    public static Bundle putBooleanToUriAndGetResult(Context context, Uri uri, Map<String, IContentProvider> map, String str, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(str, z);
        return getBundleFromUri(context, uri, map, bundle);
    }

    private static Bundle getBundleFromUri(Context context, Uri uri, Map<String, IContentProvider> map, Bundle bundle) {
        IContentProvider providerFromUri;
        Pair<String, String> methodAndKey = getMethodAndKey(uri);
        if (methodAndKey == null) {
            return null;
        }
        String str = (String) methodAndKey.first;
        String str2 = (String) methodAndKey.second;
        if (TextUtils.isEmpty(str) || (providerFromUri = getProviderFromUri(context, uri, map)) == null) {
            return null;
        }
        if (!TextUtils.isEmpty(str2)) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putString("com.android.settings.keyhint", str2);
        }
        try {
            return providerFromUri.call(context.getAttributionSource(), uri.getAuthority(), str, uri.toString(), bundle);
        } catch (RemoteException unused) {
            return null;
        }
    }

    private static IContentProvider getProviderFromUri(Context context, Uri uri, Map<String, IContentProvider> map) {
        if (uri == null) {
            return null;
        }
        String authority = uri.getAuthority();
        if (TextUtils.isEmpty(authority)) {
            return null;
        }
        if (!map.containsKey(authority)) {
            map.put(authority, context.getContentResolver().acquireUnstableProvider(uri));
        }
        return map.get(authority);
    }

    private static Pair<String, String> getMethodAndKey(Uri uri) {
        List<String> pathSegments;
        String str = null;
        if (uri == null || (pathSegments = uri.getPathSegments()) == null || pathSegments.isEmpty()) {
            return null;
        }
        String str2 = pathSegments.get(0);
        if (pathSegments.size() > 1) {
            str = pathSegments.get(1);
        }
        return Pair.create(str2, str);
    }
}
