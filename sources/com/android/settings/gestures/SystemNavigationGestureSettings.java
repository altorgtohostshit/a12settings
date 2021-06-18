package com.android.settings.gestures;

import android.content.Context;
import android.content.Intent;
import android.content.om.IOverlayManager;
import android.content.om.OverlayInfo;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.util.FeatureFlagUtils;
import android.view.View;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.InstrumentedPreferenceFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.actionbar.SearchMenuController;
import com.android.settings.support.actionbar.HelpMenuController;
import com.android.settings.support.actionbar.HelpResourceProvider;
import com.android.settings.utils.CandidateInfoExtra;
import com.android.settings.widget.RadioButtonPickerFragment;
import com.android.settings.widget.VideoPreference;
import com.android.settingslib.core.lifecycle.ObservablePreferenceFragment;
import com.android.settingslib.widget.CandidateInfo;
import com.android.settingslib.widget.RadioButtonPreference;
import java.util.ArrayList;
import java.util.List;

public class SystemNavigationGestureSettings extends RadioButtonPickerFragment implements HelpResourceProvider {
    static final String KEY_SYSTEM_NAV_2BUTTONS = "system_nav_2buttons";
    static final String KEY_SYSTEM_NAV_3BUTTONS = "system_nav_3buttons";
    static final String KEY_SYSTEM_NAV_GESTURAL = "system_nav_gestural";
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.system_navigation_gesture_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return SystemNavigationPreferenceController.isGestureAvailable(context);
        }
    };
    private IOverlayManager mOverlayManager;
    private VideoPreference mVideoPreference;

    public int getHelpResource() {
        return R.string.help_uri_default;
    }

    public int getMetricsCategory() {
        return 1374;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.system_navigation_gesture_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (!FeatureFlagUtils.isEnabled(getContext(), "settings_silky_home")) {
            SearchMenuController.init((InstrumentedPreferenceFragment) this);
            HelpMenuController.init((ObservablePreferenceFragment) this);
        }
        FeatureFactory.getFactory(context).getSuggestionFeatureProvider(context).getSharedPrefs(context).edit().putBoolean("pref_system_navigation_suggestion_complete", true).apply();
        this.mOverlayManager = IOverlayManager.Stub.asInterface(ServiceManager.getService("overlay"));
        VideoPreference videoPreference = new VideoPreference(context);
        this.mVideoPreference = videoPreference;
        setIllustrationVideo(videoPreference, getDefaultKey());
        this.mVideoPreference.setHeight(getResources().getDimension(R.dimen.system_navigation_illustration_height) / getResources().getDisplayMetrics().density);
        migrateOverlaySensitivityToSettings(context, this.mOverlayManager);
    }

    public void updateCandidates() {
        String defaultKey = getDefaultKey();
        String systemDefaultKey = getSystemDefaultKey();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        preferenceScreen.removeAll();
        preferenceScreen.addPreference(this.mVideoPreference);
        List<? extends CandidateInfo> candidates = getCandidates();
        if (candidates != null) {
            for (CandidateInfo candidateInfo : candidates) {
                RadioButtonPreference radioButtonPreference = new RadioButtonPreference(getPrefContext());
                bindPreference(radioButtonPreference, candidateInfo.getKey(), candidateInfo, defaultKey);
                bindPreferenceExtra(radioButtonPreference, candidateInfo.getKey(), candidateInfo, defaultKey, systemDefaultKey);
                preferenceScreen.addPreference(radioButtonPreference);
            }
            mayCheckOnlyRadioButton();
        }
    }

    public void bindPreferenceExtra(RadioButtonPreference radioButtonPreference, String str, CandidateInfo candidateInfo, String str2, String str3) {
        if (candidateInfo instanceof CandidateInfoExtra) {
            radioButtonPreference.setSummary(((CandidateInfoExtra) candidateInfo).loadSummary());
            if (candidateInfo.getKey() == KEY_SYSTEM_NAV_GESTURAL) {
                radioButtonPreference.setExtraWidgetOnClickListener(new SystemNavigationGestureSettings$$ExternalSyntheticLambda0(this));
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindPreferenceExtra$0(View view) {
        startActivity(new Intent("com.android.settings.GESTURE_NAVIGATION_SETTINGS"));
    }

    /* access modifiers changed from: protected */
    public List<? extends CandidateInfo> getCandidates() {
        Context context = getContext();
        ArrayList arrayList = new ArrayList();
        if (SystemNavigationPreferenceController.isOverlayPackageAvailable(context, "com.android.internal.systemui.navbar.gestural")) {
            arrayList.add(new CandidateInfoExtra(context.getText(R.string.edge_to_edge_navigation_title), context.getText(R.string.edge_to_edge_navigation_summary), KEY_SYSTEM_NAV_GESTURAL, true));
        }
        if (SystemNavigationPreferenceController.isOverlayPackageAvailable(context, "com.android.internal.systemui.navbar.twobutton")) {
            arrayList.add(new CandidateInfoExtra(context.getText(R.string.swipe_up_to_switch_apps_title), context.getText(R.string.swipe_up_to_switch_apps_summary), KEY_SYSTEM_NAV_2BUTTONS, true));
        }
        if (SystemNavigationPreferenceController.isOverlayPackageAvailable(context, "com.android.internal.systemui.navbar.threebutton")) {
            arrayList.add(new CandidateInfoExtra(context.getText(R.string.legacy_navigation_title), context.getText(R.string.legacy_navigation_summary), KEY_SYSTEM_NAV_3BUTTONS, true));
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public String getDefaultKey() {
        return getCurrentSystemNavigationMode(getContext());
    }

    /* access modifiers changed from: protected */
    public boolean setDefaultKey(String str) {
        setCurrentSystemNavigationMode(this.mOverlayManager, str);
        setIllustrationVideo(this.mVideoPreference, str);
        return true;
    }

    static void migrateOverlaySensitivityToSettings(Context context, IOverlayManager iOverlayManager) {
        if (SystemNavigationPreferenceController.isGestureNavigationEnabled(context)) {
            OverlayInfo overlayInfo = null;
            try {
                overlayInfo = iOverlayManager.getOverlayInfo("com.android.internal.systemui.navbar.gestural", -2);
            } catch (RemoteException unused) {
            }
            if (overlayInfo != null && !overlayInfo.isEnabled()) {
                setCurrentSystemNavigationMode(iOverlayManager, KEY_SYSTEM_NAV_GESTURAL);
                Settings.Secure.putFloat(context.getContentResolver(), "back_gesture_inset_scale_left", 1.0f);
                Settings.Secure.putFloat(context.getContentResolver(), "back_gesture_inset_scale_right", 1.0f);
            }
        }
    }

    static String getCurrentSystemNavigationMode(Context context) {
        if (SystemNavigationPreferenceController.isGestureNavigationEnabled(context)) {
            return KEY_SYSTEM_NAV_GESTURAL;
        }
        return SystemNavigationPreferenceController.is2ButtonNavigationEnabled(context) ? KEY_SYSTEM_NAV_2BUTTONS : KEY_SYSTEM_NAV_3BUTTONS;
    }

    static void setCurrentSystemNavigationMode(IOverlayManager iOverlayManager, String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1860313413:
                if (str.equals(KEY_SYSTEM_NAV_2BUTTONS)) {
                    c = 0;
                    break;
                }
                break;
            case -1375361165:
                if (str.equals(KEY_SYSTEM_NAV_GESTURAL)) {
                    c = 1;
                    break;
                }
                break;
            case -117503078:
                if (str.equals(KEY_SYSTEM_NAV_3BUTTONS)) {
                    c = 2;
                    break;
                }
                break;
        }
        String str2 = "com.android.internal.systemui.navbar.gestural";
        switch (c) {
            case 0:
                str2 = "com.android.internal.systemui.navbar.twobutton";
                break;
            case 2:
                str2 = "com.android.internal.systemui.navbar.threebutton";
                break;
        }
        try {
            iOverlayManager.setEnabledExclusiveInCategory(str2, -2);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002d, code lost:
        if (r4.equals(KEY_SYSTEM_NAV_2BUTTONS) == false) goto L_0x000f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void setIllustrationVideo(com.android.settings.widget.VideoPreference r3, java.lang.String r4) {
        /*
            r0 = 0
            r3.setVideo(r0, r0)
            r4.hashCode()
            int r1 = r4.hashCode()
            r2 = -1
            switch(r1) {
                case -1860313413: goto L_0x0027;
                case -1375361165: goto L_0x001c;
                case -117503078: goto L_0x0011;
                default: goto L_0x000f;
            }
        L_0x000f:
            r0 = r2
            goto L_0x0030
        L_0x0011:
            java.lang.String r0 = "system_nav_3buttons"
            boolean r4 = r4.equals(r0)
            if (r4 != 0) goto L_0x001a
            goto L_0x000f
        L_0x001a:
            r0 = 2
            goto L_0x0030
        L_0x001c:
            java.lang.String r0 = "system_nav_gestural"
            boolean r4 = r4.equals(r0)
            if (r4 != 0) goto L_0x0025
            goto L_0x000f
        L_0x0025:
            r0 = 1
            goto L_0x0030
        L_0x0027:
            java.lang.String r1 = "system_nav_2buttons"
            boolean r4 = r4.equals(r1)
            if (r4 != 0) goto L_0x0030
            goto L_0x000f
        L_0x0030:
            switch(r0) {
                case 0: goto L_0x0048;
                case 1: goto L_0x003e;
                case 2: goto L_0x0034;
                default: goto L_0x0033;
            }
        L_0x0033:
            goto L_0x0051
        L_0x0034:
            r4 = 2130903078(0x7f030026, float:1.7412964E38)
            r0 = 2130838582(0x7f020436, float:1.728215E38)
            r3.setVideo(r4, r0)
            goto L_0x0051
        L_0x003e:
            r4 = 2130903079(0x7f030027, float:1.7412966E38)
            r0 = 2130838583(0x7f020437, float:1.7282152E38)
            r3.setVideo(r4, r0)
            goto L_0x0051
        L_0x0048:
            r4 = 2130903077(0x7f030025, float:1.7412962E38)
            r0 = 2130838581(0x7f020435, float:1.7282148E38)
            r3.setVideo(r4, r0)
        L_0x0051:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.gestures.SystemNavigationGestureSettings.setIllustrationVideo(com.android.settings.widget.VideoPreference, java.lang.String):void");
    }
}
