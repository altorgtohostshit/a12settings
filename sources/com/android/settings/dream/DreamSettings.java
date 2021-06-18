package com.android.settings.dream;

import android.content.Context;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.dream.DreamBackend;
import java.util.ArrayList;
import java.util.List;

public class DreamSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.dream_fragment_overview) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return DreamSettings.buildPreferenceControllers(context);
        }
    };

    static int getDreamSettingDescriptionResId(int i) {
        return i != 0 ? i != 1 ? i != 2 ? R.string.screensaver_settings_summary_never : R.string.screensaver_settings_summary_either_long : R.string.screensaver_settings_summary_dock : R.string.screensaver_settings_summary_sleep;
    }

    static String getKeyFromSetting(int i) {
        return i != 0 ? i != 1 ? i != 2 ? "never" : "either_charging_or_docked" : "while_docked_only" : "while_charging_only";
    }

    public int getHelpResource() {
        return R.string.help_url_screen_saver;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "DreamSettings";
    }

    public int getMetricsCategory() {
        return 47;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.dream_fragment_overview;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static int getSettingFromPrefKey(java.lang.String r5) {
        /*
            int r0 = r5.hashCode()
            r1 = 3
            r2 = 0
            r3 = 2
            r4 = 1
            switch(r0) {
                case -1592701525: goto L_0x002a;
                case -294641318: goto L_0x0020;
                case 104712844: goto L_0x0016;
                case 1019349036: goto L_0x000c;
                default: goto L_0x000b;
            }
        L_0x000b:
            goto L_0x0034
        L_0x000c:
            java.lang.String r0 = "while_charging_only"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0034
            r5 = r2
            goto L_0x0035
        L_0x0016:
            java.lang.String r0 = "never"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0034
            r5 = r1
            goto L_0x0035
        L_0x0020:
            java.lang.String r0 = "either_charging_or_docked"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0034
            r5 = r3
            goto L_0x0035
        L_0x002a:
            java.lang.String r0 = "while_docked_only"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0034
            r5 = r4
            goto L_0x0035
        L_0x0034:
            r5 = -1
        L_0x0035:
            if (r5 == 0) goto L_0x003e
            if (r5 == r4) goto L_0x003d
            if (r5 == r3) goto L_0x003c
            return r1
        L_0x003c:
            return r3
        L_0x003d:
            return r4
        L_0x003e:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.dream.DreamSettings.getSettingFromPrefKey(java.lang.String):int");
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context);
    }

    public static CharSequence getSummaryTextWithDreamName(Context context) {
        return getSummaryTextFromBackend(DreamBackend.getInstance(context), context);
    }

    static CharSequence getSummaryTextFromBackend(DreamBackend dreamBackend, Context context) {
        if (!dreamBackend.isEnabled()) {
            return context.getString(R.string.screensaver_settings_summary_off);
        }
        return dreamBackend.getActiveDreamName();
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new WhenToDreamPreferenceController(context));
        return arrayList;
    }
}
