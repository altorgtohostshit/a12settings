package com.android.settings.location;

import android.content.Context;
import android.content.IntentFilter;
import android.os.UserHandle;
import androidx.preference.Preference;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.RestrictedAppPreference;
import java.util.List;
import java.util.Map;

public class LocationInjectedServicesForWorkPreferenceController extends LocationInjectedServicesPreferenceController {
    private static final String TAG = "LocationWorkPrefCtrl";

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

    public LocationInjectedServicesForWorkPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void updateState(Preference preference) {
        this.mCategoryLocationServices.removeAll();
        boolean z = false;
        for (Map.Entry next : getLocationServices().entrySet()) {
            for (Preference preference2 : (List) next.getValue()) {
                if (preference2 instanceof RestrictedAppPreference) {
                    ((RestrictedAppPreference) preference2).checkRestrictionAndSetDisabled();
                }
            }
            if (((Integer) next.getKey()).intValue() != UserHandle.myUserId()) {
                LocationSettings.addPreferencesSorted((List) next.getValue(), this.mCategoryLocationServices);
                z = true;
            }
        }
        this.mCategoryLocationServices.setVisible(z);
    }
}
