package com.android.settings.search;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.SearchIndexableResource;
import android.provider.SearchIndexablesContract;
import android.provider.SearchIndexablesProvider;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import androidx.slice.SliceViewManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFeatureProvider;
import com.android.settings.dashboard.DashboardFragmentRegistry;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.drawer.ActivityTile;
import com.android.settingslib.drawer.DashboardCategory;
import com.android.settingslib.drawer.Tile;
import com.android.settingslib.search.Indexable$SearchIndexProvider;
import com.android.settingslib.search.SearchIndexableData;
import com.android.settingslib.search.SearchIndexableRaw;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SettingsSearchIndexablesProvider extends SearchIndexablesProvider {
    private static final Collection<String> INVALID_KEYS;

    public boolean onCreate() {
        return true;
    }

    static {
        ArraySet arraySet = new ArraySet();
        INVALID_KEYS = arraySet;
        arraySet.add((Object) null);
        arraySet.add("");
    }

    public Cursor queryXmlResources(String[] strArr) {
        MatrixCursor matrixCursor = new MatrixCursor(SearchIndexablesContract.INDEXABLES_XML_RES_COLUMNS);
        for (SearchIndexableResource next : getSearchIndexableResourcesFromProvider(getContext())) {
            Object[] objArr = new Object[SearchIndexablesContract.INDEXABLES_XML_RES_COLUMNS.length];
            objArr[0] = Integer.valueOf(next.rank);
            objArr[1] = Integer.valueOf(next.xmlResId);
            objArr[2] = next.className;
            objArr[3] = Integer.valueOf(next.iconResId);
            objArr[4] = next.intentAction;
            objArr[5] = next.intentTargetPackage;
            objArr[6] = null;
            matrixCursor.addRow(objArr);
        }
        return matrixCursor;
    }

    public Cursor queryRawData(String[] strArr) {
        MatrixCursor matrixCursor = new MatrixCursor(SearchIndexablesContract.INDEXABLES_RAW_COLUMNS);
        for (SearchIndexableRaw createIndexableRawColumnObjects : getSearchIndexableRawFromProvider(getContext())) {
            matrixCursor.addRow(createIndexableRawColumnObjects(createIndexableRawColumnObjects));
        }
        return matrixCursor;
    }

    public Cursor queryNonIndexableKeys(String[] strArr) {
        MatrixCursor matrixCursor = new MatrixCursor(SearchIndexablesContract.NON_INDEXABLES_KEYS_COLUMNS);
        for (String str : getNonIndexableKeysFromProvider(getContext())) {
            Object[] objArr = new Object[SearchIndexablesContract.NON_INDEXABLES_KEYS_COLUMNS.length];
            objArr[0] = str;
            matrixCursor.addRow(objArr);
        }
        return matrixCursor;
    }

    public Cursor queryDynamicRawData(String[] strArr) {
        Context context = getContext();
        ArrayList<SearchIndexableRaw> arrayList = new ArrayList<>();
        arrayList.addAll(getDynamicSearchIndexableRawFromProvider(context));
        arrayList.addAll(getInjectionIndexableRawData(context));
        MatrixCursor matrixCursor = new MatrixCursor(SearchIndexablesContract.INDEXABLES_RAW_COLUMNS);
        for (SearchIndexableRaw createIndexableRawColumnObjects : arrayList) {
            matrixCursor.addRow(createIndexableRawColumnObjects(createIndexableRawColumnObjects));
        }
        return matrixCursor;
    }

    public Cursor querySiteMapPairs() {
        Object obj;
        MatrixCursor matrixCursor = new MatrixCursor(SearchIndexablesContract.SITE_MAP_COLUMNS);
        Context context = getContext();
        for (DashboardCategory next : FeatureFactory.getFactory(context).getDashboardFeatureProvider(context).getAllCategories()) {
            String str = DashboardFragmentRegistry.CATEGORY_KEY_TO_PARENT_MAP.get(next.key);
            if (str != null) {
                for (Tile next2 : next.getTiles()) {
                    String str2 = null;
                    if (next2.getMetaData() != null) {
                        str2 = next2.getMetaData().getString("com.android.settings.FRAGMENT_CLASS");
                    }
                    if (str2 == null) {
                        str2 = next2.getComponentName();
                        obj = next2.getTitle(getContext());
                    } else {
                        obj = "";
                    }
                    if (str2 != null) {
                        matrixCursor.newRow().add("parent_class", str).add("child_class", str2).add("child_title", obj);
                    }
                }
            }
        }
        for (String next3 : CustomSiteMapRegistry.CUSTOM_SITE_MAP.keySet()) {
            matrixCursor.newRow().add("parent_class", CustomSiteMapRegistry.CUSTOM_SITE_MAP.get(next3)).add("child_class", next3);
        }
        return matrixCursor;
    }

    public Cursor querySliceUriPairs() {
        Uri uri;
        SliceViewManager instance = SliceViewManager.getInstance(getContext());
        MatrixCursor matrixCursor = new MatrixCursor(SearchIndexablesContract.SLICE_URI_PAIRS_COLUMNS);
        String string = getContext().getString(R.string.config_non_public_slice_query_uri);
        if (!TextUtils.isEmpty(string)) {
            uri = Uri.parse(string);
        } else {
            uri = new Uri.Builder().scheme("content").authority("com.android.settings.slices").build();
        }
        Uri build = new Uri.Builder().scheme("content").authority("android.settings.slices").build();
        Collection<Uri> sliceDescendants = instance.getSliceDescendants(uri);
        sliceDescendants.addAll(instance.getSliceDescendants(build));
        for (Uri next : sliceDescendants) {
            matrixCursor.newRow().add("key", next.getLastPathSegment()).add("slice_uri", next);
        }
        return matrixCursor;
    }

    private List<String> getNonIndexableKeysFromProvider(Context context) {
        Collection<SearchIndexableData> providerValues = FeatureFactory.getFactory(context).getSearchFeatureProvider().getSearchIndexableResources().getProviderValues();
        ArrayList arrayList = new ArrayList();
        for (SearchIndexableData next : providerValues) {
            System.currentTimeMillis();
            Indexable$SearchIndexProvider searchIndexProvider = next.getSearchIndexProvider();
            try {
                List<String> nonIndexableKeys = searchIndexProvider.getNonIndexableKeys(context);
                if (nonIndexableKeys != null && !nonIndexableKeys.isEmpty()) {
                    if (nonIndexableKeys.removeAll(INVALID_KEYS)) {
                        Log.v("SettingsSearchProvider", searchIndexProvider + " tried to add an empty non-indexable key");
                    }
                    arrayList.addAll(nonIndexableKeys);
                }
            } catch (Exception e) {
                if (System.getProperty("debug.com.android.settings.search.crash_on_error") == null) {
                    Log.e("SettingsSearchProvider", "Error trying to get non-indexable keys from: " + next.getTargetClass().getName(), e);
                } else {
                    throw new RuntimeException(e);
                }
            }
        }
        return arrayList;
    }

    private List<SearchIndexableResource> getSearchIndexableResourcesFromProvider(Context context) {
        String str;
        Collection<SearchIndexableData> providerValues = FeatureFactory.getFactory(context).getSearchFeatureProvider().getSearchIndexableResources().getProviderValues();
        ArrayList arrayList = new ArrayList();
        for (SearchIndexableData next : providerValues) {
            List<SearchIndexableResource> xmlResourcesToIndex = next.getSearchIndexProvider().getXmlResourcesToIndex(context, true);
            if (xmlResourcesToIndex != null) {
                for (SearchIndexableResource next2 : xmlResourcesToIndex) {
                    if (TextUtils.isEmpty(next2.className)) {
                        str = next.getTargetClass().getName();
                    } else {
                        str = next2.className;
                    }
                    next2.className = str;
                }
                arrayList.addAll(xmlResourcesToIndex);
            }
        }
        return arrayList;
    }

    private List<SearchIndexableRaw> getSearchIndexableRawFromProvider(Context context) {
        Collection<SearchIndexableData> providerValues = FeatureFactory.getFactory(context).getSearchFeatureProvider().getSearchIndexableResources().getProviderValues();
        ArrayList arrayList = new ArrayList();
        for (SearchIndexableData next : providerValues) {
            List<SearchIndexableRaw> rawDataToIndex = next.getSearchIndexProvider().getRawDataToIndex(context, true);
            if (rawDataToIndex != null) {
                for (SearchIndexableRaw searchIndexableRaw : rawDataToIndex) {
                    searchIndexableRaw.className = next.getTargetClass().getName();
                }
                arrayList.addAll(rawDataToIndex);
            }
        }
        return arrayList;
    }

    private List<SearchIndexableRaw> getDynamicSearchIndexableRawFromProvider(Context context) {
        Collection<SearchIndexableData> providerValues = FeatureFactory.getFactory(context).getSearchFeatureProvider().getSearchIndexableResources().getProviderValues();
        ArrayList arrayList = new ArrayList();
        for (SearchIndexableData next : providerValues) {
            List<SearchIndexableRaw> dynamicRawDataToIndex = next.getSearchIndexProvider().getDynamicRawDataToIndex(context, true);
            if (dynamicRawDataToIndex != null) {
                for (SearchIndexableRaw searchIndexableRaw : dynamicRawDataToIndex) {
                    searchIndexableRaw.className = next.getTargetClass().getName();
                }
                arrayList.addAll(dynamicRawDataToIndex);
            }
        }
        return arrayList;
    }

    private List<SearchIndexableRaw> getInjectionIndexableRawData(Context context) {
        DashboardFeatureProvider dashboardFeatureProvider = FeatureFactory.getFactory(context).getDashboardFeatureProvider(context);
        ArrayList arrayList = new ArrayList();
        String packageName = context.getPackageName();
        for (DashboardCategory tiles : dashboardFeatureProvider.getAllCategories()) {
            for (Tile next : tiles.getTiles()) {
                if (isEligibleForIndexing(packageName, next)) {
                    SearchIndexableRaw searchIndexableRaw = new SearchIndexableRaw(context);
                    CharSequence title = next.getTitle(context);
                    String str = null;
                    String charSequence = TextUtils.isEmpty(title) ? null : title.toString();
                    searchIndexableRaw.title = charSequence;
                    if (!TextUtils.isEmpty(charSequence)) {
                        searchIndexableRaw.key = dashboardFeatureProvider.getDashboardKeyForTile(next);
                        CharSequence summary = next.getSummary(context);
                        if (!TextUtils.isEmpty(summary)) {
                            str = summary.toString();
                        }
                        searchIndexableRaw.summaryOn = str;
                        searchIndexableRaw.summaryOff = str;
                        searchIndexableRaw.className = DashboardFragmentRegistry.CATEGORY_KEY_TO_PARENT_MAP.get(next.getCategory());
                        arrayList.add(searchIndexableRaw);
                    }
                }
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean isEligibleForIndexing(String str, Tile tile) {
        if ((!TextUtils.equals(str, tile.getPackageName()) || !(tile instanceof ActivityTile)) && !TextUtils.equals(tile.getCategory(), "com.android.settings.category.ia.homepage")) {
            return true;
        }
        return false;
    }

    private static Object[] createIndexableRawColumnObjects(SearchIndexableRaw searchIndexableRaw) {
        Object[] objArr = new Object[SearchIndexablesContract.INDEXABLES_RAW_COLUMNS.length];
        objArr[1] = searchIndexableRaw.title;
        objArr[2] = searchIndexableRaw.summaryOn;
        objArr[3] = searchIndexableRaw.summaryOff;
        objArr[4] = searchIndexableRaw.entries;
        objArr[5] = searchIndexableRaw.keywords;
        objArr[6] = searchIndexableRaw.screenTitle;
        objArr[7] = searchIndexableRaw.className;
        objArr[8] = Integer.valueOf(searchIndexableRaw.iconResId);
        objArr[9] = searchIndexableRaw.intentAction;
        objArr[10] = searchIndexableRaw.intentTargetPackage;
        objArr[11] = searchIndexableRaw.intentTargetClass;
        objArr[12] = searchIndexableRaw.key;
        objArr[13] = Integer.valueOf(searchIndexableRaw.userId);
        return objArr;
    }
}
