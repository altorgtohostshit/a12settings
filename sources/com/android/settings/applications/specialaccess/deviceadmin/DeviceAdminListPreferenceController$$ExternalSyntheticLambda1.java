package com.android.settings.applications.specialaccess.deviceadmin;

import androidx.preference.Preference;

public final /* synthetic */ class DeviceAdminListPreferenceController$$ExternalSyntheticLambda1 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ DeviceAdminListPreferenceController f$0;
    public final /* synthetic */ DeviceAdminListItem f$1;

    public /* synthetic */ DeviceAdminListPreferenceController$$ExternalSyntheticLambda1(DeviceAdminListPreferenceController deviceAdminListPreferenceController, DeviceAdminListItem deviceAdminListItem) {
        this.f$0 = deviceAdminListPreferenceController;
        this.f$1 = deviceAdminListItem;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$bindPreference$0(this.f$1, preference);
    }
}
