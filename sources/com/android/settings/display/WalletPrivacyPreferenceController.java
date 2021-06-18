package com.android.settings.display;

import android.content.Context;
import android.content.IntentFilter;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;

public class WalletPrivacyPreferenceController extends TogglePreferenceController {
    private static final String SETTING_KEY = "lockscreen_show_wallet";
    private final QuickAccessWalletClient mClient = initWalletClient();

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

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public WalletPrivacyPreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), SETTING_KEY, 0) != 0;
    }

    public boolean setChecked(boolean z) {
        return Settings.Secure.putInt(this.mContext.getContentResolver(), SETTING_KEY, z ? 1 : 0);
    }

    public CharSequence getSummary() {
        return this.mContext.getText(isSecure() ? R.string.lockscreen_privacy_wallet_summary : R.string.lockscreen_privacy_not_secure);
    }

    public int getAvailabilityStatus() {
        return (!isEnabled() || !isSecure()) ? 5 : 0;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        preference.setEnabled(getAvailabilityStatus() != 5);
        refreshSummary(preference);
    }

    private boolean isEnabled() {
        return this.mClient.isWalletServiceAvailable();
    }

    private boolean isSecure() {
        return FeatureFactory.getFactory(this.mContext).getSecurityFeatureProvider().getLockPatternUtils(this.mContext).isSecure(UserHandle.myUserId());
    }

    /* access modifiers changed from: package-private */
    public QuickAccessWalletClient initWalletClient() {
        return QuickAccessWalletClient.create(this.mContext);
    }
}
