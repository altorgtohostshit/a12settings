package com.google.android.settings.aware;

import android.content.Context;
import android.os.Bundle;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.search.BaseSearchIndexProvider;

public class AwareSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.aware_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return FeatureFactory.getFactory(context).getAwareFeatureProvider().isSupported(context);
        }
    };

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "AwareSettings";
    }

    public int getMetricsCategory() {
        return 1632;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.aware_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((AwarePreferenceController) use(AwarePreferenceController.class)).init(this);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments != null && arguments.getBoolean("show_aware_dialog_enabled", false)) {
            AwareEnabledDialogFragment.show(this, Boolean.TRUE);
        }
    }
}
