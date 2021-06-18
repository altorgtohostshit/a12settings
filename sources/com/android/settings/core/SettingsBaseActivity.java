package com.android.settings.core;

import android.R;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.FeatureFlagUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.SubSettings;
import com.android.settings.Utils;
import com.android.settings.dashboard.CategoryManager;
import com.android.settingslib.core.lifecycle.HideNonSystemOverlayMixin;
import com.android.settingslib.drawer.Tile;
import com.android.settingslib.transition.SettingsTransitionHelper;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.resources.TextAppearanceConfig;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.R$style;
import com.google.android.setupdesign.util.ThemeHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SettingsBaseActivity extends FragmentActivity {
    /* access modifiers changed from: private */
    public static ArraySet<ComponentName> sTileDenylist = new ArraySet<>();
    private int mCategoriesUpdateTaskCount;
    private final List<CategoryListener> mCategoryListeners = new ArrayList();
    protected CollapsingToolbarLayout mCollapsingToolbarLayout;
    private final PackageReceiver mPackageReceiver = new PackageReceiver();

    public interface CategoryListener {
        void onCategoriesChanged(Set<String> set);
    }

    /* access modifiers changed from: protected */
    public boolean isToolbarEnabled() {
        return true;
    }

    static /* synthetic */ int access$108(SettingsBaseActivity settingsBaseActivity) {
        int i = settingsBaseActivity.mCategoriesUpdateTaskCount;
        settingsBaseActivity.mCategoriesUpdateTaskCount = i + 1;
        return i;
    }

    static /* synthetic */ int access$110(SettingsBaseActivity settingsBaseActivity) {
        int i = settingsBaseActivity.mCategoriesUpdateTaskCount;
        settingsBaseActivity.mCategoriesUpdateTaskCount = i - 1;
        return i;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        if (Utils.isPageTransitionEnabled(this)) {
            getWindow().requestFeature(13);
            SettingsTransitionHelper.applyForwardTransition(this);
            SettingsTransitionHelper.applyBackwardTransition(this);
        }
        super.onCreate(bundle);
        if (isLockTaskModePinned() && !isSettingsRunOnTop()) {
            Log.w("SettingsBaseActivity", "Devices lock task mode pinned.");
            finish();
        }
        System.currentTimeMillis();
        getLifecycle().addObserver(new HideNonSystemOverlayMixin(this));
        TextAppearanceConfig.setShouldLoadFontSynchronously(true);
        if (!getTheme().obtainStyledAttributes(R.styleable.Theme).getBoolean(38, false)) {
            requestWindowFeature(1);
        }
        boolean isAnySetupWizard = WizardManagerHelper.isAnySetupWizard(getIntent());
        if (isAnySetupWizard && (this instanceof SubSettings)) {
            setTheme(ThemeHelper.isSetupWizardDayNightEnabled(this) ? 2131952098 : R$style.SudThemeGlifV3_Light);
        }
        if (!FeatureFlagUtils.isEnabled(this, "settings_silky_home") || !isToolbarEnabled() || isAnySetupWizard) {
            super.setContentView((int) com.android.settings.R.layout.settings_base_layout);
        } else {
            super.setContentView((int) com.android.settings.R.layout.collapsing_toolbar_base_layout);
            this.mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(com.android.settings.R.id.collapsing_toolbar);
        }
        Toolbar toolbar = (Toolbar) findViewById(com.android.settings.R.id.action_bar);
        if (!isToolbarEnabled() || isAnySetupWizard) {
            toolbar.setVisibility(8);
        } else {
            setActionBar(toolbar);
        }
    }

    public boolean onNavigateUp() {
        if (super.onNavigateUp()) {
            return true;
        }
        finish();
        return true;
    }

    public void startActivity(Intent intent) {
        if (!Utils.isPageTransitionEnabled(this)) {
            super.startActivity(intent);
        } else {
            super.startActivity(intent, getActivityOptionsBundle());
        }
    }

    public void startActivity(Intent intent, Bundle bundle) {
        if (!Utils.isPageTransitionEnabled(this)) {
            super.startActivity(intent, bundle);
        } else if (bundle != null) {
            super.startActivity(intent, getMergedBundleForTransition(bundle));
        } else {
            super.startActivity(intent, getActivityOptionsBundle());
        }
    }

    public void startActivityForResult(Intent intent, int i) {
        if (!Utils.isPageTransitionEnabled(this) || i == -1) {
            super.startActivityForResult(intent, i);
        } else {
            super.startActivityForResult(intent, i, getActivityOptionsBundle());
        }
    }

    public void startActivityForResult(Intent intent, int i, Bundle bundle) {
        if (!Utils.isPageTransitionEnabled(this) || i == -1) {
            super.startActivityForResult(intent, i, bundle);
        } else if (bundle != null) {
            super.startActivityForResult(intent, i, getMergedBundleForTransition(bundle));
        } else {
            super.startActivityForResult(intent, i, getActivityOptionsBundle());
        }
    }

    public void startActivityForResultAsUser(Intent intent, int i, UserHandle userHandle) {
        if (!Utils.isPageTransitionEnabled(this) || i == -1) {
            super.startActivityForResultAsUser(intent, i, userHandle);
        } else {
            super.startActivityForResultAsUser(intent, i, getActivityOptionsBundle(), userHandle);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addDataScheme("package");
        registerReceiver(this.mPackageReceiver, intentFilter);
        updateCategories();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        unregisterReceiver(this.mPackageReceiver);
        super.onPause();
    }

    public void addCategoryListener(CategoryListener categoryListener) {
        this.mCategoryListeners.add(categoryListener);
    }

    public void remCategoryListener(CategoryListener categoryListener) {
        this.mCategoryListeners.remove(categoryListener);
    }

    public void setContentView(int i) {
        ViewGroup viewGroup = (ViewGroup) findViewById(com.android.settings.R.id.content_frame);
        if (viewGroup != null) {
            viewGroup.removeAllViews();
        }
        LayoutInflater.from(this).inflate(i, viewGroup);
    }

    public void setContentView(View view) {
        ((ViewGroup) findViewById(com.android.settings.R.id.content_frame)).addView(view);
    }

    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        ((ViewGroup) findViewById(com.android.settings.R.id.content_frame)).addView(view, layoutParams);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        onBackPressed();
        return true;
    }

    public void setTitle(CharSequence charSequence) {
        CollapsingToolbarLayout collapsingToolbarLayout = this.mCollapsingToolbarLayout;
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setTitle(charSequence);
        } else {
            super.setTitle(charSequence);
        }
    }

    public void setTitle(int i) {
        CollapsingToolbarLayout collapsingToolbarLayout = this.mCollapsingToolbarLayout;
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setTitle(getText(i));
        } else {
            super.setTitle(i);
        }
    }

    /* access modifiers changed from: private */
    public void onCategoriesChanged(Set<String> set) {
        int size = this.mCategoryListeners.size();
        for (int i = 0; i < size; i++) {
            this.mCategoryListeners.get(i).onCategoriesChanged(set);
        }
    }

    private boolean isLockTaskModePinned() {
        return ((ActivityManager) getApplicationContext().getSystemService(ActivityManager.class)).getLockTaskModeState() == 2;
    }

    private boolean isSettingsRunOnTop() {
        return TextUtils.equals(getPackageName(), ((ActivityManager) getApplicationContext().getSystemService(ActivityManager.class)).getRunningTasks(1).get(0).baseActivity.getPackageName());
    }

    public boolean setTileEnabled(ComponentName componentName, boolean z) {
        PackageManager packageManager = getPackageManager();
        int componentEnabledSetting = packageManager.getComponentEnabledSetting(componentName);
        if ((componentEnabledSetting == 1) == z && componentEnabledSetting != 0) {
            return false;
        }
        if (z) {
            sTileDenylist.remove(componentName);
        } else {
            sTileDenylist.add(componentName);
        }
        packageManager.setComponentEnabledSetting(componentName, z ? 1 : 2, 1);
        return true;
    }

    public void updateCategories() {
        updateCategories(false);
    }

    /* access modifiers changed from: private */
    public void updateCategories(boolean z) {
        if (this.mCategoriesUpdateTaskCount < 2) {
            new CategoriesUpdateTask().execute(new Boolean[]{Boolean.valueOf(z)});
        }
    }

    private Bundle getActivityOptionsBundle() {
        return ActivityOptions.makeSceneTransitionAnimation(this, (Toolbar) findViewById(com.android.settings.R.id.action_bar), "shared_element_view").toBundle();
    }

    private Bundle getMergedBundleForTransition(Bundle bundle) {
        Bundle bundle2 = new Bundle();
        bundle2.putAll(bundle);
        Bundle activityOptionsBundle = getActivityOptionsBundle();
        if (activityOptionsBundle != null) {
            bundle2.putAll(activityOptionsBundle);
        }
        return bundle2;
    }

    private class CategoriesUpdateTask extends AsyncTask<Boolean, Void, Set<String>> {
        private final CategoryManager mCategoryManager;
        private final Context mContext;
        private Map<ComponentName, Tile> mPreviousTileMap;

        public CategoriesUpdateTask() {
            SettingsBaseActivity.access$108(SettingsBaseActivity.this);
            this.mContext = SettingsBaseActivity.this;
            this.mCategoryManager = CategoryManager.get(SettingsBaseActivity.this);
        }

        /* access modifiers changed from: protected */
        public Set<String> doInBackground(Boolean... boolArr) {
            this.mPreviousTileMap = this.mCategoryManager.getTileByComponentMap();
            this.mCategoryManager.reloadAllCategories(this.mContext);
            this.mCategoryManager.updateCategoryFromDenylist(SettingsBaseActivity.sTileDenylist);
            return getChangedCategories(boolArr[0].booleanValue());
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Set<String> set) {
            if (set == null || !set.isEmpty()) {
                SettingsBaseActivity.this.onCategoriesChanged(set);
            }
            SettingsBaseActivity.access$110(SettingsBaseActivity.this);
        }

        private Set<String> getChangedCategories(boolean z) {
            if (!z) {
                return null;
            }
            ArraySet arraySet = new ArraySet();
            Map<ComponentName, Tile> tileByComponentMap = this.mCategoryManager.getTileByComponentMap();
            tileByComponentMap.forEach(new C0802xe431ff7a(this, arraySet));
            ArraySet arraySet2 = new ArraySet(this.mPreviousTileMap.keySet());
            arraySet2.removeAll(tileByComponentMap.keySet());
            arraySet2.forEach(new C0803xe431ff7b(this, arraySet));
            return arraySet;
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$getChangedCategories$0(Set set, ComponentName componentName, Tile tile) {
            Tile tile2 = this.mPreviousTileMap.get(componentName);
            if (tile2 == null) {
                Log.i("SettingsBaseActivity", "Tile added: " + componentName.flattenToShortString());
                set.add(tile.getCategory());
            } else if (!TextUtils.equals(tile.getTitle(this.mContext), tile2.getTitle(this.mContext)) || !TextUtils.equals(tile.getSummary(this.mContext), tile2.getSummary(this.mContext))) {
                Log.i("SettingsBaseActivity", "Tile changed: " + componentName.flattenToShortString());
                set.add(tile.getCategory());
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$getChangedCategories$1(Set set, ComponentName componentName) {
            Log.i("SettingsBaseActivity", "Tile removed: " + componentName.flattenToShortString());
            set.add(this.mPreviousTileMap.get(componentName).getCategory());
        }
    }

    private class PackageReceiver extends BroadcastReceiver {
        private PackageReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            SettingsBaseActivity.this.updateCategories(true);
        }
    }
}
