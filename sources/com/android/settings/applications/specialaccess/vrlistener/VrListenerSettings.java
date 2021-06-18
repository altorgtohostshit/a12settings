package com.android.settings.applications.specialaccess.vrlistener;

import android.content.ComponentName;
import com.android.settings.R;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.utils.ManagedServiceSettings;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;

public class VrListenerSettings extends ManagedServiceSettings {
    private static final ManagedServiceSettings.Config CONFIG;
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.vr_listeners_settings);
    private static final String TAG;

    public int getMetricsCategory() {
        return 334;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.vr_listeners_settings;
    }

    static {
        String simpleName = VrListenerSettings.class.getSimpleName();
        TAG = simpleName;
        CONFIG = new ManagedServiceSettings.Config.Builder().setTag(simpleName).setSetting("enabled_vr_listeners").setIntentAction("android.service.vr.VrListenerService").setPermission("android.permission.BIND_VR_LISTENER_SERVICE").setNoun("vr listener").setWarningDialogTitle(R.string.vr_listener_security_warning_title).setWarningDialogSummary(R.string.vr_listener_security_warning_summary).setEmptyText(R.string.no_vr_listeners).build();
    }

    /* access modifiers changed from: protected */
    public ManagedServiceSettings.Config getConfig() {
        return CONFIG;
    }

    /* access modifiers changed from: protected */
    public boolean setEnabled(ComponentName componentName, String str, boolean z) {
        logSpecialPermissionChange(z, componentName.getPackageName());
        return super.setEnabled(componentName, str, z);
    }

    /* access modifiers changed from: package-private */
    public void logSpecialPermissionChange(boolean z, String str) {
        int i = z ? 772 : 773;
        MetricsFeatureProvider metricsFeatureProvider = FeatureFactory.getFactory(getContext()).getMetricsFeatureProvider();
        metricsFeatureProvider.action(metricsFeatureProvider.getAttribution(getActivity()), i, getMetricsCategory(), str, 0);
    }
}
