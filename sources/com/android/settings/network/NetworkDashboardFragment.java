package com.android.settings.network;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.SearchIndexableResource;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.network.MobilePlanPreferenceController;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.wifi.WifiPrimarySwitchPreferenceController;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NetworkDashboardFragment extends DashboardFragment implements MobilePlanPreferenceController.MobilePlanPreferenceHost {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.network_and_internet) {
        public List<SearchIndexableResource> getXmlResourcesToIndex(Context context, boolean z) {
            if (!Utils.isProviderModelEnabled(context)) {
                return super.getXmlResourcesToIndex(context, z);
            }
            SearchIndexableResource searchIndexableResource = new SearchIndexableResource(context);
            searchIndexableResource.xmlResId = R.xml.network_provider_internet;
            return Arrays.asList(new SearchIndexableResource[]{searchIndexableResource});
        }

        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return NetworkDashboardFragment.buildPreferenceControllers(context, (Lifecycle) null, (MetricsFeatureProvider) null, (Fragment) null, (MobilePlanPreferenceController.MobilePlanPreferenceHost) null);
        }
    };

    public int getDialogMetricsCategory(int i) {
        return 1 == i ? 609 : 0;
    }

    public int getHelpResource() {
        return R.string.help_url_network_dashboard;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "NetworkDashboardFrag";
    }

    public int getMetricsCategory() {
        return 746;
    }

    /* access modifiers changed from: protected */
    public boolean isParalleledControllers() {
        return true;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return Utils.isProviderModelEnabled(getContext()) ? R.xml.network_provider_internet : R.xml.network_and_internet;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (!Utils.isProviderModelEnabled(context)) {
            ((MultiNetworkHeaderController) use(MultiNetworkHeaderController.class)).init(getSettingsLifecycle());
        }
        ((AirplaneModePreferenceController) use(AirplaneModePreferenceController.class)).setFragment(this);
        getSettingsLifecycle().addObserver((LifecycleObserver) use(AllInOneTetherPreferenceController.class));
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        super.onCreatePreferences(bundle, str);
        ((AllInOneTetherPreferenceController) use(AllInOneTetherPreferenceController.class)).initEnabler(getSettingsLifecycle());
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getSettingsLifecycle(), this.mMetricsFeatureProvider, this, this);
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, Lifecycle lifecycle, MetricsFeatureProvider metricsFeatureProvider, Fragment fragment, MobilePlanPreferenceController.MobilePlanPreferenceHost mobilePlanPreferenceHost) {
        WifiPrimarySwitchPreferenceController wifiPrimarySwitchPreferenceController;
        MobilePlanPreferenceController mobilePlanPreferenceController = new MobilePlanPreferenceController(context, mobilePlanPreferenceHost);
        InternetPreferenceController internetPreferenceController = null;
        if (Utils.isProviderModelEnabled(context)) {
            wifiPrimarySwitchPreferenceController = null;
        } else {
            wifiPrimarySwitchPreferenceController = new WifiPrimarySwitchPreferenceController(context, metricsFeatureProvider);
        }
        if (Utils.isProviderModelEnabled(context)) {
            internetPreferenceController = new InternetPreferenceController(context, lifecycle);
        }
        VpnPreferenceController vpnPreferenceController = new VpnPreferenceController(context);
        PrivateDnsPreferenceController privateDnsPreferenceController = new PrivateDnsPreferenceController(context);
        if (lifecycle != null) {
            lifecycle.addObserver(mobilePlanPreferenceController);
            if (wifiPrimarySwitchPreferenceController != null) {
                lifecycle.addObserver(wifiPrimarySwitchPreferenceController);
            }
            lifecycle.addObserver(vpnPreferenceController);
            lifecycle.addObserver(privateDnsPreferenceController);
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new MobileNetworkSummaryController(context, lifecycle));
        arrayList.add(new TetherPreferenceController(context, lifecycle));
        arrayList.add(vpnPreferenceController);
        arrayList.add(new ProxyPreferenceController(context));
        arrayList.add(mobilePlanPreferenceController);
        if (wifiPrimarySwitchPreferenceController != null) {
            arrayList.add(wifiPrimarySwitchPreferenceController);
        }
        if (internetPreferenceController != null) {
            arrayList.add(internetPreferenceController);
        }
        arrayList.add(privateDnsPreferenceController);
        if (Utils.isProviderModelEnabled(context)) {
            arrayList.add(new NetworkProviderCallsSmsController(context, lifecycle));
        }
        return arrayList;
    }

    public void showMobilePlanMessageDialog() {
        showDialog(1);
    }

    public Dialog onCreateDialog(int i) {
        Log.d("NetworkDashboardFrag", "onCreateDialog: dialogId=" + i);
        if (i != 1) {
            return super.onCreateDialog(i);
        }
        MobilePlanPreferenceController mobilePlanPreferenceController = (MobilePlanPreferenceController) use(MobilePlanPreferenceController.class);
        return new AlertDialog.Builder(getActivity()).setMessage((CharSequence) mobilePlanPreferenceController.getMobilePlanDialogMessage()).setCancelable(false).setPositiveButton(17039370, (DialogInterface.OnClickListener) new NetworkDashboardFragment$$ExternalSyntheticLambda0(mobilePlanPreferenceController)).create();
    }
}
