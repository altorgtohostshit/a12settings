package com.android.settings.applications.specialaccess;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.os.UserManager;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;

public class DefaultPaymentSettingsPreferenceController extends BasePreferenceController implements LifecycleObserver, OnResume, OnPause {
    private final NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this.mContext);
    private final PackageManager mPackageManager;
    private PaymentSettingsEnabler mPaymentSettingsEnabler;
    private final UserManager mUserManager;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public DefaultPaymentSettingsPreferenceController(Context context, String str) {
        super(context, str);
        this.mPackageManager = context.getPackageManager();
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        if (!isAvailable()) {
            this.mPaymentSettingsEnabler = null;
            return;
        }
        this.mPaymentSettingsEnabler = new PaymentSettingsEnabler(this.mContext, preferenceScreen.findPreference(getPreferenceKey()));
    }

    public void onResume() {
        PaymentSettingsEnabler paymentSettingsEnabler = this.mPaymentSettingsEnabler;
        if (paymentSettingsEnabler != null) {
            paymentSettingsEnabler.resume();
        }
    }

    public void onPause() {
        PaymentSettingsEnabler paymentSettingsEnabler = this.mPaymentSettingsEnabler;
        if (paymentSettingsEnabler != null) {
            paymentSettingsEnabler.pause();
        }
    }

    public int getAvailabilityStatus() {
        if (!this.mPackageManager.hasSystemFeature("android.hardware.nfc") || !this.mPackageManager.hasSystemFeature("android.hardware.nfc.hce")) {
            return 3;
        }
        if (!this.mUserManager.isAdminUser()) {
            return 4;
        }
        NfcAdapter nfcAdapter = this.mNfcAdapter;
        if (nfcAdapter == null) {
            return 2;
        }
        return !nfcAdapter.isEnabled() ? 5 : 0;
    }
}
