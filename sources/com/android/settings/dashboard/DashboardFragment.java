package com.android.settings.dashboard;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.FeatureFlagUtils;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.PreferenceControllerListHelper;
import com.android.settings.core.SettingsBaseActivity;
import com.android.settings.homepage.HomepagePreference;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.widget.PrimarySwitchPreference;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.drawer.DashboardCategory;
import com.android.settingslib.drawer.ProviderTile;
import com.android.settingslib.drawer.Tile;
import com.android.settingslib.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public abstract class DashboardFragment extends SettingsPreferenceFragment implements SettingsBaseActivity.CategoryListener, PreferenceGroup.OnExpandButtonClickListener, BasePreferenceController.UiBlockListener {
    UiBlockerController mBlockerController;
    private final List<AbstractPreferenceController> mControllers = new ArrayList();
    private DashboardFeatureProvider mDashboardFeatureProvider;
    final ArrayMap<String, List<DynamicDataObserver>> mDashboardTilePrefKeys = new ArrayMap<>();
    private boolean mListeningToCategoryChange;
    private DashboardTilePlaceholderPreferenceController mPlaceholderPreferenceController;
    private final Map<Class, List<AbstractPreferenceController>> mPreferenceControllers = new ArrayMap();
    private final List<DynamicDataObserver> mRegisteredObservers = new ArrayList();
    private List<String> mSuppressInjectedTileKeys;

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return null;
    }

    /* access modifiers changed from: protected */
    public abstract String getLogTag();

    /* access modifiers changed from: protected */
    public abstract int getPreferenceScreenResId();

    /* access modifiers changed from: protected */
    public boolean isParalleledControllers() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean shouldForceRoundedIcon() {
        return false;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mSuppressInjectedTileKeys = Arrays.asList(context.getResources().getStringArray(R.array.config_suppress_injected_tile_keys));
        this.mDashboardFeatureProvider = FeatureFactory.getFactory(context).getDashboardFeatureProvider(context);
        List<AbstractPreferenceController> createPreferenceControllers = createPreferenceControllers(context);
        List<BasePreferenceController> filterControllers = PreferenceControllerListHelper.filterControllers(PreferenceControllerListHelper.getPreferenceControllersFromXml(context, getPreferenceScreenResId()), createPreferenceControllers);
        if (createPreferenceControllers != null) {
            this.mControllers.addAll(createPreferenceControllers);
        }
        this.mControllers.addAll(filterControllers);
        filterControllers.forEach(new DashboardFragment$$ExternalSyntheticLambda8(getSettingsLifecycle()));
        this.mControllers.forEach(new DashboardFragment$$ExternalSyntheticLambda1(getMetricsCategory()));
        DashboardTilePlaceholderPreferenceController dashboardTilePlaceholderPreferenceController = new DashboardTilePlaceholderPreferenceController(context);
        this.mPlaceholderPreferenceController = dashboardTilePlaceholderPreferenceController;
        this.mControllers.add(dashboardTilePlaceholderPreferenceController);
        for (AbstractPreferenceController addPreferenceController : this.mControllers) {
            addPreferenceController(addPreferenceController);
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onAttach$0(Lifecycle lifecycle, BasePreferenceController basePreferenceController) {
        if (basePreferenceController instanceof LifecycleObserver) {
            lifecycle.addObserver((LifecycleObserver) basePreferenceController);
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onAttach$1(int i, AbstractPreferenceController abstractPreferenceController) {
        if (abstractPreferenceController instanceof BasePreferenceController) {
            ((BasePreferenceController) abstractPreferenceController).setMetricsCategory(i);
        }
    }

    /* access modifiers changed from: package-private */
    public void checkUiBlocker(List<AbstractPreferenceController> list) {
        ArrayList arrayList = new ArrayList();
        list.forEach(new DashboardFragment$$ExternalSyntheticLambda7(this, arrayList));
        if (!arrayList.isEmpty()) {
            UiBlockerController uiBlockerController = new UiBlockerController(arrayList);
            this.mBlockerController = uiBlockerController;
            uiBlockerController.start(new DashboardFragment$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$checkUiBlocker$2(List list, AbstractPreferenceController abstractPreferenceController) {
        if ((abstractPreferenceController instanceof BasePreferenceController.UiBlocker) && abstractPreferenceController.isAvailable()) {
            ((BasePreferenceController) abstractPreferenceController).setUiBlockListener(this);
            list.add(abstractPreferenceController.getPreferenceKey());
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$checkUiBlocker$3() {
        updatePreferenceVisibility(this.mPreferenceControllers);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getPreferenceManager().setPreferenceComparisonCallback(new PreferenceManager.SimplePreferenceComparisonCallback());
        if (bundle != null) {
            updatePreferenceStates();
        }
    }

    public void onCategoriesChanged(Set<String> set) {
        String categoryKey = getCategoryKey();
        if (this.mDashboardFeatureProvider.getTilesForCategory(categoryKey) != null) {
            if (set == null) {
                refreshDashboardTiles(getLogTag());
            } else if (set.contains(categoryKey)) {
                Log.i("DashboardFragment", "refresh tiles for " + categoryKey);
                refreshDashboardTiles(getLogTag());
            }
        }
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        checkUiBlocker(this.mControllers);
        refreshAllPreferences(getLogTag());
        this.mControllers.stream().map(new DashboardFragment$$ExternalSyntheticLambda9(this)).filter(DashboardFragment$$ExternalSyntheticLambda11.INSTANCE).forEach(new DashboardFragment$$ExternalSyntheticLambda3(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Preference lambda$onCreatePreferences$4(AbstractPreferenceController abstractPreferenceController) {
        return findPreference(abstractPreferenceController.getPreferenceKey());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreatePreferences$5(Preference preference) {
        preference.getExtras().putInt("category", getMetricsCategory());
    }

    public void onStart() {
        super.onStart();
        if (this.mDashboardFeatureProvider.getTilesForCategory(getCategoryKey()) != null) {
            FragmentActivity activity = getActivity();
            if (activity instanceof SettingsBaseActivity) {
                this.mListeningToCategoryChange = true;
                ((SettingsBaseActivity) activity).addCategoryListener(this);
            }
            this.mDashboardTilePrefKeys.values().stream().filter(DashboardFragment$$ExternalSyntheticLambda12.INSTANCE).flatMap(DashboardFragment$$ExternalSyntheticLambda10.INSTANCE).forEach(new DashboardFragment$$ExternalSyntheticLambda4(this, getContentResolver()));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onStart$6(ContentResolver contentResolver, DynamicDataObserver dynamicDataObserver) {
        if (!this.mRegisteredObservers.contains(dynamicDataObserver)) {
            lambda$registerDynamicDataObservers$8(contentResolver, dynamicDataObserver);
        }
    }

    public void onResume() {
        super.onResume();
        updatePreferenceStates();
        writeElapsedTimeMetric(1729, "isParalleledControllers:" + isParalleledControllers());
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        for (List<AbstractPreferenceController> it : this.mPreferenceControllers.values()) {
            Iterator it2 = it.iterator();
            while (true) {
                if (it2.hasNext()) {
                    if (((AbstractPreferenceController) it2.next()).handlePreferenceTreeClick(preference)) {
                        writePreferenceClickMetric(preference);
                        return true;
                    }
                }
            }
        }
        return super.onPreferenceTreeClick(preference);
    }

    public void onStop() {
        super.onStop();
        unregisterDynamicDataObservers(new ArrayList(this.mRegisteredObservers));
        if (this.mListeningToCategoryChange) {
            FragmentActivity activity = getActivity();
            if (activity instanceof SettingsBaseActivity) {
                ((SettingsBaseActivity) activity).remCategoryListener(this);
            }
            this.mListeningToCategoryChange = false;
        }
    }

    public void onExpandButtonClick() {
        this.mMetricsFeatureProvider.action(0, 834, getMetricsCategory(), (String) null, 0);
    }

    /* access modifiers changed from: protected */
    public <T extends AbstractPreferenceController> T use(Class<T> cls) {
        List list = this.mPreferenceControllers.get(cls);
        if (list == null) {
            return null;
        }
        if (list.size() > 1) {
            Log.w("DashboardFragment", "Multiple controllers of Class " + cls.getSimpleName() + " found, returning first one.");
        }
        return (AbstractPreferenceController) list.get(0);
    }

    /* access modifiers changed from: protected */
    public void addPreferenceController(AbstractPreferenceController abstractPreferenceController) {
        if (this.mPreferenceControllers.get(abstractPreferenceController.getClass()) == null) {
            this.mPreferenceControllers.put(abstractPreferenceController.getClass(), new ArrayList());
        }
        this.mPreferenceControllers.get(abstractPreferenceController.getClass()).add(abstractPreferenceController);
    }

    public String getCategoryKey() {
        return DashboardFragmentRegistry.PARENT_TO_CATEGORY_KEY_MAP.get(getClass().getName());
    }

    /* access modifiers changed from: protected */
    public boolean displayTile(Tile tile) {
        if (this.mSuppressInjectedTileKeys == null || !tile.hasKey()) {
            return true;
        }
        return !this.mSuppressInjectedTileKeys.contains(tile.getKey(getContext()));
    }

    private void displayResourceTiles() {
        int preferenceScreenResId = getPreferenceScreenResId();
        if (preferenceScreenResId > 0) {
            addPreferencesFromResource(preferenceScreenResId);
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            preferenceScreen.setOnExpandButtonClickListener(this);
            displayResourceTilesToScreen(preferenceScreen);
        }
    }

    /* access modifiers changed from: protected */
    public void displayResourceTilesToScreen(PreferenceScreen preferenceScreen) {
        this.mPreferenceControllers.values().stream().flatMap(DashboardFragment$$ExternalSyntheticLambda10.INSTANCE).forEach(new DashboardFragment$$ExternalSyntheticLambda2(preferenceScreen));
    }

    /* access modifiers changed from: protected */
    public Collection<List<AbstractPreferenceController>> getPreferenceControllers() {
        return this.mPreferenceControllers.values();
    }

    /* access modifiers changed from: protected */
    public void updatePreferenceStates() {
        if (!isParalleledControllers() || !FeatureFlagUtils.isEnabled(getContext(), "settings_controller_loading_enhancement")) {
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            for (List<AbstractPreferenceController> it : this.mPreferenceControllers.values()) {
                for (AbstractPreferenceController abstractPreferenceController : it) {
                    if (abstractPreferenceController.isAvailable()) {
                        String preferenceKey = abstractPreferenceController.getPreferenceKey();
                        if (TextUtils.isEmpty(preferenceKey)) {
                            Log.d("DashboardFragment", String.format("Preference key is %s in Controller %s", new Object[]{preferenceKey, abstractPreferenceController.getClass().getSimpleName()}));
                        } else {
                            Preference findPreference = preferenceScreen.findPreference(preferenceKey);
                            if (findPreference == null) {
                                Log.d("DashboardFragment", String.format("Cannot find preference with key %s in Controller %s", new Object[]{preferenceKey, abstractPreferenceController.getClass().getSimpleName()}));
                            } else {
                                abstractPreferenceController.updateState(findPreference);
                            }
                        }
                    }
                }
            }
            return;
        }
        updatePreferenceStatesInParallel();
    }

    /* access modifiers changed from: package-private */
    public void updatePreferenceStatesInParallel() {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        Collection<List<AbstractPreferenceController>> values = this.mPreferenceControllers.values();
        ArrayList<ControllerFutureTask> arrayList = new ArrayList<>();
        for (List<AbstractPreferenceController> it : values) {
            for (AbstractPreferenceController controllerTask : it) {
                ControllerFutureTask controllerFutureTask = new ControllerFutureTask(new ControllerTask(controllerTask, preferenceScreen, this.mMetricsFeatureProvider, getMetricsCategory()), (Void) null);
                arrayList.add(controllerFutureTask);
                ThreadUtils.postOnBackgroundThread((Runnable) controllerFutureTask);
            }
        }
        for (ControllerFutureTask controllerFutureTask2 : arrayList) {
            try {
                controllerFutureTask2.get();
            } catch (InterruptedException | ExecutionException e) {
                Log.w("DashboardFragment", controllerFutureTask2.getController().getPreferenceKey() + " " + e.getMessage());
            }
        }
    }

    private void refreshAllPreferences(String str) {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            preferenceScreen.removeAll();
        }
        displayResourceTiles();
        refreshDashboardTiles(str);
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Log.d(str, "All preferences added, reporting fully drawn");
            activity.reportFullyDrawn();
        }
        updatePreferenceVisibility(this.mPreferenceControllers);
    }

    /* access modifiers changed from: package-private */
    public void updatePreferenceVisibility(Map<Class, List<AbstractPreferenceController>> map) {
        UiBlockerController uiBlockerController;
        if (getPreferenceScreen() != null && map != null && (uiBlockerController = this.mBlockerController) != null) {
            boolean isBlockerFinished = uiBlockerController.isBlockerFinished();
            for (List<AbstractPreferenceController> it : map.values()) {
                for (AbstractPreferenceController abstractPreferenceController : it) {
                    Preference findPreference = findPreference(abstractPreferenceController.getPreferenceKey());
                    if (findPreference != null) {
                        findPreference.setVisible(isBlockerFinished && abstractPreferenceController.isAvailable());
                    }
                }
            }
        }
    }

    private void refreshDashboardTiles(String str) {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        DashboardCategory tilesForCategory = this.mDashboardFeatureProvider.getTilesForCategory(getCategoryKey());
        if (tilesForCategory == null) {
            Log.d(str, "NO dashboard tiles for " + str);
            return;
        }
        List<Tile> tiles = tilesForCategory.getTiles();
        if (tiles == null) {
            Log.d(str, "tile list is empty, skipping category " + tilesForCategory.key);
            return;
        }
        ArrayMap arrayMap = new ArrayMap(this.mDashboardTilePrefKeys);
        boolean shouldForceRoundedIcon = shouldForceRoundedIcon();
        for (Tile next : tiles) {
            String dashboardKeyForTile = this.mDashboardFeatureProvider.getDashboardKeyForTile(next);
            if (TextUtils.isEmpty(dashboardKeyForTile)) {
                Log.d(str, "tile does not contain a key, skipping " + next);
            } else if (displayTile(next)) {
                if (this.mDashboardTilePrefKeys.containsKey(dashboardKeyForTile)) {
                    Preference findPreference = preferenceScreen.findPreference(dashboardKeyForTile);
                    this.mDashboardFeatureProvider.bindPreferenceToTileAndGetObservers(getActivity(), shouldForceRoundedIcon, getMetricsCategory(), findPreference, next, dashboardKeyForTile, this.mPlaceholderPreferenceController.getOrder());
                } else {
                    Preference createPreference = createPreference(next);
                    List<DynamicDataObserver> bindPreferenceToTileAndGetObservers = this.mDashboardFeatureProvider.bindPreferenceToTileAndGetObservers(getActivity(), shouldForceRoundedIcon, getMetricsCategory(), createPreference, next, dashboardKeyForTile, this.mPlaceholderPreferenceController.getOrder());
                    preferenceScreen.addPreference(createPreference);
                    registerDynamicDataObservers(bindPreferenceToTileAndGetObservers);
                    this.mDashboardTilePrefKeys.put(dashboardKeyForTile, bindPreferenceToTileAndGetObservers);
                }
                arrayMap.remove(dashboardKeyForTile);
            }
        }
        for (Map.Entry entry : arrayMap.entrySet()) {
            String str2 = (String) entry.getKey();
            this.mDashboardTilePrefKeys.remove(str2);
            Preference findPreference2 = preferenceScreen.findPreference(str2);
            if (findPreference2 != null) {
                preferenceScreen.removePreference(findPreference2);
            }
            unregisterDynamicDataObservers((List) entry.getValue());
        }
    }

    public void onBlockerWorkFinished(BasePreferenceController basePreferenceController) {
        this.mBlockerController.countDown(basePreferenceController.getPreferenceKey());
    }

    /* access modifiers changed from: package-private */
    public Preference createPreference(Tile tile) {
        if (FeatureFlagUtils.isEnabled(getContext(), "settings_silky_home") && TextUtils.equals(tile.getCategory(), "com.android.settings.category.ia.homepage")) {
            return new HomepagePreference(getPrefContext());
        }
        if (tile instanceof ProviderTile) {
            return new SwitchPreference(getPrefContext());
        }
        if (tile.hasSwitch()) {
            return new PrimarySwitchPreference(getPrefContext());
        }
        return new Preference(getPrefContext());
    }

    /* access modifiers changed from: package-private */
    public void registerDynamicDataObservers(List<DynamicDataObserver> list) {
        if (list != null && !list.isEmpty()) {
            list.forEach(new DashboardFragment$$ExternalSyntheticLambda6(this, getContentResolver()));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: registerDynamicDataObserver */
    public void lambda$registerDynamicDataObservers$8(ContentResolver contentResolver, DynamicDataObserver dynamicDataObserver) {
        Log.d("DashboardFragment", "register observer: @" + Integer.toHexString(dynamicDataObserver.hashCode()) + ", uri: " + dynamicDataObserver.getUri());
        contentResolver.registerContentObserver(dynamicDataObserver.getUri(), false, dynamicDataObserver);
        this.mRegisteredObservers.add(dynamicDataObserver);
    }

    private void unregisterDynamicDataObservers(List<DynamicDataObserver> list) {
        if (list != null && !list.isEmpty()) {
            list.forEach(new DashboardFragment$$ExternalSyntheticLambda5(this, getContentResolver()));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$unregisterDynamicDataObservers$9(ContentResolver contentResolver, DynamicDataObserver dynamicDataObserver) {
        Log.d("DashboardFragment", "unregister observer: @" + Integer.toHexString(dynamicDataObserver.hashCode()) + ", uri: " + dynamicDataObserver.getUri());
        this.mRegisteredObservers.remove(dynamicDataObserver);
        contentResolver.unregisterContentObserver(dynamicDataObserver);
    }
}
