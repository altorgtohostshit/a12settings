package com.android.settings.applications.assist;

import android.content.Context;
import android.net.Uri;
import android.os.UserHandle;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.TwoStatePreference;
import com.android.internal.app.AssistUtils;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import java.util.Arrays;
import java.util.List;

public class AssistContextPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin, Preference.OnPreferenceChangeListener, LifecycleObserver, OnResume, OnPause {
    private final AssistUtils mAssistUtils;
    private Preference mPreference;
    private PreferenceScreen mScreen;
    private final SettingObserver mSettingObserver = new SettingObserver();

    public String getPreferenceKey() {
        return "context";
    }

    public AssistContextPreferenceController(Context context, Lifecycle lifecycle) {
        super(context);
        this.mAssistUtils = new AssistUtils(context);
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    public boolean isAvailable() {
        return this.mAssistUtils.getAssistComponentForUser(UserHandle.myUserId()) != null;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        this.mScreen = preferenceScreen;
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
        super.displayPreference(preferenceScreen);
    }

    public void onResume() {
        this.mSettingObserver.register(this.mContext.getContentResolver(), true);
        updatePreference();
    }

    public void updateState(Preference preference) {
        updatePreference();
    }

    public void onPause() {
        this.mSettingObserver.register(this.mContext.getContentResolver(), false);
    }

    /* access modifiers changed from: private */
    public void updatePreference() {
        Preference preference = this.mPreference;
        if (preference != null && (preference instanceof TwoStatePreference)) {
            if (!isAvailable()) {
                this.mScreen.removePreference(this.mPreference);
            } else if (this.mScreen.findPreference(getPreferenceKey()) == null) {
                this.mScreen.addPreference(this.mPreference);
            }
            ((TwoStatePreference) this.mPreference).setChecked(isChecked(this.mContext));
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        Settings.Secure.putInt(this.mContext.getContentResolver(), "assist_structure_enabled", ((Boolean) obj).booleanValue() ? 1 : 0);
        return true;
    }

    static boolean isChecked(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "assist_structure_enabled", 1) != 0;
    }

    class SettingObserver extends AssistSettingObserver {
        private final Uri URI = Settings.Secure.getUriFor("assist_structure_enabled");

        SettingObserver() {
        }

        /* access modifiers changed from: protected */
        public List<Uri> getSettingUris() {
            return Arrays.asList(new Uri[]{this.URI});
        }

        public void onSettingChange() {
            AssistContextPreferenceController.this.updatePreference();
        }
    }
}
