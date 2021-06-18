package com.android.settings.development.autofill;

import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings;
import android.util.Log;
import android.view.autofill.AutofillManager;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;

public final class AutofillLoggingLevelPreferenceController extends DeveloperOptionsPreferenceController implements PreferenceControllerMixin, Preference.OnPreferenceChangeListener, LifecycleObserver, OnDestroy {
    private final String[] mListSummaries;
    private final String[] mListValues;
    private final AutofillDeveloperSettingsObserver mObserver;

    public String getPreferenceKey() {
        return "autofill_logging_level";
    }

    public AutofillLoggingLevelPreferenceController(Context context, Lifecycle lifecycle) {
        super(context);
        Resources resources = context.getResources();
        this.mListValues = resources.getStringArray(R.array.autofill_logging_level_values);
        this.mListSummaries = resources.getStringArray(R.array.autofill_logging_level_entries);
        AutofillDeveloperSettingsObserver autofillDeveloperSettingsObserver = new AutofillDeveloperSettingsObserver(this.mContext, new C0844x4975411d(this));
        this.mObserver = autofillDeveloperSettingsObserver;
        autofillDeveloperSettingsObserver.register();
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    public void onDestroy() {
        this.mObserver.unregister();
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        writeLevel(obj);
        lambda$new$0();
        return true;
    }

    public void updateState(Preference preference) {
        lambda$new$0();
    }

    /* access modifiers changed from: protected */
    public void onDeveloperOptionsSwitchDisabled() {
        super.onDeveloperOptionsSwitchDisabled();
        writeLevel((Object) null);
    }

    /* access modifiers changed from: private */
    /* renamed from: updateOptions */
    public void lambda$new$0() {
        if (this.mPreference == null) {
            Log.v("AutofillLoggingLevelPreferenceController", "ignoring Settings update because UI is gone");
            return;
        }
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "autofill_logging_level", AutofillManager.DEFAULT_LOGGING_LEVEL);
        char c = 2;
        if (i == 2) {
            c = 1;
        } else if (i != 4) {
            c = 0;
        }
        ListPreference listPreference = (ListPreference) this.mPreference;
        listPreference.setValue(this.mListValues[c]);
        listPreference.setSummary(this.mListSummaries[c]);
    }

    private void writeLevel(Object obj) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "autofill_logging_level", obj instanceof String ? Integer.parseInt((String) obj) : 0);
    }
}
