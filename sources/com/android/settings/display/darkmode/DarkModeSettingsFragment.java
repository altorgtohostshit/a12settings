package com.android.settings.display.darkmode;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class DarkModeSettingsFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.dark_mode_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return !((PowerManager) context.getSystemService(PowerManager.class)).isPowerSaveMode();
        }
    };
    private Runnable mCallback = new DarkModeSettingsFragment$$ExternalSyntheticLambda0(this);
    private DarkModeObserver mContentObserver;
    private DarkModeCustomPreferenceController mCustomEndController;
    private DarkModeCustomPreferenceController mCustomStartController;

    public int getDialogMetricsCategory(int i) {
        if (i != 0) {
            return i != 1 ? 0 : 1826;
        }
        return 1825;
    }

    public int getHelpResource() {
        return R.string.help_url_dark_theme;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "DarkModeSettingsFrag";
    }

    public int getMetricsCategory() {
        return 1698;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.dark_mode_settings;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        updatePreferenceStates();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContentObserver = new DarkModeObserver(getContext());
    }

    public void onStart() {
        super.onStart();
        this.mContentObserver.subscribe(this.mCallback);
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList(2);
        this.mCustomStartController = new DarkModeCustomPreferenceController(getContext(), "dark_theme_start_time", this);
        this.mCustomEndController = new DarkModeCustomPreferenceController(getContext(), "dark_theme_end_time", this);
        arrayList.add(this.mCustomStartController);
        arrayList.add(this.mCustomEndController);
        return arrayList;
    }

    public void onStop() {
        super.onStop();
        this.mContentObserver.unsubscribe();
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        if ("dark_theme_end_time".equals(preference.getKey())) {
            showDialog(1);
            return true;
        } else if (!"dark_theme_start_time".equals(preference.getKey())) {
            return super.onPreferenceTreeClick(preference);
        } else {
            showDialog(0);
            return true;
        }
    }

    public void refresh() {
        updatePreferenceStates();
    }

    public Dialog onCreateDialog(int i) {
        if (i != 0 && i != 1) {
            return super.onCreateDialog(i);
        }
        if (i == 0) {
            return this.mCustomStartController.getDialog();
        }
        return this.mCustomEndController.getDialog();
    }
}
