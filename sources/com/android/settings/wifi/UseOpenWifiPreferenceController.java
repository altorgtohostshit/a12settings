package com.android.settings.wifi;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.NetworkScoreManager;
import android.net.NetworkScorerAppData;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;

public class UseOpenWifiPreferenceController extends TogglePreferenceController implements PreferenceControllerMixin, LifecycleObserver, OnResume, OnPause {
    private static final String KEY_USE_OPEN_WIFI_AUTOMATICALLY = "use_open_wifi_automatically";
    public static final int REQUEST_CODE_OPEN_WIFI_AUTOMATICALLY = 400;
    private final ContentResolver mContentResolver;
    private boolean mDoFeatureSupportedScorersExist;
    private ComponentName mEnableUseWifiComponentName;
    private Fragment mFragment;
    private final NetworkScoreManager mNetworkScoreManager;
    /* access modifiers changed from: private */
    public Preference mPreference;
    private final SettingObserver mSettingObserver = new SettingObserver();

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

    public UseOpenWifiPreferenceController(Context context) {
        super(context, KEY_USE_OPEN_WIFI_AUTOMATICALLY);
        this.mContentResolver = context.getContentResolver();
        this.mNetworkScoreManager = (NetworkScoreManager) context.getSystemService("network_score");
        updateEnableUseWifiComponentName();
        checkForFeatureSupportedScorers();
    }

    public void setFragment(Fragment fragment) {
        this.mFragment = fragment;
    }

    /* access modifiers changed from: private */
    public void updateEnableUseWifiComponentName() {
        ComponentName componentName;
        NetworkScorerAppData activeScorer = this.mNetworkScoreManager.getActiveScorer();
        if (activeScorer == null) {
            componentName = null;
        } else {
            componentName = activeScorer.getEnableUseOpenWifiActivity();
        }
        this.mEnableUseWifiComponentName = componentName;
    }

    private void checkForFeatureSupportedScorers() {
        if (this.mEnableUseWifiComponentName != null) {
            this.mDoFeatureSupportedScorersExist = true;
            return;
        }
        for (NetworkScorerAppData enableUseOpenWifiActivity : this.mNetworkScoreManager.getAllValidScorers()) {
            if (enableUseOpenWifiActivity.getEnableUseOpenWifiActivity() != null) {
                this.mDoFeatureSupportedScorersExist = true;
                return;
            }
        }
        this.mDoFeatureSupportedScorersExist = false;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public void onResume() {
        this.mSettingObserver.register(this.mContentResolver);
    }

    public void onPause() {
        this.mSettingObserver.unregister(this.mContentResolver);
    }

    public int getAvailabilityStatus() {
        updateEnableUseWifiComponentName();
        checkForFeatureSupportedScorers();
        return this.mDoFeatureSupportedScorersExist ? 0 : 2;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        boolean z = true;
        boolean z2 = this.mNetworkScoreManager.getActiveScorerPackage() != null;
        boolean z3 = this.mEnableUseWifiComponentName != null;
        if (!z2 || !z3) {
            z = false;
        }
        preference.setEnabled(z);
        if (!z2) {
            preference.setSummary((int) R.string.use_open_wifi_automatically_summary_scoring_disabled);
        } else if (!z3) {
            preference.setSummary((int) R.string.use_open_wifi_automatically_summary_scorer_unsupported_disabled);
        } else {
            preference.setSummary((int) R.string.use_open_wifi_automatically_summary);
        }
    }

    public boolean isChecked() {
        String str;
        String string = Settings.Global.getString(this.mContentResolver, "use_open_wifi_package");
        ComponentName componentName = this.mEnableUseWifiComponentName;
        if (componentName == null) {
            str = null;
        } else {
            str = componentName.getPackageName();
        }
        return TextUtils.equals(string, str);
    }

    public boolean setChecked(boolean z) {
        if (!z) {
            Settings.Global.putString(this.mContentResolver, "use_open_wifi_package", "");
            return true;
        } else if (this.mFragment != null) {
            Intent intent = new Intent("android.net.scoring.CUSTOM_ENABLE");
            intent.setComponent(this.mEnableUseWifiComponentName);
            this.mFragment.startActivityForResult(intent, REQUEST_CODE_OPEN_WIFI_AUTOMATICALLY);
            return false;
        } else {
            throw new IllegalStateException("No fragment to start activity");
        }
    }

    public boolean onActivityResult(int i, int i2) {
        if (i != 400) {
            return false;
        }
        if (i2 != -1) {
            return true;
        }
        Settings.Global.putString(this.mContentResolver, "use_open_wifi_package", this.mEnableUseWifiComponentName.getPackageName());
        return true;
    }

    class SettingObserver extends ContentObserver {
        private final Uri NETWORK_RECOMMENDATIONS_ENABLED_URI = Settings.Global.getUriFor("network_recommendations_enabled");

        public SettingObserver() {
            super(new Handler(Looper.getMainLooper()));
        }

        public void register(ContentResolver contentResolver) {
            contentResolver.registerContentObserver(this.NETWORK_RECOMMENDATIONS_ENABLED_URI, false, this);
            onChange(true, this.NETWORK_RECOMMENDATIONS_ENABLED_URI);
        }

        public void unregister(ContentResolver contentResolver) {
            contentResolver.unregisterContentObserver(this);
        }

        public void onChange(boolean z, Uri uri) {
            super.onChange(z, uri);
            if (this.NETWORK_RECOMMENDATIONS_ENABLED_URI.equals(uri)) {
                UseOpenWifiPreferenceController.this.updateEnableUseWifiComponentName();
                UseOpenWifiPreferenceController useOpenWifiPreferenceController = UseOpenWifiPreferenceController.this;
                useOpenWifiPreferenceController.updateState(useOpenWifiPreferenceController.mPreference);
            }
        }
    }
}
