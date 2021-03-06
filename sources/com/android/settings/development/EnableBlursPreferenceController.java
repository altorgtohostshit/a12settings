package com.android.settings.development;

import android.content.Context;
import android.os.SystemProperties;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;
import com.android.settingslib.development.SystemPropPoker;

public final class EnableBlursPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin {
    static final String DISABLE_BLURS_SYSPROP = "persist.sys.sf.disable_blurs";
    private final boolean mBlurSupported;

    public String getPreferenceKey() {
        return "enable_blurs_on_windows";
    }

    public EnableBlursPreferenceController(Context context) {
        this(context, SystemProperties.getBoolean("ro.surface_flinger.supports_background_blur", false));
    }

    public EnableBlursPreferenceController(Context context, boolean z) {
        super(context);
        this.mBlurSupported = z;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        SystemProperties.set(DISABLE_BLURS_SYSPROP, ((Boolean) obj).booleanValue() ^ true ? "1" : "0");
        SystemPropPoker.getInstance().poke();
        return true;
    }

    public boolean isAvailable() {
        return this.mBlurSupported;
    }

    public void updateState(Preference preference) {
        ((SwitchPreference) this.mPreference).setChecked(!SystemProperties.getBoolean(DISABLE_BLURS_SYSPROP, false));
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        SystemProperties.set(DISABLE_BLURS_SYSPROP, (String) null);
        updateState((Preference) null);
    }
}
