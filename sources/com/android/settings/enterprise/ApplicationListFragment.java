package com.android.settings.enterprise;

import android.content.Context;
import com.android.settings.R;
import com.android.settings.applications.ApplicationFeatureProvider;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.enterprise.ApplicationListPreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;

public abstract class ApplicationListFragment extends DashboardFragment implements ApplicationListPreferenceController.ApplicationListBuilder {
    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "EnterprisePrivacySettings";
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.app_list_disclosure_settings;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ApplicationListPreferenceController(context, this, context.getPackageManager(), this));
        return arrayList;
    }

    private static abstract class AdminGrantedPermission extends ApplicationListFragment {
        private final String[] mPermissions;

        public int getMetricsCategory() {
            return 939;
        }

        public AdminGrantedPermission(String[] strArr) {
            this.mPermissions = strArr;
        }

        public void buildApplicationList(Context context, ApplicationFeatureProvider.ListOfAppsCallback listOfAppsCallback) {
            FeatureFactory.getFactory(context).getApplicationFeatureProvider(context).listAppsWithAdminGrantedPermissions(this.mPermissions, listOfAppsCallback);
        }
    }

    public static class AdminGrantedPermissionCamera extends AdminGrantedPermission {
        public /* bridge */ /* synthetic */ void buildApplicationList(Context context, ApplicationFeatureProvider.ListOfAppsCallback listOfAppsCallback) {
            super.buildApplicationList(context, listOfAppsCallback);
        }

        public /* bridge */ /* synthetic */ int getMetricsCategory() {
            return super.getMetricsCategory();
        }

        public AdminGrantedPermissionCamera() {
            super(new String[]{"android.permission.CAMERA"});
        }
    }

    public static class AdminGrantedPermissionLocation extends AdminGrantedPermission {
        public /* bridge */ /* synthetic */ void buildApplicationList(Context context, ApplicationFeatureProvider.ListOfAppsCallback listOfAppsCallback) {
            super.buildApplicationList(context, listOfAppsCallback);
        }

        public /* bridge */ /* synthetic */ int getMetricsCategory() {
            return super.getMetricsCategory();
        }

        public AdminGrantedPermissionLocation() {
            super(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"});
        }
    }

    public static class AdminGrantedPermissionMicrophone extends AdminGrantedPermission {
        public /* bridge */ /* synthetic */ void buildApplicationList(Context context, ApplicationFeatureProvider.ListOfAppsCallback listOfAppsCallback) {
            super.buildApplicationList(context, listOfAppsCallback);
        }

        public /* bridge */ /* synthetic */ int getMetricsCategory() {
            return super.getMetricsCategory();
        }

        public AdminGrantedPermissionMicrophone() {
            super(new String[]{"android.permission.RECORD_AUDIO"});
        }
    }

    public static class EnterpriseInstalledPackages extends ApplicationListFragment {
        public int getMetricsCategory() {
            return 938;
        }

        public void buildApplicationList(Context context, ApplicationFeatureProvider.ListOfAppsCallback listOfAppsCallback) {
            FeatureFactory.getFactory(context).getApplicationFeatureProvider(context).listPolicyInstalledApps(listOfAppsCallback);
        }
    }
}
