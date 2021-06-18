package com.android.settings.security;

import android.content.Context;
import android.os.UserManager;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.List;

public class InstallCertificateFromStorage extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.install_certificate_from_storage) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return InstallCertificateFromStorage.buildPreferenceControllers(context, (Lifecycle) null);
        }

        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return ((UserManager) context.getSystemService("user")).isAdminUser();
        }
    };

    public int getHelpResource() {
        return R.string.help_url_install_certificate;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "InstallCertificateFromStorage";
    }

    public int getMetricsCategory() {
        return 1803;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.install_certificate_from_storage;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getSettingsLifecycle());
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, Lifecycle lifecycle) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new InstallCaCertificatePreferenceController(context));
        arrayList.add(new InstallUserCertificatePreferenceController(context));
        arrayList.add(new InstallWifiCertificatePreferenceController(context));
        return arrayList;
    }
}
