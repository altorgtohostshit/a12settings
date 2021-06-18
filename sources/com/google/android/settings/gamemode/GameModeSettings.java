package com.google.android.settings.gamemode;

import android.content.Context;
import android.os.Bundle;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class GameModeSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.game_mode_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return false;
        }
    };

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "GameModeSettings";
    }

    public int getMetricsCategory() {
        return 1860;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.game_mode_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        String string = arguments != null ? arguments.getString("package") : null;
        ((GameModeController) use(GameModeController.class)).init(string);
        ((GameModeHeaderController) use(GameModeHeaderController.class)).init(this, string);
    }
}
