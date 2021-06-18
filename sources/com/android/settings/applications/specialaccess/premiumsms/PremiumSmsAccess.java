package com.android.settings.applications.specialaccess.premiumsms;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import androidx.preference.DropDownPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.applications.AppStateBaseBridge;
import com.android.settings.applications.AppStateSmsPremBridge;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.widget.EmptyTextSettings;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.widget.FooterPreference;
import java.util.ArrayList;

public class PremiumSmsAccess extends EmptyTextSettings implements AppStateBaseBridge.Callback, ApplicationsState.Callbacks, Preference.OnPreferenceChangeListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.premium_sms_settings);
    /* access modifiers changed from: private */
    public ApplicationsState mApplicationsState;
    private ApplicationsState.Session mSession;
    private AppStateSmsPremBridge mSmsBackend;

    public int getMetricsCategory() {
        return 388;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.premium_sms_settings;
    }

    public void onAllSizesComputed() {
    }

    public void onLauncherInfoChanged() {
    }

    public void onLoadEntriesCompleted() {
    }

    public void onPackageIconChanged() {
    }

    public void onPackageListChanged() {
    }

    public void onPackageSizeChanged(String str) {
    }

    public void onRunningStateChanged(boolean z) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ApplicationsState instance = ApplicationsState.getInstance((Application) getContext().getApplicationContext());
        this.mApplicationsState = instance;
        this.mSession = instance.newSession(this, getSettingsLifecycle());
        this.mSmsBackend = new AppStateSmsPremBridge(getContext(), this.mApplicationsState, this);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        setLoading(true, false);
    }

    public void onResume() {
        super.onResume();
        this.mSmsBackend.resume();
    }

    public void onPause() {
        this.mSmsBackend.pause();
        super.onPause();
    }

    public void onDestroy() {
        this.mSmsBackend.release();
        super.onDestroy();
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        PremiumSmsPreference premiumSmsPreference = (PremiumSmsPreference) preference;
        int parseInt = Integer.parseInt((String) obj);
        logSpecialPermissionChange(parseInt, premiumSmsPreference.mAppEntry.info.packageName);
        this.mSmsBackend.setSmsState(premiumSmsPreference.mAppEntry.info.packageName, parseInt);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void logSpecialPermissionChange(int i, String str) {
        int i2 = i != 1 ? i != 2 ? i != 3 ? 0 : 780 : 779 : 778;
        if (i2 != 0) {
            MetricsFeatureProvider metricsFeatureProvider = FeatureFactory.getFactory(getContext()).getMetricsFeatureProvider();
            metricsFeatureProvider.action(metricsFeatureProvider.getAttribution(getActivity()), i2, getMetricsCategory(), str, i);
        }
    }

    private void updatePrefs(ArrayList<ApplicationsState.AppEntry> arrayList) {
        if (arrayList != null) {
            setEmptyText(R.string.premium_sms_none);
            setLoading(false, true);
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            preferenceScreen.removeAll();
            preferenceScreen.setOrderingAsAdded(true);
            for (int i = 0; i < arrayList.size(); i++) {
                PremiumSmsPreference premiumSmsPreference = new PremiumSmsPreference(arrayList.get(i), getPrefContext());
                premiumSmsPreference.setOnPreferenceChangeListener(this);
                preferenceScreen.addPreference(premiumSmsPreference);
            }
            if (arrayList.size() != 0) {
                FooterPreference footerPreference = new FooterPreference(getPrefContext());
                footerPreference.setTitle((int) R.string.premium_sms_warning);
                preferenceScreen.addPreference(footerPreference);
            }
        }
    }

    private void update() {
        updatePrefs(this.mSession.rebuild(AppStateSmsPremBridge.FILTER_APP_PREMIUM_SMS, ApplicationsState.ALPHA_COMPARATOR));
    }

    public void onExtraInfoUpdated() {
        update();
    }

    public void onRebuildComplete(ArrayList<ApplicationsState.AppEntry> arrayList) {
        updatePrefs(arrayList);
    }

    private class PremiumSmsPreference extends DropDownPreference {
        /* access modifiers changed from: private */
        public final ApplicationsState.AppEntry mAppEntry;

        public PremiumSmsPreference(ApplicationsState.AppEntry appEntry, Context context) {
            super(context);
            this.mAppEntry = appEntry;
            appEntry.ensureLabel(context);
            setTitle((CharSequence) appEntry.label);
            Drawable drawable = appEntry.icon;
            if (drawable != null) {
                setIcon(drawable);
            }
            setEntries((int) R.array.security_settings_premium_sms_values);
            setEntryValues(new CharSequence[]{String.valueOf(1), String.valueOf(2), String.valueOf(3)});
            setValue(String.valueOf(getCurrentValue()));
            setSummary("%s");
        }

        private int getCurrentValue() {
            Object obj = this.mAppEntry.extraInfo;
            if (obj instanceof AppStateSmsPremBridge.SmsState) {
                return ((AppStateSmsPremBridge.SmsState) obj).smsState;
            }
            return 0;
        }

        public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
            if (getIcon() == null) {
                preferenceViewHolder.itemView.post(new Runnable() {
                    public void run() {
                        PremiumSmsAccess.this.mApplicationsState.ensureIcon(PremiumSmsPreference.this.mAppEntry);
                        PremiumSmsPreference premiumSmsPreference = PremiumSmsPreference.this;
                        premiumSmsPreference.setIcon(premiumSmsPreference.mAppEntry.icon);
                    }
                });
            }
            super.onBindViewHolder(preferenceViewHolder);
        }
    }
}
