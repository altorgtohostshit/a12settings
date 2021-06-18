package com.android.settings.nfc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class PaymentSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.nfc_payment_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            if (((UserManager) context.getSystemService(UserManager.class)).getUserInfo(UserHandle.myUserId()).isGuest()) {
                return false;
            }
            return context.getPackageManager().hasSystemFeature("android.hardware.nfc");
        }
    };
    private PaymentBackend mPaymentBackend;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "PaymentSettings";
    }

    public int getMetricsCategory() {
        return 70;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.nfc_payment_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mPaymentBackend = new PaymentBackend(getActivity());
        setHasOptionsMenu(true);
        ((NfcPaymentPreferenceController) use(NfcPaymentPreferenceController.class)).setPaymentBackend(this.mPaymentBackend);
        ((NfcForegroundPreferenceController) use(NfcForegroundPreferenceController.class)).setPaymentBackend(this.mPaymentBackend);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (isShowEmptyImage(getPreferenceScreen())) {
            ((ViewGroup) view.findViewById(16908351)).addView(getActivity().getLayoutInflater().inflate(R.layout.nfc_payment_empty, (ViewGroup) null, false));
        }
    }

    public void onResume() {
        super.onResume();
        this.mPaymentBackend.onResume();
    }

    public void onPause() {
        super.onPause();
        this.mPaymentBackend.onPause();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        MenuItem add = menu.add(R.string.nfc_payment_how_it_works);
        add.setIntent(new Intent(getActivity(), HowItWorks.class));
        add.setShowAsActionFlags(0);
    }

    /* access modifiers changed from: package-private */
    public boolean isShowEmptyImage(PreferenceScreen preferenceScreen) {
        for (int i = 0; i < preferenceScreen.getPreferenceCount(); i++) {
            if (preferenceScreen.getPreference(i).isVisible()) {
                return false;
            }
        }
        return true;
    }
}
