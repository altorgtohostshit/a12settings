package com.android.settings.wifi.dpp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.android.settings.R;

public class WifiDppChooseSavedWifiNetworkFragment extends WifiDppQrCodeBaseFragment {
    public int getMetricsCategory() {
        return 1595;
    }

    /* access modifiers changed from: protected */
    public boolean isFooterAvailable() {
        return true;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        FragmentManager childFragmentManager = getChildFragmentManager();
        WifiNetworkListFragment wifiNetworkListFragment = new WifiNetworkListFragment();
        Bundle arguments = getArguments();
        if (arguments != null) {
            wifiNetworkListFragment.setArguments(arguments);
        }
        FragmentTransaction beginTransaction = childFragmentManager.beginTransaction();
        beginTransaction.replace(R.id.wifi_network_list_container, wifiNetworkListFragment, "wifi_network_list_fragment");
        beginTransaction.commit();
    }

    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.wifi_dpp_choose_saved_wifi_network_fragment, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        setHeaderTitle(R.string.wifi_dpp_choose_network, new Object[0]);
        this.mSummary.setText(R.string.wifi_dpp_choose_network_to_connect_device);
        this.mLeftButton.setText(getContext(), R.string.cancel);
        this.mLeftButton.setOnClickListener(new WifiDppChooseSavedWifiNetworkFragment$$ExternalSyntheticLambda0(this));
        this.mRightButton.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$0(View view) {
        Intent intent = getActivity().getIntent();
        String action = intent != null ? intent.getAction() : null;
        if ("android.settings.WIFI_DPP_CONFIGURATOR_QR_CODE_SCANNER".equals(action) || "android.settings.WIFI_DPP_CONFIGURATOR_QR_CODE_GENERATOR".equals(action)) {
            getFragmentManager().popBackStack();
        } else {
            getActivity().finish();
        }
    }
}
