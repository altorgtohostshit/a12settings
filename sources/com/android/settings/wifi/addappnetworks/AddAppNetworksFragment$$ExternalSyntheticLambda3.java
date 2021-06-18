package com.android.settings.wifi.addappnetworks;

import android.text.TextUtils;
import com.android.settings.wifi.addappnetworks.AddAppNetworksFragment;
import com.android.wifitrackerlib.WifiEntry;
import java.util.function.Predicate;

public final /* synthetic */ class AddAppNetworksFragment$$ExternalSyntheticLambda3 implements Predicate {
    public final /* synthetic */ AddAppNetworksFragment.UiConfigurationItem f$0;

    public /* synthetic */ AddAppNetworksFragment$$ExternalSyntheticLambda3(AddAppNetworksFragment.UiConfigurationItem uiConfigurationItem) {
        this.f$0 = uiConfigurationItem;
    }

    public final boolean test(Object obj) {
        return TextUtils.equals(this.f$0.mWifiNetworkSuggestion.getSsid(), ((WifiEntry) obj).getSsid());
    }
}
