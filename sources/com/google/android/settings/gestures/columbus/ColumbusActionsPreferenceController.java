package com.google.android.settings.gestures.columbus;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R;
import com.android.settings.SubSettings;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.widget.RadioButtonPreference;
import com.google.android.settings.gestures.columbus.ColumbusRadioButtonPreference;
import java.util.HashMap;
import java.util.Map;

public class ColumbusActionsPreferenceController extends BasePreferenceController implements RadioButtonPreference.OnClickListener, LifecycleObserver, OnStart, OnStop {
    @VisibleForTesting
    static final int[] ACTION_METRICS = {1743, 1742, 1744, 1745, 1746, 1756};
    static final ColumbusRadioButtonPreference.ContextualSummaryProvider[] ACTION_SUMMARIES = {null, null, null, null, null, ColumbusActionsPreferenceController$$ExternalSyntheticLambda1.INSTANCE};
    @VisibleForTesting
    static final int[] ACTION_TITLE_RES_IDS = {R.string.columbus_setting_action_screenshot_title, R.string.columbus_setting_action_assistant_title, R.string.columbus_setting_action_play_pause_title, R.string.columbus_setting_action_overview_title, R.string.columbus_setting_action_notification_title, R.string.columbus_setting_action_launch_title};
    @VisibleForTesting
    static final int[] ACTION_VALUE_RES_IDS = {R.string.columbus_setting_action_screenshot_value, R.string.columbus_setting_action_assistant_value, R.string.columbus_setting_action_play_pause_value, R.string.columbus_setting_action_overview_value, R.string.columbus_setting_action_notification_value, R.string.columbus_setting_action_launch_value};
    /* access modifiers changed from: private */
    public static final Uri COLUMBUS_ENABLED_URI = Settings.Secure.getUriFor("columbus_enabled");
    /* access modifiers changed from: private */
    public static final Uri COLUMBUS_LAUNCH_APP_URI = Settings.Secure.getUriFor(SECURE_KEY_COLUMBUS_LAUNCH_APP);
    static final String SECURE_KEY_COLUMBUS_ACTION = "columbus_action";
    static final String SECURE_KEY_COLUMBUS_LAUNCH_APP = "columbus_launch_app";
    private static final Map<String, String> VALUE_TO_TITLE_MAP = new HashMap();
    private static String sDefaultAction;
    private final View.OnClickListener[] mActionExtraOnClick;
    private final Map<String, ColumbusRadioButtonPreference> mActionPreferences = new HashMap();
    private final Context mContext;
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private PreferenceCategory mPreferenceCategory;
    private SettingObserver mSettingObserver;

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

