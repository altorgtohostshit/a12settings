package com.android.settings.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.FeatureFlagUtils;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.RingtonePreference;
import com.android.settings.core.OnActivityResultListener;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.List;

public class SoundWorkSettings extends DashboardFragment implements OnActivityResultListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.sound_work_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return SoundWorkSettings.isSupportWorkProfileSound(context);
        }
    };
    private RingtonePreference mRequestPreference;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "SoundWorkSettings";
    }

    public int getMetricsCategory() {
        return 1877;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R.xml.sound_work_settings;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            String string = bundle.getString("selected_preference", (String) null);
            if (!TextUtils.isEmpty(string)) {
                this.mRequestPreference = (RingtonePreference) findPreference(string);
            }
        }
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        if (!(preference instanceof RingtonePreference)) {
            return super.onPreferenceTreeClick(preference);
        }
        writePreferenceClickMetric(preference);
        RingtonePreference ringtonePreference = (RingtonePreference) preference;
        this.mRequestPreference = ringtonePreference;
        ringtonePreference.onPrepareRingtonePickerIntent(ringtonePreference.getIntent());
        getActivity().startActivityForResultAsUser(this.mRequestPreference.getIntent(), 200, (Bundle) null, UserHandle.of(this.mRequestPreference.getUserId()));
        return true;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        RingtonePreference ringtonePreference = this.mRequestPreference;
        if (ringtonePreference != null) {
            ringtonePreference.onActivityResult(i, i2, intent);
            this.mRequestPreference = null;
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        RingtonePreference ringtonePreference = this.mRequestPreference;
        if (ringtonePreference != null) {
            bundle.putString("selected_preference", ringtonePreference.getKey());
        }
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, this, getSettingsLifecycle());
    }

    private static List<AbstractPreferenceController> buildPreferenceControllers(Context context, SoundWorkSettings soundWorkSettings, Lifecycle lifecycle) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new SoundWorkSettingsController(context, soundWorkSettings, lifecycle));
        return arrayList;
    }

    static final boolean isSupportWorkProfileSound(Context context) {
        boolean isEnabled = FeatureFlagUtils.isEnabled(context, "settings_silky_home");
        AudioHelper audioHelper = new AudioHelper(context);
        boolean z = audioHelper.getManagedProfileId(UserManager.get(context)) != -10000;
        boolean z2 = !audioHelper.isSingleVolume();
        if (!isEnabled || !z || !z2) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void enableWorkSync() {
        SoundWorkSettingsController soundWorkSettingsController = (SoundWorkSettingsController) use(SoundWorkSettingsController.class);
        if (soundWorkSettingsController != null) {
            soundWorkSettingsController.enableWorkSync();
        }
    }
}
