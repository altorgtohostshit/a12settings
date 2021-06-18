package com.android.settings.development.featureflags;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.util.FeatureFlagUtils;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import java.util.Map;

public class FeatureFlagsPreferenceController extends BasePreferenceController {
    private PreferenceGroup mGroup;

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

    public FeatureFlagsPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return Build.IS_DEBUGGABLE ? 0 : 3;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mGroup = (PreferenceGroup) preferenceScreen.findPreference(getPreferenceKey());
        Map allFeatureFlags = FeatureFlagUtils.getAllFeatureFlags();
        if (allFeatureFlags != null) {
            this.mGroup.removeAll();
            allFeatureFlags.keySet().stream().sorted().forEach(new FeatureFlagsPreferenceController$$ExternalSyntheticLambda0(this, this.mGroup.getContext()));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$0(Context context, String str) {
        this.mGroup.addPreference(new FeatureFlagPreference(context, str));
    }
}