    /* access modifiers changed from: private */
    public static /* synthetic */ CharSequence lambda$static$0(Context context) {
        String string = Settings.Secure.getString(context.getContentResolver(), SECURE_KEY_COLUMBUS_LAUNCH_APP);
        if (string == null || string.isEmpty()) {
            return context.getString(R.string.columbus_setting_action_launch_summary_no_selection);
        }
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getApplicationLabel(packageManager.getApplicationInfo(string, 0));
        } catch (PackageManager.NameNotFoundException unused) {
            return context.getString(R.string.columbus_setting_action_launch_summary_not_installed);
        }
    }

    public ColumbusActionsPreferenceController(Context context, String str) {
        super(context, str);
        this.mContext = context;
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
        this.mActionExtraOnClick = new View.OnClickListener[]{null, null, null, null, null, new ColumbusActionsPreferenceController$$ExternalSyntheticLambda0(this)};
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setClass(this.mContext, SubSettings.class);
        intent.putExtra(":settings:show_fragment", ColumbusGestureLaunchSettingsFragment.class.getName());
        intent.putExtra(":settings:source_metrics", getMetricsCategory());
        this.mContext.startActivity(intent);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        if (isAvailable()) {
            PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
            this.mPreferenceCategory = preferenceCategory;
            if (preferenceCategory != null) {
                this.mSettingObserver = new SettingObserver(this.mPreferenceCategory);
            }
            this.mActionPreferences.clear();
            VALUE_TO_TITLE_MAP.clear();
            int length = ACTION_VALUE_RES_IDS.length;
            for (int i = 0; i < length; i++) {
                String string = this.mContext.getString(ACTION_VALUE_RES_IDS[i]);
                String string2 = this.mContext.getString(ACTION_TITLE_RES_IDS[i]);
                this.mActionPreferences.put(string, makeRadioPreference(string, string2, ACTION_SUMMARIES[i], ACTION_METRICS[i], this.mActionExtraOnClick[i]));
                VALUE_TO_TITLE_MAP.put(string, string2);
                if (i == 0) {
                    sDefaultAction = string;
                }
            }
        }
    }

    private ColumbusRadioButtonPreference makeRadioPreference(String str, String str2, ColumbusRadioButtonPreference.ContextualSummaryProvider contextualSummaryProvider, int i, View.OnClickListener onClickListener) {
        ColumbusRadioButtonPreference columbusRadioButtonPreference = new ColumbusRadioButtonPreference(this.mPreferenceCategory.getContext());
        columbusRadioButtonPreference.setKey(str);
        columbusRadioButtonPreference.setTitle((CharSequence) str2);
        columbusRadioButtonPreference.setContextualSummaryProvider(contextualSummaryProvider);
        columbusRadioButtonPreference.updateSummary(this.mContext);
        columbusRadioButtonPreference.setMetric(i);
        columbusRadioButtonPreference.setOnClickListener(this);
        columbusRadioButtonPreference.setExtraWidgetOnClickListener(onClickListener);
        this.mPreferenceCategory.addPreference(columbusRadioButtonPreference);
        return columbusRadioButtonPreference;
    }

    public int getAvailabilityStatus() {
        return ColumbusPreferenceController.isColumbusSupported(this.mContext) ? 0 : 3;
    }

    public void onRadioButtonClicked(RadioButtonPreference radioButtonPreference) {
        String key = radioButtonPreference.getKey();
        if (!key.equals(Settings.Secure.getString(this.mContext.getContentResolver(), SECURE_KEY_COLUMBUS_ACTION))) {
            Settings.Secure.putString(this.mContext.getContentResolver(), SECURE_KEY_COLUMBUS_ACTION, key);
            updateState(this.mPreferenceCategory);
            if (radioButtonPreference instanceof ColumbusRadioButtonPreference) {
                this.mMetricsFeatureProvider.action(this.mContext, ((ColumbusRadioButtonPreference) radioButtonPreference).getMetric(), (Pair<Integer, Object>[]) new Pair[0]);
            }
        }
    }

    public void updateState(Preference preference) {
        if (!this.mActionPreferences.isEmpty()) {
            String string = Settings.Secure.getString(this.mContext.getContentResolver(), SECURE_KEY_COLUMBUS_ACTION);
            if (string == null || !this.mActionPreferences.containsKey(string)) {
                string = sDefaultAction;
            }
            boolean isColumbusEnabled = ColumbusPreferenceController.isColumbusEnabled(this.mContext);
            for (ColumbusRadioButtonPreference next : this.mActionPreferences.values()) {
                boolean equals = next.getKey().equals(string);
                if (next.isChecked() != equals) {
                    next.setChecked(equals);
                }
                next.setEnabled(isColumbusEnabled);
                next.updateSummary(this.mContext);
            }
        }
    }

    public void onStart() {
        SettingObserver settingObserver = this.mSettingObserver;
        if (settingObserver != null) {
            settingObserver.register(this.mContext.getContentResolver());
            this.mSettingObserver.onChange(false);
        }
    }

    public void onStop() {
        SettingObserver settingObserver = this.mSettingObserver;
        if (settingObserver != null) {
            settingObserver.unregister(this.mContext.getContentResolver());
        }
    }

    static String getColumbusAction(Context context) {
        populateValueToTitleMapIfEmpty(context);
        return VALUE_TO_TITLE_MAP.getOrDefault(Settings.Secure.getString(context.getContentResolver(), SECURE_KEY_COLUMBUS_ACTION), sDefaultAction);
    }

    private static void populateValueToTitleMapIfEmpty(Context context) {
        if (VALUE_TO_TITLE_MAP.isEmpty()) {
            int length = ACTION_VALUE_RES_IDS.length;
            for (int i = 0; i < length; i++) {
                String string = context.getString(ACTION_VALUE_RES_IDS[i]);
                VALUE_TO_TITLE_MAP.put(string, context.getString(ACTION_TITLE_RES_IDS[i]));
                if (i == 0) {
                    sDefaultAction = string;
                }
            }
        }
    }

    private class SettingObserver extends ContentObserver {
        private final Preference mPreference;

        SettingObserver(Preference preference) {
            super(new Handler(Looper.myLooper()));
            this.mPreference = preference;
        }

        public void register(ContentResolver contentResolver) {
            contentResolver.registerContentObserver(ColumbusActionsPreferenceController.COLUMBUS_ENABLED_URI, false, this);
            contentResolver.registerContentObserver(ColumbusActionsPreferenceController.COLUMBUS_LAUNCH_APP_URI, false, this);
        }

        public void unregister(ContentResolver contentResolver) {
            contentResolver.unregisterContentObserver(this);
        }

        public void onChange(boolean z) {
            ColumbusActionsPreferenceController.this.updateState(this.mPreference);
        }
    }
}
