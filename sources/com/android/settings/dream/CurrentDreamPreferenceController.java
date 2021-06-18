package com.android.settings.dream;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.GearPreference;
import com.android.settingslib.dream.DreamBackend;
import java.util.Optional;

public class CurrentDreamPreferenceController extends BasePreferenceController {
    private final DreamBackend mBackend;

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

    public CurrentDreamPreferenceController(Context context, String str) {
        super(context, str);
        this.mBackend = DreamBackend.getInstance(context);
    }

    public int getAvailabilityStatus() {
        return this.mBackend.getDreamInfos().size() > 0 ? 0 : 2;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        setGearClickListenerForPreference(preference);
        setActiveDreamIcon(preference);
    }

    public CharSequence getSummary() {
        return this.mBackend.getActiveDreamName();
    }

    private void setGearClickListenerForPreference(Preference preference) {
        if (preference instanceof GearPreference) {
            GearPreference gearPreference = (GearPreference) preference;
            Optional<DreamBackend.DreamInfo> activeDreamInfo = getActiveDreamInfo();
            if (!activeDreamInfo.isPresent() || activeDreamInfo.get().settingsComponentName == null) {
                gearPreference.setOnGearClickListener((GearPreference.OnGearClickListener) null);
            } else {
                gearPreference.setOnGearClickListener(new CurrentDreamPreferenceController$$ExternalSyntheticLambda0(this));
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setGearClickListenerForPreference$0(GearPreference gearPreference) {
        launchScreenSaverSettings();
    }

    private void launchScreenSaverSettings() {
        Optional<DreamBackend.DreamInfo> activeDreamInfo = getActiveDreamInfo();
        if (activeDreamInfo.isPresent()) {
            this.mBackend.launchSettings(this.mContext, activeDreamInfo.get());
        }
    }

    private Optional<DreamBackend.DreamInfo> getActiveDreamInfo() {
        return this.mBackend.getDreamInfos().stream().filter(CurrentDreamPreferenceController$$ExternalSyntheticLambda1.INSTANCE).findFirst();
    }

    private void setActiveDreamIcon(Preference preference) {
        if (preference instanceof GearPreference) {
            GearPreference gearPreference = (GearPreference) preference;
            gearPreference.setIconSize(2);
            gearPreference.setIcon(Utils.getSafeIcon(this.mBackend.getActiveIcon()));
        }
    }
}
