package com.android.settings.notification.zen;

import android.app.NotificationManager;
import android.content.Context;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.RadioButtonPreference;

public class ZenModeVisEffectsCustomPreferenceController extends AbstractZenModePreferenceController {
    private RadioButtonPreference mPreference;

    public boolean isAvailable() {
        return true;
    }

    public ZenModeVisEffectsCustomPreferenceController(Context context, Lifecycle lifecycle, String str) {
        super(context, str, lifecycle);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        RadioButtonPreference radioButtonPreference = (RadioButtonPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = radioButtonPreference;
        radioButtonPreference.setExtraWidgetOnClickListener(new C1167xe31d5eff(this));
        this.mPreference.setOnClickListener(new C1168xe31d5f00(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$0(View view) {
        launchCustomSettings();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$1(RadioButtonPreference radioButtonPreference) {
        launchCustomSettings();
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        this.mPreference.setChecked(areCustomOptionsSelected());
    }

    /* access modifiers changed from: protected */
    public boolean areCustomOptionsSelected() {
        return !NotificationManager.Policy.areAllVisualEffectsSuppressed(this.mBackend.mPolicy.suppressedVisualEffects) && !(this.mBackend.mPolicy.suppressedVisualEffects == 0);
    }

    /* access modifiers changed from: protected */
    public void select() {
        this.mMetricsFeatureProvider.action(this.mContext, 1399, true);
    }

    private void launchCustomSettings() {
        select();
        new SubSettingLauncher(this.mContext).setDestination(ZenModeBlockedEffectsSettings.class.getName()).setTitleRes(R.string.zen_mode_what_to_block_title).setSourceMetricsCategory(1400).launch();
    }
}
