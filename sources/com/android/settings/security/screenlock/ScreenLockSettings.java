package com.android.settings.security.screenlock;

import android.content.Context;
import android.os.UserHandle;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.security.OwnerInfoPreferenceController;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public class ScreenLockSettings extends DashboardFragment implements OwnerInfoPreferenceController.OwnerInfoCallback {
    private static final int MY_USER_ID = UserHandle.myUserId();
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.screen_lock_settings) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return ScreenLockSettings.buildPreferenceControllers(context, (DashboardFragment) null, new LockPatternUtils(context));
        }
    };
    private LockPatternUtils mLockPatternUtils;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "ScreenLockSettings";
    }

    public int getMetricsCategory() {
        return 1265;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.screen_lock_settings;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        LockPatternUtils lockPatternUtils = new LockPatternUtils(context);
        this.mLockPatternUtils = lockPatternUtils;
        return buildPreferenceControllers(context, this, lockPatternUtils);
    }

    public void onOwnerInfoUpdated() {
        ((OwnerInfoPreferenceController) use(OwnerInfoPreferenceController.class)).updateSummary();
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, DashboardFragment dashboardFragment, LockPatternUtils lockPatternUtils) {
        ArrayList arrayList = new ArrayList();
        int i = MY_USER_ID;
        arrayList.add(new PatternVisiblePreferenceController(context, i, lockPatternUtils));
        arrayList.add(new PowerButtonInstantLockPreferenceController(context, i, lockPatternUtils));
        arrayList.add(new LockAfterTimeoutPreferenceController(context, i, lockPatternUtils));
        arrayList.add(new OwnerInfoPreferenceController(context, dashboardFragment));
        return arrayList;
    }
}
