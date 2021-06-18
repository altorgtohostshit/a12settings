package com.android.settings.search.actionbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.InstrumentedFragment;
import com.android.settings.core.InstrumentedPreferenceFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnCreateOptionsMenu;
import com.google.android.setupcompat.util.WizardManagerHelper;

public class SearchMenuController implements LifecycleObserver, OnCreateOptionsMenu {
    private final Fragment mHost;
    private final int mPageId;

    public static void init(InstrumentedPreferenceFragment instrumentedPreferenceFragment) {
        instrumentedPreferenceFragment.getSettingsLifecycle().addObserver(new SearchMenuController(instrumentedPreferenceFragment, instrumentedPreferenceFragment.getMetricsCategory()));
    }

    public static void init(InstrumentedFragment instrumentedFragment) {
        instrumentedFragment.getSettingsLifecycle().addObserver(new SearchMenuController(instrumentedFragment, instrumentedFragment.getMetricsCategory()));
    }

    private SearchMenuController(Fragment fragment, int i) {
        this.mHost = fragment;
        this.mPageId = i;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        FragmentActivity activity = this.mHost.getActivity();
        String string = activity.getString(R.string.config_settingsintelligence_package_name);
        if (WizardManagerHelper.isDeviceProvisioned(activity) && !WizardManagerHelper.isAnySetupWizard(activity.getIntent()) && Utils.isPackageEnabled(activity, string) && menu != null) {
            Bundle arguments = this.mHost.getArguments();
            if ((arguments == null || arguments.getBoolean("need_search_icon_in_action_bar", true)) && menu.findItem(11) == null) {
                MenuItem add = menu.add(0, 11, 0, R.string.search_menu);
                add.setIcon(R.drawable.ic_search_24dp);
                add.setShowAsAction(2);
                add.setOnMenuItemClickListener(new SearchMenuController$$ExternalSyntheticLambda0(this, activity));
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onCreateOptionsMenu$0(Activity activity, MenuItem menuItem) {
        Intent buildSearchIntent = FeatureFactory.getFactory(activity).getSearchFeatureProvider().buildSearchIntent(activity, this.mPageId);
        if (activity.getPackageManager().queryIntentActivities(buildSearchIntent, 65536).isEmpty()) {
            return true;
        }
        FeatureFactory.getFactory(activity).getMetricsFeatureProvider().action((Context) activity, 226, (Pair<Integer, Object>[]) new Pair[0]);
        this.mHost.startActivityForResult(buildSearchIntent, 501);
        return true;
    }
}
