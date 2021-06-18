package com.android.settings.deletionhelper;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.text.format.DateUtils;
import android.text.format.Formatter;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.Utils;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.widget.FooterPreference;

public class AutomaticStorageManagerDescriptionPreferenceController extends AbstractPreferenceController implements PreferenceControllerMixin {
    public String getPreferenceKey() {
        return "freed_bytes";
    }

    public boolean isAvailable() {
        return true;
    }

    public AutomaticStorageManagerDescriptionPreferenceController(Context context) {
        super(context);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        FooterPreference footerPreference = (FooterPreference) preferenceScreen.findPreference(getPreferenceKey());
        Context context = footerPreference.getContext();
        ContentResolver contentResolver = context.getContentResolver();
        long j = Settings.Secure.getLong(contentResolver, "automatic_storage_manager_bytes_cleared", 0);
        long j2 = Settings.Secure.getLong(contentResolver, "automatic_storage_manager_last_run", 0);
        if (j == 0 || j2 == 0 || !Utils.isStorageManagerEnabled(context)) {
            footerPreference.setSummary((int) R.string.automatic_storage_manager_text);
            return;
        }
        footerPreference.setSummary((CharSequence) context.getString(R.string.automatic_storage_manager_freed_bytes, new Object[]{Formatter.formatFileSize(context, j), DateUtils.formatDateTime(context, j2, 16)}));
    }
}
